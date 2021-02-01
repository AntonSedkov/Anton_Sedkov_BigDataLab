WITH aggregate AS (
  SELECT
    DISTINCT officer_defined_ethnicity,
    COUNT(*) OVER (PARTITION BY officer_defined_ethnicity) AS total_stops_etnhicity,
    COUNT(outcome) OVER (PARTITION BY officer_defined_ethnicity) AS outcome_ethnicity,
    COUNT(CASE WHEN outcome = 'Arrest' THEN 1 ELSE NULL END) OVER (PARTITION BY officer_defined_ethnicity) AS arrests,
    COUNT(CASE WHEN outcome = 'A no further action disposal' THEN 1 ELSE NULL END)
        OVER (PARTITION BY officer_defined_ethnicity) AS disposals,
    object_of_search,
    COUNT(object_of_search) OVER (PARTITION BY officer_defined_ethnicity, object_of_search) AS count_object_search
    FROM stop_and_search
    WHERE object_of_search != 'null'
      AND officer_defined_ethnicity != 'null'
      AND datetime >= '2020-01'
      AND datetime <= '2021-01')
SELECT
  DISTINCT officer_defined_ethnicity,
  total_stops_etnhicity,
  CAST(arrests * 100.0/total_stops_etnhicity AS real) AS arrest_rate,
  CAST(disposals * 100.0/total_stops_etnhicity AS real) AS no_action_rate,
  CAST((outcome_ethnicity-arrests-disposals)*100.0/total_stops_etnhicity AS real) AS other_rate,
  FIRST_VALUE(object_of_search) OVER (PARTITION BY officer_defined_ethnicity ORDER BY count_object_search DESC) AS most_popular_obj_search
FROM aggregate
ORDER BY total_stops_etnhicity DESC;