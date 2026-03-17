# AI Prompting Guide — RasterOneLab LIS Development

## Overview

This guide provides best practices for using Claude (Opus/Sonnet) to generate production-ready code for the RasterOneLab LIS project.

## C-G-C-O Framework

Every prompt should have four components:

```
C — Context: What the codebase is and where you are in it
G — Goal: What you want to achieve
C — Constraints: Rules/patterns to follow (reference this project's standards)
O — Output: What format you want (code, explanation, both)
```

### Example Prompt (Good)

```
[C] I'm working on the RasterOneLab LIS backend, specifically the lis-patient module.
    The project uses hexagonal architecture, Spring Boot 3.4, Java 21, and MapStruct.
    Base entity (BaseEntity) has: id (UUID), branchId (UUID), isDeleted, createdAt, etc.
    BranchContextHolder.requireCurrentBranchId() provides current branch context.

[G] Generate a complete PatientVisit entity and service for recording patient visits.
    A visit belongs to a patient and a branch. It stores visit date, visit type (WALK_IN, 
    APPOINTMENT, HOME_COLLECTION), notes, and referring doctor reference.

[C] Follow hexagonal architecture:
    - Entity in domain/model/, never exposed in API
    - Repository extends BranchAwareRepository
    - Use case interface in application/port/in/
    - Service in application/service/ with @Transactional
    - DTO in api/dto/, mapper in api/mapper/
    - Controller with @PreAuthorize for RECEPTIONIST and ADMIN roles
    - Flyway migration for the table
    - Unit test for service

[O] Provide complete, compilable Java code for all layers. Include the Flyway SQL migration.
```

## Slash Commands (Custom Skills)

Use the built-in slash commands for common tasks:

```
/generate-entity PatientVisit patient visitDate:LocalDate visitType:String notes:String
/generate-api patient visits GET /patients/{id}/visits "List all visits for a patient"
/generate-migration create patient_visit id:UUID patient_id:UUID visit_date:DATE
/generate-test unit PatientVisitServiceImpl patient
/generate-angular-module patient visit "id,patientId,visitDate,visitType,notes"
/review-code backend/lis-patient/src/main/java/
/security-audit lis-result
```

## Reference-Based Prompting

Instead of explaining everything, reference the docs:

```
"Following the pattern in @docs/architecture/multi-branch-implementation.md,
add branchId support to the DrugInteraction entity. The entity currently 
lacks branchId and BranchContextHolder usage."
```

## Common Prompt Templates

### Generate a new feature module

```
Generate a complete {FeatureName} module for the lis-{module} package following the 
hexagonal architecture at @docs/architecture/system-architecture.md.

Entity fields: {field1:type, field2:type, ...}

Apply all rules from @docs/development/coding-standards.md:
- branchId in entity and all queries
- MapStruct for DTO mapping
- @Transactional on service
- @PreAuthorize on controller
- Flyway migration
- JUnit 5 unit test

Target roles: {ROLE1, ROLE2}
```

### Fix a bug

```
In the RasterOneLab LIS backend (Spring Boot 3.4, Java 21):

Bug: {describe the bug with error message}
File: {full path to file}

Current code:
```java
{paste buggy code}
```

Expected behavior: {what should happen}
Actual behavior: {what happens}

Fix the bug without changing unrelated code. Follow the existing code style.
```

### Add a new API endpoint

```
Add a new API endpoint to lis-{module}:

Method: {GET/POST/PUT/DELETE}
Path: /api/v1/{resource}/{path}
Purpose: {what it does}
Input: {request body or path params}
Output: {what it returns}

Follow the API spec at @docs/architecture/api-specification.md.
Include: controller method, use case interface method, service implementation,
repository query, OpenAPI annotations, @PreAuthorize.
Required role: {ROLE}
```

## Anti-Patterns to Avoid in Prompts

| Anti-Pattern | Better Alternative |
|-------------|-------------------|
| "Write code for patient management" | "Generate PatientService.getPatient() that filters by branchId from BranchContextHolder" |
| "Add authentication" | "Add @PreAuthorize("hasRole('RECEPTIONIST')") to PatientController.createPatient()" |
| "Fix my code" | "Fix the NullPointerException at PatientServiceImpl.java:45 caused by missing BranchContextHolder" |
| "Create tests" | "Create JUnit 5 unit tests for PatientServiceImpl using Mockito, following the pattern in TestDataFactory" |

## Iterative Development Pattern

```
1. Scaffold: Generate entity + migration + skeleton service
   → /generate-entity Patient patient fullName:String gender:Gender dateOfBirth:LocalDate

2. Implement: Add business logic
   → "Implement the createPatient() method with UHID generation and duplicate detection"

3. Test: Generate tests
   → /generate-test unit PatientServiceImpl patient

4. Review: Validate against standards
   → /review-code backend/lis-patient/

5. Security: Audit the code
   → /security-audit backend/lis-patient/
```

## Tips for Large Generations

- Break large requests into smaller focused prompts
- Reference specific files (`@backend/lis-core/src/.../BaseEntity.java`) for context
- Ask for one layer at a time (entity first, then service, then controller)
- Always review generated Flyway migrations before running
- Test generated code by compiling: `./gradlew :lis-patient:compileJava`
