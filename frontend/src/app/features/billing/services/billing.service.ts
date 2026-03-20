import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Invoice, InvoiceRequest, InvoiceStatus, Payment, PaymentRequest } from '../models/billing.model';

export interface PageResponse<T> {
  data: T[];
  totalElements: number;
  totalPages: number;
  page: number;
  size: number;
}

export interface DataResponse<T> {
  data: T;
}

@Injectable({ providedIn: 'root' })
export class BillingService {
  private readonly http = inject(HttpClient);
  private readonly invoiceUrl = `${environment.apiUrl}/invoices`;
  private readonly paymentUrl = `${environment.apiUrl}/payments`;

  // --- Invoice methods ---

  getInvoices(page = 0, size = 20): Observable<PageResponse<Invoice>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Invoice>>(this.invoiceUrl, { params });
  }

  getInvoiceById(id: string): Observable<DataResponse<Invoice>> {
    return this.http.get<DataResponse<Invoice>>(`${this.invoiceUrl}/${id}`);
  }

  getInvoicesByPatient(patientId: string, page = 0, size = 20): Observable<PageResponse<Invoice>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Invoice>>(`${this.invoiceUrl}/patient/${patientId}`, { params });
  }

  getInvoicesByStatus(status: InvoiceStatus, page = 0, size = 20): Observable<PageResponse<Invoice>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Invoice>>(`${this.invoiceUrl}/status/${status}`, { params });
  }

  getInvoiceByOrder(orderId: string): Observable<DataResponse<Invoice>> {
    return this.http.get<DataResponse<Invoice>>(`${this.invoiceUrl}/order/${orderId}`);
  }

  generateInvoice(request: InvoiceRequest): Observable<DataResponse<Invoice>> {
    return this.http.post<DataResponse<Invoice>>(this.invoiceUrl, request);
  }

  applyDiscount(id: string, discountType: string, amount: number, reason?: string): Observable<DataResponse<Invoice>> {
    let params = new HttpParams()
      .set('discountType', discountType)
      .set('amount', amount.toString());
    if (reason) {
      params = params.set('reason', reason);
    }
    return this.http.put<DataResponse<Invoice>>(`${this.invoiceUrl}/${id}/discount`, null, { params });
  }

  deleteInvoice(id: string): Observable<void> {
    return this.http.delete<void>(`${this.invoiceUrl}/${id}`);
  }

  // --- Payment methods ---

  recordPayment(request: PaymentRequest): Observable<DataResponse<Payment>> {
    return this.http.post<DataResponse<Payment>>(this.paymentUrl, request);
  }

  getPaymentById(id: string): Observable<DataResponse<Payment>> {
    return this.http.get<DataResponse<Payment>>(`${this.paymentUrl}/${id}`);
  }

  getPayments(page = 0, size = 20): Observable<PageResponse<Payment>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Payment>>(this.paymentUrl, { params });
  }

  getPaymentsByInvoice(invoiceId: string): Observable<DataResponse<Payment[]>> {
    return this.http.get<DataResponse<Payment[]>>(`${this.paymentUrl}/invoice/${invoiceId}`);
  }
}
