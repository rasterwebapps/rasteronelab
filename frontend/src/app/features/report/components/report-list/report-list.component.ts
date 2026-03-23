import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ReportService } from '../../report.service';
import { LabReport, ReportStatus, REPORT_STATUS_COLORS } from '../../report.model';
import { NotificationService } from '@core/services/notification.service';

@Component({
  selector: 'app-report-list',
  standalone: true,
  imports: [
    DatePipe,
    MatTableModule, MatPaginatorModule, MatFormFieldModule,
    MatInputModule, MatButtonModule, MatButtonToggleModule, MatIconModule,
    MatChipsModule, MatProgressSpinnerModule, MatTooltipModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Lab Reports</h2>
        <p class="text-gray-500 text-sm">Manage generated, signed, and delivered reports</p>
      </div>
    </div>

    <div class="bg-white rounded-lg shadow">
      <div class="flex flex-wrap gap-4 p-4 border-b items-center">
        <mat-form-field appearance="outline" class="flex-1 min-w-[200px]">
          <mat-label>Search reports</mat-label>
          <input matInput placeholder="Search by report number or patient name..." (input)="onSearch($event)" />
          <mat-icon matSuffix>search</mat-icon>
        </mat-form-field>

        <mat-button-toggle-group [value]="statusFilter()" (change)="onStatusFilter($event.value)" appearance="standard">
          <mat-button-toggle value="ALL">All</mat-button-toggle>
          <mat-button-toggle value="GENERATED">Generated</mat-button-toggle>
          <mat-button-toggle value="SIGNED">Signed</mat-button-toggle>
          <mat-button-toggle value="DELIVERED">Delivered</mat-button-toggle>
          <mat-button-toggle value="AMENDED">Amended</mat-button-toggle>
        </mat-button-toggle-group>
      </div>

      @if (loading()) {
        <div class="flex justify-center p-8">
          <mat-spinner diameter="40" />
        </div>
      } @else {
        <table mat-table [dataSource]="filteredReports()" class="w-full">
          <ng-container matColumnDef="reportNumber">
            <th mat-header-cell *matHeaderCellDef>Report #</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ row.reportNumber }}</td>
          </ng-container>

          <ng-container matColumnDef="patientName">
            <th mat-header-cell *matHeaderCellDef>Patient</th>
            <td mat-cell *matCellDef="let row">{{ row.patientName }}</td>
          </ng-container>

          <ng-container matColumnDef="departmentName">
            <th mat-header-cell *matHeaderCellDef>Department</th>
            <td mat-cell *matCellDef="let row">{{ row.departmentName ?? '-' }}</td>
          </ng-container>

          <ng-container matColumnDef="reportType">
            <th mat-header-cell *matHeaderCellDef>Type</th>
            <td mat-cell *matCellDef="let row">{{ row.reportType }}</td>
          </ng-container>

          <ng-container matColumnDef="reportStatus">
            <th mat-header-cell *matHeaderCellDef>Status</th>
            <td mat-cell *matCellDef="let row">
              <span class="px-2 py-1 rounded-full text-xs font-medium" [class]="getStatusClass(row.reportStatus)">
                {{ row.reportStatus }}
              </span>
            </td>
          </ng-container>

          <ng-container matColumnDef="generatedAt">
            <th mat-header-cell *matHeaderCellDef>Generated At</th>
            <td mat-cell *matCellDef="let row">
              {{ row.generatedAt ? (row.generatedAt | date:'dd MMM yyyy') : '-' }}
            </td>
          </ng-container>

          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef>Actions</th>
            <td mat-cell *matCellDef="let row">
              @if (row.reportStatus === 'GENERATED') {
                <button mat-icon-button matTooltip="Sign Report" (click)="onSign(row)">
                  <mat-icon>edit_note</mat-icon>
                </button>
              }
              @if (row.reportStatus === 'SIGNED') {
                <button mat-icon-button matTooltip="Deliver Report" (click)="onDeliver(row)">
                  <mat-icon>send</mat-icon>
                </button>
              }
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
        </table>

        @if (filteredReports().length === 0) {
          <div class="text-center py-8 text-gray-400">
            <mat-icon class="!text-5xl mb-2">description</mat-icon>
            <p>No reports found</p>
          </div>
        }
      }

      <mat-paginator
        [length]="totalElements()"
        [pageSize]="pageSize()"
        [pageSizeOptions]="[10, 25, 50]"
        (page)="onPage($event)"
        showFirstLastButtons
      />
    </div>
  `,
})
export class ReportListComponent implements OnInit {
  private readonly reportService = inject(ReportService);
  private readonly notif = inject(NotificationService);

  readonly reports = signal<LabReport[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(20);
  readonly currentPage = signal(0);
  readonly searchTerm = signal('');
  readonly statusFilter = signal<string>('ALL');

  readonly displayedColumns = [
    'reportNumber', 'patientName', 'departmentName', 'reportType',
    'reportStatus', 'generatedAt', 'actions',
  ];

  ngOnInit(): void {
    this.loadReports();
  }

  loadReports(): void {
    this.loading.set(true);
    this.reportService.getAll(this.currentPage(), this.pageSize()).subscribe({
      next: (response) => {
        this.reports.set(response.data);
        this.totalElements.set(response.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  filteredReports(): LabReport[] {
    let data = this.reports();
    const term = this.searchTerm().toLowerCase();
    if (term) {
      data = data.filter(r =>
        r.reportNumber.toLowerCase().includes(term) ||
        r.patientName.toLowerCase().includes(term),
      );
    }
    const status = this.statusFilter();
    if (status !== 'ALL') {
      data = data.filter(r => r.reportStatus === status);
    }
    return data;
  }

  onSearch(event: Event): void {
    this.searchTerm.set((event.target as HTMLInputElement).value);
  }

  onStatusFilter(value: string): void {
    this.statusFilter.set(value);
  }

  onPage(event: PageEvent): void {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadReports();
  }

  onSign(report: LabReport): void {
    const signedBy = prompt('Enter your name to sign this report:');
    if (!signedBy?.trim()) return;
    this.reportService.sign(report.id, { signedBy: signedBy.trim() }).subscribe({
      next: () => {
        this.notif.showSuccess(`Report ${report.reportNumber} signed successfully`);
        this.loadReports();
      },
      error: () => this.notif.showError('Failed to sign report'),
    });
  }

  onDeliver(report: LabReport): void {
    const channel = prompt('Enter delivery channel (EMAIL / SMS / PRINT):');
    if (!channel?.trim()) return;
    this.reportService.deliver(report.id, { deliveryChannel: channel.trim().toUpperCase() }).subscribe({
      next: () => {
        this.notif.showSuccess(`Report ${report.reportNumber} delivered successfully`);
        this.loadReports();
      },
      error: () => this.notif.showError('Failed to deliver report'),
    });
  }

  getStatusClass(status: ReportStatus): string {
    return REPORT_STATUS_COLORS[status] ?? 'bg-gray-100 text-gray-700';
  }
}
