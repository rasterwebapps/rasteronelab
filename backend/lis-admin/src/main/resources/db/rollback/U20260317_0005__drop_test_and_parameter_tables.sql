-- Rollback: Drop test_parameter, parameter, and test_master tables

DROP INDEX IF EXISTS idx_test_param_parameter_id;
DROP INDEX IF EXISTS idx_test_param_test_id;
DROP TABLE IF EXISTS test_parameter;

DROP INDEX IF EXISTS idx_parameter_is_deleted;
DROP INDEX IF EXISTS idx_parameter_is_active;
DROP INDEX IF EXISTS idx_parameter_code;
DROP INDEX IF EXISTS idx_parameter_branch_id;
DROP TABLE IF EXISTS parameter;

DROP INDEX IF EXISTS idx_test_is_deleted;
DROP INDEX IF EXISTS idx_test_is_active;
DROP INDEX IF EXISTS idx_test_name;
DROP INDEX IF EXISTS idx_test_code;
DROP INDEX IF EXISTS idx_test_department_id;
DROP INDEX IF EXISTS idx_test_branch_id;
DROP TABLE IF EXISTS test_master;
