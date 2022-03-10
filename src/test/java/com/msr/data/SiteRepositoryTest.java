package com.msr.data;

import com.msr.model.projection.TotalSiteUseByType;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@CommonsLog
public class SiteRepositoryTest {

	private static final Integer MHQ_SITE_ID = 1;

	@Autowired
	private SiteRepository siteRepository;

	@Test
	public void testFindTotalSiteUseByTypeBySiteId() {
		List<TotalSiteUseByType> totalSiteUseByTypes = siteRepository.findTotalSiteUseByTypeBySiteId(MHQ_SITE_ID);
		assertThat(totalSiteUseByTypes).isNotEmpty();
		assertThat(totalSiteUseByTypes).hasSize(2);

		totalSiteUseByTypes.stream().forEach(t -> {
			log.info(String.format("id=%d; name=%s; siteId=%d; totalSize=%d", t.getUseTypeId(), t.getUseTypeName(), t.getSiteId(), t.getTotalSize()));
		});
	}

	@Test
	public void testFindAllTotalSiteUseByType() {
		List<TotalSiteUseByType> totalSiteUseByTypes = siteRepository.findAllTotalSiteUseByType();
		assertThat(totalSiteUseByTypes).isNotEmpty();
		assertThat(totalSiteUseByTypes).hasSize(10);

		totalSiteUseByTypes.stream().forEach(t -> {
			log.info(String.format("id=%d; name=%s; siteId=%d; totalSize=%d", t.getUseTypeId(), t.getUseTypeName(), t.getSiteId(), t.getTotalSize()));
		});
	}
}
