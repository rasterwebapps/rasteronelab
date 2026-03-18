import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationService } from '@core/services/notification.service';
import { Permission, Role } from '../../models/admin.models';

@Component({
  selector: 'app-role-form',
  standalone: true,
  imports: [
    RouterLink, ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatButtonModule, MatIconModule, MatCardModule, MatSlideToggleModule,
    MatCheckboxModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Role</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update role and permissions' : 'Create a new role with permissions' }}</p>
      </div>
      <a mat-button routerLink="/admin/roles">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Role Information</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Role Name</mat-label>
            <input matInput formControlName="roleName" />
            @if (form.controls.roleName.hasError('required')) {
              <mat-error>Role name is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline" class="md:col-span-2">
            <mat-label>Description</mat-label>
            <input matInput formControlName="description" />
          </mat-form-field>
          <div class="flex items-center">
            <mat-slide-toggle formControlName="isActive">Active</mat-slide-toggle>
          </div>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Permission Matrix</mat-card-title>
        </mat-card-header>
        <mat-card-content class="mt-4 overflow-x-auto">
          <table class="w-full">
            <thead>
              <tr>
                <th class="text-left p-2">Feature</th>
                @for (action of actions; track action) {
                  <th class="text-center p-2">{{ action }}</th>
                }
              </tr>
            </thead>
            <tbody>
              @for (feature of features; track feature) {
                <tr class="border-t">
                  <td class="p-2 font-medium">{{ feature }}</td>
                  @for (action of actions; track action) {
                    <td class="text-center p-2">
                      <mat-checkbox
                        [checked]="hasPermission(feature, action)"
                        (change)="togglePermission(feature, action)"
                      />
                    </td>
                  }
                </tr>
              }
            </tbody>
          </table>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/admin/roles">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Role
        </button>
      </div>
    </form>
  `,
})
export class RoleFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  private roleId = '';

  readonly features = ['Admin', 'Dashboard', 'Patient', 'Order', 'Sample', 'Result', 'Report', 'Billing', 'Inventory', 'QC'];
  readonly actions = ['View', 'Create', 'Edit', 'Delete', 'Approve'];
  readonly permissionMatrix = signal<Record<string, Set<string>>>({});

  readonly form = this.fb.nonNullable.group({
    roleName: ['', Validators.required],
    description: [''],
    isActive: [true],
  });

  ngOnInit(): void {
    this.initPermissionMatrix();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.roleId = id;
      this.loadRole(id);
    }
  }

  initPermissionMatrix(): void {
    const matrix: Record<string, Set<string>> = {};
    this.features.forEach(f => matrix[f] = new Set());
    this.permissionMatrix.set(matrix);
  }

  togglePermission(feature: string, action: string): void {
    this.permissionMatrix.update(m => {
      const updated = { ...m };
      const set = new Set(updated[feature]);
      if (set.has(action)) set.delete(action);
      else set.add(action);
      updated[feature] = set;
      return updated;
    });
  }

  hasPermission(feature: string, action: string): boolean {
    return this.permissionMatrix()[feature]?.has(action) ?? false;
  }

  private loadRole(id: string): void {
    this.api.get<Role>('config/roles', id).subscribe({
      next: (response) => {
        const r = response.data;
        this.form.patchValue({
          roleName: r.roleName,
          description: r.description ?? '',
          isActive: r.isActive,
        });
        // Populate permission matrix from existing permissions
        const matrix: Record<string, Set<string>> = {};
        this.features.forEach(f => matrix[f] = new Set());
        if (r.permissions) {
          r.permissions.forEach(p => {
            if (matrix[p.feature]) {
              matrix[p.feature] = new Set(p.actions);
            }
          });
        }
        this.permissionMatrix.set(matrix);
      },
    });
  }

  private buildPermissions(): Permission[] {
    const matrix = this.permissionMatrix();
    return this.features
      .filter(f => matrix[f]?.size > 0)
      .map(f => ({ feature: f, actions: Array.from(matrix[f]) }));
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = {
      ...this.form.getRawValue(),
      permissions: this.buildPermissions(),
    } as unknown as Partial<Role>;

    const request$ = this.isEditMode()
      ? this.api.update<Role>('config/roles', this.roleId, value)
      : this.api.create<Role>('config/roles', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Role ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/roles']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
