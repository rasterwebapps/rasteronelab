import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationService } from '@core/services/notification.service';
import { NumberSeries } from '../../models/admin.models';

@Component({
  selector: 'app-number-series-form',
  standalone: true,
  imports: [
    RouterLink, ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatButtonModule, MatIconModule, MatCardModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Number Series</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update series configuration' : 'Create a new number series' }}</p>
      </div>
      <a mat-button routerLink="/admin/number-series">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Series Configuration</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Entity Type</mat-label>
            <input matInput formControlName="entityType" placeholder="e.g. PATIENT, SAMPLE, INVOICE" />
            @if (form.controls.entityType.hasError('required')) {
              <mat-error>Entity type is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Prefix</mat-label>
            <input matInput formControlName="prefix" placeholder="e.g. PAT, SMP, INV" />
            @if (form.controls.prefix.hasError('required')) {
              <mat-error>Prefix is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Format</mat-label>
            <input matInput formControlName="format" placeholder="e.g. PREFIX-YYYY-SEQ:6" />
            @if (form.controls.format.hasError('required')) {
              <mat-error>Format is required</mat-error>
            }
            <mat-hint>Use {{ '{' }}PREFIX{{ '}' }}, {{ '{' }}YYYY{{ '}' }}, {{ '{' }}MM{{ '}' }}, {{ '{' }}DD{{ '}' }}, {{ '{' }}SEQ:N{{ '}' }} placeholders</mat-hint>
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Next Sequence</mat-label>
            <input matInput type="number" formControlName="nextSequence" />
            @if (form.controls.nextSequence.hasError('required')) {
              <mat-error>Next sequence is required</mat-error>
            }
            @if (form.controls.nextSequence.hasError('min')) {
              <mat-error>Sequence must be at least 1</mat-error>
            }
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/admin/number-series">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Series
        </button>
      </div>
    </form>
  `,
})
export class NumberSeriesFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  private itemId = '';

  readonly form = this.fb.nonNullable.group({
    entityType: ['', Validators.required],
    prefix: ['', Validators.required],
    format: ['', Validators.required],
    nextSequence: [1, [Validators.required, Validators.min(1)]],
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
    this.api.get<NumberSeries>('config/number-series', id).subscribe({
      next: (response) => {
        const item = response.data;
        this.form.patchValue({
          entityType: item.entityType,
          prefix: item.prefix,
          format: item.format,
          nextSequence: item.nextSequence,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<NumberSeries>;

    const request$ = this.isEditMode()
      ? this.api.update<NumberSeries>('config/number-series', this.itemId, value)
      : this.api.create<NumberSeries>('config/number-series', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Number series ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/number-series']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
