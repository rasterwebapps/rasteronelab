-- Rollback: Drop panel_panel, panel_test, and test_panel tables

DROP INDEX IF EXISTS idx_panel_panel_child;
DROP INDEX IF EXISTS idx_panel_panel_parent;
DROP TABLE IF EXISTS panel_panel;

DROP INDEX IF EXISTS idx_panel_test_test_id;
DROP INDEX IF EXISTS idx_panel_test_panel_id;
DROP TABLE IF EXISTS panel_test;

DROP INDEX IF EXISTS idx_panel_is_deleted;
DROP INDEX IF EXISTS idx_panel_is_active;
DROP INDEX IF EXISTS idx_panel_code;
DROP INDEX IF EXISTS idx_panel_branch_id;
DROP TABLE IF EXISTS test_panel;
