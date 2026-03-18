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
import { DeltaCheck, ParameterMaster } from '../../models/admin.models';

@Component({
  selector: 'app-delta-check-form',
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
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Delta Check</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update delta check rule' : 'Define a new delta check rule' }}</p>
      </div>
      <a mat-button routerLink="/admin/delta-checks">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Delta Check Configuration</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
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
            <mat-label>Percentage Threshold (%)</mat-label>
            <input matInput formControlName="percentageThreshold" type="number" />
            @if (form.controls.percentageThreshold.hasError('required')) {
              <mat-error>Threshold is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Time Window (days)</mat-label>
            <input matInput formControlName="timeWindowDays" type="number" />
            @if (form.controls.timeWindowDays.hasError('required')) {
              <mat-error>Time window is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Flag Action</mat-label>
            <mat-select formControlName="flagAction">
              <mat-option value="WARN">Warn</mat-option>
              <mat-option value="BLOCK">Block</mat-option>
              <mat-option value="REQUIRE_APPROVAL">Require Approval</mat-option>
            </mat-select>
            @if (form.controls.flagAction.hasError('required')) {
              <mat-error>Flag action is required</mat-error>
            }
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-content class="mt-4">
          <mat-slide-toggle formControlName="isActive">Active</mat-slide-toggle>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/admin/delta-checks">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Delta Check
        </button>
      </div>
    </form>
  `,
})
export class DeltaCheckFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  readonly parameterOptions = signal<ParameterMaster[]>([]);
  private entityId = '';

  readonly form = this.fb.nonNullable.group({
    parameterId: ['', Validators.required],
    percentageThreshold: [null as number | null, Validators.required],
    timeWindowDays: [null as number | null, Validators.required],
    flagAction: ['WARN' as string, Validators.required],
    isActive: [true],
  });

  ngOnInit(): void {
    this.loadParameterOptions();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.entityId = id;
      this.loadEntity(id);
    }
  }

  private loadParameterOptions(): void {
    this.api.list<ParameterMaster>('parameters', { page: 0, size: 500 }).subscribe({
      next: (response) => this.parameterOptions.set(response.data),
    });
  }

  private loadEntity(id: string): void {
    this.api.get<DeltaCheck>('config/delta-checks', id).subscribe({
      next: (response) => {
        const e = response.data;
        this.form.patchValue({
          parameterId: e.parameterId,
          percentageThreshold: e.percentageThreshold,
          timeWindowDays: e.timeWindowDays,
          flagAction: e.flagAction,
          isActive: e.isActive,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<DeltaCheck>;

    const request$ = this.isEditMode()
      ? this.api.update<DeltaCheck>('config/delta-checks', this.entityId, value)
      : this.api.create<DeltaCheck>('config/delta-checks', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Delta check ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/delta-checks']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
