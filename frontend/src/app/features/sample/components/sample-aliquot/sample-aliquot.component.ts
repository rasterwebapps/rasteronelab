import { Component, ChangeDetectionStrategy, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { SampleService } from '../../services/sample.service';

@Component({
  selector: 'app-sample-aliquot',
  standalone: true,
  imports: [
    ReactiveFormsModule, MatCardModule, MatFormFieldModule, MatInputModule,
    MatButtonModule, MatIconModule, MatSnackBarModule, MatProgressSpinnerModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="max-w-2xl mx-auto">
      <div class="flex items-center gap-3 mb-6">
        <button mat-icon-button onclick="history.back()"><mat-icon>arrow_back</mat-icon></button>
        <div>
          <h2 class="text-xl font-bold text-gray-800">Aliquot Sample</h2>
          <p class="text-gray-500 text-sm">Split sample into child aliquots for different departments</p>
        </div>
      </div>

      <form [formGroup]="form" (ngSubmit)="onSubmit()">
        <mat-card class="mb-4">
          <mat-card-header>
            <mat-icon mat-card-avatar>call_split</mat-icon>
            <mat-card-title>Aliquots</mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <div formArrayName="aliquots" class="mt-4">
              @for (a of aliquotsArray.controls; track $index) {
                <div [formGroupName]="$index" class="grid grid-cols-3 gap-3 mb-3 p-3 border rounded">
                  <mat-form-field appearance="outline">
                    <mat-label>Department ID</mat-label>
                    <input matInput formControlName="departmentId" required />
                  </mat-form-field>
                  <mat-form-field appearance="outline">
                    <mat-label>Volume (mL)</mat-label>
                    <input matInput type="number" formControlName="volume" />
                  </mat-form-field>
                  <div class="flex items-center">
                    <button mat-icon-button type="button" color="warn"
                            (click)="removeAliquot($index)" [disabled]="aliquotsArray.length <= 1">
                      <mat-icon>delete</mat-icon>
                    </button>
                  </div>
                </div>
              }
            </div>
            <button mat-stroked-button type="button" (click)="addAliquot()">
              <mat-icon>add</mat-icon> Add Aliquot
            </button>
          </mat-card-content>
        </mat-card>

        <div class="flex justify-end gap-3">
          <button mat-stroked-button type="button" onclick="history.back()">Cancel</button>
          <button mat-raised-button color="primary" type="submit"
                  [disabled]="saving() || form.invalid">
            @if (saving()) { <mat-spinner diameter="20" /> }
            @else { <mat-icon>call_split</mat-icon> }
            Create Aliquots
          </button>
        </div>
      </form>
    </div>
  `,
})
export class SampleAliquotComponent {
  private readonly sampleService = inject(SampleService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  private readonly snackBar = inject(MatSnackBar);

  saving = signal(false);

  form = this.fb.group({
    aliquots: this.fb.array([this.buildAliquotGroup()]),
  });

  get aliquotsArray(): FormArray { return this.form.get('aliquots') as FormArray; }

  addAliquot(): void { this.aliquotsArray.push(this.buildAliquotGroup()); }
  removeAliquot(index: number): void {
    if (this.aliquotsArray.length > 1) this.aliquotsArray.removeAt(index);
  }

  onSubmit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    if (this.form.invalid) return;
    this.saving.set(true);
    const raw = this.form.getRawValue();
    this.sampleService.aliquotSample(id, {
      aliquots: raw.aliquots.map((a: Record<string, unknown>) => ({
        departmentId: a['departmentId'] as string,
        volume: a['volume'] ? Number(a['volume']) : undefined,
        unit: 'ML',
      })),
    }).subscribe({
      next: () => {
        this.snackBar.open('Aliquots created', '', { duration: 3000 });
        this.router.navigate(['/samples', id]);
      },
      error: () => { this.snackBar.open('Failed to aliquot sample', 'Close', { duration: 5000 }); this.saving.set(false); },
    });
  }

  private buildAliquotGroup() {
    return this.fb.group({ departmentId: ['', Validators.required], volume: [null] });
  }
}
