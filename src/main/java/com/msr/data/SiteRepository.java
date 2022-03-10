package com.msr.data;

import com.msr.model.Site;
import com.msr.model.projection.TotalSiteUseByType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * A sample JPA repository for querying and storing sites
 */
public interface SiteRepository extends JpaRepository<Site, Integer> {

	String FIND_TOTAL_SITE_USE_BY_TYPE_SQL = "" +
			"SELECT ut.id AS useTypeId, ut.name AS useTypeName, s.id AS siteId, sum(su.sizeSqft) AS totalSize " +
			"FROM Site s " +
			"JOIN SiteUse su ON su.site = s.id " +
			"JOIN UseType ut ON su.useType = ut.id " +
			"GROUP BY ut.id, ut.name, s.id";

	String FIND_TOTAL_SITE_USE_BY_TYPE_BY_SITE_ID_SQL = "" +
			"SELECT ut.id AS useTypeId, ut.name AS useTypeName, s.id AS siteId, sum(su.sizeSqft) AS totalSize " +
			"FROM Site s " +
			"JOIN SiteUse su ON su.site = s.id " +
			"JOIN UseType ut ON su.useType = ut.id " +
			"WHERE s.id = ?1 " +
			"GROUP BY ut.id, ut.name, s.id";

	@Query("SELECT s FROM Site s")
	List<Site> findAll();

	@Query(FIND_TOTAL_SITE_USE_BY_TYPE_BY_SITE_ID_SQL)
	List<TotalSiteUseByType> findTotalSiteUseByTypeBySiteId(Integer siteId);

	@Query(FIND_TOTAL_SITE_USE_BY_TYPE_SQL)
	List<TotalSiteUseByType> findAllTotalSiteUseByType();

	List<Site> findAllByStateEquals(String state);
}