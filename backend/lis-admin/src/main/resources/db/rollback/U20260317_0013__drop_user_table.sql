-- Rollback: Drop app_user table

DROP INDEX IF EXISTS idx_user_is_deleted;
DROP INDEX IF EXISTS idx_user_is_active;
DROP INDEX IF EXISTS idx_user_employee_id;
DROP INDEX IF EXISTS idx_user_username;
DROP INDEX IF EXISTS idx_user_keycloak_id;
DROP INDEX IF EXISTS idx_user_branch_id;
DROP TABLE IF EXISTS app_user;
