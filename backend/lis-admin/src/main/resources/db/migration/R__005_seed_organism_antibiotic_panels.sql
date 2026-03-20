-- Repeatable migration: Seed default antibiotic testing panels
-- Idempotent: uses WHERE NOT EXISTS on (branch_id, panel_name, antibiotic_code)

-- ============================================================
-- 1. Enterobacteriaceae UTI Panel
-- ============================================================
INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae UTI', 'Enterobacteriaceae', 'AMP', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae UTI' AND antibiotic_code = 'AMP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae UTI', 'Enterobacteriaceae', 'AMC', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae UTI' AND antibiotic_code = 'AMC');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae UTI', 'Enterobacteriaceae', 'GEN', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae UTI' AND antibiotic_code = 'GEN');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae UTI', 'Enterobacteriaceae', 'CIP', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae UTI' AND antibiotic_code = 'CIP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae UTI', 'Enterobacteriaceae', 'NIT', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae UTI' AND antibiotic_code = 'NIT');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae UTI', 'Enterobacteriaceae', 'COT', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae UTI' AND antibiotic_code = 'COT');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae UTI', 'Enterobacteriaceae', 'CAZ', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae UTI' AND antibiotic_code = 'CAZ');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae UTI', 'Enterobacteriaceae', 'CRO', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae UTI' AND antibiotic_code = 'CRO');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae UTI', 'Enterobacteriaceae', 'IPM', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae UTI' AND antibiotic_code = 'IPM');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae UTI', 'Enterobacteriaceae', 'MEM', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae UTI' AND antibiotic_code = 'MEM');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae UTI', 'Enterobacteriaceae', 'FEP', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae UTI' AND antibiotic_code = 'FEP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae UTI', 'Enterobacteriaceae', 'TZP', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae UTI' AND antibiotic_code = 'TZP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae UTI', 'Enterobacteriaceae', 'AMK', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae UTI' AND antibiotic_code = 'AMK');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae UTI', 'Enterobacteriaceae', 'LVX', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae UTI' AND antibiotic_code = 'LVX');

-- ============================================================
-- 2. Enterobacteriaceae Non-UTI Panel
-- ============================================================
INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae Non-UTI', 'Enterobacteriaceae', 'AMC', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae Non-UTI' AND antibiotic_code = 'AMC');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae Non-UTI', 'Enterobacteriaceae', 'GEN', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae Non-UTI' AND antibiotic_code = 'GEN');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae Non-UTI', 'Enterobacteriaceae', 'CIP', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae Non-UTI' AND antibiotic_code = 'CIP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae Non-UTI', 'Enterobacteriaceae', 'CAZ', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae Non-UTI' AND antibiotic_code = 'CAZ');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae Non-UTI', 'Enterobacteriaceae', 'CRO', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae Non-UTI' AND antibiotic_code = 'CRO');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae Non-UTI', 'Enterobacteriaceae', 'IPM', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae Non-UTI' AND antibiotic_code = 'IPM');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae Non-UTI', 'Enterobacteriaceae', 'MEM', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae Non-UTI' AND antibiotic_code = 'MEM');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae Non-UTI', 'Enterobacteriaceae', 'FEP', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae Non-UTI' AND antibiotic_code = 'FEP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae Non-UTI', 'Enterobacteriaceae', 'TZP', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae Non-UTI' AND antibiotic_code = 'TZP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae Non-UTI', 'Enterobacteriaceae', 'AMK', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae Non-UTI' AND antibiotic_code = 'AMK');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae Non-UTI', 'Enterobacteriaceae', 'COT', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae Non-UTI' AND antibiotic_code = 'COT');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacteriaceae Non-UTI', 'Enterobacteriaceae', 'ATM', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterobacteriaceae Non-UTI' AND antibiotic_code = 'ATM');

-- ============================================================
-- 3. Pseudomonas Panel
-- ============================================================
INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas', 'Pseudomonas aeruginosa', 'CAZ', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Pseudomonas' AND antibiotic_code = 'CAZ');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas', 'Pseudomonas aeruginosa', 'FEP', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Pseudomonas' AND antibiotic_code = 'FEP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas', 'Pseudomonas aeruginosa', 'IPM', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Pseudomonas' AND antibiotic_code = 'IPM');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas', 'Pseudomonas aeruginosa', 'MEM', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Pseudomonas' AND antibiotic_code = 'MEM');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas', 'Pseudomonas aeruginosa', 'CIP', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Pseudomonas' AND antibiotic_code = 'CIP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas', 'Pseudomonas aeruginosa', 'LVX', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Pseudomonas' AND antibiotic_code = 'LVX');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas', 'Pseudomonas aeruginosa', 'AMK', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Pseudomonas' AND antibiotic_code = 'AMK');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas', 'Pseudomonas aeruginosa', 'GEN', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Pseudomonas' AND antibiotic_code = 'GEN');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas', 'Pseudomonas aeruginosa', 'TZP', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Pseudomonas' AND antibiotic_code = 'TZP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas', 'Pseudomonas aeruginosa', 'COL', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Pseudomonas' AND antibiotic_code = 'COL');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas', 'Pseudomonas aeruginosa', 'ATM', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Pseudomonas' AND antibiotic_code = 'ATM');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas', 'Pseudomonas aeruginosa', 'TOB', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Pseudomonas' AND antibiotic_code = 'TOB');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas', 'Pseudomonas aeruginosa', 'DOR', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Pseudomonas' AND antibiotic_code = 'DOR');

-- ============================================================
-- 4. Acinetobacter Panel
-- ============================================================
INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Acinetobacter', 'Acinetobacter baumannii', 'IPM', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Acinetobacter' AND antibiotic_code = 'IPM');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Acinetobacter', 'Acinetobacter baumannii', 'MEM', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Acinetobacter' AND antibiotic_code = 'MEM');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Acinetobacter', 'Acinetobacter baumannii', 'AMK', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Acinetobacter' AND antibiotic_code = 'AMK');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Acinetobacter', 'Acinetobacter baumannii', 'GEN', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Acinetobacter' AND antibiotic_code = 'GEN');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Acinetobacter', 'Acinetobacter baumannii', 'CIP', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Acinetobacter' AND antibiotic_code = 'CIP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Acinetobacter', 'Acinetobacter baumannii', 'LVX', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Acinetobacter' AND antibiotic_code = 'LVX');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Acinetobacter', 'Acinetobacter baumannii', 'COL', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Acinetobacter' AND antibiotic_code = 'COL');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Acinetobacter', 'Acinetobacter baumannii', 'TGC', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Acinetobacter' AND antibiotic_code = 'TGC');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Acinetobacter', 'Acinetobacter baumannii', 'COT', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Acinetobacter' AND antibiotic_code = 'COT');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Acinetobacter', 'Acinetobacter baumannii', 'TZP', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Acinetobacter' AND antibiotic_code = 'TZP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Acinetobacter', 'Acinetobacter baumannii', 'FEP', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Acinetobacter' AND antibiotic_code = 'FEP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Acinetobacter', 'Acinetobacter baumannii', 'DOX', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Acinetobacter' AND antibiotic_code = 'DOX');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Acinetobacter', 'Acinetobacter baumannii', 'MIN', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Acinetobacter' AND antibiotic_code = 'MIN');

-- ============================================================
-- 5. Staphylococcus Skin Panel
-- ============================================================
INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus Skin', 'Staphylococcus', 'PEN', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Staphylococcus Skin' AND antibiotic_code = 'PEN');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus Skin', 'Staphylococcus', 'OXA', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Staphylococcus Skin' AND antibiotic_code = 'OXA');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus Skin', 'Staphylococcus', 'ERY', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Staphylococcus Skin' AND antibiotic_code = 'ERY');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus Skin', 'Staphylococcus', 'CLI', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Staphylococcus Skin' AND antibiotic_code = 'CLI');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus Skin', 'Staphylococcus', 'VAN', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Staphylococcus Skin' AND antibiotic_code = 'VAN');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus Skin', 'Staphylococcus', 'LZD', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Staphylococcus Skin' AND antibiotic_code = 'LZD');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus Skin', 'Staphylococcus', 'COT', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Staphylococcus Skin' AND antibiotic_code = 'COT');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus Skin', 'Staphylococcus', 'CIP', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Staphylococcus Skin' AND antibiotic_code = 'CIP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus Skin', 'Staphylococcus', 'GEN', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Staphylococcus Skin' AND antibiotic_code = 'GEN');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus Skin', 'Staphylococcus', 'TET', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Staphylococcus Skin' AND antibiotic_code = 'TET');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus Skin', 'Staphylococcus', 'DOX', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Staphylococcus Skin' AND antibiotic_code = 'DOX');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus Skin', 'Staphylococcus', 'RIF', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Staphylococcus Skin' AND antibiotic_code = 'RIF');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus Skin', 'Staphylococcus', 'FUS', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Staphylococcus Skin' AND antibiotic_code = 'FUS');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus Skin', 'Staphylococcus', 'MUP', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Staphylococcus Skin' AND antibiotic_code = 'MUP');

-- ============================================================
-- 6. MRSA Panel
-- ============================================================
INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MRSA', 'Staphylococcus aureus (MRSA)', 'VAN', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'MRSA' AND antibiotic_code = 'VAN');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MRSA', 'Staphylococcus aureus (MRSA)', 'LZD', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'MRSA' AND antibiotic_code = 'LZD');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MRSA', 'Staphylococcus aureus (MRSA)', 'TEC', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'MRSA' AND antibiotic_code = 'TEC');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MRSA', 'Staphylococcus aureus (MRSA)', 'DAP', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'MRSA' AND antibiotic_code = 'DAP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MRSA', 'Staphylococcus aureus (MRSA)', 'COT', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'MRSA' AND antibiotic_code = 'COT');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MRSA', 'Staphylococcus aureus (MRSA)', 'RIF', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'MRSA' AND antibiotic_code = 'RIF');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MRSA', 'Staphylococcus aureus (MRSA)', 'FUS', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'MRSA' AND antibiotic_code = 'FUS');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MRSA', 'Staphylococcus aureus (MRSA)', 'MUP', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'MRSA' AND antibiotic_code = 'MUP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MRSA', 'Staphylococcus aureus (MRSA)', 'TET', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'MRSA' AND antibiotic_code = 'TET');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MRSA', 'Staphylococcus aureus (MRSA)', 'MIN', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'MRSA' AND antibiotic_code = 'MIN');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MRSA', 'Staphylococcus aureus (MRSA)', 'DOX', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'MRSA' AND antibiotic_code = 'DOX');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MRSA', 'Staphylococcus aureus (MRSA)', 'CHL', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'MRSA' AND antibiotic_code = 'CHL');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MRSA', 'Staphylococcus aureus (MRSA)', 'TGC', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'MRSA' AND antibiotic_code = 'TGC');

-- ============================================================
-- 7. Streptococcus Panel
-- ============================================================
INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Streptococcus', 'Streptococcus', 'PEN', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Streptococcus' AND antibiotic_code = 'PEN');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Streptococcus', 'Streptococcus', 'ERY', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Streptococcus' AND antibiotic_code = 'ERY');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Streptococcus', 'Streptococcus', 'CLI', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Streptococcus' AND antibiotic_code = 'CLI');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Streptococcus', 'Streptococcus', 'VAN', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Streptococcus' AND antibiotic_code = 'VAN');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Streptococcus', 'Streptococcus', 'LZD', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Streptococcus' AND antibiotic_code = 'LZD');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Streptococcus', 'Streptococcus', 'CRO', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Streptococcus' AND antibiotic_code = 'CRO');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Streptococcus', 'Streptococcus', 'LVX', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Streptococcus' AND antibiotic_code = 'LVX');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Streptococcus', 'Streptococcus', 'TET', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Streptococcus' AND antibiotic_code = 'TET');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Streptococcus', 'Streptococcus', 'CHL', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Streptococcus' AND antibiotic_code = 'CHL');

-- ============================================================
-- 8. Enterococcus Panel
-- ============================================================
INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterococcus', 'Enterococcus', 'AMP', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterococcus' AND antibiotic_code = 'AMP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterococcus', 'Enterococcus', 'VAN', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterococcus' AND antibiotic_code = 'VAN');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterococcus', 'Enterococcus', 'LZD', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterococcus' AND antibiotic_code = 'LZD');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterococcus', 'Enterococcus', 'TEC', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterococcus' AND antibiotic_code = 'TEC');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterococcus', 'Enterococcus', 'DAP', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterococcus' AND antibiotic_code = 'DAP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterococcus', 'Enterococcus', 'GEN', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterococcus' AND antibiotic_code = 'GEN');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterococcus', 'Enterococcus', 'NIT', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterococcus' AND antibiotic_code = 'NIT');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterococcus', 'Enterococcus', 'CIP', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterococcus' AND antibiotic_code = 'CIP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterococcus', 'Enterococcus', 'TET', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Enterococcus' AND antibiotic_code = 'TET');

-- ============================================================
-- 9. Gram-negative Respiratory Panel
-- ============================================================
INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Gram-negative Respiratory', 'Gram-negative', 'AMC', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Gram-negative Respiratory' AND antibiotic_code = 'AMC');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Gram-negative Respiratory', 'Gram-negative', 'CRO', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Gram-negative Respiratory' AND antibiotic_code = 'CRO');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Gram-negative Respiratory', 'Gram-negative', 'FEP', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Gram-negative Respiratory' AND antibiotic_code = 'FEP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Gram-negative Respiratory', 'Gram-negative', 'LVX', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Gram-negative Respiratory' AND antibiotic_code = 'LVX');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Gram-negative Respiratory', 'Gram-negative', 'MXF', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Gram-negative Respiratory' AND antibiotic_code = 'MXF');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Gram-negative Respiratory', 'Gram-negative', 'AZM', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Gram-negative Respiratory' AND antibiotic_code = 'AZM');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Gram-negative Respiratory', 'Gram-negative', 'CAZ', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Gram-negative Respiratory' AND antibiotic_code = 'CAZ');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Gram-negative Respiratory', 'Gram-negative', 'IPM', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Gram-negative Respiratory' AND antibiotic_code = 'IPM');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Gram-negative Respiratory', 'Gram-negative', 'MEM', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Gram-negative Respiratory' AND antibiotic_code = 'MEM');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Gram-negative Respiratory', 'Gram-negative', 'TZP', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Gram-negative Respiratory' AND antibiotic_code = 'TZP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Gram-negative Respiratory', 'Gram-negative', 'AMK', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Gram-negative Respiratory' AND antibiotic_code = 'AMK');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Gram-negative Respiratory', 'Gram-negative', 'COT', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Gram-negative Respiratory' AND antibiotic_code = 'COT');

-- ============================================================
-- 10. Anaerobes Panel
-- ============================================================
INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Anaerobes', 'Anaerobes', 'MTZ', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Anaerobes' AND antibiotic_code = 'MTZ');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Anaerobes', 'Anaerobes', 'CLI', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Anaerobes' AND antibiotic_code = 'CLI');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Anaerobes', 'Anaerobes', 'IPM', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Anaerobes' AND antibiotic_code = 'IPM');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Anaerobes', 'Anaerobes', 'MEM', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Anaerobes' AND antibiotic_code = 'MEM');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Anaerobes', 'Anaerobes', 'AMC', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Anaerobes' AND antibiotic_code = 'AMC');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Anaerobes', 'Anaerobes', 'TZP', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Anaerobes' AND antibiotic_code = 'TZP');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Anaerobes', 'Anaerobes', 'CHL', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Anaerobes' AND antibiotic_code = 'CHL');

-- ============================================================
-- 11. Candida Panel
-- ============================================================
INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Candida', 'Candida', 'FLC', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Candida' AND antibiotic_code = 'FLC');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Candida', 'Candida', 'VRC', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Candida' AND antibiotic_code = 'VRC');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Candida', 'Candida', 'AMB', 'PRIMARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Candida' AND antibiotic_code = 'AMB');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Candida', 'Candida', 'CAS', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Candida' AND antibiotic_code = 'CAS');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Candida', 'Candida', 'MCF', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Candida' AND antibiotic_code = 'MCF');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Candida', 'Candida', 'ITC', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Candida' AND antibiotic_code = 'ITC');

INSERT INTO antibiotic_panel (id, branch_id, panel_name, organism_group, antibiotic_code, test_priority, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Candida', 'Candida', '5FC', 'SECONDARY', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic_panel WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND panel_name = 'Candida' AND antibiotic_code = '5FC');
