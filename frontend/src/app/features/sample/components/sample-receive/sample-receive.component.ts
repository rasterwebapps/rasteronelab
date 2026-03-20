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
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { MatDividerModule } from '@angular/material/divider';
import { SampleService } from '../../services/sample.service';
import { BarcodeScannerService } from '../../services/barcode-scanner.service';
import { Sample, STATUS_COLORS, SampleStatus, RejectionReason } from '../../models/sample.model';

@Component({
  selector: 'app-sample-receive',
  standalone: true,
  imports: [
    ReactiveFormsModule, MatCardModule, MatFormFieldModule, MatInputModule,
    MatButtonModule, MatIconModule, MatSnackBarModule, MatProgressSpinnerModule,
    MatDialogModule, MatSelectModule, MatDividerModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="max-w-2xl mx-auto">
      <div class="flex items-center gap-3 mb-6">
        <button mat-icon-button onclick="history.back()"><mat-icon>arrow_back</mat-icon></button>
        <div>
          <h2 class="text-xl font-bold text-gray-800">Receive / Reject Sample</h2>
          <p class="text-gray-500 text-sm">Accept or reject a sample at lab reception</p>
        </div>
      </div>

      <!-- Barcode lookup -->
      <mat-card class="mb-4">
        <mat-card-content>
          <div class="flex gap-3 mt-4">
            <mat-form-field appearance="outline" class="flex-1">
              <mat-label>Scan or enter sample barcode</mat-label>
              <input matInput #barcodeInput placeholder="SMP-20260319-..."
                     (keyup.enter)="loadByBarcode(barcodeInput.value)" />
              <mat-icon matSuffix>qr_code_scanner</mat-icon>
            </mat-form-field>
            <button mat-raised-button color="accent" (click)="loadByBarcode(barcodeInput.value)">Load</button>
          </div>
        </mat-card-content>
      </mat-card>

      @if (sample()) {
        <!-- Sample detail -->
        <mat-card class="mb-4">
          <mat-card-header>
            <mat-icon mat-card-avatar>science</mat-icon>
            <mat-card-title class="font-mono">{{ sample()!.sampleBarcode }}</mat-card-title>
            <mat-card-subtitle>
              <span class="px-2 py-1 rounded-full text-xs font-medium" [class]="statusClass()">
                {{ sample()!.status }}
              </span>
            </mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <div class="grid grid-cols-2 gap-2 mt-4 text-sm">
              <span class="text-gray-500">Tube Type</span><span class="font-medium">{{ sample()!.tubeType }}</span>
              <span class="text-gray-500">Collected At</span><span>{{ formatDate(sample()!.collectedAt) }}</span>
            </div>
          </mat-card-content>
        </mat-card>

        <!-- Receive form -->
        <mat-card class="mb-4">
          <mat-card-header><mat-card-title>Accept Sample</mat-card-title></mat-card-header>
          <mat-card-content>
            <form [formGroup]="receiveForm" (ngSubmit)="onReceive()" class="grid gap-4 mt-4">
              <mat-form-field appearance="outline">
                <mat-label>Received By (UUID)</mat-label>
                <input matInput formControlName="receivedBy" />
              </mat-form-field>
              <mat-form-field appearance="outline">
                <mat-label>Temperature (°C)</mat-label>
                <input matInput type="number" formControlName="temperature" />
              </mat-form-field>
              <mat-form-field appearance="outline">
                <mat-label>Notes</mat-label>
                <textarea matInput formControlName="notes" rows="2"></textarea>
              </mat-form-field>
              <button mat-raised-button color="primary" type="submit"
                      [disabled]="saving() || receiveForm.invalid">
                @if (saving()) { <mat-spinner diameter="20" /> }
                @else { <mat-icon>check_circle</mat-icon> }
                Receive Sample
              </button>
            </form>
          </mat-card-content>
        </mat-card>

        <mat-divider class="my-6" />

        <!-- Reject form -->
        <mat-card>
          <mat-card-header>
            <mat-icon mat-card-avatar color="warn">cancel</mat-icon>
            <mat-card-title>Reject Sample</mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <form [formGroup]="rejectForm" (ngSubmit)="onReject()" class="grid gap-4 mt-4">
              <mat-form-field appearance="outline">
                <mat-label>Rejection Reason *</mat-label>
                <mat-select formControlName="rejectionReason" required>
                  @for (r of rejectionReasons; track r) {
                    <mat-option [value]="r">{{ r }}</mat-option>
                  }
                </mat-select>
              </mat-form-field>
              <mat-form-field appearance="outline">
                <mat-label>Comments</mat-label>
                <textarea matInput formControlName="comments" rows="2"></textarea>
              </mat-form-field>
              <mat-form-field appearance="outline">
                <mat-label>Rejected By (UUID)</mat-label>
                <input matInput formControlName="rejectedBy" />
              </mat-form-field>
              <button mat-raised-button color="warn" type="submit"
                      [disabled]="saving() || rejectForm.invalid">
                <mat-icon>cancel</mat-icon> Reject Sample
              </button>
            </form>
          </mat-card-content>
        </mat-card>
      }
    </div>
  `,
})
export class SampleReceiveComponent {
  private readonly sampleService = inject(SampleService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  private readonly snackBar = inject(MatSnackBar);

  sample = signal<Sample | null>(null);
  saving = signal(false);

  readonly rejectionReasons: RejectionReason[] = [
    'HEMOLYZED', 'CLOTTED', 'INSUFFICIENT', 'WRONG_TUBE',
    'UNLABELED', 'LIPEMIC', 'ICTERIC', 'DETERIORATED', 'OLD',
  ];

  receiveForm = this.fb.group({
    receivedBy:  ['', Validators.required],
    temperature: [null],
    notes:       [''],
  });

  rejectForm = this.fb.group({
    rejectionReason:     ['', Validators.required],
    comments:            [''],
    rejectedBy:          ['', Validators.required],
    recollectionRequired:[true],
  });

  formatDate(iso: string | undefined): string { return iso ? iso.slice(0, 16).replace('T', ' ') : '—'; }

  statusClass(): string {
    const s = this.sample()?.status;
    return s ? (STATUS_COLORS[s as SampleStatus] ?? 'bg-gray-100 text-gray-700') : '';
  }

  loadByBarcode(barcode: string): void {
    if (!barcode.trim()) return;
    this.sampleService.getByBarcode(barcode.trim()).subscribe({
      next: res => this.sample.set(res.data),
      error: () => this.snackBar.open('Sample not found', 'Close', { duration: 4000 }),
    });
  }

  onReceive(): void {
    const id = this.sample()?.id;
    if (!id || this.receiveForm.invalid) return;
    this.saving.set(true);
    const v = this.receiveForm.getRawValue();
    this.sampleService.receiveSample(id, {
      receivedBy:  v.receivedBy!,
      receivedAt:  new Date().toISOString(),
      temperature: v.temperature ?? undefined,
      notes:       v.notes ?? undefined,
    }).subscribe({
      next: res => {
        this.sample.set(res.data);
        this.snackBar.open('Sample received successfully', '', { duration: 3000 });
        this.saving.set(false);
      },
      error: () => { this.snackBar.open('Failed to receive sample', 'Close', { duration: 5000 }); this.saving.set(false); },
    });
  }

  onReject(): void {
    const id = this.sample()?.id;
    if (!id || this.rejectForm.invalid) return;
    this.saving.set(true);
    const v = this.rejectForm.getRawValue();
    this.sampleService.rejectSample(id, {
      rejectedBy:          v.rejectedBy!,
      rejectionReason:     v.rejectionReason as RejectionReason,
      comments:            v.comments ?? undefined,
      recollectionRequired: v.recollectionRequired ?? true,
    }).subscribe({
      next: res => {
        this.sample.set(res.data);
        this.snackBar.open('Sample rejected', '', { duration: 3000 });
        this.saving.set(false);
      },
      error: () => { this.snackBar.open('Failed to reject sample', 'Close', { duration: 5000 }); this.saving.set(false); },
    });
  }
}
