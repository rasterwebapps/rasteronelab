import { inject } from '@angular/core';
import { CanActivateFn, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Protects routes based on user roles.
 * Route must have `data.roles` array defined.
 *
 * Usage in routes:
 * { path: 'admin', canActivate: [roleGuard], data: { roles: ['ADMIN', 'SUPER_ADMIN'] } }
 */
export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const requiredRoles: string[] = route.data?.['roles'] ?? [];

  if (requiredRoles.length === 0) return true;

  const hasRole = requiredRoles.some(role => authService.hasRole(role));
  if (hasRole) return true;

  router.navigate(['/dashboard']);
  return false;
};
