-- Repeatable migration: Default report templates per department
-- Uses WHERE NOT EXISTS on (branch_id, department_code, is_default)

INSERT INTO report_template (id, branch_id, department_code, template_name, template_type, header_config, footer_config, body_config, paper_size, orientation, is_default, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'BIOCHEM', 'Standard Biochemistry Report', 'STANDARD',
  '{"show_logo": true, "show_branch_name": true, "show_nabl_logo": true, "show_address": true, "show_contact": true}'::jsonb,
  '{"show_page_number": true, "show_signature": true, "show_disclaimer": true, "disclaimer_text": "This report is for diagnostic purposes only. Results should be interpreted in clinical context."}'::jsonb,
  '{"show_reference_range": true, "show_critical_flags": true, "show_delta_flags": true, "show_method": false, "font_size": 10}'::jsonb,
  'A4', 'PORTRAIT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM report_template WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND department_code = 'BIOCHEM' AND is_default = true);

INSERT INTO report_template (id, branch_id, department_code, template_name, template_type, header_config, footer_config, body_config, paper_size, orientation, is_default, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'HEMAT', 'Standard Hematology Report', 'STANDARD',
  '{"show_logo": true, "show_branch_name": true, "show_nabl_logo": true, "show_address": true, "show_contact": true}'::jsonb,
  '{"show_page_number": true, "show_signature": true, "show_disclaimer": true, "disclaimer_text": "This report is for diagnostic purposes only. Results should be interpreted in clinical context."}'::jsonb,
  '{"show_reference_range": true, "show_critical_flags": true, "show_delta_flags": true, "show_method": false, "font_size": 10}'::jsonb,
  'A4', 'PORTRAIT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM report_template WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND department_code = 'HEMAT' AND is_default = true);

INSERT INTO report_template (id, branch_id, department_code, template_name, template_type, header_config, footer_config, body_config, paper_size, orientation, is_default, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MICRO', 'Standard Microbiology Report', 'STANDARD',
  '{"show_logo": true, "show_branch_name": true, "show_nabl_logo": true, "show_address": true, "show_contact": true}'::jsonb,
  '{"show_page_number": true, "show_signature": true, "show_disclaimer": true, "disclaimer_text": "This report is for diagnostic purposes only. Results should be interpreted in clinical context."}'::jsonb,
  '{"show_reference_range": true, "show_critical_flags": true, "show_delta_flags": true, "show_method": false, "font_size": 10}'::jsonb,
  'A4', 'PORTRAIT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM report_template WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND department_code = 'MICRO' AND is_default = true);

INSERT INTO report_template (id, branch_id, department_code, template_name, template_type, header_config, footer_config, body_config, paper_size, orientation, is_default, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'HISTO', 'Standard Histopathology Report', 'STANDARD',
  '{"show_logo": true, "show_branch_name": true, "show_nabl_logo": true, "show_address": true, "show_contact": true}'::jsonb,
  '{"show_page_number": true, "show_signature": true, "show_disclaimer": true, "disclaimer_text": "This report is for diagnostic purposes only. Results should be interpreted in clinical context."}'::jsonb,
  '{"show_reference_range": true, "show_critical_flags": true, "show_delta_flags": true, "show_method": false, "font_size": 10}'::jsonb,
  'A4', 'PORTRAIT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM report_template WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND department_code = 'HISTO' AND is_default = true);

INSERT INTO report_template (id, branch_id, department_code, template_name, template_type, header_config, footer_config, body_config, paper_size, orientation, is_default, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'SERO', 'Standard Serology Report', 'STANDARD',
  '{"show_logo": true, "show_branch_name": true, "show_nabl_logo": true, "show_address": true, "show_contact": true}'::jsonb,
  '{"show_page_number": true, "show_signature": true, "show_disclaimer": true, "disclaimer_text": "This report is for diagnostic purposes only. Results should be interpreted in clinical context."}'::jsonb,
  '{"show_reference_range": true, "show_critical_flags": true, "show_delta_flags": true, "show_method": false, "font_size": 10}'::jsonb,
  'A4', 'PORTRAIT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM report_template WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND department_code = 'SERO' AND is_default = true);

INSERT INTO report_template (id, branch_id, department_code, template_name, template_type, header_config, footer_config, body_config, paper_size, orientation, is_default, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'CLINPATH', 'Standard Clinical Pathology Report', 'STANDARD',
  '{"show_logo": true, "show_branch_name": true, "show_nabl_logo": true, "show_address": true, "show_contact": true}'::jsonb,
  '{"show_page_number": true, "show_signature": true, "show_disclaimer": true, "disclaimer_text": "This report is for diagnostic purposes only. Results should be interpreted in clinical context."}'::jsonb,
  '{"show_reference_range": true, "show_critical_flags": true, "show_delta_flags": true, "show_method": false, "font_size": 10}'::jsonb,
  'A4', 'PORTRAIT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM report_template WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND department_code = 'CLINPATH' AND is_default = true);

INSERT INTO report_template (id, branch_id, department_code, template_name, template_type, header_config, footer_config, body_config, paper_size, orientation, is_default, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'COAG', 'Standard Coagulation Report', 'STANDARD',
  '{"show_logo": true, "show_branch_name": true, "show_nabl_logo": true, "show_address": true, "show_contact": true}'::jsonb,
  '{"show_page_number": true, "show_signature": true, "show_disclaimer": true, "disclaimer_text": "This report is for diagnostic purposes only. Results should be interpreted in clinical context."}'::jsonb,
  '{"show_reference_range": true, "show_critical_flags": true, "show_delta_flags": true, "show_method": false, "font_size": 10}'::jsonb,
  'A4', 'PORTRAIT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM report_template WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND department_code = 'COAG' AND is_default = true);

INSERT INTO report_template (id, branch_id, department_code, template_name, template_type, header_config, footer_config, body_config, paper_size, orientation, is_default, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MOLBIO', 'Standard Molecular Biology Report', 'STANDARD',
  '{"show_logo": true, "show_branch_name": true, "show_nabl_logo": true, "show_address": true, "show_contact": true}'::jsonb,
  '{"show_page_number": true, "show_signature": true, "show_disclaimer": true, "disclaimer_text": "This report is for diagnostic purposes only. Results should be interpreted in clinical context."}'::jsonb,
  '{"show_reference_range": true, "show_critical_flags": true, "show_delta_flags": true, "show_method": false, "font_size": 10}'::jsonb,
  'A4', 'PORTRAIT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM report_template WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND department_code = 'MOLBIO' AND is_default = true);

INSERT INTO report_template (id, branch_id, department_code, template_name, template_type, header_config, footer_config, body_config, paper_size, orientation, is_default, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'CYTO', 'Standard Cytology Report', 'STANDARD',
  '{"show_logo": true, "show_branch_name": true, "show_nabl_logo": true, "show_address": true, "show_contact": true}'::jsonb,
  '{"show_page_number": true, "show_signature": true, "show_disclaimer": true, "disclaimer_text": "This report is for diagnostic purposes only. Results should be interpreted in clinical context."}'::jsonb,
  '{"show_reference_range": true, "show_critical_flags": true, "show_delta_flags": true, "show_method": false, "font_size": 10}'::jsonb,
  'A4', 'PORTRAIT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM report_template WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND department_code = 'CYTO' AND is_default = true);

INSERT INTO report_template (id, branch_id, department_code, template_name, template_type, header_config, footer_config, body_config, paper_size, orientation, is_default, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'MYCO', 'Standard Mycology Report', 'STANDARD',
  '{"show_logo": true, "show_branch_name": true, "show_nabl_logo": true, "show_address": true, "show_contact": true}'::jsonb,
  '{"show_page_number": true, "show_signature": true, "show_disclaimer": true, "disclaimer_text": "This report is for diagnostic purposes only. Results should be interpreted in clinical context."}'::jsonb,
  '{"show_reference_range": true, "show_critical_flags": true, "show_delta_flags": true, "show_method": false, "font_size": 10}'::jsonb,
  'A4', 'PORTRAIT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM report_template WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND department_code = 'MYCO' AND is_default = true);

INSERT INTO report_template (id, branch_id, department_code, template_name, template_type, header_config, footer_config, body_config, paper_size, orientation, is_default, is_active, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'PARA', 'Standard Parasitology Report', 'STANDARD',
  '{"show_logo": true, "show_branch_name": true, "show_nabl_logo": true, "show_address": true, "show_contact": true}'::jsonb,
  '{"show_page_number": true, "show_signature": true, "show_disclaimer": true, "disclaimer_text": "This report is for diagnostic purposes only. Results should be interpreted in clinical context."}'::jsonb,
  '{"show_reference_range": true, "show_critical_flags": true, "show_delta_flags": true, "show_method": false, "font_size": 10}'::jsonb,
  'A4', 'PORTRAIT', true, true, now(), now(), 'system', false
WHERE NOT EXISTS (SELECT 1 FROM report_template WHERE branch_id = '00000000-0000-0000-0000-000000000000' AND department_code = 'PARA' AND is_default = true);
