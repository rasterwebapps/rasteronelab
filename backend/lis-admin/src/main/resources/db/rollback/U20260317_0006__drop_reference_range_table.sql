-- Rollback: Drop reference_range table

DROP INDEX IF EXISTS idx_ref_range_is_deleted;
DROP INDEX IF EXISTS idx_ref_range_gender;
DROP INDEX IF EXISTS idx_ref_range_parameter_id;
DROP INDEX IF EXISTS idx_ref_range_branch_id;
DROP TABLE IF EXISTS reference_range;
