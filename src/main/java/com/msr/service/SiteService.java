package com.msr.service;

import com.msr.data.SiteRepository;
import com.msr.data.UseTypeRepository;
import com.msr.model.Site;
import com.msr.model.UseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SiteService {

	@Autowired
	private SiteRepository siteRepository;

	@Autowired
	private UseTypeRepository useTypeRepository;

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
			this.setSupplementFields(site);
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
		allSites.stream().forEach(site -> {
			this.setSupplementFields(site);
		});
		return allSites;
	}

	/*
		Utility method to set total size and primary type on a site object
	 */
	private void setSupplementFields(Site site) {
		site.setTotalSize(calculateTotalSize(site));
		site.setPrimaryUseType(calculatePrimaryType(site));
	}

	/*
		For a given site, calculate total size by summing the size_sqft associated with the site’s use(s).
	 */
	long calculateTotalSize(Site site) {
		return site.getSiteUses().stream().map(su -> su.getSizeSqft()).reduce(0L, Long::sum);
	}

	/*
		For a given site, calculcate primary type where the primary type is the
		largest use_type (by size_sqft) in aggregate per-site.
	 */
	UseType calculatePrimaryType(Site site) {

		Integer maxUseTypeKey = site.getSiteUses().stream()
				.collect(Collectors.groupingBy(su -> su.getUseType().getId(), Collectors.summingLong(su->su.getSizeSqft())))
				.entrySet().stream()
				.max(Comparator.comparing(Map.Entry::getKey))
				.get().getKey()
		;

		return useTypeRepository.findById(maxUseTypeKey).orElseGet(null);
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