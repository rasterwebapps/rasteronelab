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
import { Antibiotic } from '../../models/admin.models';

@Component({
  selector: 'app-antibiotic-form',
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
        <h2 class="text-xl font-bold text-gray-800">{{ isEditMode() ? 'Edit' : 'New' }} Antibiotic</h2>
        <p class="text-gray-500 text-sm">{{ isEditMode() ? 'Update antibiotic details' : 'Add a new antibiotic' }}</p>
      </div>
      <a mat-button routerLink="/admin/antibiotics">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Antibiotic Details</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Antibiotic Code</mat-label>
            <input matInput formControlName="antibioticCode" />
            @if (form.controls.antibioticCode.hasError('required')) {
              <mat-error>Code is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Antibiotic Name</mat-label>
            <input matInput formControlName="antibioticName" />
            @if (form.controls.antibioticName.hasError('required')) {
              <mat-error>Name is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Group</mat-label>
            <mat-select formControlName="group">
              @for (g of groupOptions; track g) {
                <mat-option [value]="g">{{ g }}</mat-option>
              }
            </mat-select>
            @if (form.controls.group.hasError('required')) {
              <mat-error>Group is required</mat-error>
            }
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>CLSI Breakpoints</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-3 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Susceptible (S)</mat-label>
            <input matInput formControlName="clsiBreakpointS" type="number" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Intermediate (I)</mat-label>
            <input matInput formControlName="clsiBreakpointI" type="number" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Resistant (R)</mat-label>
            <input matInput formControlName="clsiBreakpointR" type="number" />
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-content class="mt-4">
          <mat-slide-toggle formControlName="isActive">Active</mat-slide-toggle>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/admin/antibiotics">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          {{ isEditMode() ? 'Update' : 'Create' }} Antibiotic
        </button>
      </div>
    </form>
  `,
})
export class AntibioticFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(AdminApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly isEditMode = signal(false);
  readonly saving = signal(false);
  private entityId = '';

  readonly groupOptions = [
    'PENICILLIN', 'CEPHALOSPORIN', 'AMINOGLYCOSIDE', 'FLUOROQUINOLONE',
    'MACROLIDE', 'TETRACYCLINE', 'CARBAPENEM', 'GLYCOPEPTIDE', 'OTHER',
  ];

  readonly form = this.fb.nonNullable.group({
    antibioticCode: ['', Validators.required],
    antibioticName: ['', Validators.required],
    group: ['', Validators.required],
    clsiBreakpointS: [null as number | null],
    clsiBreakpointI: [null as number | null],
    clsiBreakpointR: [null as number | null],
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
    this.api.get<Antibiotic>('antibiotics', id).subscribe({
      next: (response) => {
        const e = response.data;
        this.form.patchValue({
          antibioticCode: e.antibioticCode,
          antibioticName: e.antibioticName,
          group: e.group,
          clsiBreakpointS: e.clsiBreakpointS ?? null,
          clsiBreakpointI: e.clsiBreakpointI ?? null,
          clsiBreakpointR: e.clsiBreakpointR ?? null,
          isActive: e.isActive,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const value = this.form.getRawValue() as unknown as Partial<Antibiotic>;

    const request$ = this.isEditMode()
      ? this.api.update<Antibiotic>('antibiotics', this.entityId, value)
      : this.api.create<Antibiotic>('antibiotics', value);

    request$.subscribe({
      next: () => {
        this.notification.showSuccess(`Antibiotic ${this.isEditMode() ? 'updated' : 'created'} successfully`);
        this.router.navigate(['/admin/antibiotics']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
