import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { SampleService } from '../../services/sample.service';
import { Sample } from '../../models/sample.model';

@Component({
  selector: 'app-pending-collection',
  standalone: true,
  imports: [
    RouterLink, MatTableModule, MatPaginatorModule, MatButtonModule,
    MatIconModule, MatProgressSpinnerModule, MatChipsModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Pending Collection</h2>
        <p class="text-gray-500 text-sm">Orders with samples collected and awaiting lab processing</p>
      </div>
      <a mat-stroked-button routerLink="/samples"><mat-icon>arrow_back</mat-icon> All Samples</a>
    </div>

    <div class="bg-white rounded-lg shadow">
      @if (loading()) {
        <div class="flex justify-center p-8"><mat-spinner diameter="40" /></div>
      } @else if (samples().length === 0) {
        <div class="flex flex-col items-center p-12 text-gray-400">
          <mat-icon class="text-6xl mb-4">check_circle_outline</mat-icon>
          <p class="text-lg">No samples pending collection</p>
        </div>
      } @else {
        <table mat-table [dataSource]="samples()" class="w-full">
          <ng-container matColumnDef="barcode">
            <th mat-header-cell *matHeaderCellDef>Barcode</th>
            <td mat-cell *matCellDef="let row" class="font-mono text-blue-600">{{ row.sampleBarcode }}</td>
          </ng-container>
          <ng-container matColumnDef="orderId">
            <th mat-header-cell *matHeaderCellDef>Order ID</th>
            <td mat-cell *matCellDef="let row" class="font-mono">{{ row.orderId }}</td>
          </ng-container>
          <ng-container matColumnDef="patientId">
            <th mat-header-cell *matHeaderCellDef>Patient ID</th>
            <td mat-cell *matCellDef="let row" class="font-mono">{{ row.patientId }}</td>
          </ng-container>
          <ng-container matColumnDef="tubeType">
            <th mat-header-cell *matHeaderCellDef>Tube Type</th>
            <td mat-cell *matCellDef="let row">{{ row.tubeType }}</td>
          </ng-container>
          <ng-container matColumnDef="collectedAt">
            <th mat-header-cell *matHeaderCellDef>Collected At</th>
            <td mat-cell *matCellDef="let row">{{ fmtDate(row.collectedAt) }}</td>
          </ng-container>
          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef>Actions</th>
            <td mat-cell *matCellDef="let row">
              <a mat-raised-button color="primary" [routerLink]="['/samples', row.id, 'receive']">
                <mat-icon>inbox</mat-icon> Receive
              </a>
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
export class PendingCollectionComponent implements OnInit {
  private readonly sampleService = inject(SampleService);

  samples = signal<Sample[]>([]);
  loading = signal(false);
  totalElements = signal(0);

  readonly displayedColumns = ['barcode', 'orderId', 'patientId', 'tubeType', 'collectedAt', 'actions'];

  ngOnInit(): void { this.load(); }

  load(page = 0): void {
    this.loading.set(true);
    this.sampleService.getByStatus('COLLECTED', page).subscribe({
      next: res => { this.samples.set(res.data.data ?? []); this.totalElements.set(res.data.totalElements ?? 0); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  fmtDate(iso: string): string { return iso ? iso.slice(0, 16).replace('T', ' ') : '—'; }

  onPageChange(event: PageEvent): void { this.load(event.pageIndex); }
}
