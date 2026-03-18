import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationService } from '@core/services/notification.service';
import { Branch, User } from '../../models/admin.models';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [
    RouterLink, ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatButtonModule, MatIconModule, MatCardModule,
    MatSlideToggleModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} User</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update user details' : 'Create a new user account' }}</p>
      </div>
      <a mat-button routerLink="/admin/users">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Account Information</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Username (Email)</mat-label>
            <input matInput formControlName="username" type="email" />
            @if (form.controls.username.hasError('required')) {
              <mat-error>Username is required</mat-error>
            }
            @if (form.controls.username.hasError('email')) {
              <mat-error>Must be a valid email</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>First Name</mat-label>
            <input matInput formControlName="firstName" />
            @if (form.controls.firstName.hasError('required')) {
              <mat-error>First name is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Last Name</mat-label>
            <input matInput formControlName="lastName" />
            @if (form.controls.lastName.hasError('required')) {
              <mat-error>Last name is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Email</mat-label>
            <input matInput formControlName="email" type="email" />
            @if (form.controls.email.hasError('required')) {
              <mat-error>Email is required</mat-error>
            }
            @if (form.controls.email.hasError('email')) {
              <mat-error>Invalid email format</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Role</mat-label>
            <mat-select formControlName="role">
              @for (role of roles; track role) {
                <mat-option [value]="role">{{ formatRole(role) }}</mat-option>
              }
            </mat-select>
            @if (form.controls.role.hasError('required')) {
              <mat-error>Role is required</mat-error>
            }
          </mat-form-field>
          <div class="flex items-center">
            <mat-slide-toggle formControlName="isActive">Active</mat-slide-toggle>
          </div>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Employment Details</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Employee ID</mat-label>
            <input matInput formControlName="employeeId" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Department</mat-label>
            <input matInput formControlName="department" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Designation</mat-label>
            <input matInput formControlName="designation" />
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Branch Access</mat-card-title>
        </mat-card-header>
        <mat-card-content class="mt-4">
          <mat-form-field appearance="outline" class="w-full">
            <mat-label>Accessible Branches</mat-label>
            <mat-select formControlName="branchIds" multiple>
              @for (branch of branches(); track branch.id) {
                <mat-option [value]="branch.id">{{ branch.name }} ({{ branch.code }})</mat-option>
              }
            </mat-select>
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/admin/users">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} User
        </button>
      </div>
    </form>
  `,
})
export class UserFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  readonly branches = signal<Branch[]>([]);
  private userId = '';

  readonly roles = [
    'SUPER_ADMIN', 'ORG_ADMIN', 'BRANCH_ADMIN', 'PATHOLOGIST',
    'LAB_TECHNICIAN', 'RECEPTIONIST', 'PHLEBOTOMIST', 'BILLING_STAFF',
  ];

  readonly form = this.fb.nonNullable.group({
    username: ['', [Validators.required, Validators.email]],
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    employeeId: [''],
    department: [''],
    designation: [''],
    role: ['', Validators.required],
    branchIds: [[] as string[]],
    isActive: [true],
  });

  ngOnInit(): void {
    this.loadBranches();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.userId = id;
      this.loadUser(id);
    }
  }

  private loadBranches(): void {
    this.api.list<Branch>('admin/branches', { page: 0, size: 200 }).subscribe({
      next: (response) => this.branches.set(response.data),
    });
  }

  private loadUser(id: string): void {
    this.api.get<User>('users', id).subscribe({
      next: (response) => {
        const u = response.data;
        this.form.patchValue({
          username: u.username,
          firstName: u.firstName,
          lastName: u.lastName,
          email: u.email,
          employeeId: u.employeeId ?? '',
          department: u.department ?? '',
          designation: u.designation ?? '',
          role: u.role,
          branchIds: u.branchIds ?? [],
          isActive: u.isActive,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<User>;

    const request$ = this.isEditMode()
      ? this.api.update<User>('users', this.userId, value)
      : this.api.create<User>('users', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`User ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/users']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }

  formatRole(role: string): string {
    return role.replace(/_/g, ' ').replace(/\b\w/g, c => c.toUpperCase());
  }
}
