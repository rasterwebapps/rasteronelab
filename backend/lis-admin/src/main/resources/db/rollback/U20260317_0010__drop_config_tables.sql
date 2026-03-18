-- Rollback: Drop holiday, working_hours, tat_configuration, and number_series tables

DROP INDEX IF EXISTS idx_holiday_date;
DROP INDEX IF EXISTS idx_holiday_branch_id;
DROP TABLE IF EXISTS holiday;

DROP INDEX IF EXISTS idx_working_hours_branch_id;
DROP TABLE IF EXISTS working_hours;

DROP INDEX IF EXISTS idx_tat_config_department_id;
DROP INDEX IF EXISTS idx_tat_config_test_id;
DROP INDEX IF EXISTS idx_tat_config_branch_id;
DROP TABLE IF EXISTS tat_configuration;

DROP INDEX IF EXISTS idx_number_series_branch_id;
DROP TABLE IF EXISTS number_series;
