# Process Flow: Critical Value Workflow — RasterOneLab LIS

## Overview

Critical values (panic values) are results so extreme they require immediate physician notification before report can be released.

## State Machine

```
Result Entered → Critical Detected → Pathologist Alerted → Physician Notified → Documented → Authorized → Released
```

## Detailed Flow

```
1. Result saved (instrument or manual)
       │
       ▼
2. System checks: value < critical_low OR value > critical_high?
       │
   ┌── YES ──────────────────────────────────────────┐
   │                                                  │
   ▼                                                  ▼
3a. Flag: is_critical = TRUE                      NO → Normal flow
    Create critical_value_log entry
    Status: DETECTED
       │
       ▼
4. Real-time alerts:
   - RED banner on all pathologist screens
   - WebSocket push to online pathologists
   - SMS to on-duty pathologist (immediate)
       │
       ▼
5. Pathologist opens critical alert
   - Views: Patient name, UHID, test, value, time
   - Reviews any related results
       │
       ▼
6. Pathologist contacts physician:
   - Looks up referring doctor phone from order
   - Calls physician (or WhatsApp)
   - Reads the critical value
   - Physician reads back ("Glucose 35 mg/dL, understood")
       │
       ▼
7. Pathologist documents in LIS:
   - Physician name notified: "Dr. Raj Kumar"
   - Method: PHONE
   - Time: 11:45 AM
   - Read-back confirmed: YES
   - Notes: "Physician said patient will be recalled immediately"
       │
       ▼
8. LIS validates documentation:
   - physician_notified_name is required
   - notified_at is required
   - read_back_confirmed must be TRUE
   - Only then: AUTHORIZE button becomes available
       │
       ▼
9. Pathologist authorizes result
   - Status: AUTHORIZED
       │
       ▼
10. Report generated with CRITICAL banner
    - Report header shows: "⚠ CRITICAL VALUE - URGENT"
    - PDF generated
    - Auto-delivered to referring doctor (WhatsApp PDF)
       │
       ▼
11. Receptionist informed:
    - Screen alert: "Critical value report for John Doe ready - ensure patient contacted"
       │
       ▼
END
```

## LIS Screens Involved

| Step | Screen | Role |
|------|--------|------|
| 4-5 | Dashboard → Critical Values | PATHOLOGIST |
| 6 | (Phone call — offline) | PATHOLOGIST |
| 7 | Result → Critical Value Documentation | PATHOLOGIST |
| 9 | Result → Authorize | PATHOLOGIST |
| 10 | Automated | SYSTEM |
| 11 | Notification Center | RECEPTIONIST |

## What Happens If Not Acknowledged (Escalation)

| Time After Detection | Action |
|---------------------|--------|
| 0 min | Real-time screen alert |
| 5 min | SMS to on-duty pathologist |
| 15 min | SMS to all pathologists in branch |
| 30 min | SMS to Lab Director |
| 60 min | Alert escalates to ORG_ADMIN |

## Reporting & Compliance

All critical values are reportable:
- Daily critical value report for lab director
- Monthly critical value statistics
- NABL audit: critical value documentation must be reviewable
- Retention: 7 years minimum
