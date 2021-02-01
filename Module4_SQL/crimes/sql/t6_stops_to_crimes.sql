SELECT
  DISTINCT s.id_street,
           s.name_street,
           sc.month,
           count(CASE
                    WHEN sc.category = 'drugs' THEN 1
                    ELSE NULL
                END)
             OVER (PARTITION BY sc.month, s.id_street) AS drug_crimes,
           count(CASE
                    WHEN sas.object_of_search = 'Controlled drugs'
                        THEN 1
                    ELSE NULL
                END)
             OVER (PARTITION BY sc.month, s.id_street) AS drug_search,
           count(CASE
                    WHEN sc.category = 'possession-of-weapons'
                        THEN 1
                    ELSE NULL
                 END)
             OVER (PARTITION BY sc.month, s.id_street) AS weapons_crimes,
           count(CASE
                    WHEN sas.object_of_search = 'Offensive weapons'
                            OR sas.object_of_search = 'Firearms'
                        THEN 1
                    ELSE NULL
                END)
             OVER (PARTITION BY sc.month, s.id_street) AS weapons_search,
           count(CASE
                    WHEN sc.category = 'theft-from-the-person'
                            OR sc.category = 'shoplifting'
                        THEN 1
                    ELSE NULL
                END)
             OVER (PARTITION BY sc.month, s.id_street) AS theft_crimes,
           count(CASE
                    WHEN sas.object_of_search = 'Stolen goods'
                        THEN 1
                    ELSE NULL
                END)
             OVER (PARTITION BY sc.month, s.id_street) AS theft_search
FROM street AS s
INNER JOIN location AS l ON s.id_street = l.id_street_fk
INNER JOIN street_crime AS sc ON l.latitude = sc.latitude_fk AND l.longitude = sc.longitude_fk
INNER JOIN stop_and_search AS sas ON l.latitude = sas.latitude_fk AND l.longitude = sas.longitude_fk
WHERE sc.month > '2020-01'
  AND sc.month < '2021-01'
ORDER BY drug_crimes DESC, drug_search DESC