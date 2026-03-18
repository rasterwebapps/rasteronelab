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
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationService } from '@core/services/notification.service';
import { InsuranceTariff, TestMaster } from '../../models/admin.models';

@Component({
  selector: 'app-insurance-tariff-form',
  standalone: true,
  imports: [
    RouterLink, ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatButtonModule, MatIconModule, MatCardModule,
    MatSlideToggleModule, MatDatepickerModule, MatNativeDateModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Insurance Tariff</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update tariff details' : 'Create a new insurance tariff' }}</p>
      </div>
      <a mat-button routerLink="/admin/insurance-tariffs">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Insurance Information</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Insurance Name</mat-label>
            <input matInput formControlName="insuranceName" />
            @if (form.controls.insuranceName.hasError('required')) {
              <mat-error>Insurance name is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Plan Name</mat-label>
            <input matInput formControlName="planName" />
            @if (form.controls.planName.hasError('required')) {
              <mat-error>Plan name is required</mat-error>
            }
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Tariff Details</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Test</mat-label>
            <mat-select formControlName="testId">
              @for (test of tests(); track test.id) {
                <mat-option [value]="test.id">{{ test.testName }}</mat-option>
              }
            </mat-select>
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Tariff Rate</mat-label>
            <input matInput type="number" formControlName="tariffRate" />
            @if (form.controls.tariffRate.hasError('required')) {
              <mat-error>Tariff rate is required</mat-error>
            }
            @if (form.controls.tariffRate.hasError('min')) {
              <mat-error>Rate must be positive</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Effective From</mat-label>
            <input matInput [matDatepicker]="fromPicker" formControlName="effectiveFrom" />
            <mat-datepicker-toggle matIconSuffix [for]="fromPicker" />
            <mat-datepicker #fromPicker />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Effective To</mat-label>
            <input matInput [matDatepicker]="toPicker" formControlName="effectiveTo" />
            <mat-datepicker-toggle matIconSuffix [for]="toPicker" />
            <mat-datepicker #toPicker />
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
        <a mat-button routerLink="/admin/insurance-tariffs">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Tariff
        </button>
      </div>
    </form>
  `,
})
export class InsuranceTariffFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  readonly tests = signal<TestMaster[]>([]);
  private itemId = '';

  readonly form = this.fb.nonNullable.group({
    insuranceName: ['', Validators.required],
    planName: ['', Validators.required],
    testId: [''],
    tariffRate: [0, [Validators.required, Validators.min(0)]],
    effectiveFrom: [null as Date | null],
    effectiveTo: [null as Date | null],
    isActive: [true],
  });

  ngOnInit(): void {
    this.loadTests();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.itemId = id;
      this.loadItem(id);
    }
  }

  private loadTests(): void {
    this.api.list<TestMaster>('tests', { page: 0, size: 500 }).subscribe({
      next: (response) => this.tests.set(response.data),
    });
  }

  private loadItem(id: string): void {
    this.api.get<InsuranceTariff>('config/insurance-tariffs', id).subscribe({
      next: (response) => {
        const item = response.data;
        this.form.patchValue({
          insuranceName: item.insuranceName,
          planName: item.planName,
          testId: item.testId,
          tariffRate: item.tariffRate,
          effectiveFrom: item.effectiveFrom ? new Date(item.effectiveFrom) : null,
          effectiveTo: item.effectiveTo ? new Date(item.effectiveTo) : null,
          isActive: item.isActive,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<InsuranceTariff>;

    const request$ = this.isEditMode()
      ? this.api.update<InsuranceTariff>('config/insurance-tariffs', this.itemId, value)
      : this.api.create<InsuranceTariff>('config/insurance-tariffs', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Tariff ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/insurance-tariffs']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
