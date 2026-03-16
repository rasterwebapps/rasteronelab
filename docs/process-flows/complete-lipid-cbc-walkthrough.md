# Complete Walkthrough: Lipid Profile + CBC — RasterOneLab LIS

This document walks through an end-to-end scenario of a patient ordering Lipid Profile + CBC (Complete Blood Count) from registration to report delivery.

## Patient Scenario
- **Patient**: John Doe, 45 years, Male
- **Referring Doctor**: Dr. Priya Sharma
- **Branch**: Delhi Main Lab
- **Tests Ordered**: Lipid Profile (panel) + CBC (Hematology)
- **Priority**: ROUTINE
- **Total Amount**: ₹1,200

---

## Step 1: Patient Registration (09:00 AM)

**LIS Screen**: Reception → New Patient  
**Staff**: Receptionist

1. Search by phone number → Patient NOT found → Create new
2. Fill patient form:
   - Full Name: John Doe
   - DOB: 15 March 1979
   - Gender: Male
   - Phone: +91-9876543210
   - Address: 123 Main Street, New Delhi
3. System auto-generates: `UHID = DEL-001234`
4. Patient record saved, UHID label printed

**Database Actions**:
```sql
INSERT INTO patient (id, branch_id, uhid, full_name, gender, date_of_birth, phone_number)
VALUES (gen_uuid_v7(), 'branch-uuid', 'DEL-001234', 'John Doe', 'MALE', '1979-03-15', '+919876543210');
```

---

## Step 2: Test Ordering (09:05 AM)

**LIS Screen**: Reception → New Order  
**Staff**: Receptionist

1. Scan patient wristband / search UHID DEL-001234
2. Create new visit (visit_id generated)
3. Add tests:
   - Type "Lipid" → Select "Lipid Profile (Panel)" → Adds 5 sub-tests
   - Type "CBC" → Select "Complete Blood Count"
4. Select referring doctor: Dr. Priya Sharma
5. Total: ₹1,200 (Lipid ₹600 + CBC ₹600)
6. Click "Place Order" → Order barcode: `ORD-DEL-20240115-0001`

**Database Actions**:
```sql
INSERT INTO test_order (id, branch_id, patient_id, order_barcode, order_status, net_amount)
VALUES (gen_uuid_v7(), 'branch-uuid', 'patient-uuid', 'ORD-DEL-20240115-0001', 'PLACED', 1200.00);

-- 6 line items (5 lipid + 1 CBC)
INSERT INTO order_line_item (order_id, test_id, test_name, net_price) VALUES
    ('order-uuid', 'tc-id', 'Total Cholesterol', 0),  -- Part of panel
    ('order-uuid', 'tg-id', 'Triglycerides', 0),
    ('order-uuid', 'hdl-id', 'HDL Cholesterol', 0),
    ('order-uuid', 'ldl-id', 'LDL Cholesterol', 0),   -- Calculated
    ('order-uuid', 'vldl-id', 'VLDL', 0),              -- Calculated
    ('order-uuid', 'cbc-id', 'CBC', 600.00);
```

---

## Step 3: Billing & Payment (09:07 AM)

**LIS Screen**: Billing → Invoice  
**Staff**: Billing Staff / Receptionist

1. Invoice auto-generated from order: `INV-DEL-20240115-0001`
2. Patient pays ₹1,200 cash
3. Payment recorded, receipt printed
4. Order status → PAID

```sql
INSERT INTO invoice (order_id, patient_id, invoice_number, total_amount, status)
VALUES ('order-uuid', 'patient-uuid', 'INV-DEL-20240115-0001', 1200.00, 'GENERATED');

INSERT INTO payment (invoice_id, amount, method, paid_at)
VALUES ('invoice-uuid', 1200.00, 'CASH', NOW());

UPDATE invoice SET status = 'PAID', paid_amount = 1200.00, balance_amount = 0 WHERE id = 'invoice-uuid';
UPDATE test_order SET order_status = 'PAID' WHERE id = 'order-uuid';
```

---

## Step 4: Sample Collection (09:10 AM)

**LIS Screen**: Phlebotomy → Collect Samples  
**Staff**: Phlebotomist

1. Scan order barcode `ORD-DEL-20240115-0001`
2. System shows required tubes:
   - **Gold SST** (for Lipid Profile — serum needed)
   - **Purple EDTA** (for CBC)
3. Print tube labels with barcodes:
   - `SMP-DEL-20240115-000001` (SST)
   - `SMP-DEL-20240115-000002` (EDTA)
4. Collect blood, label tubes
5. Record collection time: 09:10 AM

```sql
INSERT INTO sample_collection (id, branch_id, order_id, patient_id, sample_barcode, tube_type, sample_type, status, collected_at, collected_by)
VALUES 
    (gen_uuid_v7(), 'branch-uuid', 'order-uuid', 'patient-uuid', 'SMP-DEL-20240115-000001', 'SST_GOLD', 'SERUM', 'COLLECTED', '2024-01-15 09:10:00', 'phlebotomist-id'),
    (gen_uuid_v7(), 'branch-uuid', 'order-uuid', 'patient-uuid', 'SMP-DEL-20240115-000002', 'EDTA_PURPLE', 'WHOLE_BLOOD', 'COLLECTED', '2024-01-15 09:10:00', 'phlebotomist-id');

UPDATE test_order SET order_status = 'SAMPLE_COLLECTED' WHERE id = 'order-uuid';
```

---

## Step 5: Sample Receiving at Lab (09:20 AM)

**LIS Screen**: Lab → Receive Samples  
**Staff**: Lab Technician

1. Scan sample barcode at receiving station
2. Visual inspection:
   - SST: Serum clear, adequate volume ✓
   - EDTA: No clots, adequate ✓
3. Accept both samples
4. Samples routed to appropriate workbenches:
   - SST → Biochemistry (Cobas c311)
   - EDTA → Hematology (Sysmex XN-1000)

```sql
UPDATE sample_collection SET 
    status = 'ACCEPTED', 
    received_at = '2024-01-15 09:20:00',
    received_by = 'lab-tech-id'
WHERE sample_barcode IN ('SMP-DEL-20240115-000001', 'SMP-DEL-20240115-000002');

UPDATE test_order SET order_status = 'SAMPLE_RECEIVED' WHERE id = 'order-uuid';
```

---

## Step 6: Instrument Processing (09:20–11:00 AM)

### Biochemistry: Cobas c311 processes SST sample
1. Lab technician places SST in instrument
2. Scans tube barcode `SMP-DEL-20240115-000001`
3. Instrument queries LIS via ASTM (Host Query Mode):
   - Sends Q record with barcode
   - LIS responds with O record: "Run GLU, TC, TG, HDL"
4. Cobas analyzes sample (~15 minutes)
5. Instrument sends results via ASTM:
   - TC = 220.0 mg/dL (H)
   - TG = 180.0 mg/dL
   - HDL = 42.0 mg/dL (L)
6. LIS receives ASTM message, parses results:
   - Auto-calculates LDL = 220 - 42 - (180/5) = 142.0 mg/dL (H)
   - Auto-calculates VLDL = 180/5 = 36.0 mg/dL
7. Delta check: First visit for this patient → no previous results → skip

### Hematology: Sysmex XN-1000 processes EDTA sample
1. Places EDTA in instrument, instrument queries LIS
2. Runs CBC (~5 minutes)
3. Results: WBC 8.2, RBC 4.8, Hgb 13.5, HCT 41.2%, MCV 86, MCH 28.1, MCHC 32.8, PLT 245,000
4. All within normal range
5. Scatter plot images auto-attached (WBC differential scatter)

**LIS Auto-Validation Check** (runs automatically):
- All results within instrument range ✓
- No critical values ✓
- QC passed earlier today (Westgard OK) ✓
- Delta check: N/A (first visit) ✓
- Lipid results: TC and LDL are High (H flag) — NOT critical
- CBC: All normal ✓
- **Decision**: CBC → auto-validated, Lipid → requires manual validation (H flags)

---

## Step 7: Result Validation & Authorization (11:30 AM)

**LIS Screen**: Result → Pending Validation  
**Staff**: Lab Technician (validate) → Pathologist (authorize)

1. Lab technician opens "Pending Results" screen
2. Sees Lipid Profile for John Doe (flagged H)
3. Reviews TC = 220 (H), LDL = 142 (H), HDL = 42 (L)
4. Technician validates → sends to pathologist
5. Pathologist reviews:
   - Clinically correlates: LDL 142 mg/dL (elevated), HDL 42 (low) — dyslipidemia pattern
   - Adds comment: "Lipid profile consistent with mixed dyslipidemia. Recommend dietary modification and clinical correlation."
6. Pathologist authorizes all results
7. CBC separately auto-authorized (passed auto-validation)

```sql
UPDATE test_result SET 
    status = 'AUTHORIZED',
    authorized_by = 'pathologist-id',
    authorized_at = '2024-01-15 11:30:00',
    comments = 'Lipid profile consistent with mixed dyslipidemia...'
WHERE order_id = 'order-uuid';

UPDATE test_order SET order_status = 'AUTHORIZED' WHERE id = 'order-uuid';
```

---

## Step 8: Report Generation & Delivery (11:31 AM)

**Automated Process** (triggered by AuthorizationEvent):

1. `ReportGenerationConsumer` picks up message from `lis.report.generate` queue
2. Loads all results for order
3. Generates PDF:
   - Page 1: Branch header, patient info, Biochemistry results
   - Page 2: Hematology results + CBC scatter plot image
   - Footer: Pathologist signature, QR code, barcode
4. PDF stored in MinIO: `reports/branch-uuid/2024/01/order-uuid/report-v1.pdf`
5. Report record updated: `status = RELEASED`

**Notification triggered automatically**:
- SMS to +91-9876543210: "Dear John Doe, your lab report (ORD-DEL-20240115-0001) is ready. Download: https://lab.com/r/xyz"
- WhatsApp PDF attachment sent to same number

**Timeline**:
```
09:00 - Registration
09:05 - Order placed
09:07 - Payment
09:10 - Sample collected
09:20 - Sample received
09:20 - Processing started
11:00 - Results received from instruments
11:30 - Authorized
11:31 - Report generated
11:31 - SMS/WhatsApp sent
─────────────────────────
Total TAT: 2h 31m (Target: 4h ✓ Within TAT)
```

---

## Exception Scenarios

### Scenario A: Sample Rejected (Hemolyzed)
- At Step 5, technician sees hemolyzed serum in SST tube
- Selects "REJECT" → Reason: "HEMOLYZED"
- System auto-notifies receptionist: "Sample SMP-DEL-20240115-000001 rejected - recollect required"
- Receptionist calls patient, schedules recollection
- New sample collected with new barcode, linked to same order

### Scenario B: Critical Value
- Glucose added to order
- Result: Glucose = 45 mg/dL (LL — CRITICAL LOW)
- System immediately:
  - Shows red alert on pathologist screen
  - Sends SMS to pathologist on duty
  - Blocks report generation until acknowledged
- Pathologist calls physician, documents call
- Only then can authorize and release

### Scenario C: Delta Check Violation
- Return patient (John Doe visits 2 months later)
- Previous Creatinine = 1.1 mg/dL
- New result = 8.5 mg/dL (674% increase)
- System shows: "DELTA CHECK ALERT: Creatinine changed 674% from 1.1 to 8.5 mg/dL"
- Lab technician investigates: re-run sample → Confirmed 8.5
- Technician overrides delta check with reason
- Pathologist reviews and authorizes with comment
