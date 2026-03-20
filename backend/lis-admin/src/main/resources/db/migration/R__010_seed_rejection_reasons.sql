-- Repeatable migration: Common sample rejection reasons
-- Uses WHERE NOT EXISTS on (branch_id, code)

-- PRE_ANALYTICAL / MAJOR / requires_recollection=true
INSERT INTO rejection_reason (id, branch_id, code, reason, category, severity, requires_recollection, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'HEM001', 'Hemolyzed Sample', 'PRE_ANALYTICAL', 'MAJOR', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM rejection_reason WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'HEM001');

INSERT INTO rejection_reason (id, branch_id, code, reason, category, severity, requires_recollection, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'LIP001', 'Lipemic Sample', 'PRE_ANALYTICAL', 'MAJOR', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM rejection_reason WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'LIP001');

INSERT INTO rejection_reason (id, branch_id, code, reason, category, severity, requires_recollection, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'ICT001', 'Icteric Sample', 'PRE_ANALYTICAL', 'MAJOR', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM rejection_reason WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'ICT001');

INSERT INTO rejection_reason (id, branch_id, code, reason, category, severity, requires_recollection, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'CLT001', 'Clotted EDTA Sample', 'PRE_ANALYTICAL', 'MAJOR', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM rejection_reason WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CLT001');

INSERT INTO rejection_reason (id, branch_id, code, reason, category, severity, requires_recollection, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'IQS001', 'Insufficient Sample Quantity', 'PRE_ANALYTICAL', 'MAJOR', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM rejection_reason WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'IQS001');

INSERT INTO rejection_reason (id, branch_id, code, reason, category, severity, requires_recollection, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'WTT001', 'Wrong Tube Type', 'PRE_ANALYTICAL', 'MAJOR', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM rejection_reason WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'WTT001');

INSERT INTO rejection_reason (id, branch_id, code, reason, category, severity, requires_recollection, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'UNL001', 'Unlabeled/Mislabeled Sample', 'PRE_ANALYTICAL', 'MAJOR', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM rejection_reason WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'UNL001');

INSERT INTO rejection_reason (id, branch_id, code, reason, category, severity, requires_recollection, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'EXP001', 'Expired Tube Used', 'PRE_ANALYTICAL', 'MAJOR', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM rejection_reason WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'EXP001');

-- PRE_ANALYTICAL / MAJOR / requires_recollection=true (transport issues)
INSERT INTO rejection_reason (id, branch_id, code, reason, category, severity, requires_recollection, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'TMP001', 'Improper Transport Temperature', 'PRE_ANALYTICAL', 'MAJOR', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM rejection_reason WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'TMP001');

INSERT INTO rejection_reason (id, branch_id, code, reason, category, severity, requires_recollection, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'DLY001', 'Delayed Transport', 'PRE_ANALYTICAL', 'MAJOR', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM rejection_reason WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'DLY001');

INSERT INTO rejection_reason (id, branch_id, code, reason, category, severity, requires_recollection, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'LKD001', 'Leaked Container', 'PRE_ANALYTICAL', 'MAJOR', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM rejection_reason WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'LKD001');

-- PRE_ANALYTICAL / CRITICAL / requires_recollection=true
INSERT INTO rejection_reason (id, branch_id, code, reason, category, severity, requires_recollection, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'CNT001', 'Contaminated Sample', 'PRE_ANALYTICAL', 'CRITICAL', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM rejection_reason WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'CNT001');

INSERT INTO rejection_reason (id, branch_id, code, reason, category, severity, requires_recollection, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'WID001', 'Wrong Patient ID', 'PRE_ANALYTICAL', 'CRITICAL', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM rejection_reason WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'WID001');

-- POST_ANALYTICAL / MINOR / requires_recollection=false
INSERT INTO rejection_reason (id, branch_id, code, reason, category, severity, requires_recollection, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MRQ001', 'Missing Requisition Form', 'POST_ANALYTICAL', 'MINOR', false, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM rejection_reason WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'MRQ001');

INSERT INTO rejection_reason (id, branch_id, code, reason, category, severity, requires_recollection, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'DUP001', 'Duplicate Sample', 'POST_ANALYTICAL', 'MINOR', false, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM rejection_reason WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND code = 'DUP001');
