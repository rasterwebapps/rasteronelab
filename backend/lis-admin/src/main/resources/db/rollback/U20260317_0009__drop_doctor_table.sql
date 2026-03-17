-- Rollback: Drop doctor table

DROP INDEX IF EXISTS idx_doctor_is_deleted;
DROP INDEX IF EXISTS idx_doctor_is_active;
DROP INDEX IF EXISTS idx_doctor_registration_number;
DROP INDEX IF EXISTS idx_doctor_specialization;
DROP INDEX IF EXISTS idx_doctor_code;
DROP INDEX IF EXISTS idx_doctor_name;
DROP INDEX IF EXISTS idx_doctor_branch_id;
DROP TABLE IF EXISTS doctor;
