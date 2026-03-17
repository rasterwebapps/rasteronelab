-- Rollback: Drop branch_department and department tables

DROP INDEX IF EXISTS idx_branch_dept_department_id;
DROP INDEX IF EXISTS idx_branch_dept_branch_id;
DROP TABLE IF EXISTS branch_department;

DROP INDEX IF EXISTS idx_department_is_deleted;
DROP INDEX IF EXISTS idx_department_is_active;
DROP INDEX IF EXISTS idx_department_code;
DROP INDEX IF EXISTS idx_department_org_id;
DROP TABLE IF EXISTS department;
