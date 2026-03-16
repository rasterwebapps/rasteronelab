import { Injectable, signal, computed, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '@environments/environment';

export interface UserProfile {
  id: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  roles: string[];
  branchIds: string[];
  organizationId: string;
}

/**
 * Authentication service using Keycloak OAuth2/OIDC.
 * Manages tokens, user profile, and role checks.
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);

  private readonly _user = signal<UserProfile | null>(null);
  private readonly _token = signal<string | null>(null);

  readonly user = this._user.asReadonly();
  readonly isAuthenticated = computed(() => this._token() !== null && this._user() !== null);
  readonly fullName = computed(() => {
    const u = this._user();
    return u ? `${u.firstName} ${u.lastName}` : '';
  });

  constructor() {
    this.loadFromStorage();
  }

  private loadFromStorage(): void {
    const token = sessionStorage.getItem('access_token');
    const userJson = sessionStorage.getItem('user_profile');
    if (token && userJson) {
      this._token.set(token);
      this._user.set(JSON.parse(userJson));
    }
  }

  getAccessToken(): string | null {
    return this._token();
  }

  hasRole(role: string): boolean {
    return this._user()?.roles.includes(role) ?? false;
  }

  hasAnyRole(...roles: string[]): boolean {
    return roles.some(r => this.hasRole(r));
  }

  setAuth(token: string, user: UserProfile): void {
    this._token.set(token);
    this._user.set(user);
    sessionStorage.setItem('access_token', token);
    sessionStorage.setItem('user_profile', JSON.stringify(user));
  }

  logout(): void {
    this._token.set(null);
    this._user.set(null);
    sessionStorage.clear();
    window.location.href =
      `${environment.keycloakUrl}/realms/${environment.keycloakRealm}/protocol/openid-connect/logout` +
      `?redirect_uri=${encodeURIComponent(window.location.origin)}`;
  }
}
