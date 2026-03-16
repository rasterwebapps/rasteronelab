# Instrument Interface Specification — ASTM E1381/E1394

## Overview

ASTM (American Society for Testing and Materials) protocol E1381/E1394 is the standard for communication between laboratory instruments and LIS.

## Frame Structure

```
ENQ (0x05)         → Instrument requests to transmit
STX (0x02)         → Start of frame
[Frame Number 1]   → 1-7, wraps around
[Record Data]      → Comma-separated fields
ETX (0x03)         → End of text
[Checksum 2 bytes] → Sum of bytes from STX to ETX
CR (0x0D) LF (0x0A) → Frame end

ACK (0x06)         → LIS acknowledges good frame
NAK (0x15)         → LIS rejects bad frame (bad checksum)
EOT (0x04)         → End of transmission
```

## Record Types

| Type | Record | Description |
|------|--------|-------------|
| H | Header | Transmission info, sender/receiver, date/time |
| P | Patient | Patient demographics (often not used if LIS sends orders) |
| O | Order | Test order information |
| R | Result | Individual test result |
| C | Comment | Comments on previous record |
| Q | Query | Host query for orders |
| L | Terminator | Last record in transmission |

## Sample ASTM Message (Instrument → LIS)

```
ENQ
STX 1H|\^&|||Cobas^c311||||||LIS|P|1|20240115143025EOT CHK
STX 2P|1||Patient001|Doe^John||19780315|M|||||||REFLAB EOT CHK
STX 3O|1|SMP-DEL-20240115-000123||^^^GLU\^^^CREA\^^^UREA|R||20240115143000|||||A|||||||||20240115143025||||||EOT CHK
STX 4R|1|^^^GLU|110.5|mg/dL|70_100|N|||F|||20240115143015EOT CHK
STX 5R|2|^^^CREA|1.10|mg/dL|0.60_1.20|N|||F|||20240115143015EOT CHK
STX 6L|1|N EOT CHK
EOT
```

## Checksum Calculation

```java
public static String calculateChecksum(String data) {
    int sum = 0;
    for (char c : data.toCharArray()) {
        sum += c;
    }
    return String.format("%02X", sum % 256);
}
```

## Bidirectional Communication Flow

### Result Download (Instrument → LIS)
```
Instrument: ENQ
LIS: ACK
Instrument: STX H record ETX CHK
LIS: ACK
Instrument: STX P record ETX CHK
LIS: ACK
Instrument: STX O record ETX CHK
LIS: ACK
Instrument: STX R records ETX CHK (one per result)
LIS: ACK
Instrument: STX L record ETX CHK
LIS: ACK
Instrument: EOT
```

### Order Download (LIS → Instrument / Host Query Mode)
```
Instrument: ENQ
LIS: ACK
Instrument: STX Q record (query by barcode) ETX CHK
LIS: ACK
Instrument: EOT
LIS: ENQ
Instrument: ACK
LIS: STX H record ETX CHK
Instrument: ACK
LIS: STX P record ETX CHK (patient info)
LIS: STX O record ETX CHK (order info with tests)
Instrument: ACK
LIS: STX L record ETX CHK
Instrument: ACK
LIS: EOT
```

## TCP/IP Connection Configuration

```java
@Configuration
public class InstrumentConnectionConfig {
    
    @Bean
    public InstrumentConnection cobas311Connection() {
        return InstrumentConnection.builder()
            .instrumentId("COBAS-001")
            .ipAddress("192.168.1.100")
            .port(4100)
            .protocol(Protocol.ASTM_OVER_TCPIP)
            .reconnectIntervalSeconds(30)
            .heartbeatIntervalSeconds(60)
            .build();
    }
}
```

## Instrument-Specific ASTM Field Mappings

### Roche Cobas c311/c501
| ASTM Code | LIS Parameter Code | Test Name |
|-----------|-------------------|-----------|
| GLU | GLU | Glucose |
| CREA | CREA | Creatinine |
| BUN | UREA | Urea |
| ALT | ALT | SGPT |
| AST | AST | SGOT |
| CHOL | TC | Total Cholesterol |
| TRIG | TG | Triglycerides |

### Sysmex XN-1000 (Hematology)
| ASTM Code | LIS Parameter Code | Test Name |
|-----------|-------------------|-----------|
| WBC | WBC | WBC Count |
| RBC | RBC | RBC Count |
| HGB | HGB | Hemoglobin |
| HCT | HCT | Hematocrit |
| MCV | MCV | MCV |
| MCH | MCH | MCH (calculated) |
| MCHC | MCHC | MCHC (calculated) |
| PLT | PLT | Platelets |
| NEU% | NEU_PCT | Neutrophils % |
| LYM% | LYM_PCT | Lymphocytes % |
| MON% | MON_PCT | Monocytes % |
| EOS% | EOS_PCT | Eosinophils % |
| BAS% | BAS_PCT | Basophils % |
