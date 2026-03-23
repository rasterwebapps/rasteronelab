import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LabReport, ReportStatus, ReportSignRequest, ReportDeliverRequest } from './report.model';

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
export class ReportService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/reports`;

  getAll(page = 0, size = 20): Observable<PageResponse<LabReport>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<LabReport>>(this.baseUrl, { params });
  }

  getById(id: string): Observable<DataResponse<LabReport>> {
    return this.http.get<DataResponse<LabReport>>(`${this.baseUrl}/${id}`);
  }

  getByPatient(patientId: string, page = 0, size = 20): Observable<PageResponse<LabReport>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<LabReport>>(`${this.baseUrl}/patient/${patientId}`, { params });
  }

  getByOrder(orderId: string): Observable<DataResponse<LabReport>> {
    return this.http.get<DataResponse<LabReport>>(`${this.baseUrl}/order/${orderId}`);
  }

  getByStatus(status: ReportStatus, page = 0, size = 20): Observable<PageResponse<LabReport>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<LabReport>>(`${this.baseUrl}/status/${status}`, { params });
  }

  sign(id: string, request: ReportSignRequest): Observable<DataResponse<LabReport>> {
    return this.http.put<DataResponse<LabReport>>(`${this.baseUrl}/${id}/sign`, request);
  }

  deliver(id: string, request: ReportDeliverRequest): Observable<DataResponse<LabReport>> {
    return this.http.put<DataResponse<LabReport>>(`${this.baseUrl}/${id}/deliver`, request);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
