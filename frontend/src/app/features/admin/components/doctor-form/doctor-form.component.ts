import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationService } from '@core/services/notification.service';
import { Doctor } from '../../models/admin.models';

@Component({
  selector: 'app-doctor-form',
  standalone: true,
  imports: [
    RouterLink, ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatButtonModule, MatIconModule, MatCardModule, MatSlideToggleModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Doctor</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update doctor details' : 'Register a new doctor' }}</p>
      </div>
      <a mat-button routerLink="/admin/doctors">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Doctor Information</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Doctor Name</mat-label>
            <input matInput formControlName="name" />
            @if (form.controls.name.hasError('required')) {
              <mat-error>Name is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Specialization</mat-label>
            <input matInput formControlName="specialization" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Phone</mat-label>
            <input matInput formControlName="phone" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Email</mat-label>
            <input matInput formControlName="email" type="email" />
            @if (form.controls.email.hasError('email')) {
              <mat-error>Invalid email format</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Registration Number</mat-label>
            <input matInput formControlName="registrationNumber" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Referral Commission (%)</mat-label>
            <input matInput formControlName="referralCommission" type="number" />
          </mat-form-field>
          <div class="flex items-center">
            <mat-slide-toggle formControlName="isActive">Active</mat-slide-toggle>
          </div>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/admin/doctors">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Doctor
        </button>
      </div>
    </form>
  `,
})
export class DoctorFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  private doctorId = '';

  readonly form = this.fb.nonNullable.group({
    name: ['', Validators.required],
    specialization: [''],
    phone: [''],
    email: ['', Validators.email],
    registrationNumber: [''],
    referralCommission: [null as number | null],
    isActive: [true],
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.doctorId = id;
      this.loadDoctor(id);
    }
  }

  private loadDoctor(id: string): void {
    this.api.get<Doctor>('doctors', id).subscribe({
      next: (response) => {
        const d = response.data;
        this.form.patchValue({
          name: d.name,
          specialization: d.specialization ?? '',
          phone: d.phone ?? '',
          email: d.email ?? '',
          registrationNumber: d.registrationNumber ?? '',
          referralCommission: d.referralCommission ?? null,
          isActive: d.isActive,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<Doctor>;

    const request$ = this.isEditMode()
      ? this.api.update<Doctor>('doctors', this.doctorId, value)
      : this.api.create<Doctor>('doctors', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Doctor ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/doctors']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
