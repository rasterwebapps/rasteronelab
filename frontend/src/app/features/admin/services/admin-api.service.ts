import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { PageRequest, PageResponse } from '../models/admin.models';

@Injectable({ providedIn: 'root' })
export class AdminApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiUrl;

  list<T>(endpoint: string, params?: PageRequest & Record<string, string | number | boolean | undefined>): Observable<PageResponse<T>> {
    let httpParams = new HttpParams();
    if (params) {
      Object.entries(params).forEach(([key, value]) => {
        if (value !== undefined && value !== null && value !== '') {
          httpParams = httpParams.set(key, String(value));
        }
      });
    }
    return this.http.get<PageResponse<T>>(`${this.baseUrl}/${endpoint}`, { params: httpParams });
  }

  get<T>(endpoint: string, id: string): Observable<{ data: T }> {
    return this.http.get<{ data: T }>(`${this.baseUrl}/${endpoint}/${id}`);
  }

  create<T>(endpoint: string, body: Partial<T>): Observable<{ data: T }> {
    return this.http.post<{ data: T }>(`${this.baseUrl}/${endpoint}`, body);
  }

  update<T>(endpoint: string, id: string, body: Partial<T>): Observable<{ data: T }> {
    return this.http.put<{ data: T }>(`${this.baseUrl}/${endpoint}/${id}`, body);
  }

  remove(endpoint: string, id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${endpoint}/${id}`);
  }
}
