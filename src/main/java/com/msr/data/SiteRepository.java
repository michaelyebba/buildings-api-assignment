package com.msr.data;

import com.msr.model.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * A sample JPA repository for querying and storing sites
 */
public interface SiteRepository extends PagingAndSortingRepository<Site, Integer> {

	@Query("SELECT s FROM Site s")
	List<Site> findAll();
}