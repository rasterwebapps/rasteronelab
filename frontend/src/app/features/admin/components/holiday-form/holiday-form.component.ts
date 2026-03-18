import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationService } from '@core/services/notification.service';
import { Holiday, Branch } from '../../models/admin.models';

@Component({
  selector: 'app-holiday-form',
  standalone: true,
  imports: [
    RouterLink, ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatButtonModule, MatIconModule, MatCardModule,
    MatDatepickerModule, MatNativeDateModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Holiday</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update holiday details' : 'Create a new holiday' }}</p>
      </div>
      <a mat-button routerLink="/admin/holidays">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Holiday Details</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Holiday Name</mat-label>
            <input matInput formControlName="name" />
            @if (form.controls.name.hasError('required')) {
              <mat-error>Holiday name is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Date</mat-label>
            <input matInput [matDatepicker]="picker" formControlName="date" />
            <mat-datepicker-toggle matIconSuffix [for]="picker" />
            <mat-datepicker #picker />
            @if (form.controls.date.hasError('required')) {
              <mat-error>Date is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Holiday Type</mat-label>
            <mat-select formControlName="holidayType">
              <mat-option value="NATIONAL">National</mat-option>
              <mat-option value="ORGANIZATIONAL">Organizational</mat-option>
              <mat-option value="REGIONAL">Regional</mat-option>
            </mat-select>
            @if (form.controls.holidayType.hasError('required')) {
              <mat-error>Holiday type is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Applicable Branches</mat-label>
            <mat-select formControlName="branchIds" multiple>
              @for (branch of branches(); track branch.id) {
                <mat-option [value]="branch.id">{{ branch.name }}</mat-option>
              }
            </mat-select>
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/admin/holidays">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Holiday
        </button>
      </div>
    </form>
  `,
})
export class HolidayFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  readonly branches = signal<Branch[]>([]);
  private itemId = '';

  readonly form = this.fb.nonNullable.group({
    name: ['', Validators.required],
    date: [null as Date | null, Validators.required],
    holidayType: ['NATIONAL' as string, Validators.required],
    branchIds: [[] as string[]],
  });

  ngOnInit(): void {
    this.loadBranches();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.itemId = id;
      this.loadItem(id);
    }
  }

  private loadBranches(): void {
    this.api.list<Branch>('admin/branches', { page: 0, size: 500 }).subscribe({
      next: (response) => this.branches.set(response.data),
    });
  }

  private loadItem(id: string): void {
    this.api.get<Holiday>('config/holidays', id).subscribe({
      next: (response) => {
        const item = response.data;
        this.form.patchValue({
          name: item.name,
          date: new Date(item.date),
          holidayType: item.holidayType,
          branchIds: item.branchIds,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<Holiday>;

    const request$ = this.isEditMode()
      ? this.api.update<Holiday>('config/holidays', this.itemId, value)
      : this.api.create<Holiday>('config/holidays', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Holiday ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/holidays']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
