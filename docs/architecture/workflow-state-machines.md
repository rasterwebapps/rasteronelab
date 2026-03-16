# Workflow State Machines — RasterOneLab LIS

## Order State Machine

```
DRAFT ──place()──► PLACED ──pay()──► PAID
  │                  │                 │
cancel()           cancel()      collect_sample()
  │                  │                 │
  ▼                  ▼                 ▼
CANCELLED         CANCELLED    SAMPLE_COLLECTED
                                       │
                               receive_sample()
                                       │
                                       ▼
                              SAMPLE_RECEIVED
                                       │
                                 process()
                                       │
                                       ▼
                                  PROCESSING
                                       │
                              result_entered()
                                       │
                                       ▼
                              RESULT_PENDING
                                       │
                              all_results_entered()
                                       │
                                       ▼
                             RESULT_AVAILABLE
                                       │
                              authorize()
                                       │
                                       ▼
                                 AUTHORIZED
                                       │
                            generate_report()
                                       │
                                       ▼
                            REPORT_GENERATED
                                       │
                               deliver()
                                       │
                                       ▼
                                  DELIVERED
                                       │
                            confirm_receipt()
                                       │
                                       ▼
                                  COMPLETED
```

### Order Transitions

| From | To | Action | Required Role | Trigger |
|------|----|--------|--------------|---------|
| DRAFT | PLACED | place() | RECEPTIONIST | Manual |
| PLACED | PAID | pay() | BILLING_STAFF | Payment recording |
| PAID | SAMPLE_COLLECTED | collect_sample() | PHLEBOTOMIST | Collection screen |
| SAMPLE_COLLECTED | SAMPLE_RECEIVED | receive_sample() | LAB_TECHNICIAN | Sample receipt |
| SAMPLE_RECEIVED | PROCESSING | process() | LAB_TECHNICIAN | Auto or manual |
| PROCESSING | RESULT_PENDING | result_entered() | LAB_TECHNICIAN | First result entered |
| RESULT_PENDING | RESULT_AVAILABLE | all_results_entered() | LAB_TECHNICIAN | All results entered |
| RESULT_AVAILABLE | AUTHORIZED | authorize() | PATHOLOGIST | Authorization screen |
| AUTHORIZED | REPORT_GENERATED | generate_report() | SYSTEM | Auto-triggered |
| REPORT_GENERATED | DELIVERED | deliver() | RECEPTIONIST | Manual or auto |
| DELIVERED | COMPLETED | confirm_receipt() | SYSTEM | After 24 hours |
| Any (before PAID) | CANCELLED | cancel() | RECEPTIONIST | Manual |
| Any | ON_HOLD | hold() | PATHOLOGIST | Manual |

---

## Sample State Machine

```
COLLECTED ──receive()──► RECEIVED
                              │
              ┌───────────────┴───────────────┐
              ▼                               ▼
           ACCEPTED                        REJECTED
              │
         aliquot() / process()
              │
          ALIQUOTED / PROCESSING
              │
           complete()
              │
           COMPLETED
              │
           store()
              │
           STORED
              │
          dispose()
              │
           DISPOSED
```

### Sample Rejection Reasons
- HEMOLYZED, CLOTTED, INSUFFICIENT, WRONG_TUBE, UNLABELED, LIPEMIC, ICTERIC, DETERIORATED, OLD

---

## Result State Machine

```
PENDING
   │
instrument_received() / enter()
   │
   ▼
INSTRUMENT_RECEIVED / ENTERED
   │
validate()
   │
   ▼
VALIDATED
   │
authorize()   ◄── Only PATHOLOGIST
   │
   ▼
AUTHORIZED
   │
release()
   │
   ▼
RELEASED
   │
amend()   ──────► AMENDED (loops back to VALIDATED for re-authorization)
```

### Critical Value Rules
- Critical values cannot be auto-authorized
- Must have physician notification recorded before authorization
- Critical value log entry mandatory

---

## Invoice State Machine

```
DRAFT ──generate()──► GENERATED ──partial_pay()──► PARTIALLY_PAID
                           │                              │
                       full_pay()                    full_pay()
                           │                              │
                           └──────────────┬───────────────┘
                                          ▼
                                        PAID
                                          │
                                      refund()
                                          │
                                       REFUNDED
```

---

## Report State Machine

```
PENDING ──generate()──► GENERATED ──release()──► RELEASED ──deliver()──► DELIVERED
                                                    │
                                                 amend()
                                                    │
                                                  AMENDED (new version generated)
                                                    │
                                               re-release()
                                                    │
                                                RELEASED (v2)
```

### Report Versioning
- Each amendment creates a new version (v1, v2, v3...)
- Previous versions stored in MinIO, accessible to ADMIN
- Patient/Doctor portal always shows latest version
- Amendment reason required
