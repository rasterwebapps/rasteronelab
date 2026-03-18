-- Seed Data: Sample Types / Tube Types
-- Idempotent: uses WHERE NOT EXISTS since sample_type has no unique constraint on (branch_id, code)

INSERT INTO sample_type (id, branch_id, code, name, tube_color, container, additive, min_volume_ml, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'EDTA', 'EDTA Whole Blood', 'Purple/Lavender', 'Vacutainer', 'K2-EDTA', 3.0, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM sample_type WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'EDTA');

INSERT INTO sample_type (id, branch_id, code, name, tube_color, container, additive, min_volume_ml, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'SST', 'Serum Separator', 'Gold/Red-Gray', 'SST Tube', 'Clot Activator + Gel', 5.0, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM sample_type WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'SST');

INSERT INTO sample_type (id, branch_id, code, name, tube_color, container, additive, min_volume_ml, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'CITRATE', 'Citrated Plasma', 'Light Blue', 'Vacutainer', '3.2% Sodium Citrate', 2.7, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM sample_type WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CITRATE');

INSERT INTO sample_type (id, branch_id, code, name, tube_color, container, additive, min_volume_ml, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'HEPARIN', 'Heparin Plasma', 'Green', 'Vacutainer', 'Lithium Heparin', 4.0, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM sample_type WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'HEPARIN');

INSERT INTO sample_type (id, branch_id, code, name, tube_color, container, additive, min_volume_ml, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'FLUORIDE', 'Fluoride Oxalate', 'Gray', 'Vacutainer', 'Sodium Fluoride + Potassium Oxalate', 2.0, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM sample_type WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'FLUORIDE');

INSERT INTO sample_type (id, branch_id, code, name, tube_color, container, additive, min_volume_ml, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'PLAIN', 'Plain Serum', 'Red', 'Plain Tube', 'None (Clot Activator)', 5.0, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM sample_type WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'PLAIN');

INSERT INTO sample_type (id, branch_id, code, name, tube_color, container, additive, min_volume_ml, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'URINE_RANDOM', 'Random Urine', 'Yellow', 'Urine Cup', 'None', 10.0, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM sample_type WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'URINE_RANDOM');

INSERT INTO sample_type (id, branch_id, code, name, tube_color, container, additive, min_volume_ml, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'URINE_24H', '24-Hour Urine', 'Yellow', '24h Container', 'Varies', 10.0, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM sample_type WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'URINE_24H');

INSERT INTO sample_type (id, branch_id, code, name, tube_color, container, additive, min_volume_ml, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'CSF', 'Cerebrospinal Fluid', 'Clear', 'Sterile Tube', 'None', 1.0, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM sample_type WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CSF');

INSERT INTO sample_type (id, branch_id, code, name, tube_color, container, additive, min_volume_ml, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'STOOL', 'Stool Sample', 'Brown', 'Stool Container', 'None', 5.0, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM sample_type WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'STOOL');

INSERT INTO sample_type (id, branch_id, code, name, tube_color, container, additive, min_volume_ml, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'SWAB', 'Swab', 'White', 'Transport Swab', 'Stuart''s/Amies Medium', 0.0, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM sample_type WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'SWAB');

INSERT INTO sample_type (id, branch_id, code, name, tube_color, container, additive, min_volume_ml, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'BLOOD_CULTURE', 'Blood Culture', 'Yellow/Black', 'Culture Bottle', 'Resin/Charcoal', 10.0, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM sample_type WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'BLOOD_CULTURE');

INSERT INTO sample_type (id, branch_id, code, name, tube_color, container, additive, min_volume_ml, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'SPUTUM', 'Sputum', 'Clear', 'Sterile Cup', 'None', 2.0, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM sample_type WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'SPUTUM');

INSERT INTO sample_type (id, branch_id, code, name, tube_color, container, additive, min_volume_ml, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'TISSUE', 'Tissue/Biopsy', 'White', 'Formalin Jar', '10% Neutral Buffered Formalin', 0.0, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM sample_type WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'TISSUE');

INSERT INTO sample_type (id, branch_id, code, name, tube_color, container, additive, min_volume_ml, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'ASPIRATE', 'Fine Needle Aspirate', 'Clear', 'Slide/Tube', 'None', 0.0, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM sample_type WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ASPIRATE');
