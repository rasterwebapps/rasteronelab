# Generate Liquibase Migration — /generate-migration

Generate a Liquibase database migration changelog file.

## Usage
```
/generate-migration {action} {tableName} {fields...}
```

Actions: `create-table`, `add-column`, `add-index`, `add-fk`, `modify-column`

## Example
```
/generate-migration create-table test_parameter "name VARCHAR(100) NOT NULL, code VARCHAR(50) NOT NULL UNIQUE, unit VARCHAR(20), data_type VARCHAR(30) NOT NULL, decimal_places INT DEFAULT 2"
```

## Generated File: `resources/db/changelog/YYYY-MM-DD-HHmm-create-test-parameter.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
        http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.20.xsd">

    <changeSet id="2024-01-15-001-create-test-parameter" author="lis-system">
        <comment>Create test_parameter table for lab test parameter master</comment>
        
        <createTable tableName="test_parameter">
            <!-- Primary Key - UUID v7 -->
            <column name="id" type="UUID">
                <constraints primaryKey="true" nullable="false"/>
            </column>
            
            <!-- Multi-branch column (MANDATORY) -->
            <column name="branch_id" type="UUID">
                <constraints nullable="false"/>
            </column>
            
            <!-- Domain columns -->
            <column name="name" type="VARCHAR(100)">
                <constraints nullable="false"/>
            </column>
            <column name="code" type="VARCHAR(50)">
                <constraints nullable="false" unique="true"/>
            </column>
            <column name="unit" type="VARCHAR(20)"/>
            <column name="data_type" type="VARCHAR(30)">
                <constraints nullable="false"/>
            </column>
            <column name="decimal_places" type="INT" defaultValueNumeric="2"/>
            
            <!-- Audit columns (MANDATORY) -->
            <column name="created_at" type="TIMESTAMP WITH TIME ZONE" defaultValueComputed="CURRENT_TIMESTAMP">
                <constraints nullable="false"/>
            </column>
            <column name="updated_at" type="TIMESTAMP WITH TIME ZONE" defaultValueComputed="CURRENT_TIMESTAMP">
                <constraints nullable="false"/>
            </column>
            <column name="created_by" type="VARCHAR(100)"/>
            <column name="updated_by" type="VARCHAR(100)"/>
            
            <!-- Soft delete columns (MANDATORY) -->
            <column name="is_deleted" type="BOOLEAN" defaultValueBoolean="false">
                <constraints nullable="false"/>
            </column>
            <column name="deleted_at" type="TIMESTAMP WITH TIME ZONE"/>
        </createTable>
        
        <!-- Indexes (MANDATORY) -->
        <createIndex indexName="idx_test_parameter_branch_id" tableName="test_parameter">
            <column name="branch_id"/>
        </createIndex>
        <createIndex indexName="idx_test_parameter_branch_deleted" tableName="test_parameter">
            <column name="branch_id"/>
            <column name="is_deleted"/>
        </createIndex>
        <createIndex indexName="idx_test_parameter_code" tableName="test_parameter">
            <column name="code"/>
        </createIndex>
        
        <rollback>
            <dropTable tableName="test_parameter"/>
        </rollback>
    </changeSet>

</databaseChangeLog>
```

## Rules
1. File naming: `YYYY-MM-DD-{sequence}-{action}-{table}.xml`
2. ChangeSet id: same as filename without extension
3. Always include: id (UUID PK), branch_id, audit columns, soft delete columns
4. Always add indexes on: branch_id, (branch_id, is_deleted), foreign keys, frequently queried columns
5. Always include `<rollback>` block
6. Add to master changelog: `resources/db/changelog/master.xml`
