-- Repeatable migration: Seed CLSI M100 2024 disc diffusion breakpoints
-- Idempotent: uses WHERE NOT EXISTS on (branch_id, organism_group, antibiotic_code, method)

-- ============================================================
-- Enterobacteriaceae
-- ============================================================
INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae', 'AMP', 'DISC_DIFFUSION', 17, 14, 16, 13, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterobacteriaceae' AND antibiotic_code = 'AMP' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae', 'AMC', 'DISC_DIFFUSION', 18, 15, 17, 14, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterobacteriaceae' AND antibiotic_code = 'AMC' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae', 'CZO', 'DISC_DIFFUSION', 23, 20, 22, 19, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterobacteriaceae' AND antibiotic_code = 'CZO' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae', 'CAZ', 'DISC_DIFFUSION', 21, 18, 20, 17, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterobacteriaceae' AND antibiotic_code = 'CAZ' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae', 'CRO', 'DISC_DIFFUSION', 23, 20, 22, 19, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterobacteriaceae' AND antibiotic_code = 'CRO' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae', 'CTX', 'DISC_DIFFUSION', 26, 23, 25, 22, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterobacteriaceae' AND antibiotic_code = 'CTX' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae', 'FEP', 'DISC_DIFFUSION', 25, 22, 24, 19, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterobacteriaceae' AND antibiotic_code = 'FEP' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae', 'IPM', 'DISC_DIFFUSION', 23, 20, 22, 19, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterobacteriaceae' AND antibiotic_code = 'IPM' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae', 'MEM', 'DISC_DIFFUSION', 23, 20, 22, 19, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterobacteriaceae' AND antibiotic_code = 'MEM' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae', 'ETP', 'DISC_DIFFUSION', 23, 20, 22, 19, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterobacteriaceae' AND antibiotic_code = 'ETP' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae', 'ATM', 'DISC_DIFFUSION', 21, 18, 20, 17, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterobacteriaceae' AND antibiotic_code = 'ATM' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae', 'GEN', 'DISC_DIFFUSION', 15, 13, 14, 12, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterobacteriaceae' AND antibiotic_code = 'GEN' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae', 'AMK', 'DISC_DIFFUSION', 17, 15, 16, 14, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterobacteriaceae' AND antibiotic_code = 'AMK' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae', 'CIP', 'DISC_DIFFUSION', 21, 16, 20, 15, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterobacteriaceae' AND antibiotic_code = 'CIP' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae', 'LVX', 'DISC_DIFFUSION', 17, 14, 16, 13, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterobacteriaceae' AND antibiotic_code = 'LVX' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae', 'COT', 'DISC_DIFFUSION', 16, 11, 15, 10, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterobacteriaceae' AND antibiotic_code = 'COT' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae', 'NIT', 'DISC_DIFFUSION', 17, 15, 16, 14, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterobacteriaceae' AND antibiotic_code = 'NIT' AND method = 'DISC_DIFFUSION');

-- ============================================================
-- Pseudomonas aeruginosa
-- ============================================================
INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas aeruginosa', 'CAZ', 'DISC_DIFFUSION', 18, 15, 17, 14, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Pseudomonas aeruginosa' AND antibiotic_code = 'CAZ' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas aeruginosa', 'FEP', 'DISC_DIFFUSION', 18, 15, 17, 14, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Pseudomonas aeruginosa' AND antibiotic_code = 'FEP' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas aeruginosa', 'IPM', 'DISC_DIFFUSION', 19, 16, 18, 15, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Pseudomonas aeruginosa' AND antibiotic_code = 'IPM' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas aeruginosa', 'MEM', 'DISC_DIFFUSION', 19, 16, 18, 15, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Pseudomonas aeruginosa' AND antibiotic_code = 'MEM' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas aeruginosa', 'CIP', 'DISC_DIFFUSION', 21, 16, 20, 15, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Pseudomonas aeruginosa' AND antibiotic_code = 'CIP' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas aeruginosa', 'LVX', 'DISC_DIFFUSION', 17, 14, 16, 13, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Pseudomonas aeruginosa' AND antibiotic_code = 'LVX' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas aeruginosa', 'AMK', 'DISC_DIFFUSION', 17, 15, 16, 14, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Pseudomonas aeruginosa' AND antibiotic_code = 'AMK' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas aeruginosa', 'GEN', 'DISC_DIFFUSION', 15, 13, 14, 12, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Pseudomonas aeruginosa' AND antibiotic_code = 'GEN' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas aeruginosa', 'TZP', 'DISC_DIFFUSION', 21, 18, 20, 17, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Pseudomonas aeruginosa' AND antibiotic_code = 'TZP' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas aeruginosa', 'ATM', 'DISC_DIFFUSION', 22, 16, 21, 15, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Pseudomonas aeruginosa' AND antibiotic_code = 'ATM' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas aeruginosa', 'DOR', 'DISC_DIFFUSION', 23, 20, 22, 19, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Pseudomonas aeruginosa' AND antibiotic_code = 'DOR' AND method = 'DISC_DIFFUSION');

-- ============================================================
-- Staphylococcus
-- ============================================================
INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus', 'OXA', 'DISC_DIFFUSION', 13, NULL, NULL, 10, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Staphylococcus' AND antibiotic_code = 'OXA' AND method = 'DISC_DIFFUSION');

-- VAN disc diffusion not reliable; include for completeness
INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus', 'VAN', 'DISC_DIFFUSION', 15, NULL, NULL, 14, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Staphylococcus' AND antibiotic_code = 'VAN' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus', 'ERY', 'DISC_DIFFUSION', 23, 14, 22, 13, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Staphylococcus' AND antibiotic_code = 'ERY' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus', 'CLI', 'DISC_DIFFUSION', 21, 15, 20, 14, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Staphylococcus' AND antibiotic_code = 'CLI' AND method = 'DISC_DIFFUSION');

-- LZD vs Staphylococcus: CLSI M100 defines S>=21, R<=20 with no intermediate category
INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus', 'LZD', 'DISC_DIFFUSION', 21, NULL, NULL, 20, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Staphylococcus' AND antibiotic_code = 'LZD' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus', 'CIP', 'DISC_DIFFUSION', 21, 16, 20, 15, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Staphylococcus' AND antibiotic_code = 'CIP' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus', 'GEN', 'DISC_DIFFUSION', 15, 13, 14, 12, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Staphylococcus' AND antibiotic_code = 'GEN' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus', 'TET', 'DISC_DIFFUSION', 19, 15, 18, 14, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Staphylococcus' AND antibiotic_code = 'TET' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus', 'COT', 'DISC_DIFFUSION', 16, 11, 15, 10, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Staphylococcus' AND antibiotic_code = 'COT' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus', 'RIF', 'DISC_DIFFUSION', 20, 17, 19, 16, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Staphylococcus' AND antibiotic_code = 'RIF' AND method = 'DISC_DIFFUSION');

-- ============================================================
-- Streptococcus
-- ============================================================
INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Streptococcus', 'PEN', 'DISC_DIFFUSION', 24, 22, 23, 21, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Streptococcus' AND antibiotic_code = 'PEN' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Streptococcus', 'ERY', 'DISC_DIFFUSION', 21, 16, 20, 15, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Streptococcus' AND antibiotic_code = 'ERY' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Streptococcus', 'CLI', 'DISC_DIFFUSION', 19, 16, 18, 15, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Streptococcus' AND antibiotic_code = 'CLI' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Streptococcus', 'VAN', 'DISC_DIFFUSION', 17, NULL, NULL, 16, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Streptococcus' AND antibiotic_code = 'VAN' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Streptococcus', 'LZD', 'DISC_DIFFUSION', 21, NULL, NULL, 20, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Streptococcus' AND antibiotic_code = 'LZD' AND method = 'DISC_DIFFUSION');

-- ============================================================
-- Enterococcus
-- ============================================================
INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterococcus', 'AMP', 'DISC_DIFFUSION', 17, NULL, NULL, 16, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterococcus' AND antibiotic_code = 'AMP' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterococcus', 'VAN', 'DISC_DIFFUSION', 17, 15, 16, 14, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterococcus' AND antibiotic_code = 'VAN' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterococcus', 'LZD', 'DISC_DIFFUSION', 23, 21, 22, 20, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterococcus' AND antibiotic_code = 'LZD' AND method = 'DISC_DIFFUSION');

INSERT INTO clsi_breakpoint (id, branch_id, organism_group, antibiotic_code, method, sensitive_threshold, intermediate_min, intermediate_max, resistant_threshold, unit, guideline_version, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterococcus', 'TEC', 'DISC_DIFFUSION', 14, 11, 13, 10, 'mm', 'CLSI M100 2024', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM clsi_breakpoint WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND organism_group = 'Enterococcus' AND antibiotic_code = 'TEC' AND method = 'DISC_DIFFUSION');
