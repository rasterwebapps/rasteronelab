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
import { AutoValidationRule, Department } from '../../models/admin.models';

@Component({
  selector: 'app-auto-validation-form',
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
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Auto-Validation Rule</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update validation rule' : 'Define a new auto-validation rule' }}</p>
      </div>
      <a mat-button routerLink="/admin/auto-validation">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Rule Details</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Rule Code</mat-label>
            <input matInput formControlName="ruleCode" />
            @if (form.controls.ruleCode.hasError('required')) {
              <mat-error>Rule code is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Rule Name</mat-label>
            <input matInput formControlName="ruleName" />
            @if (form.controls.ruleName.hasError('required')) {
              <mat-error>Rule name is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Priority</mat-label>
            <input matInput formControlName="rulePriority" type="number" />
            @if (form.controls.rulePriority.hasError('required')) {
              <mat-error>Priority is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Departments</mat-label>
            <mat-select formControlName="departmentIds" multiple>
              @for (dept of departmentOptions(); track dept.id) {
                <mat-option [value]="dept.id">{{ dept.departmentName }} ({{ dept.departmentCode }})</mat-option>
              }
            </mat-select>
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Conditions</mat-card-title>
        </mat-card-header>
        <mat-card-content class="mt-4">
          <mat-form-field appearance="outline" class="w-full">
            <mat-label>Conditions</mat-label>
            <textarea matInput formControlName="conditions" rows="5"
              placeholder="Describe validation conditions, e.g. IF parameter X within range AND no critical values THEN auto-approve"></textarea>
            @if (form.controls.conditions.hasError('required')) {
              <mat-error>Conditions are required</mat-error>
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
        <a mat-button routerLink="/admin/auto-validation">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Rule
        </button>
      </div>
    </form>
  `,
})
export class AutoValidationFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  readonly departmentOptions = signal<Department[]>([]);
  private entityId = '';

  readonly form = this.fb.nonNullable.group({
    ruleCode: ['', Validators.required],
    ruleName: ['', Validators.required],
    conditions: ['', Validators.required],
    departmentIds: [[] as string[]],
    rulePriority: [null as number | null, Validators.required],
    isActive: [true],
  });

  ngOnInit(): void {
    this.loadDepartmentOptions();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.entityId = id;
      this.loadEntity(id);
    }
  }

  private loadDepartmentOptions(): void {
    this.api.list<Department>('admin/departments', { page: 0, size: 500 }).subscribe({
      next: (response) => this.departmentOptions.set(response.data),
    });
  }

  private loadEntity(id: string): void {
    this.api.get<AutoValidationRule>('config/auto-validation', id).subscribe({
      next: (response) => {
        const e = response.data;
        this.form.patchValue({
          ruleCode: e.ruleCode,
          ruleName: e.ruleName,
          conditions: e.conditions,
          departmentIds: e.departmentIds ?? [],
          rulePriority: e.rulePriority,
          isActive: e.isActive,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<AutoValidationRule>;

    const request$ = this.isEditMode()
      ? this.api.update<AutoValidationRule>('config/auto-validation', this.entityId, value)
      : this.api.create<AutoValidationRule>('config/auto-validation', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Auto-validation rule ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/auto-validation']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
