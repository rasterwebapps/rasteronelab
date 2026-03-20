import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CurrencyPipe, DatePipe, SlicePipe } from '@angular/common';
import { BillingService } from '../../services/billing.service';
import { Invoice, InvoiceStatus } from '../../models/billing.model';
import { NotificationService } from '@core/services/notification.service';

const STATUS_FILTERS: { label: string; value: InvoiceStatus | 'ALL' }[] = [
  { label: 'All', value: 'ALL' },
  { label: 'Draft', value: 'DRAFT' },
  { label: 'Generated', value: 'GENERATED' },
  { label: 'Partially Paid', value: 'PARTIALLY_PAID' },
  { label: 'Paid', value: 'PAID' },
  { label: 'Refunded', value: 'REFUNDED' },
  { label: 'Cancelled', value: 'CANCELLED' },
];

@Component({
  selector: 'app-invoice-list',
  standalone: true,
  imports: [
    RouterLink, MatTableModule, MatPaginatorModule, MatFormFieldModule,
    MatInputModule, MatButtonModule, MatIconModule, MatChipsModule,
    MatProgressSpinnerModule, MatTooltipModule, CurrencyPipe, DatePipe, SlicePipe,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Invoices</h2>
        <p class="text-gray-500 text-sm">Manage billing invoices and payments</p>
      </div>
      <a mat-raised-button color="primary" routerLink="generate">
        <mat-icon>add_circle</mat-icon> Generate Invoice
      </a>
    </div>

    <div class="bg-white rounded-lg shadow">
      <div class="flex flex-wrap gap-4 p-4 border-b">
        <mat-form-field appearance="outline" class="flex-1 min-w-[200px]">
          <mat-label>Search invoices</mat-label>
          <input matInput placeholder="Search by invoice number..." (input)="onSearch($event)" />
          <mat-icon matSuffix>search</mat-icon>
        </mat-form-field>
      </div>

      <div class="flex flex-wrap gap-2 px-4 py-3 border-b">
        @for (filter of statusFilters; track filter.value) {
          <button mat-stroked-button
            [color]="activeStatus() === filter.value ? 'primary' : undefined"
            (click)="onStatusFilter(filter.value)">
            {{ filter.label }}
          </button>
        }
      </div>

      @if (loading()) {
        <div class="flex justify-center p-8">
          <mat-spinner diameter="40" />
        </div>
      } @else {
        <table mat-table [dataSource]="invoices()" class="w-full">
          <ng-container matColumnDef="invoiceNumber">
            <th mat-header-cell *matHeaderCellDef>Invoice #</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ row.invoiceNumber }}</td>
          </ng-container>
          <ng-container matColumnDef="patientId">
            <th mat-header-cell *matHeaderCellDef>Patient</th>
            <td mat-cell *matCellDef="let row" class="font-mono text-xs">{{ row.patientId | slice:0:8 }}…</td>
          </ng-container>
          <ng-container matColumnDef="totalAmount">
            <th mat-header-cell *matHeaderCellDef>Total</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ row.totalAmount | currency:'INR' }}</td>
          </ng-container>
          <ng-container matColumnDef="paidAmount">
            <th mat-header-cell *matHeaderCellDef>Paid</th>
            <td mat-cell *matCellDef="let row" class="text-green-700">{{ row.paidAmount | currency:'INR' }}</td>
          </ng-container>
          <ng-container matColumnDef="balanceAmount">
            <th mat-header-cell *matHeaderCellDef>Balance</th>
            <td mat-cell *matCellDef="let row" [class]="row.balanceAmount > 0 ? 'text-red-600 font-medium' : 'text-gray-500'">
              {{ row.balanceAmount | currency:'INR' }}
            </td>
          </ng-container>
          <ng-container matColumnDef="status">
            <th mat-header-cell *matHeaderCellDef>Status</th>
            <td mat-cell *matCellDef="let row">
              <span class="inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium"
                [class]="getStatusClass(row.status)">
                {{ formatStatus(row.status) }}
              </span>
            </td>
          </ng-container>
          <ng-container matColumnDef="invoiceDate">
            <th mat-header-cell *matHeaderCellDef>Date</th>
            <td mat-cell *matCellDef="let row">{{ row.invoiceDate | date:'mediumDate' }}</td>
          </ng-container>
          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef>Actions</th>
            <td mat-cell *matCellDef="let row">
              <a mat-icon-button [routerLink]="[row.id]" matTooltip="View Details">
                <mat-icon>visibility</mat-icon>
              </a>
              @if (row.status !== 'PAID' && row.status !== 'CANCELLED' && row.status !== 'REFUNDED') {
                <a mat-icon-button [routerLink]="[row.id, 'pay']" matTooltip="Record Payment" color="primary">
                  <mat-icon>payment</mat-icon>
                </a>
              }
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
        </table>

        @if (invoices().length === 0) {
          <div class="text-center py-8 text-gray-400">
            <mat-icon class="!text-5xl mb-2">receipt_long</mat-icon>
            <p>No invoices found</p>
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
export class InvoiceListComponent implements OnInit {
  private readonly billingService = inject(BillingService);
  private readonly notification = inject(NotificationService);

  readonly invoices = signal<Invoice[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(20);
  readonly currentPage = signal(0);
  readonly searchTerm = signal('');
  readonly activeStatus = signal<InvoiceStatus | 'ALL'>('ALL');

  readonly displayedColumns = ['invoiceNumber', 'patientId', 'totalAmount', 'paidAmount', 'balanceAmount', 'status', 'invoiceDate', 'actions'];
  readonly statusFilters = STATUS_FILTERS;

  ngOnInit(): void {
    this.loadInvoices();
  }

  loadInvoices(): void {
    this.loading.set(true);
    const status = this.activeStatus();
    const request$ = status === 'ALL'
      ? this.billingService.getInvoices(this.currentPage(), this.pageSize())
      : this.billingService.getInvoicesByStatus(status, this.currentPage(), this.pageSize());

    request$.subscribe({
      next: (response) => {
        let data = response.data;
        const term = this.searchTerm().toLowerCase();
        if (term) {
          data = data.filter(inv => inv.invoiceNumber?.toLowerCase().includes(term));
        }
        this.invoices.set(data);
        this.totalElements.set(response.totalElements);
        this.loading.set(false);
      },
      error: () => {
        this.notification.showError('Failed to load invoices');
        this.loading.set(false);
      },
    });
  }

  onSearch(event: Event): void {
    this.searchTerm.set((event.target as HTMLInputElement).value);
    this.currentPage.set(0);
    this.loadInvoices();
  }

  onStatusFilter(status: InvoiceStatus | 'ALL'): void {
    this.activeStatus.set(status);
    this.currentPage.set(0);
    this.loadInvoices();
  }

  onPage(event: PageEvent): void {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadInvoices();
  }

  getStatusClass(status: string): string {
    const classes: Record<string, string> = {
      DRAFT: 'bg-gray-100 text-gray-800',
      GENERATED: 'bg-blue-100 text-blue-800',
      PARTIALLY_PAID: 'bg-yellow-100 text-yellow-800',
      PAID: 'bg-green-100 text-green-800',
      REFUNDED: 'bg-purple-100 text-purple-800',
      CANCELLED: 'bg-red-100 text-red-800',
    };
    return classes[status] ?? 'bg-gray-100 text-gray-800';
  }

  formatStatus(status: string): string {
    return status.replace(/_/g, ' ');
  }
}
