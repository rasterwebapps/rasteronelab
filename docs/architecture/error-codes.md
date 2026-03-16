# LIS Error Code Registry

All LIS errors follow the format: `LIS-{MODULE}-{NNN}`

## System Errors (LIS-SYS-xxx)

| Code | HTTP | Description |
|------|------|-------------|
| LIS-SYS-001 | 404 | Resource not found |
| LIS-SYS-002 | 422 | Validation failed |
| LIS-SYS-003 | 500 | Internal server error |
| LIS-SYS-004 | 503 | Service unavailable |
| LIS-SYS-005 | 400 | Invalid request format |
| LIS-SYS-006 | 409 | Duplicate resource |
| LIS-SYS-007 | 400 | Invalid UUID format |
| LIS-SYS-008 | 400 | Invalid date format |
| LIS-SYS-009 | 413 | File too large |
| LIS-SYS-010 | 415 | Unsupported file type |

## Security Errors (LIS-SEC-xxx)

| Code | HTTP | Description |
|------|------|-------------|
| LIS-SEC-001 | 403 | Unauthorized action |
| LIS-SEC-002 | 403 | Branch access denied |
| LIS-SEC-003 | 401 | Token expired |
| LIS-SEC-004 | 401 | Invalid token |
| LIS-SEC-005 | 403 | Insufficient role |
| LIS-SEC-006 | 429 | Rate limit exceeded |
| LIS-SEC-007 | 403 | IP blocked |
| LIS-SEC-008 | 401 | Session expired |

## Patient Errors (LIS-PAT-xxx)

| Code | HTTP | Description |
|------|------|-------------|
| LIS-PAT-001 | 404 | Patient not found |
| LIS-PAT-002 | 409 | Duplicate patient |
| LIS-PAT-003 | 422 | Invalid date of birth |
| LIS-PAT-004 | 422 | Invalid phone number |
| LIS-PAT-005 | 422 | Invalid email format |
| LIS-PAT-006 | 400 | UHID already assigned |
| LIS-PAT-007 | 404 | Patient visit not found |
| LIS-PAT-008 | 422 | Minor requires guardian |

## Order Errors (LIS-ORD-xxx)

| Code | HTTP | Description |
|------|------|-------------|
| LIS-ORD-001 | 404 | Order not found |
| LIS-ORD-002 | 409 | Order already paid |
| LIS-ORD-003 | 422 | No tests selected |
| LIS-ORD-004 | 422 | Invalid test code |
| LIS-ORD-005 | 422 | Test not available at branch |
| LIS-ORD-006 | 409 | Order already cancelled |
| LIS-ORD-007 | 422 | Cannot cancel after sample collected |
| LIS-ORD-008 | 404 | Doctor not found |
| LIS-ORD-009 | 422 | Order past TAT deadline |
| LIS-ORD-010 | 422 | Invalid priority |

## Sample Errors (LIS-SMP-xxx)

| Code | HTTP | Description |
|------|------|-------------|
| LIS-SMP-001 | 404 | Sample not found |
| LIS-SMP-002 | 409 | Sample already received |
| LIS-SMP-003 | 422 | Sample rejected: hemolyzed |
| LIS-SMP-004 | 422 | Sample rejected: clotted |
| LIS-SMP-005 | 422 | Sample rejected: insufficient quantity |
| LIS-SMP-006 | 422 | Sample rejected: wrong tube |
| LIS-SMP-007 | 422 | Sample rejected: unlabeled |
| LIS-SMP-008 | 422 | Sample rejected: lipemic |
| LIS-SMP-009 | 409 | Sample already aliquoted |
| LIS-SMP-010 | 422 | Barcode already in use |
| LIS-SMP-011 | 404 | Barcode not found |
| LIS-SMP-012 | 422 | Invalid tube type for test |
| LIS-SMP-013 | 409 | Sample already disposed |

## Result Errors (LIS-RES-xxx)

| Code | HTTP | Description |
|------|------|-------------|
| LIS-RES-001 | 404 | Result not found |
| LIS-RES-002 | 409 | Result already authorized |
| LIS-RES-003 | 422 | Value out of instrument range |
| LIS-RES-004 | 422 | Delta check violation |
| LIS-RES-005 | 422 | Critical value: physician notification required |
| LIS-RES-006 | 422 | Invalid result value for parameter type |
| LIS-RES-007 | 409 | Result cannot be amended: already delivered |
| LIS-RES-008 | 422 | Differential count must sum to 100% |
| LIS-RES-009 | 422 | Required parameter missing |
| LIS-RES-010 | 403 | Only pathologist can authorize results |
| LIS-RES-011 | 422 | QC failed: result cannot be validated |
| LIS-RES-012 | 409 | Instrument result already processed |
| LIS-RES-013 | 422 | Culture sensitivity requires organism first |
| LIS-RES-014 | 404 | Parameter reference range not configured |

## Billing Errors (LIS-BIL-xxx)

| Code | HTTP | Description |
|------|------|-------------|
| LIS-BIL-001 | 404 | Invoice not found |
| LIS-BIL-002 | 409 | Invoice already paid |
| LIS-BIL-003 | 422 | Payment exceeds invoice total |
| LIS-BIL-004 | 422 | Invalid payment method |
| LIS-BIL-005 | 422 | Credit limit exceeded |
| LIS-BIL-006 | 404 | Price not configured for test |
| LIS-BIL-007 | 422 | Discount exceeds maximum allowed |
| LIS-BIL-008 | 409 | Invoice already cancelled |

## Report Errors (LIS-RPT-xxx)

| Code | HTTP | Description |
|------|------|-------------|
| LIS-RPT-001 | 404 | Report not found |
| LIS-RPT-002 | 422 | Report not yet generated |
| LIS-RPT-003 | 409 | Report already released |
| LIS-RPT-004 | 422 | Report template not configured for branch |
| LIS-RPT-005 | 500 | PDF generation failed |
| LIS-RPT-006 | 422 | All results must be authorized before report generation |
| LIS-RPT-007 | 409 | Report already recalled |
