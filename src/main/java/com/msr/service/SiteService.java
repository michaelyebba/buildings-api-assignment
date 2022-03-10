package com.msr.service;

import com.msr.data.SiteRepository;
import com.msr.model.Site;
import com.msr.model.projection.TotalSiteUseByType;
import lombok.NonNull;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@CommonsLog
public class SiteService {

	@Autowired
	private SiteRepository siteRepository;

	@Autowired
	private SiteDecoratorService siteDecoratorService;

	/**
	 * Returns a site by Id then returns sums up the total size from all the site uses
	 * and identifies the primary use type by grouping the use-types together and selecting
	 * the one with the most combined square footage usage.
	 *
	 * @param id
	 * @return
	 */
	public Site findSiteById(@NonNull Integer id) {
		Optional<Site> siteOpt = siteRepository.findById(id);

		if (siteOpt.isPresent()) {
			Site site = siteOpt.get();
			List<TotalSiteUseByType> totalSiteUseByTypesBySite = siteRepository.findTotalSiteUseByTypeBySiteId(site.getId());
			siteDecoratorService.setSupplementalFields(site, totalSiteUseByTypesBySite);
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
		siteDecoratorService.setSupplementalFields(allSites);
		return allSites;
	}

	/**
	 * Returns a list of sites by state
	 * @return
	 */
	public List<Site> findAllByState(@NonNull String state) {
		List<Site> sitesByState = siteRepository.findAllByStateEquals(state);
		siteDecoratorService.setSupplementalFields(sitesByState);
		return sitesByState;
	}

	/**
	 * Saves a site object
	 *
	 * @param site
	 */
	@Transactional
	public Site saveSite(@NonNull Site site) {
		try {
			Site newSite = siteRepository.saveAndFlush(site);
			log.info(String.format("Successfully saved new site: Id=%d; Name=%s", newSite.getId(), newSite.getName()));
			return newSite;
		} catch (Exception e) {
			log.error(String.format("Error saving site: %s", e.getMessage()));
		}
		return null;
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