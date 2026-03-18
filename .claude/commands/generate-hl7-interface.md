# Generate HL7/ASTM Instrument Interface — /generate-hl7-interface

Generate complete instrument interface implementation for ASTM E1381/E1394 protocol.

## Usage
```
/generate-hl7-interface {instrumentName} {instrumentModel} {connectionType}
```

Example: `/generate-hl7-interface cobas RocheCobas8000 TCP_IP`

## What Gets Generated

### 1. ASTM Message Handler
```java
@Component
public class CobasAstmMessageHandler implements AstmMessageHandler {
    private final InstrumentResultMapper resultMapper;
    private final ResultAutoValidator autoValidator;
    private final RabbitMQPublisher publisher;
    private final InstrumentLogger instrumentLogger;

    public CobasAstmMessageHandler(InstrumentResultMapper resultMapper,
                                   ResultAutoValidator autoValidator,
                                   RabbitMQPublisher publisher,
                                   InstrumentLogger instrumentLogger) {
        this.resultMapper = resultMapper;
        this.autoValidator = autoValidator;
        this.publisher = publisher;
        this.instrumentLogger = instrumentLogger;
    }
    
    @Override
    public void handleResultMessage(AstmMessage message) {
        instrumentLogger.logReceived(message);
        try {
            List<InstrumentResult> results = parseResults(message);
            for (InstrumentResult result : results) {
                var mappedResult = resultMapper.mapToLisResult(result);
                var validationResult = autoValidator.validate(mappedResult);
                
                if (validationResult.isAutoApproved()) {
                    publisher.publishAutoApprovedResult(mappedResult);
                } else {
                    publisher.publishPendingValidationResult(mappedResult);
                }
            }
            sendAcknowledgment(AstmAck.ACK);
        } catch (Exception e) {
            instrumentLogger.logError(e, message);
            sendAcknowledgment(AstmAck.NAK);
        }
    }
}
```

### 2. ASTM Message Parser
```java
public class AstmMessageParser {
    // Parse frame structure: ENQ → STX + data + ETX + checksum → EOT
    // Record types: H (Header), P (Patient), O (Order), R (Result), C (Comment), Q (Query), L (Terminator)
    
    public AstmMessage parse(byte[] rawMessage) {
        // Validate checksum
        // Split into records by record type
        // Map to AstmMessage object
    }
    
    private byte calculateChecksum(byte[] data) {
        // Sum of ASCII values mod 256
    }
}
```

### 3. Result Mapper (Instrument-specific)
```java
@Component
public class CobasResultMapper implements InstrumentResultMapper {
    @Override
    public LisResult mapToLisResult(InstrumentResult instrumentResult) {
        return LisResult.builder()
            .sampleBarcode(instrumentResult.getSampleId())
            .parameterCode(mapParameterCode(instrumentResult.getTestCode()))
            .value(new BigDecimal(instrumentResult.getValue()))
            .unit(instrumentResult.getUnit())
            .flags(parseFlags(instrumentResult.getResultFlags()))
            .instrumentId(instrumentResult.getInstrumentId())
            .analyzedAt(parseDateTime(instrumentResult.getDateTime()))
            .build();
    }
    
    private String mapParameterCode(String instrumentCode) {
        // Map instrument-specific codes to LIS LOINC codes
        return instrumentCodeMappings.get(instrumentCode);
    }
}
```

### 4. Auto-Validation Rules
```java
@Service
public class ResultAutoValidator {
    public ValidationResult validate(LisResult result) {
        // Check: value in acceptable instrument range
        // Check: QC passed for this run
        // Check: no delta check violation
        // Check: not a critical value (requires manual authorization)
        // Check: no auto-validation exclusions (e.g., new patient, first result)
        
        if (allChecksPass(result)) {
            return ValidationResult.autoApproved();
        }
        return ValidationResult.requiresManualReview(failedChecks);
    }
}
```

### 5. Instrument Configuration
```java
@Entity
public class InstrumentConfig extends BaseEntity {
    private String instrumentName;    // "Roche Cobas 8000"
    private String instrumentCode;    // "COBAS_8000_01"
    private ConnectionType connectionType; // TCP_IP, SERIAL
    private String ipAddress;
    private Integer port;
    private String comPort;           // For serial: COM3
    private Integer baudRate;
    private AstmMode astmMode;        // UNIDIRECTIONAL, BIDIRECTIONAL, HOST_QUERY
    private Boolean autoValidation;
    private Integer resultTimeoutSeconds;
    private String departmentCode;
    
    @ElementCollection
    private Map<String, String> parameterMappings; // instrumentCode → LOINC
}
```

### 6. TCP/IP Connection Manager
```java
@Component
public class TcpInstrumentConnectionManager {
    private ServerSocket serverSocket;
    
    @EventListener(ApplicationReadyEvent.class)
    public void startListening() {
        // Start TCP server on configured port
        // Accept connections from instruments
        // Hand off to ASTM handler
    }
}
```
