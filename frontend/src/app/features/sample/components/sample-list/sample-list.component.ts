import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { SampleService } from '../../services/sample.service';
import { Sample, SampleStatus, STATUS_COLORS } from '../../models/sample.model';

@Component({
  selector: 'app-sample-list',
  standalone: true,
  imports: [
    RouterLink, MatTableModule, MatPaginatorModule, MatButtonModule,
    MatIconModule, MatChipsModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatProgressSpinnerModule, MatTooltipModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Sample Management</h2>
        <p class="text-gray-500 text-sm">Track and manage sample lifecycle</p>
      </div>
      <div class="flex gap-2">
        <a mat-stroked-button routerLink="pending-receipt">
          <mat-icon>pending_actions</mat-icon> Pending Receipt
        </a>
        <a mat-raised-button color="primary" routerLink="collect">
          <mat-icon>add_circle</mat-icon> Collect Samples
        </a>
      </div>
    </div>

    <div class="bg-white rounded-lg shadow">
      <div class="flex flex-wrap gap-4 p-4 border-b">
        <mat-form-field appearance="outline" class="flex-1 min-w-[200px]">
          <mat-label>Search by barcode</mat-label>
          <input matInput placeholder="SMP-20260319-..." (keyup.enter)="onBarcodeSearch($event)" />
          <mat-icon matSuffix>qr_code_scanner</mat-icon>
        </mat-form-field>
        <mat-form-field appearance="outline" class="w-48">
          <mat-label>Status filter</mat-label>
          <mat-select (selectionChange)="onStatusFilter($event.value)">
            <mat-option value="">All</mat-option>
            @for (s of statusOptions; track s) {
              <mat-option [value]="s">{{ s }}</mat-option>
            }
          </mat-select>
        </mat-form-field>
      </div>

      @if (loading()) {
        <div class="flex justify-center p-8"><mat-spinner diameter="40" /></div>
      } @else {
        <table mat-table [dataSource]="samples()" class="w-full">
          <ng-container matColumnDef="barcode">
            <th mat-header-cell *matHeaderCellDef>Barcode</th>
            <td mat-cell *matCellDef="let row" class="font-mono font-medium text-blue-600">{{ row.sampleBarcode }}</td>
          </ng-container>
          <ng-container matColumnDef="tubeType">
            <th mat-header-cell *matHeaderCellDef>Tube</th>
            <td mat-cell *matCellDef="let row">
              <span class="px-2 py-1 rounded text-xs font-medium text-white"
                    [style.background-color]="getTubeColor(row.tubeType)">
                {{ row.tubeType }}
              </span>
            </td>
          </ng-container>
          <ng-container matColumnDef="status">
            <th mat-header-cell *matHeaderCellDef>Status</th>
            <td mat-cell *matCellDef="let row">
              <span class="px-2 py-1 rounded-full text-xs font-medium"
                    [class]="getStatusClass(row.status)">{{ row.status }}</span>
            </td>
          </ng-container>
          <ng-container matColumnDef="collectedAt">
            <th mat-header-cell *matHeaderCellDef>Collected At</th>
            <td mat-cell *matCellDef="let row">{{ formatDate(row.collectedAt) }}</td>
          </ng-container>
          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef>Actions</th>
            <td mat-cell *matCellDef="let row">
              <a mat-icon-button [routerLink]="[row.id]" matTooltip="View detail"><mat-icon>visibility</mat-icon></a>
              <a mat-icon-button [routerLink]="[row.id, 'tracking']" matTooltip="Timeline"><mat-icon>timeline</mat-icon></a>
              @if (row.status === 'COLLECTED') {
                <a mat-icon-button [routerLink]="[row.id, 'receive']" matTooltip="Receive"><mat-icon>inbox</mat-icon></a>
              }
              @if (row.status === 'ACCEPTED') {
                <a mat-icon-button [routerLink]="[row.id, 'aliquot']" matTooltip="Aliquot"><mat-icon>call_split</mat-icon></a>
              }
            </td>
          </ng-container>
          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns;" class="hover:bg-gray-50"></tr>
        </table>
        <mat-paginator [length]="totalElements()" [pageSize]="20"
                       (page)="onPageChange($event)" />
      }
    </div>
  `,
})
export class SampleListComponent implements OnInit {
  private readonly sampleService = inject(SampleService);

  samples = signal<Sample[]>([]);
  loading = signal(false);
  totalElements = signal(0);

  readonly displayedColumns = ['barcode', 'tubeType', 'status', 'collectedAt', 'actions'];
  readonly statusOptions: SampleStatus[] = [
    'COLLECTED', 'RECEIVED', 'ACCEPTED', 'REJECTED', 'ALIQUOTED',
    'PROCESSING', 'COMPLETED', 'STORED', 'DISPOSED',
  ];

  private currentStatus = '';
  private currentPage = 0;

  ngOnInit(): void { this.loadSamples(); }

  loadSamples(): void {
    this.loading.set(true);
    const obs = this.currentStatus
      ? this.sampleService.getByStatus(this.currentStatus, this.currentPage)
      : this.sampleService.getAll(this.currentPage);

    obs.subscribe({
      next: res => {
        this.samples.set(res.data.data ?? []);
        this.totalElements.set(res.data.totalElements ?? 0);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  onBarcodeSearch(event: Event): void {
    const barcode = (event.target as HTMLInputElement).value.trim();
    if (!barcode) return;
    this.loading.set(true);
    this.sampleService.getByBarcode(barcode).subscribe({
      next: res => { this.samples.set([res.data]); this.totalElements.set(1); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  onStatusFilter(status: string): void {
    this.currentStatus = status;
    this.currentPage = 0;
    this.loadSamples();
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.loadSamples();
  }

  formatDate(iso: string): string { return iso ? iso.slice(0, 16).replace('T', ' ') : '—'; }

  getStatusClass(status: SampleStatus): string {
    return STATUS_COLORS[status] ?? 'bg-gray-100 text-gray-700';
  }

  getTubeColor(tubeType: string): string {
    const colors: Record<string, string> = {
      RED: '#ef4444', EDTA: '#8b5cf6', CITRATE: '#3b82f6',
      FLUORIDE: '#6b7280', HEPARIN: '#22c55e', SST: '#f59e0b', OTHER: '#94a3b8',
    };
    return colors[tubeType] ?? '#94a3b8';
  }
}
