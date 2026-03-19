import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {
  TestResult,
  TestResultCreateRequest,
  ResultEntryRequest,
  ResultAmendRequest,
} from '../models/result.model';

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
export class ResultService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/results`;

  getAll(page = 0, size = 20): Observable<PageResponse<TestResult>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<TestResult>>(this.baseUrl, { params });
  }

  getById(id: string): Observable<DataResponse<TestResult>> {
    return this.http.get<DataResponse<TestResult>>(`${this.baseUrl}/${id}`);
  }

  getByOrder(orderId: string, page = 0, size = 20): Observable<PageResponse<TestResult>> {
    const params = new HttpParams()
      .set('orderId', orderId)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<TestResult>>(`${this.baseUrl}/by-order`, { params });
  }

  getByPatient(patientId: string, page = 0, size = 20): Observable<PageResponse<TestResult>> {
    const params = new HttpParams()
      .set('patientId', patientId)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<TestResult>>(`${this.baseUrl}/by-patient`, { params });
  }

  getWorklist(departmentId: string, page = 0, size = 20): Observable<PageResponse<TestResult>> {
    const params = new HttpParams()
      .set('departmentId', departmentId)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<TestResult>>(`${this.baseUrl}/worklist`, { params });
  }

  create(request: TestResultCreateRequest): Observable<DataResponse<TestResult>> {
    return this.http.post<DataResponse<TestResult>>(this.baseUrl, request);
  }

  enterResults(request: ResultEntryRequest): Observable<DataResponse<TestResult>> {
    return this.http.post<DataResponse<TestResult>>(`${this.baseUrl}/enter`, request);
  }

  validateResult(id: string): Observable<DataResponse<TestResult>> {
    return this.http.post<DataResponse<TestResult>>(`${this.baseUrl}/${id}/validate`, {});
  }

  authorizeResult(id: string): Observable<DataResponse<TestResult>> {
    return this.http.post<DataResponse<TestResult>>(`${this.baseUrl}/${id}/authorize`, {});
  }

  batchAuthorize(resultIds: string[]): Observable<DataResponse<TestResult[]>> {
    return this.http.post<DataResponse<TestResult[]>>(`${this.baseUrl}/batch-authorize`, { resultIds });
  }

  amendResult(request: ResultAmendRequest): Observable<DataResponse<TestResult>> {
    return this.http.post<DataResponse<TestResult>>(`${this.baseUrl}/amend`, request);
  }
}
