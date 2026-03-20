-- Repeatable migration: Seed standard measurement units
-- Idempotent: uses WHERE NOT EXISTS on (branch_id, code)

-- ============================================================
-- Concentration
-- ============================================================
INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MG_DL', 'mg/dL', 'Concentration', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MG_DL');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'G_DL', 'g/dL', 'Concentration', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'G_DL');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'G_L', 'g/L', 'Concentration', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'G_L');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MMOL_L', 'mmol/L', 'Concentration', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MMOL_L');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'UMOL_L', 'µmol/L', 'Concentration', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'UMOL_L');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MEQ_L', 'mEq/L', 'Concentration', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MEQ_L');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MG_L', 'mg/L', 'Concentration', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MG_L');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'UG_DL', 'µg/dL', 'Concentration', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'UG_DL');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'NG_ML', 'ng/mL', 'Concentration', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'NG_ML');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'PG_ML', 'pg/mL', 'Concentration', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'PG_ML');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'NG_DL', 'ng/dL', 'Concentration', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'NG_DL');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'UG_L', 'µg/L', 'Concentration', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'UG_L');

-- ============================================================
-- Enzyme Activity
-- ============================================================
INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'IU_L', 'IU/L', 'Enzyme Activity', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'IU_L');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'IU_ML', 'IU/mL', 'Enzyme Activity', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'IU_ML');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MIU_L', 'mIU/L', 'Enzyme Activity', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MIU_L');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MIU_ML', 'mIU/mL', 'Enzyme Activity', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MIU_ML');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'U_L', 'U/L', 'Enzyme Activity', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'U_L');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'U_ML', 'U/mL', 'Enzyme Activity', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'U_ML');

-- ============================================================
-- Cell Counts
-- ============================================================
INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'CELLS_UL', 'cells/µL', 'Cell Count', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CELLS_UL');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'CELLS_MM3', 'cells/mm³', 'Cell Count', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CELLS_MM3');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'X103_UL', '×10³/µL', 'Cell Count', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'X103_UL');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'X106_UL', '×10⁶/µL', 'Cell Count', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'X106_UL');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'X109_L', '×10⁹/L', 'Cell Count', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'X109_L');

-- ============================================================
-- Microscopy
-- ============================================================
INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'PER_HPF', '/hpf', 'Microscopy', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'PER_HPF');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'PER_LPF', '/lpf', 'Microscopy', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'PER_LPF');

-- ============================================================
-- Percentage / Ratio
-- ============================================================
INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'PERCENT', '%', 'Percentage', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'PERCENT');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'RATIO', 'ratio', 'Ratio', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'RATIO');

-- ============================================================
-- Coagulation
-- ============================================================
INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'SECONDS', 'seconds', 'Coagulation', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'SECONDS');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'INR', 'INR', 'Coagulation', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'INR');

-- ============================================================
-- Molecular
-- ============================================================
INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'COPIES_ML', 'copies/mL', 'Molecular', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'COPIES_ML');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'LOG_COPIES_ML', 'log copies/mL', 'Molecular', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'LOG_COPIES_ML');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'CT_VALUE', 'Ct value', 'Molecular', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CT_VALUE');

-- ============================================================
-- Volume / Flow Rate / Kidney Function
-- ============================================================
INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'ML', 'mL', 'Volume', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ML');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'ML_MIN', 'mL/min', 'Flow Rate', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ML_MIN');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'ML_MIN_M2', 'mL/min/1.73m²', 'Kidney Function', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ML_MIN_M2');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'L', 'L', 'Volume', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'L');

-- ============================================================
-- Mass
-- ============================================================
INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MG', 'mg', 'Mass', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MG');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'G', 'g', 'Mass', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'G');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'KG', 'kg', 'Mass', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'KG');

-- ============================================================
-- Other
-- ============================================================
INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MM_HR', 'mm/hr', 'Sedimentation', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MM_HR');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MM', 'mm', 'Length', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MM');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MOSM_KG', 'mOsm/kg', 'Osmolality', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MOSM_KG');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'PH', 'pH', 'Acidity', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'PH');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'TITER', 'titer', 'Serology', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'TITER');

INSERT INTO measurement_unit (id, branch_id, code, name, category, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MMHG', 'mmHg', 'Pressure', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM measurement_unit WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MMHG');
