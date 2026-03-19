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
import { DatePipe, SlicePipe } from '@angular/common';
import { OrderService } from '../../services/order.service';
import { TestOrder, OrderStatus } from '../../models/order.model';
import { NotificationService } from '@core/services/notification.service';

const STATUS_FILTERS: { label: string; value: OrderStatus | 'ALL' }[] = [
  { label: 'All', value: 'ALL' },
  { label: 'Draft', value: 'DRAFT' },
  { label: 'Placed', value: 'PLACED' },
  { label: 'Paid', value: 'PAID' },
  { label: 'Sample Collected', value: 'SAMPLE_COLLECTED' },
  { label: 'In Progress', value: 'IN_PROGRESS' },
  { label: 'Resulted', value: 'RESULTED' },
  { label: 'Authorised', value: 'AUTHORISED' },
  { label: 'Completed', value: 'COMPLETED' },
  { label: 'Cancelled', value: 'CANCELLED' },
];

@Component({
  selector: 'app-order-list',
  standalone: true,
  imports: [
    RouterLink, MatTableModule, MatPaginatorModule, MatFormFieldModule,
    MatInputModule, MatButtonModule, MatIconModule, MatChipsModule,
    MatProgressSpinnerModule, MatTooltipModule, DatePipe, SlicePipe,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Test Orders</h2>
        <p class="text-gray-500 text-sm">Manage laboratory test orders</p>
      </div>
      <a mat-raised-button color="primary" routerLink="new">
        <mat-icon>add_circle</mat-icon> New Order
      </a>
    </div>

    <div class="bg-white rounded-lg shadow">
      <div class="flex flex-wrap gap-4 p-4 border-b">
        <mat-form-field appearance="outline" class="flex-1 min-w-[200px]">
          <mat-label>Search orders</mat-label>
          <input matInput placeholder="Search by order number..." (input)="onSearch($event)" />
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
        <table mat-table [dataSource]="orders()" class="w-full">
          <ng-container matColumnDef="orderNumber">
            <th mat-header-cell *matHeaderCellDef>Order #</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ row.orderNumber }}</td>
          </ng-container>
          <ng-container matColumnDef="patientId">
            <th mat-header-cell *matHeaderCellDef>Patient</th>
            <td mat-cell *matCellDef="let row" class="font-mono text-xs">{{ row.patientId | slice:0:8 }}…</td>
          </ng-container>
          <ng-container matColumnDef="priority">
            <th mat-header-cell *matHeaderCellDef>Priority</th>
            <td mat-cell *matCellDef="let row">
              <span [class]="getPriorityClass(row.priority)">{{ row.priority }}</span>
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
          <ng-container matColumnDef="orderDate">
            <th mat-header-cell *matHeaderCellDef>Order Date</th>
            <td mat-cell *matCellDef="let row">{{ row.orderDate | date:'medium' }}</td>
          </ng-container>
          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef>Actions</th>
            <td mat-cell *matCellDef="let row">
              <a mat-icon-button [routerLink]="[row.id]" matTooltip="View">
                <mat-icon>visibility</mat-icon>
              </a>
              @if (row.status === 'DRAFT') {
                <button mat-icon-button matTooltip="Place Order" (click)="onPlaceOrder(row)">
                  <mat-icon>send</mat-icon>
                </button>
              }
              @if (row.status === 'DRAFT' || row.status === 'PLACED') {
                <button mat-icon-button matTooltip="Cancel Order" color="warn" (click)="onCancelOrder(row)">
                  <mat-icon>cancel</mat-icon>
                </button>
              }
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
        </table>

        @if (orders().length === 0) {
          <div class="text-center py-8 text-gray-400">
            <mat-icon class="!text-5xl mb-2">assignment</mat-icon>
            <p>No orders found</p>
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
export class OrderListComponent implements OnInit {
  private readonly orderService = inject(OrderService);
  private readonly notification = inject(NotificationService);

  readonly orders = signal<TestOrder[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(20);
  readonly currentPage = signal(0);
  readonly searchTerm = signal('');
  readonly activeStatus = signal<OrderStatus | 'ALL'>('ALL');

  readonly displayedColumns = ['orderNumber', 'patientId', 'priority', 'status', 'orderDate', 'actions'];
  readonly statusFilters = STATUS_FILTERS;

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading.set(true);
    const status = this.activeStatus();
    const request$ = status === 'ALL'
      ? this.orderService.getAll(this.currentPage(), this.pageSize())
      : this.orderService.getByStatus(status, this.currentPage(), this.pageSize());

    request$.subscribe({
      next: (response) => {
        let data = response.data;
        const term = this.searchTerm().toLowerCase();
        if (term) {
          data = data.filter(o => o.orderNumber?.toLowerCase().includes(term));
        }
        this.orders.set(data);
        this.totalElements.set(response.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  onSearch(event: Event): void {
    this.searchTerm.set((event.target as HTMLInputElement).value);
    this.currentPage.set(0);
    this.loadOrders();
  }

  onStatusFilter(status: OrderStatus | 'ALL'): void {
    this.activeStatus.set(status);
    this.currentPage.set(0);
    this.loadOrders();
  }

  onPage(event: PageEvent): void {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadOrders();
  }

  onPlaceOrder(order: TestOrder): void {
    this.orderService.placeOrder(order.id).subscribe({
      next: () => {
        this.notification.showSuccess('Order placed successfully');
        this.loadOrders();
      },
      error: () => this.notification.showError('Failed to place order'),
    });
  }

  onCancelOrder(order: TestOrder): void {
    this.orderService.cancelOrder(order.id, 'Cancelled by user').subscribe({
      next: () => {
        this.notification.showSuccess('Order cancelled successfully');
        this.loadOrders();
      },
      error: () => this.notification.showError('Failed to cancel order'),
    });
  }

  getStatusClass(status: string): string {
    const classes: Record<string, string> = {
      DRAFT: 'bg-gray-100 text-gray-800',
      PLACED: 'bg-blue-100 text-blue-800',
      PAID: 'bg-indigo-100 text-indigo-800',
      SAMPLE_COLLECTED: 'bg-purple-100 text-purple-800',
      IN_PROGRESS: 'bg-yellow-100 text-yellow-800',
      RESULTED: 'bg-orange-100 text-orange-800',
      AUTHORISED: 'bg-teal-100 text-teal-800',
      COMPLETED: 'bg-green-100 text-green-800',
      CANCELLED: 'bg-red-100 text-red-800',
    };
    return classes[status] ?? 'bg-gray-100 text-gray-800';
  }

  getPriorityClass(priority: string): string {
    const classes: Record<string, string> = {
      ROUTINE: 'text-gray-600',
      STAT: 'text-red-600 font-bold',
      URGENT: 'text-orange-600 font-semibold',
    };
    return classes[priority] ?? 'text-gray-600';
  }

  formatStatus(status: string): string {
    return status.replace(/_/g, ' ');
  }
}
