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
import { ReferenceRange, ParameterMaster } from '../../models/admin.models';

@Component({
  selector: 'app-reference-range-form',
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
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Reference Range</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update reference range' : 'Define a new reference range' }}</p>
      </div>
      <a mat-button routerLink="/admin/reference-ranges">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Parameter & Demographics</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Parameter</mat-label>
            <mat-select formControlName="parameterId">
              @for (param of parameterOptions(); track param.id) {
                <mat-option [value]="param.id">{{ param.paramName }} ({{ param.paramCode }})</mat-option>
              }
            </mat-select>
            @if (form.controls.parameterId.hasError('required')) {
              <mat-error>Parameter is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Gender</mat-label>
            <mat-select formControlName="gender">
              <mat-option value="ALL">All</mat-option>
              <mat-option value="MALE">Male</mat-option>
              <mat-option value="FEMALE">Female</mat-option>
            </mat-select>
            @if (form.controls.gender.hasError('required')) {
              <mat-error>Gender is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Trimester</mat-label>
            <mat-select formControlName="trimester">
              <mat-option [value]="null">None</mat-option>
              <mat-option value="T1">T1 - First</mat-option>
              <mat-option value="T2">T2 - Second</mat-option>
              <mat-option value="T3">T3 - Third</mat-option>
            </mat-select>
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Age Min (days)</mat-label>
            <input matInput formControlName="ageMinDays" type="number" />
            @if (form.controls.ageMinDays.hasError('required')) {
              <mat-error>Min age is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Age Max (days)</mat-label>
            <input matInput formControlName="ageMaxDays" type="number" />
            @if (form.controls.ageMaxDays.hasError('required')) {
              <mat-error>Max age is required</mat-error>
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
          <mat-card-title>Normal Range</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Normal Low</mat-label>
            <input matInput formControlName="normalLow" type="number" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Normal High</mat-label>
            <input matInput formControlName="normalHigh" type="number" />
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Critical & Panic Ranges</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Critical Low</mat-label>
            <input matInput formControlName="criticalLow" type="number" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Critical High</mat-label>
            <input matInput formControlName="criticalHigh" type="number" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Panic Low</mat-label>
            <input matInput formControlName="panicLow" type="number" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Panic High</mat-label>
            <input matInput formControlName="panicHigh" type="number" />
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Interpretation</mat-card-title>
        </mat-card-header>
        <mat-card-content class="mt-4">
          <mat-form-field appearance="outline" class="w-full">
            <mat-label>Interpretation Text</mat-label>
            <textarea matInput formControlName="interpretation" rows="3"></textarea>
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/admin/reference-ranges">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Reference Range
        </button>
      </div>
    </form>
  `,
})
export class ReferenceRangeFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  readonly parameterOptions = signal<ParameterMaster[]>([]);
  private rangeId = '';

  readonly form = this.fb.nonNullable.group({
    parameterId: ['', Validators.required],
    gender: ['ALL' as string, Validators.required],
    ageMinDays: [0, Validators.required],
    ageMaxDays: [36500, Validators.required], // ~100 years in days
    trimester: [null as string | null],
    normalLow: [null as number | null],
    normalHigh: [null as number | null],
    criticalLow: [null as number | null],
    criticalHigh: [null as number | null],
    panicLow: [null as number | null],
    panicHigh: [null as number | null],
    unitCode: [''],
    interpretation: [''],
  });

  ngOnInit(): void {
    this.loadParameterOptions();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.rangeId = id;
      this.loadRange(id);
    }
  }

  private loadParameterOptions(): void {
    this.api.list<ParameterMaster>('parameters', { page: 0, size: 500 }).subscribe({
      next: (response) => this.parameterOptions.set(response.data),
    });
  }

  private loadRange(id: string): void {
    this.api.get<ReferenceRange>('reference-ranges', id).subscribe({
      next: (response) => {
        const r = response.data;
        this.form.patchValue({
          parameterId: r.parameterId,
          gender: r.gender,
          ageMinDays: r.ageMinDays,
          ageMaxDays: r.ageMaxDays,
          trimester: r.trimester ?? null,
          normalLow: r.normalLow ?? null,
          normalHigh: r.normalHigh ?? null,
          criticalLow: r.criticalLow ?? null,
          criticalHigh: r.criticalHigh ?? null,
          panicLow: r.panicLow ?? null,
          panicHigh: r.panicHigh ?? null,
          unitCode: r.unitCode ?? '',
          interpretation: r.interpretation ?? '',
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<ReferenceRange>;

    const request$ = this.isEditMode()
      ? this.api.update<ReferenceRange>('reference-ranges', this.rangeId, value)
      : this.api.create<ReferenceRange>('reference-ranges', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Reference range ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/reference-ranges']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
