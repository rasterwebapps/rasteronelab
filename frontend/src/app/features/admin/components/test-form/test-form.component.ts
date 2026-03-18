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
import { TestMaster, Department } from '../../models/admin.models';

@Component({
  selector: 'app-test-form',
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
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Test</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update test details' : 'Create a new test' }}</p>
      </div>
      <a mat-button routerLink="/admin/tests">
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
            <mat-label>Test Code</mat-label>
            <input matInput formControlName="testCode" placeholder="e.g. TST-001" />
            @if (form.controls.testCode.hasError('required')) {
              <mat-error>Test code is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Test Name</mat-label>
            <input matInput formControlName="testName" />
            @if (form.controls.testName.hasError('required')) {
              <mat-error>Test name is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Short Name</mat-label>
            <input matInput formControlName="shortName" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Department</mat-label>
            <mat-select formControlName="departmentId">
              @for (dept of departments(); track dept.id) {
                <mat-option [value]="dept.id">{{ dept.departmentName }}</mat-option>
              }
            </mat-select>
            @if (form.controls.departmentId.hasError('required')) {
              <mat-error>Department is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Test Type</mat-label>
            <mat-select formControlName="testType">
              <mat-option value="SINGLE">Single</mat-option>
              <mat-option value="PANEL">Panel</mat-option>
              <mat-option value="PROFILE">Profile</mat-option>
              <mat-option value="CALCULATED">Calculated</mat-option>
            </mat-select>
            @if (form.controls.testType.hasError('required')) {
              <mat-error>Test type is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Method</mat-label>
            <input matInput formControlName="method" />
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Sample Information</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Sample Type ID</mat-label>
            <input matInput formControlName="sampleTypeId" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Tube Type ID</mat-label>
            <input matInput formControlName="tubeTypeId" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Sample Volume (ml)</mat-label>
            <input matInput formControlName="sampleVolumeMl" type="number" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>TAT (hours)</mat-label>
            <input matInput formControlName="tatHours" type="number" />
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Outsourcing & Coding</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          <div class="flex items-center">
            <mat-slide-toggle formControlName="isOutsourced">Outsourced</mat-slide-toggle>
          </div>
          <mat-form-field appearance="outline">
            <mat-label>Partner Lab ID</mat-label>
            <input matInput formControlName="partnerLabId" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>LOINC Code</mat-label>
            <input matInput formControlName="loincCode" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>CPT Code</mat-label>
            <input matInput formControlName="cptCode" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Display Order</mat-label>
            <input matInput formControlName="displayOrder" type="number" />
          </mat-form-field>
          <div class="flex items-center">
            <mat-slide-toggle formControlName="isActive">Active</mat-slide-toggle>
          </div>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Patient Instructions</mat-card-title>
        </mat-card-header>
        <mat-card-content class="mt-4">
          <mat-form-field appearance="outline" class="w-full">
            <mat-label>Instructions</mat-label>
            <textarea matInput formControlName="patientInstructions" rows="3"></textarea>
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/admin/tests">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Test
        </button>
      </div>
    </form>
  `,
})
export class TestFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  readonly departments = signal<Department[]>([]);
  private testId = '';

  readonly form = this.fb.nonNullable.group({
    testCode: ['', Validators.required],
    testName: ['', Validators.required],
    shortName: [''],
    departmentId: ['', Validators.required],
    sampleTypeId: [''],
    tubeTypeId: [''],
    sampleVolumeMl: [null as number | null],
    testType: ['SINGLE' as string, Validators.required],
    method: [''],
    tatHours: [null as number | null],
    isOutsourced: [false],
    partnerLabId: [''],
    loincCode: [''],
    cptCode: [''],
    displayOrder: [0],
    patientInstructions: [''],
    isActive: [true],
  });

  ngOnInit(): void {
    this.loadDepartments();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.testId = id;
      this.loadTest(id);
    }
  }

  private loadDepartments(): void {
    this.api.list<Department>('admin/departments', { page: 0, size: 200 }).subscribe({
      next: (response) => this.departments.set(response.data),
    });
  }

  private loadTest(id: string): void {
    this.api.get<TestMaster>('tests', id).subscribe({
      next: (response) => {
        const t = response.data;
        this.form.patchValue({
          testCode: t.testCode,
          testName: t.testName,
          shortName: t.shortName ?? '',
          departmentId: t.departmentId,
          sampleTypeId: t.sampleTypeId ?? '',
          tubeTypeId: t.tubeTypeId ?? '',
          sampleVolumeMl: t.sampleVolumeMl ?? null,
          testType: t.testType,
          method: t.method ?? '',
          tatHours: t.tatHours ?? null,
          isOutsourced: t.isOutsourced,
          partnerLabId: t.partnerLabId ?? '',
          loincCode: t.loincCode ?? '',
          cptCode: t.cptCode ?? '',
          displayOrder: t.displayOrder,
          patientInstructions: t.patientInstructions ?? '',
          isActive: t.isActive,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<TestMaster>;

    const request$ = this.isEditMode()
      ? this.api.update<TestMaster>('tests', this.testId, value)
      : this.api.create<TestMaster>('tests', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Test ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/tests']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
