package com.msr.service;

import com.msr.data.UseTypeRepository;
import com.msr.model.Site;
import com.msr.model.UseType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SiteServiceTest {

	private static final Integer MHQ_SITE_ID = 1;
	private static final Integer EXPECTED_USE_TYPE_ID = 54;
	private static final Long MHQ_TOTAL_SIZE = 13000L;

	@Autowired
	private SiteService siteService;

	@Autowired
	private UseTypeRepository useTypeRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	public void findSiteById_MHQ() {
		Site mhq = siteService.findSiteById(MHQ_SITE_ID);
		assertThat(mhq).isNotNull();
		assertThat(mhq.getId()).isEqualTo(MHQ_SITE_ID);
		assertThat(mhq.getTotalSize()).isEqualTo(MHQ_TOTAL_SIZE);

		UseType expectedUseType = useTypeRepository.findById(EXPECTED_USE_TYPE_ID).get();
		assertThat(mhq.getPrimaryUseType()).isEqualTo(expectedUseType);
	}

	@Test
	public void testFindAll_Success() {
		List<Site> allSites = siteService.findAll();

		Integer verifiedTotalSiteCount = getTotalSiteCount();
		assertThat(allSites).hasSize(verifiedTotalSiteCount);

		// Now check all sites dynamically using verifyPrimaryType()
		allSites.stream().forEach(site -> {
			verifyPrimaryType(site);
			verifyTotalSize(site);
		});
	}

	@Test
	public void testFindAllByState_Success() {
		List<Site> allSites = siteService.findAllByState("CA");

		Integer verifiedTotalSiteCount = getTotalSiteCountByState("CA");
		assertThat(allSites).hasSize(verifiedTotalSiteCount);

		// Now check all sites dynamically using verifyPrimaryType()
		allSites.stream().forEach(site -> {
			verifyPrimaryType(site);
			verifyTotalSize(site);
		});
	}

	@Test
	public void testSaveSite_newSite() {
		Site newSite = Site.builder()
				.name("New Site")
				.address("123 Diamond St")
				.city("San Diego")
				.state("CA")
				.zipcode("92109")
				.build();

		newSite = siteService.saveSite(newSite);
		assertThat(newSite).isNotNull();
		assertThat(newSite.getId()).isNotNull();
		assertThat(newSite.getName()).isEqualTo("New Site");
		assertThat(newSite.getAddress()).isEqualTo("123 Diamond St");
		assertThat(newSite.getCity()).isEqualTo("San Diego");
		assertThat(newSite.getState()).isEqualTo("CA");
		assertThat(newSite.getZipcode()).isEqualTo("92109");
	}

	@Test
	public void testSaveSite_existingSite() {
		Site mhq = siteService.findSiteById(MHQ_SITE_ID);
		String newName = String.format("%s - Modified", mhq.getName());
		mhq.setName(newName);
		mhq = siteService.saveSite(mhq);
		assertThat(mhq).isNotNull();
		assertThat(mhq.getName()).isEqualTo(newName);
	}

	/*
		For a given site, calculcate primary type where the primary type is the
		largest use_type (by size_sqft) in aggregate per-site.
 	*/
	void verifyPrimaryType(Site site) {

		Integer maxUseTypeKey = site.getSiteUses().stream()
				.collect(Collectors.groupingBy(su -> su.getUseType().getId(), Collectors.summingLong(su->su.getSizeSqft())))
				.entrySet().stream()
				.max(Comparator.comparing(Map.Entry::getKey))
				.get().getKey()
				;

		UseType verifiedPrimaryType = useTypeRepository.findById(maxUseTypeKey).orElseGet(null);
		assertThat(site.getPrimaryUseType()).isEqualTo(verifiedPrimaryType);
	}

	/*
		Verifies that totalSize for a give site is correct against a sql query
	 */
	void verifyTotalSize(Site site) {
		Long verifiedTotalSize = getTotalSizeForSite(site);
		assertThat(site.getTotalSize()).isEqualTo(verifiedTotalSize);
	}

	/*
		Returns a map of siteId and square footage
	 */
	Long getTotalSizeForSite (Site site) {
		String sql =
				"SELECT sum(su.size_sqft) AS totalSize " +
				"FROM Site s " +
				"JOIN SiteUse su ON su.site_id = s.Id " +
				"WHERE s.Id = ?";

		return jdbcTemplate.queryForObject(sql, Long.class, site.getId());
	}

	/*
		Returns the total number of all sites
	 */
	Integer getTotalSiteCount() {
		String sql = "SELECT COUNT(0) FROM Site";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	/*
		Returns the total number of all sites by state
	 */
	Integer getTotalSiteCountByState(String state) {
		String sql = "SELECT COUNT(0) FROM Site WHERE State = ?";
		return jdbcTemplate.queryForObject(sql, Integer.class, state);
	}
}