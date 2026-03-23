import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatCardModule } from '@angular/material/card';
import { ReportService } from '../../report.service';
import { LabReport, ReportStatus, REPORT_STATUS_COLORS } from '../../report.model';
import { NotificationService } from '@core/services/notification.service';

@Component({
  selector: 'app-report-list',
  standalone: true,
  imports: [
    ReactiveFormsModule, DatePipe,
    MatTableModule, MatPaginatorModule, MatFormFieldModule,
    MatInputModule, MatSelectModule, MatButtonModule, MatButtonToggleModule,
    MatIconModule, MatChipsModule, MatProgressSpinnerModule, MatTooltipModule, MatCardModule,
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

    <!-- Sign Form -->
    @if (showSignForm() && selectedReport()) {
      <mat-card class="mt-4 border border-purple-200">
        <mat-card-header>
          <mat-card-title>Sign Report — {{ selectedReport()!.reportNumber }}</mat-card-title>
        </mat-card-header>
        <mat-card-content class="mt-4">
          <form [formGroup]="signForm" (ngSubmit)="submitSign()">
            <mat-form-field appearance="outline" class="w-full mb-3">
              <mat-label>Signed By</mat-label>
              <input matInput formControlName="signedBy" placeholder="Enter your full name" />
              @if (signForm.controls.signedBy.hasError('required')) {
                <mat-error>Signatory name is required</mat-error>
              }
            </mat-form-field>
            <mat-form-field appearance="outline" class="w-full">
              <mat-label>Notes (optional)</mat-label>
              <textarea matInput formControlName="notes" rows="2"
                placeholder="Any additional sign-off notes"></textarea>
            </mat-form-field>
            <div class="flex justify-end gap-3 mt-2">
              <button mat-button type="button" (click)="cancelAction()">Cancel</button>
              <button mat-raised-button color="primary" type="submit"
                [disabled]="signForm.invalid || actionLoading()">
                <mat-icon>edit_note</mat-icon> Sign Report
              </button>
            </div>
          </form>
        </mat-card-content>
      </mat-card>
    }

    <!-- Deliver Form -->
    @if (showDeliverForm() && selectedReport()) {
      <mat-card class="mt-4 border border-green-200">
        <mat-card-header>
          <mat-card-title>Deliver Report — {{ selectedReport()!.reportNumber }}</mat-card-title>
        </mat-card-header>
        <mat-card-content class="mt-4">
          <form [formGroup]="deliverForm" (ngSubmit)="submitDeliver()">
            <mat-form-field appearance="outline" class="w-full mb-3">
              <mat-label>Delivery Channel</mat-label>
              <mat-select formControlName="deliveryChannel">
                <mat-option value="EMAIL">Email</mat-option>
                <mat-option value="SMS">SMS</mat-option>
                <mat-option value="PRINT">Print</mat-option>
                <mat-option value="PORTAL">Patient Portal</mat-option>
              </mat-select>
              @if (deliverForm.controls.deliveryChannel.hasError('required')) {
                <mat-error>Delivery channel is required</mat-error>
              }
            </mat-form-field>
            @if (deliverForm.controls.deliveryChannel.value === 'EMAIL') {
              <mat-form-field appearance="outline" class="w-full mb-3">
                <mat-label>Recipient Email</mat-label>
                <input matInput formControlName="recipientEmail" type="email"
                  placeholder="patient@example.com" />
              </mat-form-field>
            }
            @if (deliverForm.controls.deliveryChannel.value === 'SMS') {
              <mat-form-field appearance="outline" class="w-full mb-3">
                <mat-label>Recipient Phone</mat-label>
                <input matInput formControlName="recipientPhone"
                  placeholder="+91 98765 43210" />
              </mat-form-field>
            }
            <div class="flex justify-end gap-3 mt-2">
              <button mat-button type="button" (click)="cancelAction()">Cancel</button>
              <button mat-raised-button color="primary" type="submit"
                [disabled]="deliverForm.invalid || actionLoading()">
                <mat-icon>send</mat-icon> Deliver Report
              </button>
            </div>
          </form>
        </mat-card-content>
      </mat-card>
    }
  `,
})
export class ReportListComponent implements OnInit {
  private readonly reportService = inject(ReportService);
  private readonly notif = inject(NotificationService);
  private readonly fb = inject(FormBuilder);

  readonly reports = signal<LabReport[]>([]);
  readonly loading = signal(false);
  readonly actionLoading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(20);
  readonly currentPage = signal(0);
  readonly searchTerm = signal('');
  readonly statusFilter = signal<string>('ALL');
  readonly showSignForm = signal(false);
  readonly showDeliverForm = signal(false);
  readonly selectedReport = signal<LabReport | null>(null);

  readonly displayedColumns = [
    'reportNumber', 'patientName', 'departmentName', 'reportType',
    'reportStatus', 'generatedAt', 'actions',
  ];

  readonly signForm = this.fb.nonNullable.group({
    signedBy: ['', Validators.required],
    notes: [''],
  });

  readonly deliverForm = this.fb.nonNullable.group({
    deliveryChannel: ['', Validators.required],
    recipientEmail: [''],
    recipientPhone: [''],
  });

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
    this.selectedReport.set(report);
    this.signForm.reset();
    this.showSignForm.set(true);
    this.showDeliverForm.set(false);
  }

  onDeliver(report: LabReport): void {
    this.selectedReport.set(report);
    this.deliverForm.reset();
    this.showDeliverForm.set(true);
    this.showSignForm.set(false);
  }

  cancelAction(): void {
    this.showSignForm.set(false);
    this.showDeliverForm.set(false);
    this.selectedReport.set(null);
  }

  submitSign(): void {
    if (this.signForm.invalid || !this.selectedReport()) return;
    const { signedBy, notes } = this.signForm.getRawValue();
    this.actionLoading.set(true);
    this.reportService.sign(this.selectedReport()!.id, { signedBy, notes: notes || undefined }).subscribe({
      next: () => {
        this.notif.showSuccess(`Report ${this.selectedReport()!.reportNumber} signed successfully`);
        this.cancelAction();
        this.loadReports();
        this.actionLoading.set(false);
      },
      error: () => {
        this.notif.showError('Failed to sign report');
        this.actionLoading.set(false);
      },
    });
  }

  submitDeliver(): void {
    if (this.deliverForm.invalid || !this.selectedReport()) return;
    const { deliveryChannel, recipientEmail, recipientPhone } = this.deliverForm.getRawValue();
    this.actionLoading.set(true);
    this.reportService.deliver(this.selectedReport()!.id, {
      deliveryChannel,
      recipientEmail: recipientEmail || undefined,
      recipientPhone: recipientPhone || undefined,
    }).subscribe({
      next: () => {
        this.notif.showSuccess(`Report ${this.selectedReport()!.reportNumber} delivered via ${deliveryChannel}`);
        this.cancelAction();
        this.loadReports();
        this.actionLoading.set(false);
      },
      error: () => {
        this.notif.showError('Failed to deliver report');
        this.actionLoading.set(false);
      },
    });
  }

  getStatusClass(status: ReportStatus): string {
    return REPORT_STATUS_COLORS[status] ?? 'bg-gray-100 text-gray-700';
  }
}
