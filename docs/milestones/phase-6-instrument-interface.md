# Phase 6: Instrument Interface (Months 10-13)

> ASTM E1381/E1394 bidirectional communication with lab instruments.

## Milestone Goals

- ASTM E1381/E1394 protocol implementation
- TCP/IP connection management with instruments
- Bidirectional communication (results download + order upload)
- Tested with Roche Cobas c311 and Sysmex XN-1000
- RabbitMQ integration for result processing
- Angular screens: Instrument (8 screens)

## Documentation References

- [Instrument Interface Specification](../domain/instrument-interface-spec.md) — Full ASTM protocol
- [Async Processing — RabbitMQ](../architecture/async-processing.md)
- [Screen List — Instrument](../screens/screen-list-complete.md) — Screens 110-117

---

## Issues

### Backend — Instrument Module (`lis-instrument`)

#### LIS-089: Implement ASTM E1381 TCP/IP Connection Manager
**Labels:** `backend`, `infrastructure`
**Description:**
TCP/IP connection management for instruments:
- TCP server socket listening for instrument connections
- Connection configuration: host, port, instrument name, protocol version
- Connection lifecycle: DISCONNECTED → CONNECTING → CONNECTED → ERROR
- Heartbeat monitoring (configurable interval)
- Auto-reconnect on disconnect (with exponential backoff)
- Connection status dashboard data
- Support multiple simultaneous instrument connections

**Acceptance Criteria:**
- [ ] TCP server socket implementation (Netty or raw NIO)
- [ ] Connection configuration entity
- [ ] Connection state management
- [ ] Heartbeat monitoring
- [ ] Auto-reconnect logic
- [ ] Multi-connection support
- [ ] Unit tests with mock sockets

---

#### LIS-090: Implement ASTM E1394 Frame Parser
**Labels:** `backend`
**Description:**
ASTM message parsing:
- Frame structure: STX [Frame#][Record Data] ETX [Checksum] CR LF
- Record types: H (Header), P (Patient), O (Order), R (Result), C (Comment), Q (Query), L (Terminator)
- Checksum calculation: sum of bytes from frame number through ETX, mod 256
- Frame reassembly for multi-frame messages
- Handle ENQ/ACK/NAK handshake protocol
- Parse field delimiters (|, \, ^, &)

**Acceptance Criteria:**
- [ ] ASTM frame parser
- [ ] All record type parsers (H, P, O, R, C, Q, L)
- [ ] Checksum calculation and verification
- [ ] Multi-frame reassembly
- [ ] ENQ/ACK/NAK protocol handler
- [ ] Unit tests with real ASTM messages

---

#### LIS-091: Implement Result Mapper (Instrument Code → Parameter Code)
**Labels:** `backend`, `database`
**Description:**
Map instrument-specific codes to LIS parameter codes:
- InstrumentParameterMapping entity: instrument, instrumentCode, parameterCode, unit, conversionFactor
- Mapping configuration UI (admin screen)
- Auto-mapping suggestions based on code similarity
- Instrument-specific result parsing (different instruments send results differently)
- Roche Cobas mapping: GLU → Glucose, CHOL → Total Cholesterol, etc.
- Sysmex mapping: WBC → WBC Count, RBC → RBC Count, etc.

**Acceptance Criteria:**
- [ ] InstrumentParameterMapping entity
- [ ] Mapping lookup service
- [ ] Unit conversion support
- [ ] Roche Cobas mapping configuration
- [ ] Sysmex XN-1000 mapping configuration
- [ ] Flyway migration
- [ ] Unit tests

---

#### LIS-092: Implement Result Download (Instrument → LIS) Flow
**Labels:** `backend`
**Description:**
Receive results from instruments:
1. Instrument sends ENQ → LIS responds ACK
2. Instrument sends H record (header) → LIS responds ACK
3. Instrument sends P record (patient) → match to LIS patient
4. Instrument sends O record (order) → match to LIS order/sample
5. Instrument sends R record(s) (results) → map to parameters, store values
6. Instrument sends L record (terminator) → process complete message
7. Publish result to RabbitMQ queue for async processing
8. Consumer processes: map codes, validate, store in test_result

**Acceptance Criteria:**
- [ ] Complete result download flow
- [ ] Patient/order matching by sample barcode
- [ ] Parameter code mapping
- [ ] RabbitMQ publishing
- [ ] Result processing consumer
- [ ] Error handling for unmatched samples
- [ ] Integration tests

---

#### LIS-093: Implement Host Query Mode (LIS → Instrument Orders)
**Labels:** `backend`
**Description:**
Send pending orders to instruments:
- Instrument sends Q record (query by sample ID)
- LIS looks up pending tests for the sample
- LIS responds with H, P, O records containing order details
- Support for test profiles/panels
- Only send tests configured for the querying instrument

**Acceptance Criteria:**
- [ ] Host query handler
- [ ] Pending order lookup by sample barcode
- [ ] ASTM message construction (H, P, O records)
- [ ] Instrument-specific test filtering
- [ ] Unit tests

---

#### LIS-094: Implement Serial Port Support for Legacy Instruments
**Labels:** `backend`
**Description:**
RS-232 serial communication:
- Serial port connection manager (using jSerialComm library)
- Configuration: COM port, baud rate (9600), data bits (8), stop bits (1), parity (None)
- Same ASTM parsing as TCP but over serial
- USB-to-serial adapter support

**Acceptance Criteria:**
- [ ] Serial port connection manager
- [ ] jSerialComm integration
- [ ] ASTM over serial communication
- [ ] Configuration entity
- [ ] Manual testing with serial instruments

---

#### LIS-095: Implement RabbitMQ Integration for Instrument Results
**Labels:** `backend`, `infrastructure`
**Description:**
Async result processing pipeline:
- Exchange: `instrument.results` (topic exchange)
- Queue: `instrument.results.process` (with DLQ)
- Routing key: `result.{instrumentId}`
- Consumer: validate, map, store results
- Retry policy: 3 attempts with exponential backoff
- Dead letter queue for failed processing
- Result processing log for debugging

**Acceptance Criteria:**
- [ ] RabbitMQ exchange and queue configuration
- [ ] Message producer (instrument module)
- [ ] Message consumer (result processing)
- [ ] DLQ setup and monitoring
- [ ] Retry policy
- [ ] Processing log
- [ ] Integration tests

---

### Frontend — Instrument Screens

#### LIS-096: Build Instrument List and Connection Setup screens
**Labels:** `frontend`
**Description:**
Instrument management screens (Screens #110-111):
- Instrument list with connection status indicators (green/red/yellow)
- Real-time status via WebSocket
- Instrument connection setup form: name, model, protocol, host, port, baud rate
- Test connection button
- Connection logs viewer

**Acceptance Criteria:**
- [ ] Instrument list with status badges
- [ ] WebSocket real-time status
- [ ] Connection setup form
- [ ] Test connection functionality
- [ ] Connection log viewer

---

#### LIS-097: Build ASTM Parameter Mapping and Result Queue screens
**Labels:** `frontend`
**Description:**
Instrument configuration screens (Screens #112-113):
- ASTM parameter mapping: two-column mapping (instrument code ↔ LIS parameter)
- Drag-and-drop or dropdown mapping
- Auto-suggest based on code similarity
- Instrument result queue: incoming results with status
- Unmatched result highlighting

**Acceptance Criteria:**
- [ ] Mapping configuration UI
- [ ] Auto-suggest feature
- [ ] Result queue with status indicators
- [ ] Unmatched result alerts

---

#### LIS-098: Build Result Processing Log, Error Log, and Import screens
**Labels:** `frontend`
**Description:**
Monitoring screens (Screens #114-117):
- Result processing log: timestamp, instrument, sample, status, errors
- Error log: connection errors, parsing errors, mapping errors
- Manual result import: CSV file upload with column mapping
- Instrument calibration schedule management

**Acceptance Criteria:**
- [ ] Processing log with search/filter
- [ ] Error log with severity levels
- [ ] CSV import with preview
- [ ] Calibration schedule calendar

---

### Database

#### LIS-099: Create Flyway migrations for Instrument module tables
**Labels:** `backend`, `database`
**Description:**
Phase 6 database migrations:
- InstrumentConnection table (host, port, protocol, status)
- InstrumentParameterMapping table (instrument_code ↔ parameter_code)
- InstrumentResultQueue table (raw ASTM messages)
- InstrumentResultProcessingLog table
- InstrumentErrorLog table
- InstrumentCalibration table

**Acceptance Criteria:**
- [ ] All migrations created
- [ ] Indexes on instrument_id, status
- [ ] Queue table optimized for FIFO access
- [ ] Log tables with timestamp indexes

---

### Testing

#### LIS-100: ASTM protocol integration tests with mock instruments
**Labels:** `backend`, `testing`
**Description:**
Integration tests simulating instrument communication:
- Mock TCP instrument server sending ASTM messages
- Test ENQ/ACK/NAK handshake
- Test result download with Roche Cobas format
- Test result download with Sysmex format
- Test host query mode
- Test reconnection on disconnect
- Test malformed message handling

**Acceptance Criteria:**
- [ ] Mock instrument TCP server
- [ ] Roche Cobas result parsing test
- [ ] Sysmex result parsing test
- [ ] Host query test
- [ ] Reconnection test
- [ ] Error handling tests

---

## Completion Criteria

- [ ] TCP/IP ASTM communication functional
- [ ] Results flowing from instrument to LIS automatically
- [ ] Parameter mapping configuration working
- [ ] Host query mode operational
- [ ] RabbitMQ result processing pipeline stable
- [ ] Tested with Roche Cobas and Sysmex message formats
- [ ] Serial port support for legacy instruments
- [ ] All 8 instrument Angular screens implemented
- [ ] Connection monitoring and auto-reconnect working
- [ ] 80% test coverage on lis-instrument module
