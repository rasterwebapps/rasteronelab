-- Seed Data: Lab Departments
-- Idempotent: uses ON CONFLICT (org_id, code) DO NOTHING
-- Uses system org_id '00000000-0000-0000-0000-000000000000' as placeholder for global seed data.
-- When a real organization is provisioned, departments are cloned with the actual org_id.

INSERT INTO department (id, org_id, name, code, description, display_order, is_active, created_at, updated_at, created_by, is_deleted)
VALUES
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Biochemistry', 'BIOCHEM', 'Clinical chemistry and metabolic tests', 1, true, now(), now(), 'system', false),
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Hematology', 'HEMAT', 'Blood cell analysis and coagulation', 2, true, now(), now(), 'system', false),
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Microbiology', 'MICRO', 'Culture, sensitivity, and organism identification', 3, true, now(), now(), 'system', false),
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Histopathology', 'HISTO', 'Tissue examination and biopsy analysis', 4, true, now(), now(), 'system', false),
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Serology / Immunology', 'SERO', 'Antibody and antigen testing', 5, true, now(), now(), 'system', false),
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Clinical Pathology', 'CLINPATH', 'Urine, stool, and body fluid analysis', 6, true, now(), now(), 'system', false),
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Coagulation', 'COAG', 'Clotting factor and platelet function tests', 7, true, now(), now(), 'system', false),
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Molecular Biology', 'MOLBIO', 'PCR, gene sequencing, and molecular diagnostics', 8, true, now(), now(), 'system', false),
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Cytology', 'CYTO', 'Cell morphology and Pap smear analysis', 9, true, now(), now(), 'system', false),
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Mycology', 'MYCO', 'Fungal culture and identification', 10, true, now(), now(), 'system', false),
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Parasitology', 'PARA', 'Parasitic infection diagnosis', 11, true, now(), now(), 'system', false)
ON CONFLICT (org_id, code) DO NOTHING;
