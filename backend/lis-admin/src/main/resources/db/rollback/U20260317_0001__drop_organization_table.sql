-- Rollback: Drop organization table
-- Reverses V20260317_0001__create_organization_table.sql

DROP INDEX IF EXISTS idx_organization_is_active;
DROP INDEX IF EXISTS idx_organization_code;
DROP TABLE IF EXISTS organization;
