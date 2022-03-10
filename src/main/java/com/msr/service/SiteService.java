package com.msr.service;

import com.msr.data.SiteRepository;
import com.msr.model.Site;
import com.msr.model.UseType;
import com.msr.model.projection.TotalSiteUseByType;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SiteService {

	@Autowired
	private SiteRepository siteRepository;

	/**
	 * Returns a site by Id then returns sums up the total size from all the site uses
	 * and identifies the primary use type by grouping the use-types together and selecting
	 * the one with the most combined square footage usage.
	 *
	 * @param id
	 * @return
	 */
	public Site findSiteById(Integer id) {
		Optional<Site> siteOpt = siteRepository.findById(id);

		if (siteOpt.isPresent()) {
			Site site = siteOpt.get();
			List<TotalSiteUseByType> totalSiteUseByTypesBySite = siteRepository.findTotalSiteUseByTypeBySiteId(site.getId());
			this.setSupplementalFields(site, totalSiteUseByTypesBySite);
			return site;
		}

		return null;
	}

	/**
	 * Returns a list of all sites with supplemental fields set
	 *
	 * @return
	 */
	public List<Site> findAll() {
		List<Site> allSites = siteRepository.findAll();
		this.setSupplementalFields(allSites);
		return allSites;
	}

	/**
	 * Returns a list of sites by state
	 * @return
	 */
	public List<Site> findAllByState(@NonNull String state) {
		List<Site> sitesByState = siteRepository.findAllByStateEquals(state);
		this.setSupplementalFields(sitesByState);
		return sitesByState;
	}

	/*
		For a given site, calculate total size by summing the size_sqft associated with the site’s use(s).
	 */
	private long calculateTotalSize(Site site) {
		return site.getSiteUses().stream().map(su -> su.getSizeSqft()).reduce(0L, Long::sum);
	}

	/*
		For a given site, calculcate primary type where the primary type is the
		largest use_type (by size_sqft) in aggregate per-site.

		This implementation uses SQL to calculate return a list of totalSiteUseByType projections
		then uses streams to select the max.
	 */
	private UseType calculatePrimaryType(List<TotalSiteUseByType> totalSiteUseByTypesBySite) {
		TotalSiteUseByType primaryType = totalSiteUseByTypesBySite.stream().max(Comparator.comparing(TotalSiteUseByType::getTotalSize)).orElseGet(null);
		return new UseType(primaryType);
	}

	/*
		Sets the totalSize and primary use type for a given site
 	*/
	private void setSupplementalFields(Site site, List<TotalSiteUseByType> totalSiteUseByTypesBySiteId) {
		site.setTotalSize(calculateTotalSize(site));
		site.setPrimaryUseType(calculatePrimaryType(totalSiteUseByTypesBySiteId));
	}

	/*
		Sets supplemental fields for a list of sites
 	*/
	private void setSupplementalFields(List<Site> allSites) {
		final List<TotalSiteUseByType> totalSiteUseByTypes = siteRepository.findAllTotalSiteUseByType();  // get list of total site uses by siteid

		allSites.stream().forEach(site -> {

			// Get the total uses by site and use to calculate primary use type
			List<TotalSiteUseByType> totalSiteUseByTypesBySiteId = totalSiteUseByTypes.stream().filter(t->t.getSiteId().equals(site.getId())).collect(Collectors.toList());
			setSupplementalFields(site, totalSiteUseByTypesBySiteId);
		});
	}

	/**
	 * Returns a list of sites that are pageable.  Default page size is 10.
	 *
	 * @param page
	 * @param size
	 * @return
	 */
	public List<Site> findAll(int page, int size) {
		return siteRepository.findAll(PageRequest.of(page, size)).getContent();
	}
}