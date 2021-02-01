SELECT
  DISTINCT s.id_street                                                    AS street_ID,
  s.name_street                                                  AS street_name,
  sc.category_outcome                                            AS outcome_category_value,
  COUNT(*) OVER (PARTITION BY s.id_street, sc.category_outcome)  AS crime_count_outcome,
  CAST(COUNT(*) OVER (PARTITION BY s.id_street, sc.category_outcome) * 100.00 /
    COUNT(*) OVER (PARTITION BY sc.category_outcome) AS real) AS percentage_street_for_category
FROM street AS s
INNER JOIN location AS l ON s.id_street = l.id_street_fk
INNER JOIN street_crime AS sc ON l.latitude = sc.latitude_fk
  AND l.longitude = sc.longitude_fk
WHERE sc.month >= '2020-01'
  AND sc.month <= '2020-12'
  AND category_outcome = 'Awaiting court outcome'
ORDER BY s.id_street;