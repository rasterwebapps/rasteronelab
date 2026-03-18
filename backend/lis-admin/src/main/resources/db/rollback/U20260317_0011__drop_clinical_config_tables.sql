-- Rollback: Drop auto_validation_rule, delta_check_config, and critical_value_config tables

DROP INDEX IF EXISTS idx_auto_validation_test_id;
DROP INDEX IF EXISTS idx_auto_validation_parameter_id;
DROP INDEX IF EXISTS idx_auto_validation_branch_id;
DROP TABLE IF EXISTS auto_validation_rule;

DROP INDEX IF EXISTS idx_delta_check_parameter_id;
DROP INDEX IF EXISTS idx_delta_check_branch_id;
DROP TABLE IF EXISTS delta_check_config;

DROP INDEX IF EXISTS idx_critical_config_parameter_id;
DROP INDEX IF EXISTS idx_critical_config_branch_id;
DROP TABLE IF EXISTS critical_value_config;
