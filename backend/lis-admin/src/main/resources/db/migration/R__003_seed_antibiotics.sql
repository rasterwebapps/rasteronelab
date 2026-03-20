-- Seed Data: Antibiotics (CLSI standard codes)
-- Idempotent: uses WHERE NOT EXISTS since antibiotic has no unique constraint on (branch_id, code)
-- branch_id '00000000-0000-0000-0000-000000000000' is the system-level placeholder.
-- antibiotic_group values match CLSI drug class categories.
-- clsi_method: DISK_DIFFUSION or MIC

-- ============================================================
-- PENICILLINS
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Ampicillin', 'AMP', 'Penicillins', 17, 13, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'AMP');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Amoxicillin', 'AMX', 'Penicillins', 17, 13, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'AMX');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Amoxicillin-Clavulanate', 'AMC', 'Penicillins', 18, 13, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'AMC');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Piperacillin', 'PIP', 'Penicillins', 21, 17, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'PIP');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Piperacillin-Tazobactam', 'TZP', 'Penicillins', 21, 17, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'TZP');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Oxacillin', 'OXA', 'Penicillins', 13, 10, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'OXA');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Penicillin G', 'PEN', 'Penicillins', 29, 28, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'PEN');

-- ============================================================
-- CEPHALOSPORINS - 1st Generation
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Cefazolin', 'CZO', 'Cephalosporins 1G', 23, 19, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CZO');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Cephalexin', 'LEX', 'Cephalosporins 1G', 18, 14, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'LEX');

-- ============================================================
-- CEPHALOSPORINS - 2nd Generation
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Cefuroxime', 'CXM', 'Cephalosporins 2G', 18, 14, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CXM');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Cefaclor', 'CEC', 'Cephalosporins 2G', 18, 14, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CEC');

-- ============================================================
-- CEPHALOSPORINS - 3rd Generation
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Cefotaxime', 'CTX', 'Cephalosporins 3G', 26, 22, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CTX');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Ceftazidime', 'CAZ', 'Cephalosporins 3G', 21, 17, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CAZ');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Ceftriaxone', 'CRO', 'Cephalosporins 3G', 23, 19, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CRO');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Cefixime', 'CFM', 'Cephalosporins 3G', 19, 15, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CFM');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Cefpodoxime', 'CPD', 'Cephalosporins 3G', 21, 17, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CPD');

-- ============================================================
-- CEPHALOSPORINS - 4th & 5th Generation
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Cefepime', 'FEP', 'Cephalosporins 4G', 25, 19, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'FEP');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Ceftaroline', 'CPT', 'Cephalosporins 5G', 24, 20, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CPT');

-- ============================================================
-- CARBAPENEMS
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Imipenem', 'IPM', 'Carbapenems', 23, 19, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'IPM');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Meropenem', 'MEM', 'Carbapenems', 23, 19, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MEM');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Ertapenem', 'ETP', 'Carbapenems', 23, 19, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ETP');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Doripenem', 'DOR', 'Carbapenems', 23, 19, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'DOR');

-- ============================================================
-- MONOBACTAMS
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Aztreonam', 'ATM', 'Monobactams', 21, 17, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ATM');

-- ============================================================
-- AMINOGLYCOSIDES
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Gentamicin', 'GEN', 'Aminoglycosides', 15, 12, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'GEN');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Amikacin', 'AMK', 'Aminoglycosides', 17, 14, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'AMK');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Tobramycin', 'TOB', 'Aminoglycosides', 15, 12, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'TOB');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Netilmicin', 'NET', 'Aminoglycosides', 15, 12, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'NET');

-- ============================================================
-- FLUOROQUINOLONES
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Ciprofloxacin', 'CIP', 'Fluoroquinolones', 21, 15, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CIP');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Levofloxacin', 'LVX', 'Fluoroquinolones', 17, 13, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'LVX');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Moxifloxacin', 'MXF', 'Fluoroquinolones', 19, 15, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MXF');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Norfloxacin', 'NOR', 'Fluoroquinolones', 17, 12, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'NOR');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Ofloxacin', 'OFX', 'Fluoroquinolones', 16, 12, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'OFX');

-- ============================================================
-- MACROLIDES
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Azithromycin', 'AZM', 'Macrolides', 18, 14, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'AZM');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Erythromycin', 'ERY', 'Macrolides', 23, 12, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ERY');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Clarithromycin', 'CLR', 'Macrolides', 21, 16, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CLR');

-- ============================================================
-- LINCOSAMIDES
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Clindamycin', 'CLI', 'Lincosamides', 21, 14, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CLI');

-- ============================================================
-- GLYCOPEPTIDES
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Vancomycin', 'VAN', 'Glycopeptides', 15, 14, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'VAN');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Teicoplanin', 'TEC', 'Glycopeptides', 14, 10, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'TEC');

-- ============================================================
-- OXAZOLIDINONES
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Linezolid', 'LZD', 'Oxazolidinones', 21, 20, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'LZD');

-- ============================================================
-- LIPOPEPTIDES
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Daptomycin', 'DAP', 'Lipopeptides', NULL, NULL, 'MIC', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'DAP');

-- ============================================================
-- TETRACYCLINES
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Tetracycline', 'TET', 'Tetracyclines', 19, 14, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'TET');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Doxycycline', 'DOX', 'Tetracyclines', 16, 12, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'DOX');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Minocycline', 'MIN', 'Tetracyclines', 16, 12, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MIN');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Tigecycline', 'TGC', 'Tetracyclines', 19, 15, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'TGC');

-- ============================================================
-- SULFONAMIDES
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Co-trimoxazole (TMP-SMX)', 'COT', 'Sulfonamides', 16, 10, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'COT');

-- ============================================================
-- NITROFURANS
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Nitrofurantoin', 'NIT', 'Nitrofurans', 17, 14, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'NIT');

-- ============================================================
-- POLYMYXINS
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Colistin', 'COL', 'Polymyxins', NULL, NULL, 'MIC', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'COL');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Polymyxin B', 'POL', 'Polymyxins', NULL, NULL, 'MIC', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'POL');

-- ============================================================
-- RIFAMYCINS
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Rifampicin', 'RIF', 'Rifamycins', 20, 16, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'RIF');

-- ============================================================
-- MISCELLANEOUS ANTIBACTERIALS
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Chloramphenicol', 'CHL', 'Miscellaneous', 18, 12, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CHL');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Fosfomycin', 'FOS', 'Miscellaneous', 16, 12, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'FOS');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Fusidic Acid', 'FUS', 'Miscellaneous', 24, 22, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'FUS');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Metronidazole', 'MTZ', 'Miscellaneous', 25, 20, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MTZ');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Mupirocin', 'MUP', 'Miscellaneous', 14, 10, 'DISK_DIFFUSION', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MUP');

-- ============================================================
-- ANTIFUNGALS
-- ============================================================
INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Fluconazole', 'FLC', 'Antifungals', NULL, NULL, 'MIC', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'FLC');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Voriconazole', 'VRC', 'Antifungals', NULL, NULL, 'MIC', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'VRC');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Amphotericin B', 'AMB', 'Antifungals', NULL, NULL, 'MIC', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'AMB');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Caspofungin', 'CAS', 'Antifungals', NULL, NULL, 'MIC', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CAS');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Micafungin', 'MCF', 'Antifungals', NULL, NULL, 'MIC', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MCF');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Itraconazole', 'ITC', 'Antifungals', NULL, NULL, 'MIC', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ITC');

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, clsi_breakpoint_s, clsi_breakpoint_r, clsi_method, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Flucytosine', '5FC', 'Antifungals', NULL, NULL, 'MIC', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM antibiotic WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = '5FC');
