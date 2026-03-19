import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Patient, PatientRequest, PatientVisit } from '../models/patient.model';

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
export class PatientService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/patients`;

  getAll(page = 0, size = 20): Observable<PageResponse<Patient>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Patient>>(this.baseUrl, { params });
  }

  getById(id: string): Observable<DataResponse<Patient>> {
    return this.http.get<DataResponse<Patient>>(`${this.baseUrl}/${id}`);
  }

  search(query: string, page = 0, size = 20): Observable<PageResponse<Patient>> {
    const params = new HttpParams()
      .set('query', query)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Patient>>(`${this.baseUrl}/search`, { params });
  }

  create(request: PatientRequest): Observable<DataResponse<Patient>> {
    return this.http.post<DataResponse<Patient>>(this.baseUrl, request);
  }

  update(id: string, request: PatientRequest): Observable<DataResponse<Patient>> {
    return this.http.put<DataResponse<Patient>>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  findDuplicates(firstName: string, lastName: string, phone: string): Observable<PageResponse<Patient>> {
    const params = new HttpParams()
      .set('firstName', firstName)
      .set('lastName', lastName)
      .set('phone', phone);
    return this.http.get<PageResponse<Patient>>(`${this.baseUrl}/duplicates`, { params });
  }

  getVisits(patientId: string, page = 0, size = 20): Observable<PageResponse<PatientVisit>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<PatientVisit>>(`${this.baseUrl}/${patientId}/visits`, { params });
  }

  createVisit(patientId: string, request: Partial<PatientVisit>): Observable<DataResponse<PatientVisit>> {
    return this.http.post<DataResponse<PatientVisit>>(`${this.baseUrl}/${patientId}/visits`, request);
  }
}
