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
import { MatDividerModule } from '@angular/material/divider';
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationService } from '@core/services/notification.service';
import { Branch } from '../../models/admin.models';

@Component({
  selector: 'app-branch-form',
  standalone: true,
  imports: [
    RouterLink, ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatButtonModule, MatIconModule, MatCardModule,
    MatSlideToggleModule, MatDividerModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Branch</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update branch details' : 'Create a new branch' }}</p>
      </div>
      <a mat-button routerLink="/admin/branches">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Basic Information</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Branch Code</mat-label>
            <input matInput formControlName="code" placeholder="e.g. BR-001" />
            @if (form.controls.code.hasError('required')) {
              <mat-error>Code is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Branch Name</mat-label>
            <input matInput formControlName="name" />
            @if (form.controls.name.hasError('required')) {
              <mat-error>Name is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Branch Type</mat-label>
            <mat-select formControlName="type">
              <mat-option value="MAIN_LAB">Main Lab</mat-option>
              <mat-option value="SATELLITE">Satellite</mat-option>
              <mat-option value="COLLECTION_CENTER">Collection Center</mat-option>
              <mat-option value="FRANCHISE">Franchise</mat-option>
            </mat-select>
            @if (form.controls.type.hasError('required')) {
              <mat-error>Type is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Phone</mat-label>
            <input matInput formControlName="phone" />
            @if (form.controls.phone.hasError('required')) {
              <mat-error>Phone is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Email</mat-label>
            <input matInput formControlName="email" type="email" />
            @if (form.controls.email.hasError('email')) {
              <mat-error>Invalid email format</mat-error>
            }
          </mat-form-field>
          <div class="flex items-center">
            <mat-slide-toggle formControlName="isActive">Active</mat-slide-toggle>
          </div>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Address</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          <mat-form-field appearance="outline" class="md:col-span-2">
            <mat-label>Address Line 1</mat-label>
            <input matInput formControlName="addressLine1" />
            @if (form.controls.addressLine1.hasError('required')) {
              <mat-error>Address is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Address Line 2</mat-label>
            <input matInput formControlName="addressLine2" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>City</mat-label>
            <input matInput formControlName="city" />
            @if (form.controls.city.hasError('required')) {
              <mat-error>City is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>State</mat-label>
            <input matInput formControlName="state" />
            @if (form.controls.state.hasError('required')) {
              <mat-error>State is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Country</mat-label>
            <input matInput formControlName="country" />
            @if (form.controls.country.hasError('required')) {
              <mat-error>Country is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Postal Code</mat-label>
            <input matInput formControlName="postalCode" />
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Additional Details</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>License Number</mat-label>
            <input matInput formControlName="licenseNumber" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>NABL Number</mat-label>
            <input matInput formControlName="nablNumber" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Latitude</mat-label>
            <input matInput formControlName="latitude" type="number" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Longitude</mat-label>
            <input matInput formControlName="longitude" type="number" />
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/admin/branches">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Branch
        </button>
      </div>
    </form>
  `,
})
export class BranchFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  private branchId = '';

  readonly form = this.fb.nonNullable.group({
    code: ['', Validators.required],
    name: ['', Validators.required],
    type: ['MAIN_LAB' as string, Validators.required],
    addressLine1: ['', Validators.required],
    addressLine2: [''],
    city: ['', Validators.required],
    state: ['', Validators.required],
    country: ['India', Validators.required],
    postalCode: [''],
    phone: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    licenseNumber: [''],
    nablNumber: [''],
    latitude: [null as number | null],
    longitude: [null as number | null],
    isActive: [true],
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.branchId = id;
      this.loadBranch(id);
    }
  }

  private loadBranch(id: string): void {
    this.api.get<Branch>('admin/branches', id).subscribe({
      next: (response) => {
        const b = response.data;
        this.form.patchValue({
          code: b.code,
          name: b.name,
          type: b.type,
          addressLine1: b.addressLine1,
          addressLine2: b.addressLine2 ?? '',
          city: b.city,
          state: b.state,
          country: b.country,
          postalCode: b.postalCode,
          phone: b.phone,
          email: b.email,
          licenseNumber: b.licenseNumber ?? '',
          nablNumber: b.nablNumber ?? '',
          latitude: b.latitude ?? null,
          longitude: b.longitude ?? null,
          isActive: b.isActive,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<Branch>;

    const request$ = this.isEditMode()
      ? this.api.update<Branch>('admin/branches', this.branchId, value)
      : this.api.create<Branch>('admin/branches', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Branch ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/branches']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
