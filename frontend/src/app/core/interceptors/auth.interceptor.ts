import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { environment } from '@environments/environment';

/**
 * Adds the Authorization Bearer token to all outgoing HTTP requests.
 * Handles 401 responses by redirecting to login.
 * Skips Keycloak token/auth endpoints to avoid sending stale tokens.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Do not attach Authorization header to Keycloak's own endpoints
  if (req.url.startsWith(environment.keycloakUrl)) {
    return next(req);
  }

  const token = authService.getAccessToken();

  const authReq = token
    ? req.clone({ headers: req.headers.set('Authorization', `Bearer ${token}`) })
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        authService.logout();
        router.navigate(['/auth/login']);
      }
      return throwError(() => error);
    })
  );
};
