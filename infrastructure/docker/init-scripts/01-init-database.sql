-- ============================================================
-- RasterOneLab LIS — PostgreSQL Initialization Script
-- ============================================================
-- This script runs automatically when the PostgreSQL container
-- is first created (via docker-entrypoint-initdb.d/).
-- It sets up extensions, schemas, and initial configuration.
-- ============================================================

-- Enable required extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";       -- Trigram index for fuzzy text search
CREATE EXTENSION IF NOT EXISTS "btree_gist";     -- GiST index support

-- Set default timezone
SET timezone = 'UTC';

-- Create application schema (optional — default 'public' is used)
-- All LIS tables use the public schema with row-level branch_id filtering.

-- Grant permissions to the application user
-- (The default user from POSTGRES_USER env var already has full access)

-- Log initialization
DO $$
BEGIN
    RAISE NOTICE 'RasterOneLab LIS database initialized successfully.';
END $$;
