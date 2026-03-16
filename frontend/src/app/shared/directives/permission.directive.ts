import { Directive, Input, TemplateRef, ViewContainerRef, OnInit, inject } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';

/**
 * Structural directive to conditionally show/hide elements based on user roles.
 *
 * Usage:
 *   <button *appPermission="['ADMIN', 'PATHOLOGIST']">Authorize Result</button>
 *   <div *appPermission="'SUPER_ADMIN'">Admin Panel</div>
 */
@Directive({
  selector: '[appPermission]',
  standalone: true,
})
export class PermissionDirective implements OnInit {
  @Input('appPermission') roles: string | string[] = [];

  private readonly authService = inject(AuthService);
  private readonly templateRef = inject(TemplateRef<unknown>);
  private readonly viewContainer = inject(ViewContainerRef);

  ngOnInit(): void {
    const requiredRoles = Array.isArray(this.roles) ? this.roles : [this.roles];
    const hasPermission = requiredRoles.some(role => this.authService.hasRole(role));

    if (hasPermission) {
      this.viewContainer.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainer.clear();
    }
  }
}
