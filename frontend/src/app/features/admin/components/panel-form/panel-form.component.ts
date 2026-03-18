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
import { TestPanel, TestMaster, Department } from '../../models/admin.models';

@Component({
  selector: 'app-panel-form',
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
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Test Panel</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update panel details' : 'Create a new test panel' }}</p>
      </div>
      <a mat-button routerLink="/admin/panels">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Panel Information</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Panel Code</mat-label>
            <input matInput formControlName="panelCode" placeholder="e.g. PNL-001" />
            @if (form.controls.panelCode.hasError('required')) {
              <mat-error>Panel code is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Panel Name</mat-label>
            <input matInput formControlName="panelName" />
            @if (form.controls.panelName.hasError('required')) {
              <mat-error>Panel name is required</mat-error>
            }
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
          <mat-card-title>Tests in Panel</mat-card-title>
        </mat-card-header>
        <mat-card-content class="mt-4">
          <mat-form-field appearance="outline" class="w-full">
            <mat-label>Select Tests</mat-label>
            <mat-select formControlName="testIds" multiple>
              @for (test of availableTests(); track test.id) {
                <mat-option [value]="test.id">{{ test.testCode }} - {{ test.testName }}</mat-option>
              }
            </mat-select>
            @if (form.controls.testIds.hasError('required')) {
              <mat-error>At least one test is required</mat-error>
            }
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/admin/panels">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Panel
        </button>
      </div>
    </form>
  `,
})
export class PanelFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  readonly departments = signal<Department[]>([]);
  readonly availableTests = signal<TestMaster[]>([]);
  private panelId = '';

  readonly form = this.fb.nonNullable.group({
    panelCode: ['', Validators.required],
    panelName: ['', Validators.required],
    departmentId: ['', Validators.required],
    testIds: [[] as string[], Validators.required],
    displayOrder: [0],
    isActive: [true],
  });

  ngOnInit(): void {
    this.loadDepartments();
    this.loadAvailableTests();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.panelId = id;
      this.loadPanel(id);
    }
  }

  private loadDepartments(): void {
    this.api.list<Department>('admin/departments', { page: 0, size: 200 }).subscribe({
      next: (response) => this.departments.set(response.data),
    });
  }

  private loadAvailableTests(): void {
    this.api.list<TestMaster>('tests', { page: 0, size: 500 }).subscribe({
      next: (response) => this.availableTests.set(response.data),
    });
  }

  private loadPanel(id: string): void {
    this.api.get<TestPanel>('panels', id).subscribe({
      next: (response) => {
        const p = response.data;
        this.form.patchValue({
          panelCode: p.panelCode,
          panelName: p.panelName,
          departmentId: p.departmentId,
          testIds: p.testIds,
          displayOrder: p.displayOrder,
          isActive: p.isActive,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<TestPanel>;

    const request$ = this.isEditMode()
      ? this.api.update<TestPanel>('panels', this.panelId, value)
      : this.api.create<TestPanel>('panels', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Panel ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/panels']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
