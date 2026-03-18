-- Rollback: Drop antibiotic_organism_mapping, microorganism, and antibiotic tables

DROP INDEX IF EXISTS idx_abx_org_microorganism_id;
DROP INDEX IF EXISTS idx_abx_org_antibiotic_id;
DROP INDEX IF EXISTS idx_abx_org_branch_id;
DROP TABLE IF EXISTS antibiotic_organism_mapping;

DROP INDEX IF EXISTS idx_microorganism_is_deleted;
DROP INDEX IF EXISTS idx_microorganism_is_active;
DROP INDEX IF EXISTS idx_microorganism_gram_type;
DROP INDEX IF EXISTS idx_microorganism_code;
DROP INDEX IF EXISTS idx_microorganism_branch_id;
DROP TABLE IF EXISTS microorganism;

DROP INDEX IF EXISTS idx_antibiotic_is_deleted;
DROP INDEX IF EXISTS idx_antibiotic_is_active;
DROP INDEX IF EXISTS idx_antibiotic_group;
DROP INDEX IF EXISTS idx_antibiotic_code;
DROP INDEX IF EXISTS idx_antibiotic_branch_id;
DROP TABLE IF EXISTS antibiotic;
