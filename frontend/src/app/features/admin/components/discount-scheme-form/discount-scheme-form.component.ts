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
import { DiscountScheme } from '../../models/admin.models';

@Component({
  selector: 'app-discount-scheme-form',
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
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Discount Scheme</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update scheme details' : 'Create a new discount scheme' }}</p>
      </div>
      <a mat-button routerLink="/admin/discount-schemes">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Scheme Information</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Scheme Code</mat-label>
            <input matInput formControlName="schemeCode" placeholder="e.g. DISC-001" />
            @if (form.controls.schemeCode.hasError('required')) {
              <mat-error>Scheme code is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Scheme Name</mat-label>
            <input matInput formControlName="schemeName" />
            @if (form.controls.schemeName.hasError('required')) {
              <mat-error>Scheme name is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Applicable To</mat-label>
            <mat-select formControlName="applicableTo">
              <mat-option value="WALK_IN">Walk-In</mat-option>
              <mat-option value="CORPORATE">Corporate</mat-option>
              <mat-option value="INSURANCE">Insurance</mat-option>
            </mat-select>
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Discount Details</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Discount Type</mat-label>
            <mat-select formControlName="discountType">
              <mat-option value="PERCENTAGE">Percentage</mat-option>
              <mat-option value="FIXED_AMOUNT">Fixed Amount</mat-option>
            </mat-select>
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Discount Value</mat-label>
            <input matInput type="number" formControlName="discountValue" />
            @if (form.controls.discountValue.hasError('required')) {
              <mat-error>Discount value is required</mat-error>
            }
            @if (form.controls.discountValue.hasError('min')) {
              <mat-error>Value must be positive</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Min Transaction Amount</mat-label>
            <input matInput type="number" formControlName="minTransactionAmount" />
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Validity Period</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Start Date</mat-label>
            <input matInput [matDatepicker]="startPicker" formControlName="startDate" />
            <mat-datepicker-toggle matIconSuffix [for]="startPicker" />
            <mat-datepicker #startPicker />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>End Date</mat-label>
            <input matInput [matDatepicker]="endPicker" formControlName="endDate" />
            <mat-datepicker-toggle matIconSuffix [for]="endPicker" />
            <mat-datepicker #endPicker />
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
        <a mat-button routerLink="/admin/discount-schemes">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Scheme
        </button>
      </div>
    </form>
  `,
})
export class DiscountSchemeFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  private itemId = '';

  readonly form = this.fb.nonNullable.group({
    schemeCode: ['', Validators.required],
    schemeName: ['', Validators.required],
    applicableTo: ['WALK_IN' as string],
    discountType: ['PERCENTAGE' as string],
    discountValue: [0, [Validators.required, Validators.min(0)]],
    minTransactionAmount: [null as number | null],
    startDate: [null as Date | null],
    endDate: [null as Date | null],
    isActive: [true],
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.itemId = id;
      this.loadItem(id);
    }
  }

  private loadItem(id: string): void {
    this.api.get<DiscountScheme>('config/discount-schemes', id).subscribe({
      next: (response) => {
        const item = response.data;
        this.form.patchValue({
          schemeCode: item.schemeCode,
          schemeName: item.schemeName,
          applicableTo: item.applicableTo,
          discountType: item.discountType,
          discountValue: item.discountValue,
          minTransactionAmount: item.minTransactionAmount ?? null,
          startDate: item.startDate ? new Date(item.startDate) : null,
          endDate: item.endDate ? new Date(item.endDate) : null,
          isActive: item.isActive,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<DiscountScheme>;

    const request$ = this.isEditMode()
      ? this.api.update<DiscountScheme>('config/discount-schemes', this.itemId, value)
      : this.api.create<DiscountScheme>('config/discount-schemes', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Scheme ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/discount-schemes']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
