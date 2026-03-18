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
import { Microorganism } from '../../models/admin.models';

@Component({
  selector: 'app-microorganism-form',
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
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Microorganism</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update microorganism details' : 'Add a new microorganism' }}</p>
      </div>
      <a mat-button routerLink="/admin/microorganisms">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Microorganism Details</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Organism Code</mat-label>
            <input matInput formControlName="organismCode" />
            @if (form.controls.organismCode.hasError('required')) {
              <mat-error>Organism code is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Organism Name</mat-label>
            <input matInput formControlName="organismName" />
            @if (form.controls.organismName.hasError('required')) {
              <mat-error>Organism name is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Gram Type</mat-label>
            <mat-select formControlName="gramType">
              <mat-option value="GRAM_POSITIVE">Gram Positive</mat-option>
              <mat-option value="GRAM_NEGATIVE">Gram Negative</mat-option>
              <mat-option value="FUNGAL">Fungal</mat-option>
              <mat-option value="ACID_FAST">Acid Fast</mat-option>
            </mat-select>
            @if (form.controls.gramType.hasError('required')) {
              <mat-error>Gram type is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Clinical Significance</mat-label>
            <input matInput formControlName="clinicalSignificance" />
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-content class="mt-4">
          <mat-slide-toggle formControlName="isActive">Active</mat-slide-toggle>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/admin/microorganisms">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Microorganism
        </button>
      </div>
    </form>
  `,
})
export class MicroorganismFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  private entityId = '';

  readonly form = this.fb.nonNullable.group({
    organismCode: ['', Validators.required],
    organismName: ['', Validators.required],
    gramType: ['' as string, Validators.required],
    clinicalSignificance: [''],
    isActive: [true],
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.entityId = id;
      this.loadEntity(id);
    }
  }

  private loadEntity(id: string): void {
    this.api.get<Microorganism>('microorganisms', id).subscribe({
      next: (response) => {
        const e = response.data;
        this.form.patchValue({
          organismCode: e.organismCode,
          organismName: e.organismName,
          gramType: e.gramType,
          clinicalSignificance: e.clinicalSignificance ?? '',
          isActive: e.isActive,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<Microorganism>;

    const request$ = this.isEditMode()
      ? this.api.update<Microorganism>('microorganisms', this.entityId, value)
      : this.api.create<Microorganism>('microorganisms', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Microorganism ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/microorganisms']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
