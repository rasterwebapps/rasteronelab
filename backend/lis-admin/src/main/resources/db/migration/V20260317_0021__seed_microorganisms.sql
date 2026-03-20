-- ============================================================================
-- V20260317_0021__seed_microorganisms.sql
-- Seed data: Common microorganisms for microbiology culture identification
-- Environment: Development / Testing
-- Uses default branch_id as placeholder
-- ============================================================================

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
VALUES
    -- Gram-positive bacteria
    ('30000000-0000-0000-0001-000000000001', '00000000-0000-0000-0000-000000000001', 'Staphylococcus aureus',        'SAUR',  'GRAM_POSITIVE', 'BACTERIA', 'Common cause of skin, soft tissue, bloodstream, and surgical site infections',                    true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000002', '00000000-0000-0000-0000-000000000001', 'MRSA',                          'MRSA',  'GRAM_POSITIVE', 'BACTERIA', 'Methicillin-resistant S. aureus; major nosocomial pathogen with limited treatment options',       true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000003', '00000000-0000-0000-0000-000000000001', 'Streptococcus pneumoniae',      'SPNEU', 'GRAM_POSITIVE', 'BACTERIA', 'Leading cause of community-acquired pneumonia, meningitis, and otitis media',                     true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000004', '00000000-0000-0000-0000-000000000001', 'Streptococcus pyogenes',        'SPYO',  'GRAM_POSITIVE', 'BACTERIA', 'Group A streptococcus; causes pharyngitis, cellulitis, and necrotizing fasciitis',                true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000005', '00000000-0000-0000-0000-000000000001', 'Enterococcus faecalis',         'EFCLS', 'GRAM_POSITIVE', 'BACTERIA', 'Common cause of urinary tract infections, endocarditis, and intra-abdominal infections',          true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000006', '00000000-0000-0000-0000-000000000001', 'Enterococcus faecium',          'EFCM',  'GRAM_POSITIVE', 'BACTERIA', 'Often vancomycin-resistant (VRE); nosocomial pathogen in immunocompromised patients',              true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),

    -- Gram-negative bacteria
    ('30000000-0000-0000-0001-000000000007', '00000000-0000-0000-0000-000000000001', 'Escherichia coli',              'ECOLI', 'GRAM_NEGATIVE', 'BACTERIA', 'Most common cause of urinary tract infections and gram-negative bacteremia',                      true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000008', '00000000-0000-0000-0000-000000000001', 'Klebsiella pneumoniae',         'KPNEU', 'GRAM_NEGATIVE', 'BACTERIA', 'Causes pneumonia and UTI; increasingly carbapenem-resistant strains emerging',                    true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000009', '00000000-0000-0000-0000-000000000001', 'Pseudomonas aeruginosa',        'PAER',  'GRAM_NEGATIVE', 'BACTERIA', 'Opportunistic pathogen in burn wounds, cystic fibrosis, and ventilator-associated pneumonia',     true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000010', '00000000-0000-0000-0000-000000000001', 'Acinetobacter baumannii',       'ABAUM', 'GRAM_NEGATIVE', 'BACTERIA', 'Multi-drug resistant nosocomial pathogen; commonly in ICU settings',                              true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000011', '00000000-0000-0000-0000-000000000001', 'Proteus mirabilis',             'PMIR',  'GRAM_NEGATIVE', 'BACTERIA', 'Causes urinary tract infections, especially catheter-associated and urinary stones',              true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000012', '00000000-0000-0000-0000-000000000001', 'Enterobacter cloacae',          'ECLOA', 'GRAM_NEGATIVE', 'BACTERIA', 'Nosocomial pathogen with inducible AmpC beta-lactamase resistance',                              true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000013', '00000000-0000-0000-0000-000000000001', 'Serratia marcescens',           'SMARC', 'GRAM_NEGATIVE', 'BACTERIA', 'Opportunistic pathogen causing respiratory and urinary tract infections',                         true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000014', '00000000-0000-0000-0000-000000000001', 'Salmonella typhi',              'STYPH', 'GRAM_NEGATIVE', 'BACTERIA', 'Causative agent of typhoid fever; blood culture isolation is diagnostic',                         true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000015', '00000000-0000-0000-0000-000000000001', 'Shigella sonnei',               'SSON',  'GRAM_NEGATIVE', 'BACTERIA', 'Causes bacillary dysentery; highly infectious with low inoculum',                                true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000016', '00000000-0000-0000-0000-000000000001', 'Haemophilus influenzae',         'HINF',  'GRAM_NEGATIVE', 'BACTERIA', 'Causes meningitis, pneumonia, and otitis media; especially in children',                          true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000017', '00000000-0000-0000-0000-000000000001', 'Neisseria meningitidis',        'NMEN',  'GRAM_NEGATIVE', 'BACTERIA', 'Causes bacterial meningitis and meningococcemia; medical emergency',                              true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000018', '00000000-0000-0000-0000-000000000001', 'Neisseria gonorrhoeae',         'NGON',  'GRAM_NEGATIVE', 'BACTERIA', 'Causes gonorrhea; increasing antimicrobial resistance worldwide',                                 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),

    -- Anaerobes
    ('30000000-0000-0000-0001-000000000019', '00000000-0000-0000-0000-000000000001', 'Clostridium difficile',         'CDIFF', 'GRAM_POSITIVE', 'BACTERIA', 'Causes antibiotic-associated diarrhea and pseudomembranous colitis',                              true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000020', '00000000-0000-0000-0000-000000000001', 'Bacteroides fragilis',          'BFRAG', 'GRAM_NEGATIVE', 'BACTERIA', 'Most common anaerobic pathogen in intra-abdominal and pelvic infections',                         true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),

    -- Mycobacteria
    ('30000000-0000-0000-0001-000000000021', '00000000-0000-0000-0000-000000000001', 'Mycobacterium tuberculosis',    'MTB',   'ACID_FAST',     'BACTERIA', 'Causative agent of tuberculosis; requires AFB smear and culture for diagnosis',                   true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000022', '00000000-0000-0000-0000-000000000001', 'Mycobacterium avium complex',   'MAC',   'ACID_FAST',     'BACTERIA', 'Non-tuberculous mycobacterium; opportunistic in immunocompromised patients',                      true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),

    -- Fungi
    ('30000000-0000-0000-0001-000000000023', '00000000-0000-0000-0000-000000000001', 'Candida albicans',              'CALB',  'FUNGUS',        'FUNGUS',   'Most common cause of candidiasis; mucosal and systemic infections',                               true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000024', '00000000-0000-0000-0000-000000000001', 'Candida tropicalis',            'CTROP', 'FUNGUS',        'FUNGUS',   'Emerging non-albicans Candida species with increasing antifungal resistance',                     true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000025', '00000000-0000-0000-0000-000000000001', 'Aspergillus fumigatus',         'AFUM',  'FUNGUS',        'FUNGUS',   'Causes invasive pulmonary aspergillosis in immunocompromised patients',                           true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000026', '00000000-0000-0000-0000-000000000001', 'Cryptococcus neoformans',       'CNEO',  'FUNGUS',        'FUNGUS',   'Causes cryptococcal meningitis, especially in HIV/AIDS patients',                                 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),

    -- Others
    ('30000000-0000-0000-0001-000000000027', '00000000-0000-0000-0000-000000000001', 'Helicobacter pylori',           'HPYL',  'GRAM_NEGATIVE', 'BACTERIA', 'Associated with peptic ulcers and gastric carcinoma',                                             true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000028', '00000000-0000-0000-0000-000000000001', 'Chlamydia trachomatis',         'CTRA',  'OTHER',         'BACTERIA', 'Causes chlamydial urogenital infections and trachoma',                                            true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000029', '00000000-0000-0000-0000-000000000001', 'Mycoplasma pneumoniae',         'MPNEU', 'OTHER',         'BACTERIA', 'Causes atypical community-acquired pneumonia, especially in young adults',                        true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false),
    ('30000000-0000-0000-0001-000000000030', '00000000-0000-0000-0000-000000000001', 'Legionella pneumophila',        'LPNEU', 'GRAM_NEGATIVE', 'BACTERIA', 'Causes Legionnaires disease; acquired from contaminated water systems',                           true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', false)
ON CONFLICT (id) DO NOTHING;
