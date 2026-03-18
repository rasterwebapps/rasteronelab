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
import { TatConfiguration, TestMaster } from '../../models/admin.models';

@Component({
  selector: 'app-tat-config-form',
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
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} TAT Configuration</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update TAT targets' : 'Define turnaround time targets for a test' }}</p>
      </div>
      <a mat-button routerLink="/admin/tat-config">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Test & TAT Targets</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
          <mat-form-field appearance="outline" class="md:col-span-2">
            <mat-label>Test</mat-label>
            <mat-select formControlName="testId">
              @for (test of testOptions(); track test.id) {
                <mat-option [value]="test.id">{{ test.testName }} ({{ test.testCode }})</mat-option>
              }
            </mat-select>
            @if (form.controls.testId.hasError('required')) {
              <mat-error>Test is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Routine TAT (hours)</mat-label>
            <input matInput formControlName="routineTatHours" type="number" />
            @if (form.controls.routineTatHours.hasError('required')) {
              <mat-error>Routine TAT is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>STAT TAT (hours)</mat-label>
            <input matInput formControlName="statTatHours" type="number" />
            @if (form.controls.statTatHours.hasError('required')) {
              <mat-error>STAT TAT is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Emergency TAT (hours)</mat-label>
            <input matInput formControlName="emergencyTatHours" type="number" />
            @if (form.controls.emergencyTatHours.hasError('required')) {
              <mat-error>Emergency TAT is required</mat-error>
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
        <a mat-button routerLink="/admin/tat-config">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} TAT Configuration
        </button>
      </div>
    </form>
  `,
})
export class TatConfigFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  readonly testOptions = signal<TestMaster[]>([]);
  private entityId = '';

  readonly form = this.fb.nonNullable.group({
    testId: ['', Validators.required],
    routineTatHours: [null as number | null, Validators.required],
    statTatHours: [null as number | null, Validators.required],
    emergencyTatHours: [null as number | null, Validators.required],
    isActive: [true],
  });

  ngOnInit(): void {
    this.loadTestOptions();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.entityId = id;
      this.loadEntity(id);
    }
  }

  private loadTestOptions(): void {
    this.api.list<TestMaster>('tests', { page: 0, size: 500 }).subscribe({
      next: (response) => this.testOptions.set(response.data),
    });
  }

  private loadEntity(id: string): void {
    this.api.get<TatConfiguration>('config/tat', id).subscribe({
      next: (response) => {
        const e = response.data;
        this.form.patchValue({
          testId: e.testId,
          routineTatHours: e.routineTatHours,
          statTatHours: e.statTatHours,
          emergencyTatHours: e.emergencyTatHours,
          isActive: e.isActive,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<TatConfiguration>;

    const request$ = this.isEditMode()
      ? this.api.update<TatConfiguration>('config/tat', this.entityId, value)
      : this.api.create<TatConfiguration>('config/tat', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`TAT configuration ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/tat-config']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
