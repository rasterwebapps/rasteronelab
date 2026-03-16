# Notification Architecture — RasterOneLab LIS

## Notification Channels

| Channel | Provider | Use Case |
|---------|----------|---------|
| SMS | Twilio / AWS SNS | Report ready, critical value alert, OTP |
| Email | SMTP / AWS SES | Detailed report, invoice, welcome |
| WhatsApp | BSP (Meta Partner) | Report PDF attachment, critical alert |
| Push | Firebase FCM | Mobile app notifications |
| In-App | WebSocket | Real-time LIS screen alerts |

## Notification Event Types (30+)

### Patient Events
1. `PATIENT_REGISTERED` — Welcome SMS/Email
2. `ORDER_PLACED` — Order confirmation
3. `SAMPLE_COLLECTED` — Sample collected confirmation
4. `REPORT_READY` — Report available notification
5. `INVOICE_GENERATED` — Invoice with amount due
6. `PAYMENT_RECEIVED` — Payment confirmation
7. `APPOINTMENT_REMINDER` — Home collection reminder

### Staff Events
8. `CRITICAL_VALUE_ALERT` — Immediate pathologist alert
9. `TAT_BREACH_WARNING` — Result near TAT deadline
10. `QC_FAILURE` — QC Westgard rule violation
11. `INSTRUMENT_DISCONNECTED` — Instrument connectivity lost
12. `LOW_STOCK_ALERT` — Inventory below minimum
13. `SAMPLE_REJECTION` — Sample rejected, recollection needed
14. `SENDOUT_RESULT_RECEIVED` — External lab result arrived

### Doctor Events
15. `REPORT_RELEASED` — Report available in portal
16. `CRITICAL_VALUE_DOCTOR` — Critical value for doctor's patient
17. `PENDING_REPORTS` — Daily summary of pending reports

## Notification Template Engine

```java
@Service
public class NotificationTemplateEngine {
    
    public String render(String templateCode, Map<String, Object> variables) {
        NotificationTemplate template = templateRepository
            .findByCodeAndBranchId(templateCode, BranchContextHolder.getCurrentBranchId());
        
        // Simple {{variable}} substitution
        String rendered = template.getBody();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            rendered = rendered.replace("{{" + entry.getKey() + "}}", 
                                       String.valueOf(entry.getValue()));
        }
        return rendered;
    }
}
```

### SMS Template Example
```
Template Code: REPORT_READY
Body: "Dear {{patientName}}, your lab report for order {{orderBarcode}} is ready. 
Download: {{reportUrl}} | {{branchName}} {{branchPhone}}"
```

## Retry Strategy

| Attempt | Delay | Escalation |
|---------|-------|-----------|
| 1st | Immediate | - |
| 2nd | 5 minutes | - |
| 3rd | 15 minutes | - |
| 4th | 1 hour | Alert admin |
| Final | Give up | Log as FAILED |

## DND (Do Not Disturb) Rules

- Patient notifications: 8:00 AM – 9:00 PM only
- Critical value alerts: 24/7 (no DND for critical)
- Staff notifications: Based on shift hours
- Doctor notifications: 8:00 AM – 10:00 PM only

## WhatsApp Business Setup

1. Register with Meta-approved BSP (Business Solution Provider)
2. Submit message templates for approval (24-72 hours)
3. Configure webhook URL in BSP portal: `https://api.lab.com/webhooks/whatsapp`
4. Store BSP credentials in environment variables

## Delivery Tracking

```sql
CREATE TABLE notification_log (
    id              UUID PRIMARY KEY,
    branch_id       UUID NOT NULL,
    event_type      VARCHAR(50) NOT NULL,
    channel         VARCHAR(20) NOT NULL,
    recipient       VARCHAR(200) NOT NULL,
    template_code   VARCHAR(50),
    message_body    TEXT,
    status          VARCHAR(20) DEFAULT 'PENDING',
    provider_id     VARCHAR(100),
    sent_at         TIMESTAMPTZ,
    delivered_at    TIMESTAMPTZ,
    failed_at       TIMESTAMPTZ,
    failure_reason  TEXT,
    retry_count     INTEGER DEFAULT 0,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
) PARTITION BY RANGE (created_at);
```
