# Generate Flyway Migration — /generate-migration

Generate a Flyway database migration SQL file.

## Usage
```
/generate-migration {action} {tableName} {fields...}
```

Actions: `create-table`, `add-column`, `add-index`, `add-fk`, `modify-column`

## Example
```
/generate-migration create-table test_parameter "name VARCHAR(100) NOT NULL, code VARCHAR(50) NOT NULL UNIQUE, unit VARCHAR(20), data_type VARCHAR(30) NOT NULL, decimal_places INT DEFAULT 2"
```

## Generated File: `resources/db/migration/V20240115_0100__create_test_parameter.sql`

```sql
-- =============================================================================
-- Migration: Create test_parameter table for lab test parameter master
-- Author: lis-system
-- =============================================================================

CREATE TABLE test_parameter (
    -- Primary Key - UUID v7
    id UUID PRIMARY KEY,
    
    -- Multi-branch column (MANDATORY)
    branch_id UUID NOT NULL,
    
    -- Domain columns
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    unit VARCHAR(20),
    data_type VARCHAR(30) NOT NULL,
    decimal_places INT DEFAULT 2,
    
    -- Audit columns (MANDATORY)
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    
    -- Soft delete columns (MANDATORY)
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP WITH TIME ZONE
);

-- Indexes (MANDATORY)
CREATE INDEX idx_test_parameter_branch_id ON test_parameter(branch_id);
CREATE INDEX idx_test_parameter_branch_deleted ON test_parameter(branch_id, is_deleted);
CREATE INDEX idx_test_parameter_code ON test_parameter(code);
```

## Rules
1. File naming: `V{YYYYMMDD_HHmm}__{description}.sql` (double underscore separator)
2. Always include: id (UUID PK), branch_id, audit columns, soft delete columns
3. Always add indexes on: branch_id, (branch_id, is_deleted), foreign keys, frequently queried columns
4. Never edit a migration after it has been applied — create a new migration instead
5. Place migration files in: `resources/db/migration/`
6. Use `R__{description}.sql` for repeatable migrations (views, seed data)
