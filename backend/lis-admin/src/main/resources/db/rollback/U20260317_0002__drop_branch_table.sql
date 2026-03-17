-- Rollback: Drop branch table
-- Reverses V20260317_0002__create_branch_table.sql

DROP INDEX IF EXISTS idx_branch_is_deleted;
DROP INDEX IF EXISTS idx_branch_is_active;
DROP INDEX IF EXISTS idx_branch_code;
DROP INDEX IF EXISTS idx_branch_org_id;
DROP TABLE IF EXISTS branch;
