package com.msr.data;

import com.msr.model.Site;
import com.msr.model.projection.DecoratedSite;
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

	List<Site> findDistinctBySiteUsesSizeSqftGreaterThan(Long sqft);

	// TODO would create a MySQL view for this native query and then we could create a findDecoratedSiteById method pretty easily
	@Query(nativeQuery = true, value="WITH\n" +
			"  TotalSizeSqftBySiteId AS (\n" +
			"    SELECT s.Id, sum(su.size_sqft) as totalSizeSqft\n" +
			"    FROM Site s\n" +
			"    JOIN SiteUse su ON su.site_id = s.id\n" +
			"    GROUP BY s.Id\n" +
			"),\n" +
			"  TotalSizeSqftBySiteIdAndUseType AS (\n" +
			"    SELECT s.Id, su.use_type_id, sum(su.size_sqft) as totalSizeSqft\n" +
			"    FROM Site s\n" +
			"    JOIN SiteUse su ON su.site_id = s.id\n" +
			"    GROUP BY s.Id, use_type_id\n" +
			"),\n" +
			"  MaxSizeBySiteId AS (\n" +
			"    SELECT id, MAX(totalSizeSqft ) AS MaxTotalSizeSqFt\n" +
			"    FROM TotalSizeSqftBySiteIdAndUseType\n" +
			"    GROUP BY id\n" +
			"),\n" +
			"  MaxSizeSqftBySiteIdAndUseType AS (\n" +
			"    SELECT id, use_type_id, MAX(totalSizeSqft ) AS MaxTotalSizeSqFt\n" +
			"    FROM TotalSizeSqftBySiteIdAndUseType\n" +
			"    GROUP BY id, use_type_id\n" +
			"),\n" +
			"  SiteDecorated AS (\n" +
			"\n" +
			"    SELECT\n" +
			"      s.Id as siteId,\n" +
			"      s.totalSizeSqft,\n" +
			"      MAX(ut.use_type_id) AS useTypeId,\n" +
			"      ms.MaxTotalSizeSqFt\n" +
			"    FROM TotalSizeSqftBySiteId s\n" +
			"    JOIN MaxSizeSqftBySiteIdAndUseType ut ON ut.id = s.id\n" +
			"    JOIN MaxSizeBySiteId ms ON ms.id = ut.id AND ms.MaxTotalSizeSqFt = ut.MaxTotalSizeSqFt\n" +
			"    GROUP BY  s.Id, s.totalSizeSqft, ms.MaxTotalSizeSqFt\n" +
			")\n" +
			"SELECT\n" +
			"     s.Id, s.Address, s.City, s.Name, s.State, s.ZipCode, sd.totalSizeSqft,\n" +
			"     sd.useTypeId, ut.name as UseTypeName\n" +
			"FROM SiteDecorated sd\n" +
			"JOIN Site s ON s.id = sd.siteId\n" +
			"JOIN UseType ut on ut.id = sd.useTypeId")
	List<DecoratedSite> findAllDecoratedSites();
}