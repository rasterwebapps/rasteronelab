# Async Processing — RabbitMQ Architecture

## Exchange Design

```
lis.direct (Direct Exchange)
├── lis.instrument.results     → Instrument result processing
├── lis.report.generate        → Report generation queue
├── lis.notification.sms       → SMS sending queue
├── lis.notification.email     → Email sending queue
├── lis.notification.whatsapp  → WhatsApp sending queue
└── lis.audit.log              → Audit trail logging

lis.topic (Topic Exchange)
├── lis.order.*                → All order events
├── lis.result.*               → All result events
└── lis.sample.*               → All sample events

lis.fanout (Fanout Exchange)
└── lis.critical.value         → Critical value (all consumers)
```

## Queue Configuration

```java
@Configuration
public class RabbitMQConfig {

    // Instrument Results Queue
    @Bean
    public Queue instrumentResultQueue() {
        return QueueBuilder.durable("lis.instrument.results")
            .withArgument("x-dead-letter-exchange", "lis.dlx")
            .withArgument("x-dead-letter-routing-key", "lis.instrument.results.dlq")
            .withArgument("x-message-ttl", 3600000) // 1 hour TTL
            .build();
    }

    // Dead Letter Queue (for failed messages)
    @Bean
    public Queue instrumentResultDlq() {
        return QueueBuilder.durable("lis.instrument.results.dlq").build();
    }

    // Report Generation Queue
    @Bean
    public Queue reportGenerationQueue() {
        return QueueBuilder.durable("lis.report.generate")
            .withArgument("x-dead-letter-exchange", "lis.dlx")
            .withArgument("x-max-priority", 10) // Priority queue
            .build();
    }
}
```

## Consumer Configurations

### Instrument Result Consumer
```java
@Component
@RabbitListener(queues = "lis.instrument.results", concurrency = "2-5")
public class InstrumentResultConsumer {

    @RabbitHandler
    public void processResult(InstrumentResultMessage message) {
        // 1. Look up patient and order by sample barcode
        // 2. Map instrument result to LIS result
        // 3. Run auto-validation
        // 4. Save result (auto-approved or pending manual)
        // 5. Check for critical values
        // 6. Publish events (ResultEnteredEvent, CriticalValueEvent if needed)
    }
}
```

### Report Generation Consumer
```java
@Component
@RabbitListener(queues = "lis.report.generate")
public class ReportGenerationConsumer {

    @RabbitHandler
    public void generateReport(GenerateReportMessage message) {
        // 1. Load all authorized results for order
        // 2. Load branch report template
        // 3. Generate PDF (OpenPDF)
        // 4. Store in MinIO
        // 5. Update report record with PDF path
        // 6. Publish ReportGeneratedEvent → notification queue
    }
}
```

## Message Structures

```java
// Instrument result message
public record InstrumentResultMessage(
    String sampleBarcode,
    String instrumentCode,
    String testCode,
    String parameterCode,
    String value,
    String unit,
    String flags,
    LocalDateTime analyzedAt
) {}

// Report generation message
public record GenerateReportMessage(
    UUID orderId,
    UUID branchId,
    Priority priority  // STAT reports generated first
) {}

// Notification message
public record NotificationMessage(
    String channel,       // SMS, EMAIL, WHATSAPP
    String recipient,     // phone or email
    String templateCode,
    Map<String, Object> variables
) {}
```

## Dead Letter Queue Monitoring

Failed messages go to DLQ after max retry attempts. Monitor via:
- RabbitMQ Management UI: `http://localhost:15672`
- Alert: If DLQ message count > 0 → PagerDuty/Slack alert
- Manual reprocessing: Admin can replay DLQ messages

## Message Persistence

All queues are `durable = true` ensuring messages survive RabbitMQ restart.
Messages are persistent (delivery mode 2) for instrument results and reports.
