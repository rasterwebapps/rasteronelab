-- ============================================================================
-- V20260317_0019__seed_departments.sql
-- Seed data: Standard laboratory departments
-- Environment: Development / Testing
-- Uses default organization ID as placeholder
-- ============================================================================

INSERT INTO department (id, org_id, name, code, description, display_order, is_active, created_at, updated_at, created_by, is_deleted)
VALUES
    ('10000000-0000-0000-0001-000000000001', '00000000-0000-0000-0000-000000000001', 'Biochemistry',       'BIOCHEM', 'Clinical biochemistry and chemistry tests',                1,  true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('10000000-0000-0000-0001-000000000002', '00000000-0000-0000-0000-000000000001', 'Hematology',         'HEMAT',   'Complete blood count, coagulation, and blood film studies', 2,  true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('10000000-0000-0000-0001-000000000003', '00000000-0000-0000-0000-000000000001', 'Microbiology',       'MICRO',   'Culture and sensitivity, gram stain, AFB',                 3,  true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('10000000-0000-0000-0001-000000000004', '00000000-0000-0000-0000-000000000001', 'Serology',           'SERO',    'Immunoassays, viral markers, autoimmune panels',           4,  true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('10000000-0000-0000-0001-000000000005', '00000000-0000-0000-0000-000000000001', 'Histopathology',     'HISTO',   'Tissue examination, biopsy, and cytology',                 5,  true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('10000000-0000-0000-0001-000000000006', '00000000-0000-0000-0000-000000000001', 'Clinical Pathology', 'CLPATH',  'Urine, stool, and body fluid analysis',                    6,  true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('10000000-0000-0000-0001-000000000007', '00000000-0000-0000-0000-000000000001', 'Immunology',         'IMMUNO',  'Immunological assays and allergy testing',                 7,  true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('10000000-0000-0000-0001-000000000008', '00000000-0000-0000-0000-000000000001', 'Molecular Biology',  'MOLBIO',  'PCR, gene sequencing, and molecular diagnostics',          8,  true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('10000000-0000-0000-0001-000000000009', '00000000-0000-0000-0000-000000000001', 'Cytogenetics',       'CYTO',    'Chromosome analysis and FISH studies',                     9,  true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('10000000-0000-0000-0001-000000000010', '00000000-0000-0000-0000-000000000001', 'Toxicology',         'TOXICO',  'Drug screening and toxicological analysis',                10, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('10000000-0000-0000-0001-000000000011', '00000000-0000-0000-0000-000000000001', 'Blood Bank',         'BBANK',   'Blood grouping, cross-match, and component therapy',       11, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false)
ON CONFLICT (id) DO NOTHING;
