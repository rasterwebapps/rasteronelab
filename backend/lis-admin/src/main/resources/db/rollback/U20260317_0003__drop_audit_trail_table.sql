-- Rollback: Drop audit_trail table
-- Reverses V20260317_0003__create_audit_trail_table.sql

DROP INDEX IF EXISTS idx_audit_trail_changed_by;
DROP INDEX IF EXISTS idx_audit_trail_changed_at;
DROP INDEX IF EXISTS idx_audit_trail_entity;
DROP INDEX IF EXISTS idx_audit_trail_branch_id;
DROP TABLE IF EXISTS audit_trail;
