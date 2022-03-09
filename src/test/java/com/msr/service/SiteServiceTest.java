package com.msr.service;

import com.msr.data.UseTypeRepository;
import com.msr.model.Site;
import com.msr.model.UseType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SiteServiceTest {

	private static final Integer MHQ_SITE_ID = 1;
	private static final Integer EXPECTED_USE_TYPE_ID = 54;

	@Autowired
	private SiteService siteService;

	@Autowired
	private UseTypeRepository useTypeRepository;

	@Test
	public void testCalculateTotalSizeAndPrimaryType() {
		Site mhq = siteService.findSiteById(MHQ_SITE_ID);
		assertThat(mhq).isNotNull();
		assertThat(mhq.getTotalSize()).isEqualTo(13000L);

		UseType expectedUseType = useTypeRepository.findById(EXPECTED_USE_TYPE_ID).get();
		assertThat(mhq.getPrimaryUseType()).isEqualTo(expectedUseType);
	}

}
