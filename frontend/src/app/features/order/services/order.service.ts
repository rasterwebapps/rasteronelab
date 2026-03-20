import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { TestOrder, TestOrderRequest, OrderStatus } from '../models/order.model';

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
export class OrderService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/orders`;

  getAll(page = 0, size = 20): Observable<PageResponse<TestOrder>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<TestOrder>>(this.baseUrl, { params });
  }

  getById(id: string): Observable<DataResponse<TestOrder>> {
    return this.http.get<DataResponse<TestOrder>>(`${this.baseUrl}/${id}`);
  }

  getByPatient(patientId: string, page = 0, size = 20): Observable<PageResponse<TestOrder>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<TestOrder>>(`${this.baseUrl}/patient/${patientId}`, { params });
  }

  getByStatus(status: OrderStatus, page = 0, size = 20): Observable<PageResponse<TestOrder>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<TestOrder>>(`${this.baseUrl}/status/${status}`, { params });
  }

  create(request: TestOrderRequest): Observable<DataResponse<TestOrder>> {
    return this.http.post<DataResponse<TestOrder>>(this.baseUrl, request);
  }

  placeOrder(id: string): Observable<DataResponse<TestOrder>> {
    return this.http.put<DataResponse<TestOrder>>(`${this.baseUrl}/${id}/place`, {});
  }

  cancelOrder(id: string, reason: string): Observable<DataResponse<TestOrder>> {
    return this.http.put<DataResponse<TestOrder>>(`${this.baseUrl}/${id}/cancel`, { reason });
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
