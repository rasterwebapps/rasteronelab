-- Seed Data: Microorganisms
-- Idempotent: uses WHERE NOT EXISTS since microorganism has no unique constraint on (branch_id, code)
-- branch_id '00000000-0000-0000-0000-000000000000' is the system-level placeholder.
-- gram_type values: GRAM_NEGATIVE, GRAM_POSITIVE, ACID_FAST, FUNGUS, OTHER
-- organism_type values: BACTERIA, FUNGUS, PARASITE, VIRUS, MYCOBACTERIUM

-- ============================================================
-- GRAM-NEGATIVE BACTERIA
-- ============================================================
INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Escherichia coli', 'ECOLI', 'GRAM_NEGATIVE', 'BACTERIA', 'Most common cause of UTI, bacteremia, and neonatal meningitis', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ECOLI');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Klebsiella pneumoniae', 'KPNEU', 'GRAM_NEGATIVE', 'BACTERIA', 'Pneumonia, UTI, liver abscess; associated with carbapenem resistance', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'KPNEU');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Klebsiella oxytoca', 'KOXYT', 'GRAM_NEGATIVE', 'BACTERIA', 'UTI, wound infections, opportunistic pathogen', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'KOXYT');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pseudomonas aeruginosa', 'PSAER', 'GRAM_NEGATIVE', 'BACTERIA', 'Nosocomial infections, ventilator-associated pneumonia, burn wound infections', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'PSAER');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Acinetobacter baumannii', 'ACBAU', 'GRAM_NEGATIVE', 'BACTERIA', 'Multidrug-resistant nosocomial pathogen; pneumonia, bacteremia, wound infections', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ACBAU');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Proteus mirabilis', 'PRMIR', 'GRAM_NEGATIVE', 'BACTERIA', 'UTI, wound infections; produces urease causing alkaline urine and struvite stones', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'PRMIR');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Proteus vulgaris', 'PRVUL', 'GRAM_NEGATIVE', 'BACTERIA', 'Opportunistic UTI and wound infections; more resistant than P. mirabilis', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'PRVUL');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacter cloacae', 'ENCLO', 'GRAM_NEGATIVE', 'BACTERIA', 'Nosocomial infections; inherent AmpC beta-lactamase resistance', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ENCLO');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterobacter aerogenes', 'ENAER', 'GRAM_NEGATIVE', 'BACTERIA', 'UTI, respiratory infections, bacteremia in immunocompromised patients', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ENAER');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Citrobacter freundii', 'CIFRE', 'GRAM_NEGATIVE', 'BACTERIA', 'Opportunistic pathogen; UTI, neonatal meningitis, intra-abdominal infections', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CIFRE');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Citrobacter koseri', 'CIKOS', 'GRAM_NEGATIVE', 'BACTERIA', 'Neonatal meningitis and brain abscess; UTI in adults', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CIKOS');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Serratia marcescens', 'SEMAR', 'GRAM_NEGATIVE', 'BACTERIA', 'Nosocomial pneumonia, UTI, bacteremia; produces red pigment prodigiosin', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'SEMAR');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Morganella morganii', 'MOMOR', 'GRAM_NEGATIVE', 'BACTERIA', 'Opportunistic pathogen; wound infections, UTI; inherent ampicillin resistance', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MOMOR');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Salmonella typhi', 'SALTYP', 'GRAM_NEGATIVE', 'BACTERIA', 'Typhoid fever; systemic febrile illness with rose spots, relative bradycardia', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'SALTYP');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Salmonella paratyphi', 'SALPAR', 'GRAM_NEGATIVE', 'BACTERIA', 'Paratyphoid fever; milder than typhoid but similar clinical presentation', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'SALPAR');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Shigella dysenteriae', 'SHDYS', 'GRAM_NEGATIVE', 'BACTERIA', 'Bacillary dysentery with bloody diarrhea; produces Shiga toxin', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'SHDYS');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Shigella flexneri', 'SHFLE', 'GRAM_NEGATIVE', 'BACTERIA', 'Most common Shigella species; acute dysentery and diarrheal illness', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'SHFLE');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Vibrio cholerae', 'VICHO', 'GRAM_NEGATIVE', 'BACTERIA', 'Cholera; profuse rice-water diarrhea leading to severe dehydration', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'VICHO');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Neisseria meningitidis', 'NEMEN', 'GRAM_NEGATIVE', 'BACTERIA', 'Bacterial meningitis and septicemia; petechial/purpuric rash', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'NEMEN');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Neisseria gonorrhoeae', 'NEGON', 'GRAM_NEGATIVE', 'BACTERIA', 'Gonorrhea; urethritis, cervicitis, pelvic inflammatory disease, disseminated infection', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'NEGON');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Haemophilus influenzae', 'HAINF', 'GRAM_NEGATIVE', 'BACTERIA', 'Meningitis (type b), pneumonia, otitis media, epiglottitis', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'HAINF');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Moraxella catarrhalis', 'MOCAT', 'GRAM_NEGATIVE', 'BACTERIA', 'Otitis media, sinusitis, COPD exacerbations; produces beta-lactamase', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MOCAT');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Stenotrophomonas maltophilia', 'STMAL', 'GRAM_NEGATIVE', 'BACTERIA', 'Opportunistic nosocomial pathogen; intrinsically resistant to carbapenems', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'STMAL');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Burkholderia cepacia', 'BUCEP', 'GRAM_NEGATIVE', 'BACTERIA', 'Lung infections in cystic fibrosis and chronic granulomatous disease patients', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'BUCEP');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Campylobacter jejuni', 'CAJE', 'GRAM_NEGATIVE', 'BACTERIA', 'Most common bacterial cause of gastroenteritis; associated with Guillain-Barre syndrome', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CAJE');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Helicobacter pylori', 'HEPYL', 'GRAM_NEGATIVE', 'BACTERIA', 'Peptic ulcer disease, chronic gastritis, gastric carcinoma and MALT lymphoma', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'HEPYL');

-- ============================================================
-- GRAM-POSITIVE BACTERIA
-- ============================================================
INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus aureus', 'STAUR', 'GRAM_POSITIVE', 'BACTERIA', 'Skin/soft tissue infections, pneumonia, bacteremia, endocarditis, osteomyelitis', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'STAUR');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus aureus (MRSA)', 'MRSA', 'GRAM_POSITIVE', 'BACTERIA', 'Methicillin-resistant S. aureus; major nosocomial and community-acquired pathogen', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MRSA');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus epidermidis', 'STEPI', 'GRAM_POSITIVE', 'BACTERIA', 'Prosthetic device infections, catheter-related bacteremia, endocarditis', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'STEPI');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Staphylococcus saprophyticus', 'STSAP', 'GRAM_POSITIVE', 'BACTERIA', 'Second most common cause of UTI in young sexually active women', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'STSAP');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Streptococcus pyogenes', 'STRPY', 'GRAM_POSITIVE', 'BACTERIA', 'Pharyngitis, impetigo, scarlet fever, rheumatic fever, glomerulonephritis', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'STRPY');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Streptococcus agalactiae', 'STRAG', 'GRAM_POSITIVE', 'BACTERIA', 'Neonatal meningitis and sepsis; UTI and bacteremia in pregnant women and elderly', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'STRAG');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Streptococcus pneumoniae', 'STRPN', 'GRAM_POSITIVE', 'BACTERIA', 'Pneumonia, meningitis, otitis media, sinusitis; leading cause of community-acquired pneumonia', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'STRPN');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterococcus faecalis', 'ENFAE', 'GRAM_POSITIVE', 'BACTERIA', 'UTI, endocarditis, intra-abdominal infections; intrinsically resistant to many antibiotics', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ENFAE');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Enterococcus faecium', 'ENFAM', 'GRAM_POSITIVE', 'BACTERIA', 'Vancomycin-resistant enterococcus (VRE); nosocomial infections, bacteremia', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ENFAM');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Listeria monocytogenes', 'LIMON', 'GRAM_POSITIVE', 'BACTERIA', 'Meningitis and septicemia in neonates, elderly, and immunocompromised; food-borne', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'LIMON');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Corynebacterium diphtheriae', 'CODIP', 'GRAM_POSITIVE', 'BACTERIA', 'Diphtheria; pseudomembranous pharyngitis with myocarditis and neuropathy', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CODIP');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Bacillus cereus', 'BACER', 'GRAM_POSITIVE', 'BACTERIA', 'Food poisoning (emetic and diarrheal syndromes); opportunistic infections', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'BACER');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Clostridioides difficile', 'CLDIF', 'GRAM_POSITIVE', 'BACTERIA', 'Antibiotic-associated colitis and pseudomembranous colitis; produces toxins A and B', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CLDIF');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Clostridium perfringens', 'CLPER', 'GRAM_POSITIVE', 'BACTERIA', 'Gas gangrene (myonecrosis), food poisoning, necrotizing fasciitis', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CLPER');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Nocardia asteroides', 'NOAST', 'GRAM_POSITIVE', 'BACTERIA', 'Pulmonary nocardiosis and brain abscess in immunocompromised patients', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'NOAST');

-- ============================================================
-- MYCOBACTERIA (ACID_FAST)
-- ============================================================
INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Mycobacterium tuberculosis', 'MYTUB', 'ACID_FAST', 'MYCOBACTERIUM', 'Tuberculosis; pulmonary and extrapulmonary disease; major global health burden', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MYTUB');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Mycobacterium avium complex', 'MYAVI', 'ACID_FAST', 'MYCOBACTERIUM', 'Disseminated infections in AIDS patients; pulmonary disease in immunocompetent hosts', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MYAVI');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Mycobacterium fortuitum', 'MYFOR', 'ACID_FAST', 'MYCOBACTERIUM', 'Rapidly growing mycobacterium; post-surgical wound infections, catheter infections', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MYFOR');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Mycobacterium abscessus', 'MYABS', 'ACID_FAST', 'MYCOBACTERIUM', 'Rapidly growing mycobacterium; pulmonary disease, skin infections; highly drug resistant', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MYABS');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Mycobacterium leprae', 'MYLEP', 'ACID_FAST', 'MYCOBACTERIUM', 'Leprosy (Hansen''s disease); peripheral nerve damage and skin lesions', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MYLEP');

-- ============================================================
-- FUNGI
-- ============================================================
INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Candida albicans', 'CANAL', 'FUNGUS', 'FUNGUS', 'Most common Candida species; oral thrush, vaginal candidiasis, invasive candidiasis', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CANAL');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Candida tropicalis', 'CANTRO', 'FUNGUS', 'FUNGUS', 'Invasive candidiasis; common in neutropenic and haematology patients', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CANTRO');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Candida parapsilosis', 'CANPAR', 'FUNGUS', 'FUNGUS', 'Catheter-related bloodstream infections; common in neonates and surgical patients', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CANPAR');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Candida glabrata', 'CANGLA', 'FUNGUS', 'FUNGUS', 'Reduced azole susceptibility; UTI and candidemia in elderly and diabetic patients', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CANGLA');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Candida auris', 'CANAU', 'FUNGUS', 'FUNGUS', 'Emerging multidrug-resistant pathogen; nosocomial outbreaks, high mortality candidemia', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CANAU');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Candida krusei', 'CANKRU', 'FUNGUS', 'FUNGUS', 'Intrinsically resistant to fluconazole; candidemia in immunocompromised hosts', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CANKRU');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Cryptococcus neoformans', 'CRNEOF', 'FUNGUS', 'FUNGUS', 'Cryptococcal meningitis in HIV/AIDS patients; pulmonary cryptococcosis', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CRNEOF');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Aspergillus fumigatus', 'ASPFUM', 'FUNGUS', 'FUNGUS', 'Invasive pulmonary aspergillosis in immunocompromised; allergic bronchopulmonary aspergillosis', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ASPFUM');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Aspergillus niger', 'ASPNIG', 'FUNGUS', 'FUNGUS', 'Otomycosis (fungal ear infection), pulmonary disease, sinus infections', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ASPNIG');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Aspergillus flavus', 'ASPFLA', 'FUNGUS', 'FUNGUS', 'Invasive sinusitis, pulmonary aspergillosis; produces aflatoxin; voriconazole resistant strains', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ASPFLA');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Mucor species', 'MUCOR', 'FUNGUS', 'FUNGUS', 'Mucormycosis; rhinocerebral, pulmonary, cutaneous forms; high mortality in diabetic ketoacidosis', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MUCOR');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Trichophyton rubrum', 'TRRUB', 'FUNGUS', 'FUNGUS', 'Most common cause of tinea pedis (athlete''s foot), onychomycosis, and tinea corporis', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'TRRUB');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Histoplasma capsulatum', 'HISCAP', 'FUNGUS', 'FUNGUS', 'Histoplasmosis; pulmonary and disseminated disease; endemic in river valleys', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'HISCAP');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Pneumocystis jirovecii', 'PNJIR', 'FUNGUS', 'FUNGUS', 'Pneumocystis pneumonia (PCP) in HIV/AIDS; major opportunistic infection', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'PNJIR');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Fusarium species', 'FUSAR', 'FUNGUS', 'FUNGUS', 'Keratitis, onychomycosis, disseminated infection in immunocompromised patients', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'FUSAR');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Sporothrix schenckii', 'SPSCHE', 'FUNGUS', 'FUNGUS', 'Sporotrichosis; subcutaneous mycosis with nodular lymphangitis; from thorn pricks', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'SPSCHE');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Coccidioides immitis', 'COCIMM', 'FUNGUS', 'FUNGUS', 'Coccidioidomycosis (Valley fever); pulmonary and disseminated disease; endemic in arid regions', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'COCIMM');

-- ============================================================
-- PARASITES
-- ============================================================
INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Plasmodium falciparum', 'PLASFAL', 'OTHER', 'PARASITE', 'Most severe malaria; causes cerebral malaria, severe anemia, multi-organ failure', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'PLASFAL');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Plasmodium vivax', 'PLASVIV', 'OTHER', 'PARASITE', 'Benign tertian malaria; relapses from hypnozoites; widespread geographic distribution', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'PLASVIV');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Plasmodium malariae', 'PLASMAL', 'OTHER', 'PARASITE', 'Quartan malaria; chronic low-grade parasitemia; associated with nephrotic syndrome', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'PLASMAL');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Plasmodium ovale', 'PLASOVA', 'OTHER', 'PARASITE', 'Benign tertian malaria; relapses from dormant liver hypnozoites; found in West Africa', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'PLASOVA');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Entamoeba histolytica', 'ENTHIS', 'OTHER', 'PARASITE', 'Amoebic dysentery and liver abscess; transmitted via fecal-oral route', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ENTHIS');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Giardia lamblia', 'GIALAM', 'OTHER', 'PARASITE', 'Giardiasis; watery diarrhea, malabsorption, bloating; most common intestinal protozoan', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'GIALAM');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Toxoplasma gondii', 'TOXGON', 'OTHER', 'PARASITE', 'Toxoplasmosis; congenital infection, encephalitis in immunocompromised, retinochoroiditis', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'TOXGON');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Trichomonas vaginalis', 'TRIVAG', 'OTHER', 'PARASITE', 'Trichomoniasis; sexually transmitted vaginitis and urethritis', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'TRIVAG');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Ascaris lumbricoides', 'ASCLUM', 'OTHER', 'PARASITE', 'Ascariasis; most common intestinal helminth; Loeffler syndrome, intestinal obstruction', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ASCLUM');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Cryptosporidium species', 'CRYPTOS', 'OTHER', 'PARASITE', 'Cryptosporidiosis; profuse watery diarrhea; life-threatening in AIDS patients', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CRYPTOS');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Leishmania donovani', 'LEISHD', 'OTHER', 'PARASITE', 'Visceral leishmaniasis (kala-azar); hepatosplenomegaly, pancytopenia, fever', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'LEISHD');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Trypanosoma cruzi', 'TRYCRU', 'OTHER', 'PARASITE', 'Chagas disease; cardiomyopathy and megacolon; transmitted by triatomine bugs', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'TRYCRU');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Wuchereria bancrofti', 'WUBAN', 'OTHER', 'PARASITE', 'Lymphatic filariasis; elephantiasis due to chronic lymphatic obstruction', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'WUBAN');

INSERT INTO microorganism (id, branch_id, name, code, gram_type, organism_type, clinical_significance, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Strongyloides stercoralis', 'STRSTER', 'OTHER', 'PARASITE', 'Strongyloidiasis; hyperinfection syndrome in immunocompromised; autoinfection cycle', true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM microorganism WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'STRSTER');
