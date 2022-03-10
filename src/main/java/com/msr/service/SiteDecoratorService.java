package com.msr.service;

import com.msr.data.SiteRepository;
import com.msr.model.Site;
import com.msr.model.UseType;
import com.msr.model.projection.TotalSiteUseByType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is a utility service responsible for setting the 2 supplemental fields
 * on the Site object:
 * 	Total Size
 * 	Primary Type
 */
@Service
public class SiteDecoratorService {

	@Autowired
	private SiteRepository siteRepository;

	/**
	 * For a given site, calculate total size by summing the size_sqft associated with the site’s use(s).
	 * @param site
	 * @return
	 */
	public long calculateTotalSize(Site site) {
		return site.getSiteUses().stream().map(su -> su.getSizeSqft()).reduce(0L, Long::sum);
	}

	/**
	 * For a given site, calculcate primary type where the primary type is the
	 * largest use_type (by size_sqft) in aggregate per-site.
	 *
	 * This implementation uses SQL to calculate return a list of totalSiteUseByType projections
	 * then uses streams to select the max.
	 *
	 * @param totalSiteUseByTypesBySite
	 * @return
	 */
	public UseType calculatePrimaryType(List<TotalSiteUseByType> totalSiteUseByTypesBySite) {
		TotalSiteUseByType primaryType = totalSiteUseByTypesBySite.stream().max(Comparator.comparing(TotalSiteUseByType::getTotalSize)).orElseGet(null);
		return new UseType(primaryType);
	}


	/**
	 * Sets the totalSize and primary use type for a given site
	 *
	 * @param site
	 * @param totalSiteUseByTypesBySiteId
	 */
	public void setSupplementalFields(Site site, List<TotalSiteUseByType> totalSiteUseByTypesBySiteId) {
		site.setTotalSize(calculateTotalSize(site));
		site.setPrimaryUseType(calculatePrimaryType(totalSiteUseByTypesBySiteId));
	}

	/**
	 * Sets supplemental fields for a list of sites
	 * @param allSites
	 */
	public void setSupplementalFields(List<Site> allSites) {
		final List<TotalSiteUseByType> totalSiteUseByTypes = siteRepository.findAllTotalSiteUseByType();  // get list of total site uses by siteid

		allSites.stream().forEach(site -> {

			// Get the total uses by site and use to calculate primary use type
			List<TotalSiteUseByType> totalSiteUseByTypesBySiteId = totalSiteUseByTypes.stream().filter(t->t.getSiteId().equals(site.getId())).collect(Collectors.toList());
			setSupplementalFields(site, totalSiteUseByTypesBySiteId);
		});
	}
}