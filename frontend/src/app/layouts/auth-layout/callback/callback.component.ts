import { Component, ChangeDetectionStrategy, OnInit, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { environment } from '@environments/environment';
import { AuthService, UserProfile } from '@core/services/auth.service';

interface TokenResponse {
  access_token: string;
  refresh_token: string;
  id_token: string;
  token_type: string;
  expires_in: number;
}

interface JwtPayload {
  sub: string;
  preferred_username: string;
  email: string;
  given_name: string;
  family_name: string;
  realm_access?: { roles: string[] };
  organizationId?: string;
  branchIds?: string[];
  employeeId?: string;
}

@Component({
  selector: 'app-callback',
  standalone: true,
  imports: [MatProgressSpinnerModule, MatIconModule, MatButtonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (error()) {
      <div class="flex flex-col items-center gap-4 text-center">
        <mat-icon class="!text-4xl text-red-500">error_outline</mat-icon>
        <p class="text-red-600 font-medium">Authentication Failed</p>
        <p class="text-sm text-gray-500">{{ error() }}</p>
        <button mat-stroked-button color="primary" (click)="retryLogin()">
          <mat-icon>arrow_back</mat-icon>
          <span class="ml-1">Back to Login</span>
        </button>
      </div>
    } @else {
      <div class="flex flex-col items-center gap-4">
        <mat-spinner diameter="48"></mat-spinner>
        <p class="text-gray-600">Completing sign in...</p>
      </div>
    }
  `,
})
export class CallbackComponent implements OnInit {
  private readonly router = inject(Router);
  private readonly http = inject(HttpClient);
  private readonly authService = inject(AuthService);

  readonly error = signal<string | null>(null);

  ngOnInit(): void {
    this.handleCallback();
  }

  retryLogin(): void {
    this.router.navigate(['/auth/login']);
  }

  private handleCallback(): void {
    const params = new URLSearchParams(window.location.search);
    const code = params.get('code');
    const errorParam = params.get('error');

    if (errorParam) {
      this.error.set(params.get('error_description') ?? 'Authorization was denied.');
      return;
    }

    if (!code) {
      this.error.set('No authorization code received.');
      return;
    }

    const codeVerifier = sessionStorage.getItem('pkce_code_verifier');
    if (!codeVerifier) {
      this.error.set('PKCE verification failed. Please try signing in again.');
      return;
    }

    sessionStorage.removeItem('pkce_code_verifier');
    this.exchangeCodeForTokens(code, codeVerifier);
  }

  private exchangeCodeForTokens(code: string, codeVerifier: string): void {
    const tokenUrl = `${environment.keycloakUrl}/realms/${environment.keycloakRealm}/protocol/openid-connect/token`;
    const redirectUri = `${window.location.origin}/auth/callback`;

    const body = new HttpParams()
      .set('grant_type', 'authorization_code')
      .set('client_id', environment.keycloakClientId)
      .set('code', code)
      .set('redirect_uri', redirectUri)
      .set('code_verifier', codeVerifier);

    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');

    this.http.post<TokenResponse>(tokenUrl, body.toString(), { headers }).subscribe({
      next: (response) => this.handleTokenResponse(response),
      error: () => this.error.set('Failed to exchange authorization code for tokens.'),
    });
  }

  private handleTokenResponse(response: TokenResponse): void {
    const payload = this.parseJwt(response.access_token);
    if (!payload) {
      this.error.set('Failed to parse authentication token.');
      return;
    }

    const user: UserProfile = {
      id: payload.sub,
      username: payload.preferred_username,
      email: payload.email,
      firstName: payload.given_name ?? '',
      lastName: payload.family_name ?? '',
      roles: payload.realm_access?.roles ?? [],
      branchIds: payload.branchIds ?? [],
      organizationId: payload.organizationId ?? '',
    };

    this.authService.setAuth(response.access_token, user);

    if (response.refresh_token) {
      sessionStorage.setItem('refresh_token', response.refresh_token);
    }

    this.router.navigate(['/dashboard']);
  }

  private parseJwt(token: string): JwtPayload | null {
    try {
      const parts = token.split('.');
      if (parts.length !== 3) return null;
      const payload = parts[1];
      const decoded = atob(payload.replace(/-/g, '+').replace(/_/g, '/'));
      return JSON.parse(decoded) as JwtPayload;
    } catch {
      return null;
    }
  }
}
