# Code Review — /review-code

Review code against RasterOneLab LIS project standards.

## Usage
```
/review-code {file-or-diff}
```

## Review Checklist

### Architecture Compliance
- [ ] Hexagonal architecture packages followed (`domain/`, `application/`, `infrastructure/`, `api/`)
- [ ] No direct entity exposure in API responses (DTOs used)
- [ ] MapStruct used for all mapping (no manual mapping)
- [ ] Spring Events used for cross-module communication (not direct service calls)
- [ ] No circular dependencies between modules

### Multi-Branch
- [ ] All entities extend `BaseEntity` (which includes `branchId`)
- [ ] All repository queries filter by `branchId`
- [ ] `BranchContextHolder.getCurrentBranchId()` used in services
- [ ] No hardcoded branch IDs

### Naming Conventions
- [ ] Classes: `PascalCase`
- [ ] Methods/fields: `camelCase`
- [ ] REST endpoints: `kebab-case`
- [ ] DB tables/columns: `snake_case`
- [ ] Constants: `UPPER_SNAKE_CASE`

### Error Handling
- [ ] Custom exceptions used (`NotFoundException`, `ValidationException`, `UnauthorizedException`)
- [ ] Global exception handler (@ControllerAdvice) catches all exceptions
- [ ] Error responses use standard `ApiResponse` format with error codes

### Testing
- [ ] Unit tests for all service methods
- [ ] Integration tests for all controller endpoints
- [ ] Test coverage ≥ 80%
- [ ] Multi-branch isolation tested
- [ ] Test data builders used (not hardcoded test data)

### Security
- [ ] `@PreAuthorize` on all controller methods
- [ ] No sensitive data in logs
- [ ] Input validation on all DTOs

### Performance
- [ ] N+1 queries avoided (use `JOIN FETCH` or `@EntityGraph`)
- [ ] Pagination used for list endpoints
- [ ] Indexes on all `branch_id` columns
- [ ] Redis caching for frequently accessed master data

### Code Quality
- [ ] Constructor injection only (no `@Autowired` field injection)
- [ ] `BigDecimal` for monetary and lab values (not `double`/`float`)
- [ ] No `System.out.println` (use `@Slf4j`)
- [ ] Lombok annotations used to reduce boilerplate
- [ ] Java 21 features used where appropriate (records, sealed classes, text blocks)

### Angular/TypeScript
- [ ] Standalone components (no NgModules)
- [ ] Angular Signals used (not RxJS Subjects for state)
- [ ] `@if`/`@for` used (not `*ngIf`/`*ngFor`)
- [ ] `inject()` function used (not constructor injection)
- [ ] No `any` type
- [ ] `date-fns` for dates (not Moment.js)
- [ ] Proper error handling in HTTP calls
