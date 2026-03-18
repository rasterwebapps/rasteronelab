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
import { ParameterMaster, TestMaster } from '../../models/admin.models';

@Component({
  selector: 'app-parameter-form',
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
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Parameter</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update parameter details' : 'Create a new parameter' }}</p>
      </div>
      <a mat-button routerLink="/admin/parameters">
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
            <mat-label>Parameter Code</mat-label>
            <input matInput formControlName="paramCode" placeholder="e.g. PRM-001" />
            @if (form.controls.paramCode.hasError('required')) {
              <mat-error>Parameter code is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Parameter Name</mat-label>
            <input matInput formControlName="paramName" />
            @if (form.controls.paramName.hasError('required')) {
              <mat-error>Parameter name is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Short Name</mat-label>
            <input matInput formControlName="shortName" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Test</mat-label>
            <mat-select formControlName="testId">
              @for (test of tests(); track test.id) {
                <mat-option [value]="test.id">{{ test.testName }}</mat-option>
              }
            </mat-select>
            @if (form.controls.testId.hasError('required')) {
              <mat-error>Test is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Data Type</mat-label>
            <mat-select formControlName="dataType">
              <mat-option value="NUMERIC">Numeric</mat-option>
              <mat-option value="TEXT">Text</mat-option>
              <mat-option value="OPTION">Option</mat-option>
              <mat-option value="FORMULA">Formula</mat-option>
              <mat-option value="SEMI_QUANTITATIVE">Semi-Quantitative</mat-option>
            </mat-select>
            @if (form.controls.dataType.hasError('required')) {
              <mat-error>Data type is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Unit Code</mat-label>
            <input matInput formControlName="unitCode" />
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Configuration</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>LOINC Code</mat-label>
            <input matInput formControlName="loincCode" />
          </mat-form-field>
          @if (form.controls.dataType.value === 'NUMERIC') {
            <mat-form-field appearance="outline">
              <mat-label>Decimal Places</mat-label>
              <input matInput formControlName="decimalPlaces" type="number" />
            </mat-form-field>
          }
          @if (form.controls.dataType.value === 'FORMULA') {
            <mat-form-field appearance="outline" class="md:col-span-2">
              <mat-label>Formula</mat-label>
              <input matInput formControlName="formula" placeholder="e.g. (P1 / P2) * 100" />
            </mat-form-field>
          }
          <mat-form-field appearance="outline">
            <mat-label>Display Order</mat-label>
            <input matInput formControlName="displayOrder" type="number" />
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Flags</mat-card-title>
        </mat-card-header>
        <mat-card-content class="flex flex-wrap gap-6 mt-4">
          <mat-slide-toggle formControlName="isCritical">Critical</mat-slide-toggle>
          <mat-slide-toggle formControlName="isReportable">Reportable</mat-slide-toggle>
          <mat-slide-toggle formControlName="isDerived">Derived</mat-slide-toggle>
          <mat-slide-toggle formControlName="isActive">Active</mat-slide-toggle>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/admin/parameters">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Parameter
        </button>
      </div>
    </form>
  `,
})
export class ParameterFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  readonly tests = signal<TestMaster[]>([]);
  private parameterId = '';

  readonly form = this.fb.nonNullable.group({
    paramCode: ['', Validators.required],
    paramName: ['', Validators.required],
    shortName: [''],
    testId: ['', Validators.required],
    dataType: ['NUMERIC' as string, Validators.required],
    unitCode: [''],
    loincCode: [''],
    decimalPlaces: [null as number | null],
    formula: [''],
    displayOrder: [0],
    isCritical: [false],
    isReportable: [true],
    isDerived: [false],
    isActive: [true],
  });

  ngOnInit(): void {
    this.loadTests();
    this.form.controls.dataType.valueChanges.subscribe(type => {
      const { decimalPlaces, formula } = this.form.controls;
      if (type === 'NUMERIC') {
        decimalPlaces.setValidators(Validators.required);
      } else {
        decimalPlaces.clearValidators();
      }
      if (type === 'FORMULA') {
        formula.setValidators(Validators.required);
      } else {
        formula.clearValidators();
      }
      decimalPlaces.updateValueAndValidity();
      formula.updateValueAndValidity();
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.parameterId = id;
      this.loadParameter(id);
    }
  }

  private loadTests(): void {
    this.api.list<TestMaster>('tests', { page: 0, size: 500 }).subscribe({
      next: (response) => this.tests.set(response.data),
    });
  }

  private loadParameter(id: string): void {
    this.api.get<ParameterMaster>('parameters', id).subscribe({
      next: (response) => {
        const p = response.data;
        this.form.patchValue({
          paramCode: p.paramCode,
          paramName: p.paramName,
          shortName: p.shortName ?? '',
          testId: p.testId,
          dataType: p.dataType,
          unitCode: p.unitCode ?? '',
          loincCode: p.loincCode ?? '',
          decimalPlaces: p.decimalPlaces ?? null,
          formula: p.formula ?? '',
          displayOrder: p.displayOrder,
          isCritical: p.isCritical,
          isReportable: p.isReportable,
          isDerived: p.isDerived,
          isActive: p.isActive,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<ParameterMaster>;

    const request$ = this.isEditMode()
      ? this.api.update<ParameterMaster>('parameters', this.parameterId, value)
      : this.api.create<ParameterMaster>('parameters', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Parameter ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/parameters']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
