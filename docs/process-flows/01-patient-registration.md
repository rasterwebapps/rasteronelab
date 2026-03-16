# Process Flow: Patient Registration — RasterOneLab LIS

## Trigger
Patient arrives at reception or home collection request received.

## Flow

```
START
  │
  ▼
Search patient (phone / name / UHID)
  │
  ├─ Found ──────► Verify patient identity ──► Create new visit
  │                                                  │
  └─ Not Found ──► Register new patient              │
                         │                           │
                         ▼                           │
                   Fill patient form                 │
                   (demographics)                    │
                         │                           │
                   Validate form                     │
                         │                           │
                   Generate UHID                     │
                         │                           │
                   Save patient record               │
                         │                           │
                   Print UHID card/label             │
                         │                           │
                         └──────────────────────────►│
                                                     ▼
                                             Patient visit created
                                                     │
                                                     ▼
                                                   END
```

## Required Fields
- Full Name (mandatory)
- Date of Birth or Age (mandatory)
- Gender (mandatory)
- Phone Number (mandatory, used for notifications)
- Email (optional)
- Address (recommended)
- Referring Doctor (optional at registration, can add at order)
- Blood Group (optional)

## UHID Generation
Format: `{BranchCode}-{6-digit-sequence}`  
Example: `DEL-001234`

- Branch-specific sequence (not organization-wide)
- Sequence never reused or recycled
- Stored in `branch_number_series` table with pessimistic lock

## Duplicate Detection
System checks before saving:
1. Same name + DOB → "Possible duplicate found" warning
2. Same phone number → Show existing patient list
3. Operator must confirm or proceed

## Audit
Patient creation logged in `audit_trail` (PHI access).

## API
```
POST /api/v1/patients
X-Branch-Id: {branchId}

Body: PatientRequest { fullName, gender, dateOfBirth, phoneNumber, email, ... }
Response: ApiResponse<PatientResponse> { id, uhid, ... }
```
