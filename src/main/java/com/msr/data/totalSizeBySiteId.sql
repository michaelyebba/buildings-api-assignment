CREATE OR REPLACE VIEW v_DecoratedSite AS
WITH
  TotalSizeSqftBySiteId AS (
    SELECT s.Id, sum(su.size_sqft) as totalSizeSqft
    FROM Site s
    JOIN SiteUse su ON su.site_id = s.id
    GROUP BY s.Id
),
  TotalSizeSqftBySiteIdAndUseType AS (
    SELECT s.Id, su.use_type_id, sum(su.size_sqft) as totalSizeSqft
    FROM Site s
    JOIN SiteUse su ON su.site_id = s.id
    GROUP BY s.Id, use_type_id
),
  MaxSizeBySiteId AS (
    SELECT id, MAX(totalSizeSqft ) AS MaxTotalSizeSqFt
    FROM TotalSizeSqftBySiteIdAndUseType
    GROUP BY id
),
  MaxSizeSqftBySiteIdAndUseType AS (
    SELECT id, use_type_id, MAX(totalSizeSqft ) AS MaxTotalSizeSqFt
    FROM TotalSizeSqftBySiteIdAndUseType
    GROUP BY id, use_type_id
),
  SiteDecorated AS (

    SELECT
      s.Id as siteId,
      s.totalSizeSqft,
      MAX(ut.use_type_id) AS useTypeId,
      ms.MaxTotalSizeSqFt
    FROM TotalSizeSqftBySiteId s
    JOIN MaxSizeSqftBySiteIdAndUseType ut ON ut.id = s.id
    JOIN MaxSizeBySiteId ms ON ms.id = ut.id AND ms.MaxTotalSizeSqFt = ut.MaxTotalSizeSqFt
    GROUP BY  s.Id, s.totalSizeSqft, ms.MaxTotalSizeSqFt
)
SELECT
     s.Id, s.Address, s.City, s.Name, s.State, s.ZipCode, sd.totalSizeSqft,
     sd.useTypeId, ut.name as UseTypeName
FROM SiteDecorated sd
JOIN Site s ON s.id = sd.siteId
JOIN UseType ut on ut.id = sd.useTypeId
GO