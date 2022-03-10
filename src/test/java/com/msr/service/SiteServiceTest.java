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
import java.util.Optional;
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
	public void testFindAll() {
		List<Site> allSites = siteService.findAll();
		assertThat(allSites).hasSize(6);

		// select out mhq
		Optional<Site> mhqOpt = allSites.stream().filter(s->s.getId() == MHQ_SITE_ID).findFirst();
		assertThat(mhqOpt.isPresent()).isTrue();

		Site mhq = mhqOpt.get();
		assertThat(mhq.getId()).isEqualTo(MHQ_SITE_ID);
		assertThat(mhq.getTotalSize()).isEqualTo(MHQ_TOTAL_SIZE);

		// Now check all sites dynamically using verifyPrimaryType()
		allSites.stream().forEach(site -> {
			verifyPrimaryType(site);
			verifyTotalSize(site);
		});
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
}