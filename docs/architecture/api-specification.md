# API Specification — RasterOneLab LIS

## Conventions

### Base URL
```
http://localhost:8080/api/v1   (local)
https://api.rasteronelab.com/api/v1  (production)
```

### Required Headers
```
Authorization: Bearer <jwt-token>
X-Branch-Id: <branch-uuid>
Content-Type: application/json
```

### Standard Response Format

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { },
  "timestamp": "2024-01-15T14:30:00"
}
```

### Error Response
```json
{
  "success": false,
  "errorCode": "LIS-PAT-001",
  "message": "Patient not found",
  "timestamp": "2024-01-15T14:30:00"
}
```

### Paginated Response
```json
{
  "success": true,
  "data": {
    "content": [],
    "page": 0,
    "size": 20,
    "totalElements": 150,
    "totalPages": 8,
    "first": true,
    "last": false
  }
}
```

### HTTP Status Codes
| Status | Meaning |
|--------|---------|
| 200 | Success |
| 201 | Created |
| 204 | No Content (delete) |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 409 | Conflict |
| 422 | Validation Failed |
| 429 | Rate Limited |
| 500 | Server Error |

## Patient API

| Method | Path | Description | Roles |
|--------|------|-------------|-------|
| POST | `/patients` | Register patient | RECEPTIONIST, ADMIN |
| GET | `/patients` | List patients (paginated) | All staff |
| GET | `/patients/{id}` | Get patient | All staff |
| PUT | `/patients/{id}` | Update patient | RECEPTIONIST, ADMIN |
| DELETE | `/patients/{id}` | Soft delete | ADMIN |
| GET | `/patients/search` | Search by name/phone/UHID | All staff |
| GET | `/patients/uhid/{uhid}` | Get by UHID | All staff |
| GET | `/patients/{id}/visits` | Visit history | All staff |
| POST | `/patients/{id}/visits` | New visit | RECEPTIONIST |
| GET | `/patients/{id}/orders` | Order history | All staff |
| POST | `/patients/{id}/merge` | Merge duplicates | ADMIN |

## Order API

| Method | Path | Description | Roles |
|--------|------|-------------|-------|
| POST | `/orders` | Create order | RECEPTIONIST, ADMIN |
| GET | `/orders` | List orders | All staff |
| GET | `/orders/{id}` | Get order | All staff |
| PUT | `/orders/{id}` | Update DRAFT order | RECEPTIONIST |
| DELETE | `/orders/{id}` | Cancel order | RECEPTIONIST, ADMIN |
| POST | `/orders/{id}/place` | Place order | RECEPTIONIST |
| GET | `/orders/barcode/{barcode}` | Find by barcode | All staff |
| GET | `/orders/pending-collection` | Pending collection | PHLEBOTOMIST |

## Sample API

| Method | Path | Description | Roles |
|--------|------|-------------|-------|
| POST | `/samples/collect` | Record collection | PHLEBOTOMIST |
| GET | `/samples` | List samples | Lab staff |
| GET | `/samples/{id}` | Get sample | Lab staff |
| POST | `/samples/{id}/receive` | Accept/reject | LAB_TECHNICIAN |
| POST | `/samples/{id}/aliquot` | Create aliquots | LAB_TECHNICIAN |
| GET | `/samples/barcode/{barcode}` | Find by barcode | Lab staff |
| POST | `/samples/{id}/reject` | Reject sample | LAB_TECHNICIAN |
| GET | `/samples/pending-receipt` | Pending receipt | LAB_TECHNICIAN |
| POST | `/samples/{id}/transfer` | Transfer to branch | LAB_TECHNICIAN |
| PUT | `/samples/{id}/storage` | Update location | LAB_TECHNICIAN |
| POST | `/samples/{id}/dispose` | Mark disposed | LAB_TECHNICIAN |

## Result API

| Method | Path | Description | Roles |
|--------|------|-------------|-------|
| GET | `/results` | List results | Lab staff |
| GET | `/results/{id}` | Get result | Lab staff |
| POST | `/results/{orderId}/enter` | Enter result | LAB_TECHNICIAN |
| PUT | `/results/{id}` | Update result | LAB_TECHNICIAN |
| POST | `/results/{id}/validate` | Validate | LAB_TECHNICIAN |
| POST | `/results/{id}/authorize` | Authorize | PATHOLOGIST |
| POST | `/results/{id}/amend` | Amend | PATHOLOGIST |
| GET | `/results/pending-entry` | Pending entry | LAB_TECHNICIAN |
| GET | `/results/pending-authorization` | Pending auth | PATHOLOGIST |
| GET | `/results/{id}/delta-check` | Delta comparison | PATHOLOGIST |
| POST | `/results/{id}/critical-ack` | Acknowledge critical | PATHOLOGIST |
| GET | `/results/critical-pending` | Unacknowledged critical | PATHOLOGIST |
| GET | `/results/by-order/{orderId}` | By order | All staff |

## Report API

| Method | Path | Description | Roles |
|--------|------|-------------|-------|
| GET | `/reports` | List reports | All staff |
| GET | `/reports/{id}` | Get report | All staff |
| POST | `/reports/{orderId}/generate` | Generate PDF | PATHOLOGIST |
| POST | `/reports/{id}/release` | Release | PATHOLOGIST |
| GET | `/reports/{id}/download` | Download PDF | All staff |
| POST | `/reports/{id}/deliver` | Send via SMS/Email | RECEPTIONIST |
| POST | `/reports/{id}/amend` | Amend | PATHOLOGIST |

## Billing API

| Method | Path | Description | Roles |
|--------|------|-------------|-------|
| GET | `/invoices` | List invoices | BILLING_STAFF |
| GET | `/invoices/{id}` | Get invoice | BILLING_STAFF |
| POST | `/invoices/{orderId}/generate` | Generate invoice | BILLING_STAFF |
| POST | `/invoices/{id}/payment` | Record payment | BILLING_STAFF |
| POST | `/invoices/{id}/discount` | Apply discount | ADMIN |
| GET | `/invoices/outstanding` | Outstanding invoices | BILLING_STAFF |
| POST | `/invoices/{id}/refund` | Process refund | ADMIN |

## Admin API (Master Data)

All admin endpoints require ADMIN or SUPER_ADMIN role.

| Resource | CRUD Endpoints |
|----------|----------------|
| branches | GET/POST/PUT/DELETE `/admin/branches` |
| departments | GET/POST/PUT/DELETE `/admin/departments` |
| tests | GET/POST/PUT/DELETE `/admin/tests` |
| panels | GET/POST/PUT/DELETE `/admin/panels` |
| parameters | GET/POST/PUT/DELETE `/admin/parameters` |
| reference-ranges | GET/POST/PUT/DELETE `/admin/reference-ranges` |
| doctors | GET/POST/PUT/DELETE `/admin/doctors` |
| instruments | GET/POST/PUT/DELETE `/admin/instruments` |
| qc-materials | GET/POST/PUT/DELETE `/admin/qc-materials` |
| users | GET/POST/PUT `/admin/users` |
| price-catalog | GET/POST/PUT `/admin/price-catalog` |

## Dashboard API

| Method | Path | Description |
|--------|------|-------------|
| GET | `/dashboard/summary` | Today's stats |
| GET | `/dashboard/tat-compliance` | TAT compliance % |
| GET | `/dashboard/pending-actions` | Items needing action |
| GET | `/dashboard/critical-pending` | Unacknowledged critical values |
| GET | `/dashboard/instrument-status` | Instrument connection status |
