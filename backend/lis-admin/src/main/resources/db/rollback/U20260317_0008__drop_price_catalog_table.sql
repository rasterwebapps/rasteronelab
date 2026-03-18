-- Rollback: Drop price_catalog table

DROP INDEX IF EXISTS idx_price_catalog_is_deleted;
DROP INDEX IF EXISTS idx_price_catalog_is_active;
DROP INDEX IF EXISTS idx_price_catalog_rate_list_type;
DROP INDEX IF EXISTS idx_price_catalog_panel_id;
DROP INDEX IF EXISTS idx_price_catalog_test_id;
DROP INDEX IF EXISTS idx_price_catalog_branch_id;
DROP TABLE IF EXISTS price_catalog;
