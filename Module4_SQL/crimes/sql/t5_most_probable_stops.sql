WITH aggregation AS (
  SELECT
    DISTINCT s.id_street,
    s.name_street,
    sas.age_range,
    COUNT(*) OVER (PARTITION BY sas.age_range, s.id_street) AS q_age,
    sas.gender,
    COUNT(*) OVER (PARTITION BY sas.gender, s.id_street) AS q_gender,
    sas.officer_defined_ethnicity,
    COUNT(*) OVER (PARTITION BY sas.officer_defined_ethnicity, s.id_street) AS q_ethnicity,
    sas.object_of_search,
    COUNT(*) OVER (PARTITION BY sas.object_of_search, s.id_street) AS q_obj_search,
    sas.outcome,
    COUNT(*) OVER (PARTITION BY sas.outcome, s.id_street) AS q_outcome
  FROM street AS s
  INNER JOIN location AS l ON s.id_street = l.id_street_fk
  INNER JOIN stop_and_search AS sas ON l.latitude = sas.latitude_fk
    AND l.longitude = sas.longitude_fk
  WHERE sas.age_range != 'null'
    AND sas.gender != 'null'
    AND sas.officer_defined_ethnicity != 'null'
    AND sas.object_of_search != 'null'
    AND sas.outcome != 'null'
    AND sas.datetime >= '2020-01'
    AND sas.datetime <= '2021-01')
SELECT
  DISTINCT id_street,
  name_street,
  FIRST_VALUE(age_range) OVER (PARTITION BY id_street ORDER BY q_age DESC),
  FIRST_VALUE(gender) OVER (PARTITION BY id_street ORDER BY q_gender DESC),
  FIRST_VALUE(officer_defined_ethnicity) OVER (PARTITION BY id_street ORDER BY q_ethnicity DESC),
  FIRST_VALUE(object_of_search) OVER (PARTITION BY id_street ORDER BY q_obj_search DESC),
  FIRST_VALUE(outcome) OVER (PARTITION BY id_street ORDER BY q_outcome DESC)
FROM aggregation