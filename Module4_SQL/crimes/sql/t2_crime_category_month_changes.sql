WITH category_count_month AS
  (SELECT
    Distinct category,
    month,
    COUNT(*) OVER (PARTITION BY category, month ORDER BY month) AS count_crime
  FROM street_crime
  ORDER BY category, month
  )
SELECT
  category AS crime_category,
  month AS month,
  count_crime AS current_month_count,
  LAG(count_crime) OVER (PARTITION BY category) AS previous_month_count,
  count_crime - LAG(count_crime) OVER (PARTITION BY category) AS delta_count,
  CAST((count_crime - LAG(count_crime) OVER (PARTITION BY category)) * 100.00 /
         LAG(count_crime) OVER (PARTITION BY category) AS real) AS basic_growth_rate
FROM category_count_month
WHERE month >= '2020-01'
  AND month <= '2020-12'
ORDER BY category, month;