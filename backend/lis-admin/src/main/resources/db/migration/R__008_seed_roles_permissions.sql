-- Repeatable migration: Seed system roles, permissions, and role-permission mappings
-- Idempotent:
--   role         : ON CONFLICT (branch_id, code) DO NOTHING
--   permission   : ON CONFLICT (code) DO NOTHING
--   role_permission: WHERE NOT EXISTS on (role_id, permission_id)

-- ============================================================
-- ROLES
-- ============================================================
INSERT INTO role (id, branch_id, code, name, description, is_system_role, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'SUPER_ADMIN', 'Super Administrator', 'Full system access across all organizations', true, true, now(), now(), 'system', false)
ON CONFLICT (branch_id, code) DO NOTHING;

INSERT INTO role (id, branch_id, code, name, description, is_system_role, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'ORG_ADMIN', 'Organization Administrator', 'Full access within an organization', true, true, now(), now(), 'system', false)
ON CONFLICT (branch_id, code) DO NOTHING;

INSERT INTO role (id, branch_id, code, name, description, is_system_role, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'BRANCH_ADMIN', 'Branch Administrator', 'Full access within a branch', true, true, now(), now(), 'system', false)
ON CONFLICT (branch_id, code) DO NOTHING;

INSERT INTO role (id, branch_id, code, name, description, is_system_role, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'LAB_DIRECTOR', 'Lab Director', 'Supervises all laboratory operations and authorizes reports', true, true, now(), now(), 'system', false)
ON CONFLICT (branch_id, code) DO NOTHING;

INSERT INTO role (id, branch_id, code, name, description, is_system_role, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'PATHOLOGIST', 'Pathologist', 'Reviews and authorizes lab results', true, true, now(), now(), 'system', false)
ON CONFLICT (branch_id, code) DO NOTHING;

INSERT INTO role (id, branch_id, code, name, description, is_system_role, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'LAB_TECHNICIAN', 'Lab Technician', 'Enters test results and processes samples', true, true, now(), now(), 'system', false)
ON CONFLICT (branch_id, code) DO NOTHING;

INSERT INTO role (id, branch_id, code, name, description, is_system_role, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'PHLEBOTOMIST', 'Phlebotomist', 'Collects blood samples and manages collections', true, true, now(), now(), 'system', false)
ON CONFLICT (branch_id, code) DO NOTHING;

INSERT INTO role (id, branch_id, code, name, description, is_system_role, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'RECEPTIONIST', 'Receptionist', 'Registers patients and creates test orders', true, true, now(), now(), 'system', false)
ON CONFLICT (branch_id, code) DO NOTHING;

INSERT INTO role (id, branch_id, code, name, description, is_system_role, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'BILLING_OPERATOR', 'Billing Operator', 'Manages invoicing and payments', true, true, now(), now(), 'system', false)
ON CONFLICT (branch_id, code) DO NOTHING;

INSERT INTO role (id, branch_id, code, name, description, is_system_role, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'VIEWER', 'Viewer', 'Read-only access to reports', true, true, now(), now(), 'system', false)
ON CONFLICT (branch_id, code) DO NOTHING;

-- ============================================================
-- PERMISSIONS
-- ============================================================
INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'PATIENT_CREATE', 'Create Patient', 'PATIENT', 'Create new patient records', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'PATIENT_READ', 'View Patient', 'PATIENT', 'View patient records and history', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'PATIENT_UPDATE', 'Update Patient', 'PATIENT', 'Update existing patient records', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'ORDER_CREATE', 'Create Order', 'ORDER', 'Create new test orders', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'ORDER_READ', 'View Order', 'ORDER', 'View test orders', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'ORDER_CANCEL', 'Cancel Order', 'ORDER', 'Cancel pending test orders', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'SAMPLE_COLLECT', 'Collect Sample', 'SAMPLE', 'Record sample collection', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'SAMPLE_RECEIVE', 'Receive Sample', 'SAMPLE', 'Mark sample as received in lab', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'RESULT_ENTER', 'Enter Result', 'RESULT', 'Enter test results', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'RESULT_VALIDATE', 'Validate Result', 'RESULT', 'Validate entered results', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'RESULT_AUTHORIZE', 'Authorize Result', 'RESULT', 'Authorize final results', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'REPORT_GENERATE', 'Generate Report', 'REPORT', 'Generate PDF reports', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'REPORT_RELEASE', 'Release Report', 'REPORT', 'Release report to patient/doctor', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'REPORT_PRINT', 'Print Report', 'REPORT', 'Print lab reports', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'BILLING_CREATE', 'Create Bill', 'BILLING', 'Create invoices and bills', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'BILLING_PAYMENT', 'Record Payment', 'BILLING', 'Record payments', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'BILLING_REFUND', 'Process Refund', 'BILLING', 'Process refunds', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'ADMIN_MANAGE', 'Admin Manage', 'ADMIN', 'General administrative functions', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'ADMIN_USER_MANAGE', 'Manage Users', 'ADMIN', 'Create and manage user accounts', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'ADMIN_BRANCH_MANAGE', 'Manage Branches', 'ADMIN', 'Configure branches', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'QC_MANAGE', 'Manage QC', 'QC', 'Manage quality control', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'INVENTORY_MANAGE', 'Manage Inventory', 'INVENTORY', 'Manage lab inventory and reagents', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

INSERT INTO permission (id, code, name, module, description, is_active, created_at, updated_at, created_by, is_deleted)
VALUES (gen_random_uuid(), 'INSTRUMENT_MANAGE', 'Manage Instruments', 'INSTRUMENT', 'Manage lab instruments', true, now(), now(), 'system', false)
ON CONFLICT (code) DO NOTHING;

-- ============================================================
-- ROLE-PERMISSION MAPPINGS
-- ============================================================

-- SUPER_ADMIN: ALL permissions
INSERT INTO role_permission (id, role_id, permission_id, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), r.id, p.id, now(), now(), 'system', false
FROM role r, permission p
WHERE r.branch_id = '00000000-0000-0000-0000-000000000000' AND r.code = 'SUPER_ADMIN'
  AND NOT EXISTS (SELECT 1 FROM role_permission rp WHERE rp.role_id = r.id AND rp.permission_id = p.id AND rp.is_deleted = false);

-- ORG_ADMIN: ALL permissions except ADMIN_BRANCH_MANAGE
INSERT INTO role_permission (id, role_id, permission_id, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), r.id, p.id, now(), now(), 'system', false
FROM role r, permission p
WHERE r.branch_id = '00000000-0000-0000-0000-000000000000' AND r.code = 'ORG_ADMIN'
  AND p.code <> 'ADMIN_BRANCH_MANAGE'
  AND NOT EXISTS (SELECT 1 FROM role_permission rp WHERE rp.role_id = r.id AND rp.permission_id = p.id AND rp.is_deleted = false);

-- BRANCH_ADMIN: PATIENT_*, ORDER_*, SAMPLE_*, RESULT_*, REPORT_*, BILLING_*, ADMIN_MANAGE, ADMIN_USER_MANAGE, QC_MANAGE, INVENTORY_MANAGE, INSTRUMENT_MANAGE
INSERT INTO role_permission (id, role_id, permission_id, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), r.id, p.id, now(), now(), 'system', false
FROM role r, permission p
WHERE r.branch_id = '00000000-0000-0000-0000-000000000000' AND r.code = 'BRANCH_ADMIN'
  AND p.code IN (
    'PATIENT_CREATE', 'PATIENT_READ', 'PATIENT_UPDATE',
    'ORDER_CREATE', 'ORDER_READ', 'ORDER_CANCEL',
    'SAMPLE_COLLECT', 'SAMPLE_RECEIVE',
    'RESULT_ENTER', 'RESULT_VALIDATE', 'RESULT_AUTHORIZE',
    'REPORT_GENERATE', 'REPORT_RELEASE', 'REPORT_PRINT',
    'BILLING_CREATE', 'BILLING_PAYMENT', 'BILLING_REFUND',
    'ADMIN_MANAGE', 'ADMIN_USER_MANAGE',
    'QC_MANAGE', 'INVENTORY_MANAGE', 'INSTRUMENT_MANAGE'
  )
  AND NOT EXISTS (SELECT 1 FROM role_permission rp WHERE rp.role_id = r.id AND rp.permission_id = p.id AND rp.is_deleted = false);

-- LAB_DIRECTOR: PATIENT_READ, ORDER_READ, SAMPLE_*, RESULT_*, REPORT_*, QC_MANAGE, INSTRUMENT_MANAGE
INSERT INTO role_permission (id, role_id, permission_id, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), r.id, p.id, now(), now(), 'system', false
FROM role r, permission p
WHERE r.branch_id = '00000000-0000-0000-0000-000000000000' AND r.code = 'LAB_DIRECTOR'
  AND p.code IN (
    'PATIENT_READ',
    'ORDER_READ',
    'SAMPLE_COLLECT', 'SAMPLE_RECEIVE',
    'RESULT_ENTER', 'RESULT_VALIDATE', 'RESULT_AUTHORIZE',
    'REPORT_GENERATE', 'REPORT_RELEASE', 'REPORT_PRINT',
    'QC_MANAGE', 'INSTRUMENT_MANAGE'
  )
  AND NOT EXISTS (SELECT 1 FROM role_permission rp WHERE rp.role_id = r.id AND rp.permission_id = p.id AND rp.is_deleted = false);

-- PATHOLOGIST: PATIENT_READ, ORDER_READ, RESULT_ENTER, RESULT_VALIDATE, RESULT_AUTHORIZE, REPORT_GENERATE, REPORT_RELEASE, REPORT_PRINT
INSERT INTO role_permission (id, role_id, permission_id, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), r.id, p.id, now(), now(), 'system', false
FROM role r, permission p
WHERE r.branch_id = '00000000-0000-0000-0000-000000000000' AND r.code = 'PATHOLOGIST'
  AND p.code IN (
    'PATIENT_READ', 'ORDER_READ',
    'RESULT_ENTER', 'RESULT_VALIDATE', 'RESULT_AUTHORIZE',
    'REPORT_GENERATE', 'REPORT_RELEASE', 'REPORT_PRINT'
  )
  AND NOT EXISTS (SELECT 1 FROM role_permission rp WHERE rp.role_id = r.id AND rp.permission_id = p.id AND rp.is_deleted = false);

-- LAB_TECHNICIAN: PATIENT_READ, ORDER_READ, SAMPLE_COLLECT, SAMPLE_RECEIVE, RESULT_ENTER, REPORT_GENERATE
INSERT INTO role_permission (id, role_id, permission_id, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), r.id, p.id, now(), now(), 'system', false
FROM role r, permission p
WHERE r.branch_id = '00000000-0000-0000-0000-000000000000' AND r.code = 'LAB_TECHNICIAN'
  AND p.code IN (
    'PATIENT_READ', 'ORDER_READ',
    'SAMPLE_COLLECT', 'SAMPLE_RECEIVE',
    'RESULT_ENTER', 'REPORT_GENERATE'
  )
  AND NOT EXISTS (SELECT 1 FROM role_permission rp WHERE rp.role_id = r.id AND rp.permission_id = p.id AND rp.is_deleted = false);

-- PHLEBOTOMIST: PATIENT_READ, ORDER_READ, SAMPLE_COLLECT
INSERT INTO role_permission (id, role_id, permission_id, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), r.id, p.id, now(), now(), 'system', false
FROM role r, permission p
WHERE r.branch_id = '00000000-0000-0000-0000-000000000000' AND r.code = 'PHLEBOTOMIST'
  AND p.code IN ('PATIENT_READ', 'ORDER_READ', 'SAMPLE_COLLECT')
  AND NOT EXISTS (SELECT 1 FROM role_permission rp WHERE rp.role_id = r.id AND rp.permission_id = p.id AND rp.is_deleted = false);

-- RECEPTIONIST: PATIENT_CREATE, PATIENT_READ, PATIENT_UPDATE, ORDER_CREATE, ORDER_READ, ORDER_CANCEL, REPORT_PRINT
INSERT INTO role_permission (id, role_id, permission_id, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), r.id, p.id, now(), now(), 'system', false
FROM role r, permission p
WHERE r.branch_id = '00000000-0000-0000-0000-000000000000' AND r.code = 'RECEPTIONIST'
  AND p.code IN (
    'PATIENT_CREATE', 'PATIENT_READ', 'PATIENT_UPDATE',
    'ORDER_CREATE', 'ORDER_READ', 'ORDER_CANCEL',
    'REPORT_PRINT'
  )
  AND NOT EXISTS (SELECT 1 FROM role_permission rp WHERE rp.role_id = r.id AND rp.permission_id = p.id AND rp.is_deleted = false);

-- BILLING_OPERATOR: PATIENT_READ, ORDER_READ, BILLING_CREATE, BILLING_PAYMENT, BILLING_REFUND
INSERT INTO role_permission (id, role_id, permission_id, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), r.id, p.id, now(), now(), 'system', false
FROM role r, permission p
WHERE r.branch_id = '00000000-0000-0000-0000-000000000000' AND r.code = 'BILLING_OPERATOR'
  AND p.code IN (
    'PATIENT_READ', 'ORDER_READ',
    'BILLING_CREATE', 'BILLING_PAYMENT', 'BILLING_REFUND'
  )
  AND NOT EXISTS (SELECT 1 FROM role_permission rp WHERE rp.role_id = r.id AND rp.permission_id = p.id AND rp.is_deleted = false);

-- VIEWER: PATIENT_READ, ORDER_READ, REPORT_PRINT
INSERT INTO role_permission (id, role_id, permission_id, created_at, updated_at, created_by, is_deleted)
SELECT gen_random_uuid(), r.id, p.id, now(), now(), 'system', false
FROM role r, permission p
WHERE r.branch_id = '00000000-0000-0000-0000-000000000000' AND r.code = 'VIEWER'
  AND p.code IN ('PATIENT_READ', 'ORDER_READ', 'REPORT_PRINT')
  AND NOT EXISTS (SELECT 1 FROM role_permission rp WHERE rp.role_id = r.id AND rp.permission_id = p.id AND rp.is_deleted = false);
