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
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationService } from '@core/services/notification.service';
import { ReportTemplate, Department } from '../../models/admin.models';

@Component({
  selector: 'app-report-template-form',
  standalone: true,
  imports: [
    RouterLink, ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatButtonModule, MatIconModule, MatCardModule,
    MatSlideToggleModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Report Template</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update template details' : 'Create a new report template' }}</p>
      </div>
      <a mat-button routerLink="/admin/report-templates">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Basic Information</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Template Name</mat-label>
            <input matInput formControlName="templateName" />
            @if (form.controls.templateName.hasError('required')) {
              <mat-error>Template name is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Department</mat-label>
            <mat-select formControlName="departmentId">
              @for (dept of departments(); track dept.id) {
                <mat-option [value]="dept.id">{{ dept.departmentName }}</mat-option>
              }
            </mat-select>
          </mat-form-field>
          <mat-form-field appearance="outline" class="md:col-span-2">
            <mat-label>Description</mat-label>
            <textarea matInput formControlName="description" rows="3"></textarea>
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Layout Configuration</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Layout Type</mat-label>
            <mat-select formControlName="layoutType">
              <mat-option value="PORTRAIT">Portrait</mat-option>
              <mat-option value="LANDSCAPE">Landscape</mat-option>
            </mat-select>
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Font Family</mat-label>
            <input matInput formControlName="fontFamily" placeholder="e.g. Arial, Helvetica" />
          </mat-form-field>
          <mat-form-field appearance="outline" class="md:col-span-2">
            <mat-label>Header Configuration (JSON)</mat-label>
            <textarea matInput formControlName="headerConfig" rows="4"
              placeholder='{"showLogo": true, "showAddress": true}'></textarea>
          </mat-form-field>
          <mat-form-field appearance="outline" class="md:col-span-2">
            <mat-label>Footer Configuration (JSON)</mat-label>
            <textarea matInput formControlName="footerConfig" rows="4"
              placeholder='{"showSignature": true, "disclaimer": "..."}'></textarea>
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Status</mat-card-title>
        </mat-card-header>
        <mat-card-content class="mt-4">
          <mat-slide-toggle formControlName="isActive">Active</mat-slide-toggle>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/admin/report-templates">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Template
        </button>
      </div>
    </form>
  `,
})
export class ReportTemplateFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  readonly departments = signal<Department[]>([]);
  private itemId = '';

  readonly form = this.fb.nonNullable.group({
    templateName: ['', Validators.required],
    departmentId: [''],
    description: [''],
    headerConfig: [''],
    footerConfig: [''],
    fontFamily: [''],
    layoutType: ['PORTRAIT' as string],
    isActive: [true],
  });

  ngOnInit(): void {
    this.loadDepartments();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.itemId = id;
      this.loadItem(id);
    }
  }

  private loadDepartments(): void {
    this.api.list<Department>('admin/departments', { page: 0, size: 500 }).subscribe({
      next: (response) => this.departments.set(response.data),
    });
  }

  private loadItem(id: string): void {
    this.api.get<ReportTemplate>('config/report-templates', id).subscribe({
      next: (response) => {
        const item = response.data;
        this.form.patchValue({
          templateName: item.templateName,
          departmentId: item.departmentId,
          description: item.description ?? '',
          headerConfig: item.headerConfig ?? '',
          footerConfig: item.footerConfig ?? '',
          fontFamily: item.fontFamily ?? '',
          layoutType: item.layoutType,
          isActive: item.isActive,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<ReportTemplate>;

    const request$ = this.isEditMode()
      ? this.api.update<ReportTemplate>('config/report-templates', this.itemId, value)
      : this.api.create<ReportTemplate>('config/report-templates', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Template ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/report-templates']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
