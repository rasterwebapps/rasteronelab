-- Repeatable migration: Default number series patterns
-- Uses ON CONFLICT (branch_id, entity_type) DO NOTHING

INSERT INTO number_series (id, branch_id, entity_type, prefix, suffix, current_number, padding_length, format_pattern, reset_frequency, is_active, created_at, updated_at, created_by, is_deleted)
VALUES
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'UHID',       'PAT', NULL, 0, 6, 'PAT-{YYYYMMDD}-{SEQ}', 'YEARLY', true, now(), now(), 'system', false),
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'ORDER',      'ORD', NULL, 0, 6, 'ORD-{YYYYMMDD}-{SEQ}', 'YEARLY', true, now(), now(), 'system', false),
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'SAMPLE',     'SMP', NULL, 0, 6, 'SMP-{YYYYMMDD}-{SEQ}', 'YEARLY', true, now(), now(), 'system', false),
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'INVOICE',    'INV', NULL, 0, 6, 'INV-{YYYYMMDD}-{SEQ}', 'YEARLY', true, now(), now(), 'system', false),
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'REPORT',     'RPT', NULL, 0, 6, 'RPT-{YYYYMMDD}-{SEQ}', 'YEARLY', true, now(), now(), 'system', false),
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'RECEIPT',    'RCT', NULL, 0, 6, 'RCT-{YYYYMMDD}-{SEQ}', 'YEARLY', true, now(), now(), 'system', false),
  (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'LAB_NUMBER', 'LAB', NULL, 0, 6, 'LAB-{YYYYMMDD}-{SEQ}', 'DAILY',  true, now(), now(), 'system', false)
ON CONFLICT (branch_id, entity_type) DO NOTHING;
