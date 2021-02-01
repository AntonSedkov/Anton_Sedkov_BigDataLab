SELECT
  DISTINCT (s.id_street) AS street_ID,
  s.name_street AS street_name,
  MIN(sc.month) OVER (PARTITION BY s.id_street) AS from_month,
  MAX(sc.month) OVER (PARTITION BY s.id_street) AS till_month,
  COUNT(*) OVER (PARTITION BY s.id_street) AS crime_count
FROM street AS s
INNER JOIN location AS l ON s.id_street = l.id_street_fk
INNER JOIN street_crime AS sc ON l.latitude = sc.latitude_fk
  AND l.longitude = sc.longitude_fk
WHERE
  sc.month >= '2020-01'
  AND sc.month < '2021-01'
ORDER BY crime_count DESC;