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
import { CriticalValue, ParameterMaster } from '../../models/admin.models';

@Component({
  selector: 'app-critical-value-form',
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
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Critical Value</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update critical value configuration' : 'Configure a new critical value alert' }}</p>
      </div>
      <a mat-button routerLink="/admin/critical-values">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Parameter & Thresholds</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-3 gap-4 mt-4">
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
            <mat-label>Critical Low</mat-label>
            <input matInput formControlName="criticalLow" type="number" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Critical High</mat-label>
            <input matInput formControlName="criticalHigh" type="number" />
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Alert Configuration</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Alert Action</mat-label>
            <mat-select formControlName="alertAction">
              <mat-option value="EMAIL">Email</mat-option>
              <mat-option value="SMS">SMS</mat-option>
              <mat-option value="CALL">Call</mat-option>
            </mat-select>
            @if (form.controls.alertAction.hasError('required')) {
              <mat-error>Alert action is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Alert Priority</mat-label>
            <mat-select formControlName="alertPriority">
              <mat-option value="LOW">Low</mat-option>
              <mat-option value="MEDIUM">Medium</mat-option>
              <mat-option value="HIGH">High</mat-option>
            </mat-select>
            @if (form.controls.alertPriority.hasError('required')) {
              <mat-error>Priority is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline" class="md:col-span-2">
            <mat-label>Contact List</mat-label>
            <textarea matInput formControlName="contactList" rows="3"
              placeholder="Enter contacts (one per line or comma-separated)"></textarea>
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-content class="mt-4">
          <mat-slide-toggle formControlName="isActive">Active</mat-slide-toggle>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/admin/critical-values">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Critical Value
        </button>
      </div>
    </form>
  `,
})
export class CriticalValueFormComponent implements OnInit {
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
    criticalLow: [null as number | null],
    criticalHigh: [null as number | null],
    alertAction: ['EMAIL' as string, Validators.required],
    contactList: [''],
    alertPriority: ['HIGH' as string, Validators.required],
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
    this.api.get<CriticalValue>('config/critical-values', id).subscribe({
      next: (response) => {
        const e = response.data;
        this.form.patchValue({
          parameterId: e.parameterId,
          criticalLow: e.criticalLow ?? null,
          criticalHigh: e.criticalHigh ?? null,
          alertAction: e.alertAction,
          contactList: e.contactList ?? '',
          alertPriority: e.alertPriority,
          isActive: e.isActive,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<CriticalValue>;

    const request$ = this.isEditMode()
      ? this.api.update<CriticalValue>('config/critical-values', this.entityId, value)
      : this.api.create<CriticalValue>('config/critical-values', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Critical value ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/critical-values']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
