import { Component, ChangeDetectionStrategy, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { SampleService } from '../../services/sample.service';

@Component({
  selector: 'app-sample-transfer',
  standalone: true,
  imports: [
    ReactiveFormsModule, MatCardModule, MatFormFieldModule, MatInputModule,
    MatButtonModule, MatIconModule, MatSnackBarModule, MatProgressSpinnerModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="max-w-xl mx-auto">
      <div class="flex items-center gap-3 mb-6">
        <button mat-icon-button onclick="history.back()"><mat-icon>arrow_back</mat-icon></button>
        <div>
          <h2 class="text-xl font-bold text-gray-800">Transfer Sample</h2>
          <p class="text-gray-500 text-sm">Send sample to another branch for specialist testing</p>
        </div>
      </div>

      <mat-card>
        <mat-card-header>
          <mat-icon mat-card-avatar>swap_horiz</mat-icon>
          <mat-card-title>Inter-Branch Transfer</mat-card-title>
          <mat-card-subtitle>BRANCH_ADMIN or higher required</mat-card-subtitle>
        </mat-card-header>
        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-4 mt-4">
            <mat-form-field appearance="outline">
              <mat-label>Destination Branch ID</mat-label>
              <input matInput formControlName="destinationBranchId" required />
            </mat-form-field>
            <mat-form-field appearance="outline">
              <mat-label>Transferred By (UUID)</mat-label>
              <input matInput formControlName="transferredBy" required />
            </mat-form-field>
            <mat-form-field appearance="outline">
              <mat-label>Reason</mat-label>
              <input matInput formControlName="reason" />
            </mat-form-field>
            <mat-form-field appearance="outline">
              <mat-label>Notes</mat-label>
              <textarea matInput formControlName="notes" rows="2"></textarea>
            </mat-form-field>
            <div class="flex justify-end gap-3">
              <button mat-stroked-button type="button" onclick="history.back()">Cancel</button>
              <button mat-raised-button color="primary" type="submit"
                      [disabled]="saving() || form.invalid">
                @if (saving()) { <mat-spinner diameter="20" /> }
                @else { <mat-icon>swap_horiz</mat-icon> }
                Initiate Transfer
              </button>
            </div>
          </form>
        </mat-card-content>
      </mat-card>
    </div>
  `,
})
export class SampleTransferComponent {
  private readonly sampleService = inject(SampleService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  private readonly snackBar = inject(MatSnackBar);

  saving = signal(false);

  form = this.fb.group({
    destinationBranchId: ['', Validators.required],
    transferredBy:       ['', Validators.required],
    reason:              [''],
    notes:               [''],
  });

  onSubmit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    if (this.form.invalid) return;
    this.saving.set(true);
    const v = this.form.getRawValue();
    this.sampleService.transferSample(id, {
      destinationBranchId: v.destinationBranchId!,
      transferredBy:       v.transferredBy!,
      reason:              v.reason ?? undefined,
      notes:               v.notes ?? undefined,
    }).subscribe({
      next: () => {
        this.snackBar.open('Transfer initiated', '', { duration: 3000 });
        this.router.navigate(['/samples', id]);
      },
      error: () => { this.snackBar.open('Failed to initiate transfer', 'Close', { duration: 5000 }); this.saving.set(false); },
    });
  }
}
