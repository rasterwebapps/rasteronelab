-- Repeatable migration: Critical value reference ranges per parameter
-- Uses WHERE NOT EXISTS on (branch_id, parameter_code, gender)

-- Glucose
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'GLUCOSE', 'ALL', NULL, NULL, 40, 500, 'mg/dL', 'STAT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'GLUCOSE' AND gender = 'ALL');

-- Potassium
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'POTASSIUM', 'ALL', NULL, NULL, 2.5, 6.5, 'mEq/L', 'STAT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'POTASSIUM' AND gender = 'ALL');

-- Sodium
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'SODIUM', 'ALL', NULL, NULL, 120, 160, 'mEq/L', 'STAT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'SODIUM' AND gender = 'ALL');

-- Calcium
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'CALCIUM', 'ALL', NULL, NULL, 6.0, 13.0, 'mg/dL', 'STAT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'CALCIUM' AND gender = 'ALL');

-- Hemoglobin
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'HGB', 'ALL', NULL, NULL, 5.0, 20.0, 'g/dL', 'STAT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'HGB' AND gender = 'ALL');

-- White Blood Cell count
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'WBC', 'ALL', NULL, NULL, 2.0, 30.0, '×10³/µL', 'URGENT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'WBC' AND gender = 'ALL');

-- Platelets
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'PLT', 'ALL', NULL, NULL, 20, 1000, '×10³/µL', 'URGENT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'PLT' AND gender = 'ALL');

-- INR (critical_low is NULL)
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'INR', 'ALL', NULL, NULL, NULL, 5.0, 'INR', 'STAT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'INR' AND gender = 'ALL');

-- Prothrombin Time (critical_low is NULL)
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'PT', 'ALL', NULL, NULL, NULL, 30, 'seconds', 'URGENT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'PT' AND gender = 'ALL');

-- Creatinine (critical_low is NULL)
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'CREATININE', 'ALL', NULL, NULL, NULL, 10.0, 'mg/dL', 'URGENT', false, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'CREATININE' AND gender = 'ALL');

-- Total Bilirubin (critical_low is NULL)
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'TBILI', 'ALL', NULL, NULL, NULL, 15.0, 'mg/dL', 'URGENT', false, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'TBILI' AND gender = 'ALL');

-- Troponin I (critical_low is NULL)
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'TROPONIN_I', 'ALL', NULL, NULL, NULL, 0.4, 'ng/mL', 'STAT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'TROPONIN_I' AND gender = 'ALL');

-- Lactate (critical_low is NULL)
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'LACTATE', 'ALL', NULL, NULL, NULL, 4.0, 'mmol/L', 'STAT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'LACTATE' AND gender = 'ALL');

-- Blood pH
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'BLOOD_PH', 'ALL', NULL, NULL, 7.20, 7.60, 'pH', 'STAT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'BLOOD_PH' AND gender = 'ALL');

-- pCO2
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'PCO2', 'ALL', NULL, NULL, 20, 70, 'mmHg', 'STAT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'PCO2' AND gender = 'ALL');

-- pO2 (critical_high is NULL)
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'PO2', 'ALL', NULL, NULL, 40, NULL, 'mmHg', 'URGENT', false, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'PO2' AND gender = 'ALL');

-- CSF Glucose (critical_high is NULL)
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'CSF_GLUCOSE', 'ALL', NULL, NULL, 40, NULL, 'mg/dL', 'URGENT', false, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'CSF_GLUCOSE' AND gender = 'ALL');

-- CSF WBC (critical_low is NULL)
INSERT INTO critical_value_reference (id, branch_id, parameter_code, gender, age_min, age_max, critical_low, critical_high, unit, notification_priority, requires_verbal_confirmation, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'CSF_WBC', 'ALL', NULL, NULL, NULL, 10, 'cells/µL', 'URGENT', false, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM critical_value_reference WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND parameter_code = 'CSF_WBC' AND gender = 'ALL');
