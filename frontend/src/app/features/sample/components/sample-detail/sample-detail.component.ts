import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { SampleService } from '../../services/sample.service';
import { Sample, STATUS_COLORS, SampleStatus, TUBE_TYPE_COLORS, TubeType } from '../../models/sample.model';

@Component({
  selector: 'app-sample-detail',
  standalone: true,
  imports: [
    RouterLink, MatCardModule, MatButtonModule, MatIconModule,
    MatChipsModule, MatProgressSpinnerModule, MatDividerModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="max-w-3xl mx-auto">
      <div class="flex items-center gap-3 mb-6">
        <button mat-icon-button routerLink="/samples"><mat-icon>arrow_back</mat-icon></button>
        <div>
          <h2 class="text-xl font-bold text-gray-800">Sample Detail</h2>
          <p class="text-gray-500 text-sm font-mono">{{ sample()?.sampleBarcode }}</p>
        </div>
        @if (sample()) {
          <span class="ml-auto px-3 py-1 rounded-full text-sm font-medium" [class]="statusClass()">
            {{ sample()!.status }}
          </span>
        }
      </div>

      @if (loading()) {
        <div class="flex justify-center p-8"><mat-spinner diameter="40" /></div>
      } @else if (sample()) {
        <div class="grid gap-4">
          <mat-card>
            <mat-card-header>
              <mat-icon mat-card-avatar>science</mat-icon>
              <mat-card-title>Collection Details</mat-card-title>
            </mat-card-header>
            <mat-card-content>
              <div class="grid grid-cols-2 gap-3 mt-4 text-sm">
                <span class="text-gray-500">Order ID</span>
                <span class="font-mono text-blue-600">{{ sample()!.orderId }}</span>

                <span class="text-gray-500">Patient ID</span>
                <span class="font-mono">{{ sample()!.patientId }}</span>

                <span class="text-gray-500">Tube Type</span>
                <span class="font-medium">
                  <span class="inline-block w-3 h-3 rounded-full mr-1"
                        [style.background-color]="tubeColor()"></span>
                  {{ sample()!.tubeType }}
                </span>

                <span class="text-gray-500">Sample Type</span>
                <span>{{ sample()!.sampleType ?? '—' }}</span>

                <span class="text-gray-500">Collected At</span>
                <span>{{ fmtDate(sample()!.collectedAt) }}</span>

                <span class="text-gray-500">Collection Site</span>
                <span>{{ sample()!.collectionSite ?? '—' }}</span>

                <span class="text-gray-500">Volume</span>
                <span>{{ sample()!.quantity ? (sample()!.quantity + ' ' + sample()!.unit) : '—' }}</span>

                <span class="text-gray-500">Home Collection</span>
                <span>{{ sample()!.homeCollection ? 'Yes' : 'No' }}</span>
              </div>
            </mat-card-content>
          </mat-card>

          @if (sample()!.receivedAt) {
            <mat-card>
              <mat-card-header><mat-card-title>Receiving Details</mat-card-title></mat-card-header>
              <mat-card-content>
                <div class="grid grid-cols-2 gap-3 mt-4 text-sm">
                  <span class="text-gray-500">Received At</span>
                  <span>{{ fmtDate(sample()!.receivedAt) }}</span>
                  <span class="text-gray-500">TAT Started</span>
                  <span>{{ fmtDate(sample()!.tatStartedAt) }}</span>
                  <span class="text-gray-500">Temperature</span>
                  <span>{{ sample()!.temperature != null ? (sample()!.temperature + ' °C') : '—' }}</span>
                </div>
              </mat-card-content>
            </mat-card>
          }

          @if (sample()!.status === 'REJECTED') {
            <mat-card class="border-red-200">
              <mat-card-header>
                <mat-icon mat-card-avatar class="text-red-500">cancel</mat-icon>
                <mat-card-title class="text-red-700">Rejection Details</mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <div class="grid grid-cols-2 gap-3 mt-4 text-sm">
                  <span class="text-gray-500">Reason</span>
                  <span class="font-medium text-red-600">{{ sample()!.rejectionReason }}</span>
                  <span class="text-gray-500">Comment</span>
                  <span>{{ sample()!.rejectionComment ?? '—' }}</span>
                  <span class="text-gray-500">Recollection</span>
                  <span>{{ sample()!.recollectionRequired ? 'Required' : 'Not required' }}</span>
                  <span class="text-gray-500">Rejected At</span>
                  <span>{{ fmtDate(sample()!.rejectedAt) }}</span>
                </div>
              </mat-card-content>
            </mat-card>
          }

          @if (sample()!.storageRack) {
            <mat-card>
              <mat-card-header><mat-card-title>Storage Location</mat-card-title></mat-card-header>
              <mat-card-content>
                <div class="grid grid-cols-2 gap-3 mt-4 text-sm">
                  <span class="text-gray-500">Rack</span><span>{{ sample()!.storageRack }}</span>
                  <span class="text-gray-500">Shelf</span><span>{{ sample()!.storageShelf }}</span>
                  <span class="text-gray-500">Position</span><span>{{ sample()!.storagePosition }}</span>
                  <span class="text-gray-500">Stored At</span>
                  <span>{{ fmtDate(sample()!.storedAt) }}</span>
                  <span class="text-gray-500">Disposal Date</span>
                  <span>{{ sample()!.disposalDate ? sample()!.disposalDate!.slice(0, 10) : '—' }}</span>
                </div>
              </mat-card-content>
            </mat-card>
          }

          <div class="flex gap-3 flex-wrap">
            <a mat-stroked-button [routerLink]="[sample()!.id, 'tracking']">
              <mat-icon>timeline</mat-icon> View Timeline
            </a>
            @if (sample()!.status === 'COLLECTED') {
              <a mat-raised-button color="primary" [routerLink]="[sample()!.id, 'receive']">
                <mat-icon>inbox</mat-icon> Receive
              </a>
            }
            @if (sample()!.status === 'ACCEPTED') {
              <a mat-raised-button color="accent" [routerLink]="[sample()!.id, 'aliquot']">
                <mat-icon>call_split</mat-icon> Aliquot
              </a>
            }
          </div>
        </div>
      }
    </div>
  `,
})
export class SampleDetailComponent implements OnInit {
  private readonly sampleService = inject(SampleService);
  private readonly route = inject(ActivatedRoute);

  sample = signal<Sample | null>(null);
  loading = signal(true);

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.sampleService.getById(id).subscribe({
      next: res => { this.sample.set(res.data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  statusClass(): string {
    const s = this.sample()?.status as SampleStatus;
    return s ? (STATUS_COLORS[s] ?? 'bg-gray-100 text-gray-700') : '';
  }

  fmtDate(iso: string | undefined): string { return iso ? iso.slice(0, 16).replace('T', ' ') : '—'; }

  tubeColor(): string {
    const t = this.sample()?.tubeType as TubeType;
    return t ? (TUBE_TYPE_COLORS[t] ?? '#94a3b8') : '#94a3b8';
  }
}
