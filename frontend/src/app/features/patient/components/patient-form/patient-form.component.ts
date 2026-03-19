import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDividerModule } from '@angular/material/divider';
import { PatientService } from '../../services/patient.service';
import { NotificationService } from '@core/services/notification.service';
import { Patient, PatientRequest } from '../../models/patient.model';

@Component({
  selector: 'app-patient-form',
  standalone: true,
  imports: [
    RouterLink, ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatButtonModule, MatIconModule, MatCardModule,
    MatDatepickerModule, MatNativeDateModule, MatDividerModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'Register' }} Patient</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update patient details' : 'Register a new patient' }}</p>
      </div>
      <a mat-button routerLink="/patient">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Personal Information</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
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
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Gender</mat-label>
            <mat-select formControlName="gender">
              <mat-option value="MALE">Male</mat-option>
              <mat-option value="FEMALE">Female</mat-option>
              <mat-option value="OTHER">Other</mat-option>
              <mat-option value="UNKNOWN">Unknown</mat-option>
            </mat-select>
            @if (form.controls.gender.hasError('required')) {
              <mat-error>Gender is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Date of Birth</mat-label>
            <input matInput [matDatepicker]="dobPicker" formControlName="dateOfBirth"
              (dateChange)="onDobChange()" />
            <mat-datepicker-toggle matIconSuffix [for]="dobPicker" />
            <mat-datepicker #dobPicker />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Age (Years)</mat-label>
            <input matInput formControlName="ageYears" type="number" min="0"
              (input)="onAgeChange()" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Age (Months)</mat-label>
            <input matInput formControlName="ageMonths" type="number" min="0" max="11" />
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
          <mat-form-field appearance="outline">
            <mat-label>Blood Group</mat-label>
            <mat-select formControlName="bloodGroup">
              <mat-option value="">None</mat-option>
              <mat-option value="A+">A+</mat-option>
              <mat-option value="A-">A-</mat-option>
              <mat-option value="B+">B+</mat-option>
              <mat-option value="B-">B-</mat-option>
              <mat-option value="AB+">AB+</mat-option>
              <mat-option value="AB-">AB-</mat-option>
              <mat-option value="O+">O+</mat-option>
              <mat-option value="O-">O-</mat-option>
            </mat-select>
          </mat-form-field>
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
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Address Line 2</mat-label>
            <input matInput formControlName="addressLine2" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>City</mat-label>
            <input matInput formControlName="city" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>State</mat-label>
            <input matInput formControlName="state" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Postal Code</mat-label>
            <input matInput formControlName="postalCode" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Country</mat-label>
            <input matInput formControlName="country" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Nationality</mat-label>
            <input matInput formControlName="nationality" />
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Emergency Contact & Identification</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Emergency Contact Name</mat-label>
            <input matInput formControlName="emergencyContactName" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Emergency Contact Phone</mat-label>
            <input matInput formControlName="emergencyContactPhone" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>ID Type</mat-label>
            <mat-select formControlName="idType">
              <mat-option value="">None</mat-option>
              <mat-option value="AADHAAR">Aadhaar</mat-option>
              <mat-option value="PAN">PAN</mat-option>
              <mat-option value="PASSPORT">Passport</mat-option>
              <mat-option value="DRIVING_LICENSE">Driving License</mat-option>
              <mat-option value="VOTER_ID">Voter ID</mat-option>
            </mat-select>
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>ID Number</mat-label>
            <input matInput formControlName="idNumber" />
          </mat-form-field>
          <mat-form-field appearance="outline" class="md:col-span-2">
            <mat-label>Notes</mat-label>
            <textarea matInput formControlName="notes" rows="3"></textarea>
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/patient">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Register' }} Patient
        </button>
      </div>
    </form>
  `,
})
export class PatientFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly patientService = inject(PatientService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  private patientId = '';

  readonly form = this.fb.nonNullable.group({
    firstName: ['', Validators.required],
    lastName: [''],
    dateOfBirth: [null as Date | null],
    ageYears: [null as number | null],
    ageMonths: [null as number | null],
    ageDays: [null as number | null],
    gender: ['MALE' as string, Validators.required],
    phone: ['', Validators.required],
    email: ['', Validators.email],
    bloodGroup: [''],
    addressLine1: [''],
    addressLine2: [''],
    city: [''],
    state: [''],
    postalCode: [''],
    country: ['India'],
    emergencyContactName: [''],
    emergencyContactPhone: [''],
    nationality: ['Indian'],
    idType: [''],
    idNumber: [''],
    notes: [''],
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.patientId = id;
      this.loadPatient(id);
    }
  }

  private loadPatient(id: string): void {
    this.patientService.getById(id).subscribe({
      next: (response) => {
        const p: Patient = response.data;
        this.form.patchValue({
          firstName: p.firstName,
          lastName: p.lastName ?? '',
          dateOfBirth: p.dateOfBirth ? new Date(p.dateOfBirth) : null,
          ageYears: p.ageYears ?? null,
          ageMonths: p.ageMonths ?? null,
          ageDays: p.ageDays ?? null,
          gender: p.gender,
          phone: p.phone,
          email: p.email ?? '',
          bloodGroup: p.bloodGroup ?? '',
          addressLine1: p.addressLine1 ?? '',
          addressLine2: p.addressLine2 ?? '',
          city: p.city ?? '',
          state: p.state ?? '',
          postalCode: p.postalCode ?? '',
          country: p.country ?? 'India',
          emergencyContactName: p.emergencyContactName ?? '',
          emergencyContactPhone: p.emergencyContactPhone ?? '',
          nationality: p.nationality ?? 'Indian',
          idType: p.idType ?? '',
          idNumber: p.idNumber ?? '',
          notes: p.notes ?? '',
        });
      },
    });
  }

  onDobChange(): void {
    const dob = this.form.controls.dateOfBirth.value;
    if (!dob) return;
    const today = new Date();
    let years = today.getFullYear() - dob.getFullYear();
    let months = today.getMonth() - dob.getMonth();
    let days = today.getDate() - dob.getDate();
    if (days < 0) {
      months--;
      days += new Date(today.getFullYear(), today.getMonth(), 0).getDate();
    }
    if (months < 0) {
      years--;
      months += 12;
    }
    this.form.patchValue({ ageYears: years, ageMonths: months, ageDays: days });
  }

  onAgeChange(): void {
    const years = this.form.controls.ageYears.value;
    if (years == null) return;
    const dob = new Date();
    dob.setFullYear(dob.getFullYear() - years);
    this.form.patchValue({ dateOfBirth: dob });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const raw = this.form.getRawValue();
    const request: PatientRequest = {
      ...raw,
      dateOfBirth: raw.dateOfBirth ? raw.dateOfBirth.toISOString().split('T')[0] : undefined,
      ageYears: raw.ageYears ?? undefined,
      ageMonths: raw.ageMonths ?? undefined,
      ageDays: raw.ageDays ?? undefined,
    };

    const request$ = this.isEditMode()
      ? this.patientService.update(this.patientId, request)
      : this.patientService.create(request);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Patient ${this.isEditMode() ? 'updated' : 'registered'} successfully`);
        this.router.navigate(['/patient']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
