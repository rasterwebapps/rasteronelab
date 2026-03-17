import { Component, ChangeDetectionStrategy, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { environment } from '@environments/environment';
import { generateCodeVerifier, generateCodeChallenge } from '@core/utils/pkce';
import { AuthService } from '@core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [MatButtonModule, MatIconModule, MatProgressSpinnerModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex flex-col items-center gap-6">
      <p class="text-gray-600 text-center">
        Sign in to access lab operations, patient records, and test results.
      </p>

      <button
        mat-raised-button
        color="primary"
        class="w-full !py-3"
        (click)="signInWithKeycloak()"
      >
        <mat-icon>login</mat-icon>
        <span class="ml-2">Sign in with Keycloak</span>
      </button>

      <p class="text-xs text-gray-400 text-center">
        Secured by OAuth 2.0 with PKCE
      </p>
    </div>
  `,
})
export class LoginComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  constructor() {
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/dashboard']);
    }
  }

  async signInWithKeycloak(): Promise<void> {
    const codeVerifier = generateCodeVerifier();
    const codeChallenge = await generateCodeChallenge(codeVerifier);

    sessionStorage.setItem('pkce_code_verifier', codeVerifier);

    const redirectUri = `${window.location.origin}/auth/callback`;
    const authUrl =
      `${environment.keycloakUrl}/realms/${environment.keycloakRealm}/protocol/openid-connect/auth` +
      `?client_id=${encodeURIComponent(environment.keycloakClientId)}` +
      `&response_type=code` +
      `&scope=openid profile email` +
      `&redirect_uri=${encodeURIComponent(redirectUri)}` +
      `&code_challenge=${codeChallenge}` +
      `&code_challenge_method=S256`;

    window.location.href = authUrl;
  }
}
