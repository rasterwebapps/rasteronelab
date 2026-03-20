import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
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
import { Sample, STATUS_COLORS, SampleStatus } from '../../models/sample.model';

@Component({
  selector: 'app-sample-storage',
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
          <h2 class="text-xl font-bold text-gray-800">Store Sample</h2>
          <p class="text-gray-500 text-sm">Assign storage location to a sample</p>
        </div>
      </div>

      @if (loading()) {
        <div class="flex justify-center p-8"><mat-spinner diameter="40" /></div>
      } @else if (sample()) {
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
              <span class="text-gray-500">Order ID</span><span class="font-mono">{{ sample()!.orderId }}</span>
              <span class="text-gray-500">Patient ID</span><span class="font-mono">{{ sample()!.patientId }}</span>
            </div>
          </mat-card-content>
        </mat-card>

        <!-- Storage form -->
        <mat-card>
          <mat-card-header>
            <mat-icon mat-card-avatar>inventory_2</mat-icon>
            <mat-card-title>Storage Location</mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <form [formGroup]="storageForm" (ngSubmit)="onStore()" class="grid gap-4 mt-4">
              <mat-form-field appearance="outline">
                <mat-label>Storage Rack *</mat-label>
                <input matInput formControlName="storageRack" placeholder="e.g. R-01" />
              </mat-form-field>
              <mat-form-field appearance="outline">
                <mat-label>Shelf *</mat-label>
                <input matInput formControlName="storageShelf" placeholder="e.g. S-03" />
              </mat-form-field>
              <mat-form-field appearance="outline">
                <mat-label>Position *</mat-label>
                <input matInput formControlName="storagePosition" placeholder="e.g. P-12" />
              </mat-form-field>
              <mat-form-field appearance="outline">
                <mat-label>Disposal Date</mat-label>
                <input matInput type="date" formControlName="disposalDate" />
              </mat-form-field>
              <button mat-raised-button color="primary" type="submit"
                      [disabled]="saving() || storageForm.invalid">
                @if (saving()) { <mat-spinner diameter="20" /> }
                @else { <mat-icon>inventory_2</mat-icon> }
                Assign Storage
              </button>
            </form>
          </mat-card-content>
        </mat-card>
      }
    </div>
  `,
})
export class SampleStorageComponent implements OnInit {
  private readonly sampleService = inject(SampleService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  private readonly snackBar = inject(MatSnackBar);

  sample = signal<Sample | null>(null);
  loading = signal(false);
  saving = signal(false);

  storageForm = this.fb.group({
    storageRack:     ['', Validators.required],
    storageShelf:    ['', Validators.required],
    storagePosition: ['', Validators.required],
    disposalDate:    [''],
  });

  formatDate(iso: string | undefined): string { return iso ? iso.slice(0, 16).replace('T', ' ') : '—'; }

  statusClass(): string {
    const s = this.sample()?.status;
    return s ? (STATUS_COLORS[s as SampleStatus] ?? 'bg-gray-100 text-gray-700') : '';
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loading.set(true);
      this.sampleService.getById(id).subscribe({
        next: res => { this.sample.set(res.data); this.loading.set(false); },
        error: () => { this.snackBar.open('Sample not found', 'Close', { duration: 4000 }); this.loading.set(false); },
      });
    }
  }

  onStore(): void {
    const id = this.sample()?.id;
    if (!id || this.storageForm.invalid) return;
    this.saving.set(true);
    const v = this.storageForm.getRawValue();
    this.sampleService.storeSample(id, {
      storageRack:     v.storageRack!,
      storageShelf:    v.storageShelf!,
      storagePosition: v.storagePosition!,
      disposalDate:    v.disposalDate || undefined,
    }).subscribe({
      next: () => {
        this.snackBar.open('Sample stored successfully', '', { duration: 3000 });
        this.saving.set(false);
        this.router.navigate(['/samples']);
      },
      error: () => { this.snackBar.open('Failed to store sample', 'Close', { duration: 5000 }); this.saving.set(false); },
    });
  }
}
