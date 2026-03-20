import {
  Component, ChangeDetectionStrategy, inject, signal, OnInit, OnDestroy,
} from '@angular/core';
import { Router } from '@angular/router';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { SampleService } from '../../services/sample.service';
import { BarcodeScannerService } from '../../services/barcode-scanner.service';
import { TubeType } from '../../models/sample.model';

@Component({
  selector: 'app-sample-collect',
  standalone: true,
  imports: [
    ReactiveFormsModule, MatCardModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatButtonModule, MatIconModule, MatSnackBarModule,
    MatProgressSpinnerModule, MatDividerModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="max-w-3xl mx-auto">
      <div class="flex items-center gap-3 mb-6">
        <button mat-icon-button onclick="history.back()"><mat-icon>arrow_back</mat-icon></button>
        <div>
          <h2 class="text-xl font-bold text-gray-800">Collect Samples</h2>
          <p class="text-gray-500 text-sm">Record sample collection for a test order</p>
        </div>
      </div>

      <!-- Barcode scan to load order -->
      <mat-card class="mb-4">
        <mat-card-header>
          <mat-icon mat-card-avatar>qr_code_scanner</mat-icon>
          <mat-card-title>Scan or Enter Order Barcode</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <div class="flex gap-3 mt-4">
            <mat-form-field appearance="outline" class="flex-1">
              <mat-label>Order Barcode / Order Number</mat-label>
              <input matInput #barcodeInput placeholder="ORD-20260319-000001"
                     (keyup.enter)="onOrderBarcode(barcodeInput.value)" />
              <mat-icon matSuffix>search</mat-icon>
            </mat-form-field>
            <button mat-raised-button color="accent" (click)="onOrderBarcode(barcodeInput.value)">
              Load
            </button>
          </div>
        </mat-card-content>
      </mat-card>

      <form [formGroup]="form" (ngSubmit)="onSubmit()">
        <mat-card class="mb-4">
          <mat-card-header>
            <mat-icon mat-card-avatar>science</mat-icon>
            <mat-card-title>Tubes to Collect</mat-card-title>
            <mat-card-subtitle>Add one row per tube type</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <div class="grid gap-4 mt-4">
              <mat-form-field appearance="outline">
                <mat-label>Order ID</mat-label>
                <input matInput formControlName="orderId" placeholder="UUID" />
              </mat-form-field>
              <mat-form-field appearance="outline">
                <mat-label>Patient ID</mat-label>
                <input matInput formControlName="patientId" placeholder="UUID" />
              </mat-form-field>
            </div>

            <mat-divider class="my-4" />

            <div formArrayName="tubes">
              @for (tube of tubesArray.controls; track $index) {
                <div [formGroupName]="$index" class="grid grid-cols-2 md:grid-cols-3 gap-3 mb-4 p-3 border rounded-lg">
                  <mat-form-field appearance="outline">
                    <mat-label>Tube Type</mat-label>
                    <mat-select formControlName="tubeType" required>
                      @for (t of tubeTypes; track t) {
                        <mat-option [value]="t">{{ t }}</mat-option>
                      }
                    </mat-select>
                  </mat-form-field>
                  <mat-form-field appearance="outline">
                    <mat-label>Collection Site</mat-label>
                    <mat-select formControlName="collectionSite">
                      @for (s of collectionSites; track s) {
                        <mat-option [value]="s">{{ s }}</mat-option>
                      }
                    </mat-select>
                  </mat-form-field>
                  <mat-form-field appearance="outline">
                    <mat-label>Volume (mL)</mat-label>
                    <input matInput type="number" formControlName="quantity" min="0" />
                  </mat-form-field>
                  <mat-form-field appearance="outline">
                    <mat-label>Collector ID</mat-label>
                    <input matInput formControlName="collectedBy" placeholder="UUID" />
                  </mat-form-field>
                  <div class="flex items-center">
                    <button mat-icon-button type="button" color="warn"
                            (click)="removeTube($index)" [disabled]="tubesArray.length <= 1">
                      <mat-icon>delete</mat-icon>
                    </button>
                  </div>
                </div>
              }
            </div>

            <button mat-stroked-button type="button" (click)="addTube()">
              <mat-icon>add</mat-icon> Add Tube
            </button>
          </mat-card-content>
        </mat-card>

        <mat-card class="mb-4">
          <mat-card-content>
            <mat-form-field appearance="outline" class="w-full mt-4">
              <mat-label>Notes</mat-label>
              <textarea matInput formControlName="notes" rows="2"></textarea>
            </mat-form-field>
          </mat-card-content>
        </mat-card>

        <div class="flex justify-end gap-3">
          <button mat-stroked-button type="button" onclick="history.back()">Cancel</button>
          <button mat-raised-button color="primary" type="submit" [disabled]="saving() || form.invalid">
            @if (saving()) { <mat-spinner diameter="20" class="inline" /> }
            @else { <mat-icon>biotech</mat-icon> }
            Record Collection
          </button>
        </div>
      </form>
    </div>
  `,
})
export class SampleCollectComponent implements OnDestroy {
  private readonly sampleService = inject(SampleService);
  private readonly barcodeScanner = inject(BarcodeScannerService);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  private readonly snackBar = inject(MatSnackBar);

  saving = signal(false);

  readonly tubeTypes: TubeType[] = ['RED', 'EDTA', 'CITRATE', 'FLUORIDE', 'HEPARIN', 'SST', 'OTHER'];
  readonly collectionSites = ['LEFT_ARM', 'RIGHT_ARM', 'LEFT_HAND', 'RIGHT_HAND', 'FINGER_PRICK', 'HEEL_PRICK', 'OTHER'];

  form = this.fb.group({
    orderId:        ['', Validators.required],
    patientId:      ['', Validators.required],
    homeCollection: [false],
    notes:          [''],
    tubes:          this.fb.array([this.buildTubeGroup()]),
  });

  get tubesArray(): FormArray { return this.form.get('tubes') as FormArray; }

  ngOnDestroy(): void {
    this.barcodeScanner.stopKeyboardWedge();
  }

  onOrderBarcode(barcode: string): void {
    if (!barcode.trim()) return;
    // Pre-fill orderId with barcode value; in a real system this would lookup the order
    this.form.patchValue({ orderId: barcode.trim() });
    this.snackBar.open('Order loaded — please fill in Patient ID and tube details', '', { duration: 3000 });
  }

  addTube(): void {
    this.tubesArray.push(this.buildTubeGroup());
  }

  removeTube(index: number): void {
    if (this.tubesArray.length > 1) this.tubesArray.removeAt(index);
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);

    const now = new Date().toISOString();
    const raw = this.form.getRawValue();

    const request = {
      orderId:        raw.orderId!,
      patientId:      raw.patientId!,
      homeCollection: raw.homeCollection ?? false,
      notes:          raw.notes ?? undefined,
      tubes: raw.tubes.map((t: Record<string, unknown>) => ({
        tubeType:       t['tubeType'],
        collectionSite: t['collectionSite'] || undefined,
        collectedBy:    t['collectedBy'],
        collectedAt:    now,
        quantity:       t['quantity'] ? Number(t['quantity']) : undefined,
        unit:           'ML',
      })),
    };

    this.sampleService.collectSamples(request as Parameters<SampleService['collectSamples']>[0]).subscribe({
      next: () => {
        this.snackBar.open('Samples collected successfully!', '', { duration: 3000 });
        this.router.navigate(['/samples']);
      },
      error: () => {
        this.snackBar.open('Failed to record collection', 'Close', { duration: 5000 });
        this.saving.set(false);
      },
    });
  }

  private buildTubeGroup() {
    return this.fb.group({
      tubeType:       ['', Validators.required],
      collectionSite: [''],
      collectedBy:    ['', Validators.required],
      quantity:       [null],
    });
  }
}
