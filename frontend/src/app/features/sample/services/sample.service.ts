import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {
  Sample,
  SampleCollectRequest,
  SampleReceiveRequest,
  SampleRejectRequest,
  SampleAliquotRequest,
  SampleStorageRequest,
  SampleTransferRequest,
  SampleTrackingEntry,
} from '../models/sample.model';

export interface ApiResponse<T> {
  success: boolean;
  message?: string;
  data: T;
}

export interface PageResponse<T> {
  data: T[];
  totalElements: number;
  totalPages: number;
  page: number;
  size: number;
}

@Injectable({ providedIn: 'root' })
export class SampleService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/samples`;

  // ── Collection ─────────────────────────────────────────────────────────────

  collectSamples(request: SampleCollectRequest): Observable<ApiResponse<Sample[]>> {
    return this.http.post<ApiResponse<Sample[]>>(`${this.baseUrl}/collect`, request);
  }

  // ── CRUD ────────────────────────────────────────────────────────────────────

  getAll(page = 0, size = 20): Observable<ApiResponse<PageResponse<Sample>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PageResponse<Sample>>>(this.baseUrl, { params });
  }

  getById(id: string): Observable<ApiResponse<Sample>> {
    return this.http.get<ApiResponse<Sample>>(`${this.baseUrl}/${id}`);
  }

  getByOrder(orderId: string): Observable<ApiResponse<Sample[]>> {
    return this.http.get<ApiResponse<Sample[]>>(`${this.baseUrl}/order/${orderId}`);
  }

  getByBarcode(barcode: string): Observable<ApiResponse<Sample>> {
    return this.http.get<ApiResponse<Sample>>(`${this.baseUrl}/barcode/${barcode}`);
  }

  getByStatus(status: string, page = 0, size = 20): Observable<ApiResponse<PageResponse<Sample>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PageResponse<Sample>>>(`${this.baseUrl}/status/${status}`, { params });
  }

  // ── Receiving ───────────────────────────────────────────────────────────────

  receiveSample(id: string, request: SampleReceiveRequest): Observable<ApiResponse<Sample>> {
    return this.http.put<ApiResponse<Sample>>(`${this.baseUrl}/${id}/receive`, request);
  }

  acceptSample(id: string, acceptedBy: string): Observable<ApiResponse<Sample>> {
    const params = new HttpParams().set('acceptedBy', acceptedBy);
    return this.http.put<ApiResponse<Sample>>(`${this.baseUrl}/${id}/accept`, null, { params });
  }

  rejectSample(id: string, request: SampleRejectRequest): Observable<ApiResponse<Sample>> {
    return this.http.put<ApiResponse<Sample>>(`${this.baseUrl}/${id}/reject`, request);
  }

  // ── Aliquoting ──────────────────────────────────────────────────────────────

  aliquotSample(id: string, request: SampleAliquotRequest): Observable<ApiResponse<Sample[]>> {
    return this.http.post<ApiResponse<Sample[]>>(`${this.baseUrl}/${id}/aliquot`, request);
  }

  // ── Storage ─────────────────────────────────────────────────────────────────

  storeSample(id: string, request: SampleStorageRequest): Observable<ApiResponse<Sample>> {
    return this.http.put<ApiResponse<Sample>>(`${this.baseUrl}/${id}/storage`, request);
  }

  disposeSample(id: string, disposedBy: string): Observable<ApiResponse<Sample>> {
    const params = new HttpParams().set('disposedBy', disposedBy);
    return this.http.put<ApiResponse<Sample>>(`${this.baseUrl}/${id}/dispose`, null, { params });
  }

  // ── Tracking ────────────────────────────────────────────────────────────────

  getTracking(id: string): Observable<ApiResponse<SampleTrackingEntry[]>> {
    return this.http.get<ApiResponse<SampleTrackingEntry[]>>(`${this.baseUrl}/${id}/tracking`);
  }

  // ── Transfer ────────────────────────────────────────────────────────────────

  transferSample(id: string, request: SampleTransferRequest): Observable<ApiResponse<unknown>> {
    return this.http.post<ApiResponse<unknown>>(`${this.baseUrl}/${id}/transfer`, request);
  }

  // ── Worklists ───────────────────────────────────────────────────────────────

  getPendingReceipt(page = 0, size = 20): Observable<ApiResponse<PageResponse<Sample>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PageResponse<Sample>>>(`${this.baseUrl}/pending-receipt`, { params });
  }
}
