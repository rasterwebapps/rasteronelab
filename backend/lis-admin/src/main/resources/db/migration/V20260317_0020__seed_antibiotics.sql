-- ============================================================================
-- V20260317_0020__seed_antibiotics.sql
-- Seed data: Common antibiotics for microbiology culture & sensitivity
-- Environment: Development / Testing
-- Uses default branch_id as placeholder
-- ============================================================================

INSERT INTO antibiotic (id, branch_id, name, code, antibiotic_group, is_active, created_at, updated_at, created_by, is_deleted)
VALUES
    -- Penicillins
    ('20000000-0000-0000-0001-000000000001', '00000000-0000-0000-0000-000000000001', 'Amoxicillin',                    'AMX',  'PENICILLIN',        true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000002', '00000000-0000-0000-0000-000000000001', 'Ampicillin',                     'AMP',  'PENICILLIN',        true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000003', '00000000-0000-0000-0000-000000000001', 'Piperacillin',                   'PIP',  'PENICILLIN',        true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),

    -- Cephalosporins
    ('20000000-0000-0000-0001-000000000004', '00000000-0000-0000-0000-000000000001', 'Cefazolin',                      'CFZ',  'CEPHALOSPORIN',     true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000005', '00000000-0000-0000-0000-000000000001', 'Ceftriaxone',                    'CRO',  'CEPHALOSPORIN',     true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000006', '00000000-0000-0000-0000-000000000001', 'Cefepime',                       'FEP',  'CEPHALOSPORIN',     true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000007', '00000000-0000-0000-0000-000000000001', 'Cefuroxime',                     'CXM',  'CEPHALOSPORIN',     true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000008', '00000000-0000-0000-0000-000000000001', 'Cephalexin',                     'LEX',  'CEPHALOSPORIN',     true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),

    -- Carbapenems
    ('20000000-0000-0000-0001-000000000009', '00000000-0000-0000-0000-000000000001', 'Meropenem',                      'MEM',  'CARBAPENEM',        true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000010', '00000000-0000-0000-0000-000000000001', 'Imipenem',                       'IPM',  'CARBAPENEM',        true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000011', '00000000-0000-0000-0000-000000000001', 'Ertapenem',                      'ETP',  'CARBAPENEM',        true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),

    -- Aminoglycosides
    ('20000000-0000-0000-0001-000000000012', '00000000-0000-0000-0000-000000000001', 'Gentamicin',                     'GEN',  'AMINOGLYCOSIDE',    true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000013', '00000000-0000-0000-0000-000000000001', 'Amikacin',                       'AMK',  'AMINOGLYCOSIDE',    true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000014', '00000000-0000-0000-0000-000000000001', 'Tobramycin',                     'TOB',  'AMINOGLYCOSIDE',    true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),

    -- Fluoroquinolones
    ('20000000-0000-0000-0001-000000000015', '00000000-0000-0000-0000-000000000001', 'Ciprofloxacin',                  'CIP',  'FLUOROQUINOLONE',   true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000016', '00000000-0000-0000-0000-000000000001', 'Levofloxacin',                   'LVX',  'FLUOROQUINOLONE',   true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000017', '00000000-0000-0000-0000-000000000001', 'Moxifloxacin',                   'MXF',  'FLUOROQUINOLONE',   true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),

    -- Macrolides
    ('20000000-0000-0000-0001-000000000018', '00000000-0000-0000-0000-000000000001', 'Azithromycin',                   'AZM',  'MACROLIDE',         true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000019', '00000000-0000-0000-0000-000000000001', 'Erythromycin',                   'ERY',  'MACROLIDE',         true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000020', '00000000-0000-0000-0000-000000000001', 'Clarithromycin',                 'CLR',  'MACROLIDE',         true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),

    -- Glycopeptides
    ('20000000-0000-0000-0001-000000000021', '00000000-0000-0000-0000-000000000001', 'Vancomycin',                     'VAN',  'GLYCOPEPTIDE',      true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000022', '00000000-0000-0000-0000-000000000001', 'Teicoplanin',                    'TEC',  'GLYCOPEPTIDE',      true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),

    -- Others
    ('20000000-0000-0000-0001-000000000023', '00000000-0000-0000-0000-000000000001', 'Clindamycin',                    'CLI',  'LINCOSAMIDE',       true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000024', '00000000-0000-0000-0000-000000000001', 'Metronidazole',                  'MTZ',  'NITROIMIDAZOLE',    true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000025', '00000000-0000-0000-0000-000000000001', 'Trimethoprim-Sulfamethoxazole',  'SXT',  'SULFONAMIDE',       true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000026', '00000000-0000-0000-0000-000000000001', 'Linezolid',                      'LZD',  'OXAZOLIDINONE',     true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000027', '00000000-0000-0000-0000-000000000001', 'Doxycycline',                    'DOX',  'TETRACYCLINE',      true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000028', '00000000-0000-0000-0000-000000000001', 'Nitrofurantoin',                 'NIT',  'NITROFURAN',        true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('20000000-0000-0000-0001-000000000029', '00000000-0000-0000-0000-000000000001', 'Colistin',                       'COL',  'POLYMYXIN',         true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false)
ON CONFLICT (id) DO NOTHING;
