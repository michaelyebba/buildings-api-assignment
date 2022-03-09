package com.msr.service;

import com.msr.data.SiteRepository;
import com.msr.model.Site;
import com.msr.model.UseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		Site site = siteRepository.findById(id).get();
		site.setTotalSize(calculateTotalSize(site));
		site.setPrimaryUseType(calculatePrimaryType(site));
		return site;
	}

	/*
		For a given site, calculate total size by summing the size_sqft associated with the site’s use(s).
	 */
	private Long calculateTotalSize(Site site) {
		return 0L;
	}

	/*
		For a given site, calculcate primary type where the primary type is the
		largest use_type (by size_sqft) in aggregate per-site.
	 */
	private UseType calculatePrimaryType(Site site) {
		return null;
	}
}