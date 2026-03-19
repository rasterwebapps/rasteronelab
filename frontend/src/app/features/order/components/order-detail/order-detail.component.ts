import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatStepperModule } from '@angular/material/stepper';
import { DatePipe, CurrencyPipe } from '@angular/common';
import { OrderService } from '../../services/order.service';
import { TestOrder, OrderStatus } from '../../models/order.model';
import { NotificationService } from '@core/services/notification.service';

const STATUS_STEPS: OrderStatus[] = [
  'DRAFT', 'PLACED', 'PAID', 'SAMPLE_COLLECTED', 'IN_PROGRESS', 'RESULTED', 'AUTHORISED', 'COMPLETED',
];

@Component({
  selector: 'app-order-detail',
  standalone: true,
  imports: [
    RouterLink, MatCardModule, MatButtonModule, MatIconModule,
    MatChipsModule, MatDividerModule, MatProgressSpinnerModule,
    MatTableModule, MatStepperModule, DatePipe, CurrencyPipe,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (loading()) {
      <div class="flex justify-center p-8">
        <mat-spinner diameter="40" />
      </div>
    } @else if (order()) {
      <div class="flex items-center justify-between mb-6">
        <div>
          <h2 class="text-xl font-bold text-gray-800">Order {{ order()!.orderNumber }}</h2>
          <p class="text-gray-500 text-sm">Placed on {{ order()!.orderDate | date:'medium' }}</p>
        </div>
        <div class="flex gap-2">
          <a mat-button routerLink="/order">
            <mat-icon>arrow_back</mat-icon> Back to List
          </a>
          @if (order()!.status === 'DRAFT') {
            <button mat-raised-button color="primary" (click)="onPlaceOrder()">
              <mat-icon>send</mat-icon> Place Order
            </button>
          }
          @if (order()!.status === 'DRAFT' || order()!.status === 'PLACED') {
            <button mat-raised-button color="warn" (click)="onCancelOrder()">
              <mat-icon>cancel</mat-icon> Cancel Order
            </button>
          }
        </div>
      </div>

      <!-- Status Timeline -->
      <mat-card class="mb-6">
        <mat-card-header>
          <mat-card-title>Order Progress</mat-card-title>
        </mat-card-header>
        <mat-card-content class="mt-4">
          @if (order()!.status === 'CANCELLED') {
            <div class="flex items-center gap-2 p-3 bg-red-50 rounded-lg">
              <mat-icon class="text-red-500">cancel</mat-icon>
              <span class="text-red-700 font-medium">Order Cancelled</span>
              @if (order()!.cancelReason) {
                <span class="text-red-500 ml-2">— {{ order()!.cancelReason }}</span>
              }
            </div>
          } @else {
            <div class="flex items-center gap-1 overflow-x-auto pb-2">
              @for (step of statusSteps; track step; let i = $index) {
                <div class="flex items-center">
                  <div class="flex flex-col items-center min-w-[80px]">
                    <div class="w-8 h-8 rounded-full flex items-center justify-center text-xs font-bold"
                      [class]="i <= currentStepIndex() ? 'bg-primary text-white bg-blue-600' : 'bg-gray-200 text-gray-500'">
                      @if (i < currentStepIndex()) {
                        <mat-icon class="!text-base">check</mat-icon>
                      } @else {
                        {{ i + 1 }}
                      }
                    </div>
                    <span class="text-xs mt-1 text-center" [class]="i <= currentStepIndex() ? 'text-blue-600 font-medium' : 'text-gray-400'">
                      {{ formatStatus(step) }}
                    </span>
                  </div>
                  @if (i < statusSteps.length - 1) {
                    <div class="w-8 h-0.5 mx-1" [class]="i < currentStepIndex() ? 'bg-blue-600' : 'bg-gray-200'"></div>
                  }
                </div>
              }
            </div>
          }
        </mat-card-content>
      </mat-card>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
        <!-- Order Summary -->
        <mat-card>
          <mat-card-header>
            <mat-card-title>Order Summary</mat-card-title>
          </mat-card-header>
          <mat-card-content class="grid grid-cols-2 gap-y-3 mt-4">
            <div class="text-gray-500 text-sm">Order Number</div>
            <div class="font-medium">{{ order()!.orderNumber }}</div>
            <div class="text-gray-500 text-sm">Status</div>
            <div>
              <span class="inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium"
                [class]="getStatusClass(order()!.status)">
                {{ formatStatus(order()!.status) }}
              </span>
            </div>
            <div class="text-gray-500 text-sm">Priority</div>
            <div [class]="getPriorityClass(order()!.priority)">{{ order()!.priority }}</div>
            <div class="text-gray-500 text-sm">Order Date</div>
            <div>{{ order()!.orderDate | date:'medium' }}</div>
            <div class="text-gray-500 text-sm">Barcode</div>
            <div>{{ order()!.barcode ?? '-' }}</div>
            @if (order()!.estimatedCompletionTime) {
              <div class="text-gray-500 text-sm">Est. Completion</div>
              <div>{{ order()!.estimatedCompletionTime | date:'medium' }}</div>
            }
            @if (order()!.completedAt) {
              <div class="text-gray-500 text-sm">Completed At</div>
              <div>{{ order()!.completedAt | date:'medium' }}</div>
            }
          </mat-card-content>
        </mat-card>

        <!-- Patient & Clinical Info -->
        <mat-card>
          <mat-card-header>
            <mat-card-title>Patient & Clinical Info</mat-card-title>
          </mat-card-header>
          <mat-card-content class="grid grid-cols-2 gap-y-3 mt-4">
            <div class="text-gray-500 text-sm">Patient ID</div>
            <div class="font-mono text-sm">{{ order()!.patientId }}</div>
            @if (order()!.visitId) {
              <div class="text-gray-500 text-sm">Visit ID</div>
              <div class="font-mono text-sm">{{ order()!.visitId }}</div>
            }
            @if (order()!.referringDoctorId) {
              <div class="text-gray-500 text-sm">Referring Doctor</div>
              <div class="font-mono text-sm">{{ order()!.referringDoctorId }}</div>
            }
            <div class="text-gray-500 text-sm">Clinical History</div>
            <div>{{ order()!.clinicalHistory ?? '-' }}</div>
            <div class="text-gray-500 text-sm">Special Instructions</div>
            <div>{{ order()!.specialInstructions ?? '-' }}</div>
          </mat-card-content>
        </mat-card>
      </div>

      <!-- Line Items -->
      <mat-card>
        <mat-card-header>
          <mat-card-title>Line Items ({{ order()!.lineItems?.length ?? 0 }})</mat-card-title>
        </mat-card-header>
        <mat-card-content class="mt-4">
          @if (order()!.lineItems && order()!.lineItems.length > 0) {
            <table mat-table [dataSource]="order()!.lineItems" class="w-full">
              <ng-container matColumnDef="testCode">
                <th mat-header-cell *matHeaderCellDef>Test Code</th>
                <td mat-cell *matCellDef="let row" class="font-medium">{{ row.testCode }}</td>
              </ng-container>
              <ng-container matColumnDef="testName">
                <th mat-header-cell *matHeaderCellDef>Test Name</th>
                <td mat-cell *matCellDef="let row">{{ row.testName }}</td>
              </ng-container>
              <ng-container matColumnDef="sampleType">
                <th mat-header-cell *matHeaderCellDef>Sample Type</th>
                <td mat-cell *matCellDef="let row">{{ row.sampleType ?? '-' }}</td>
              </ng-container>
              <ng-container matColumnDef="status">
                <th mat-header-cell *matHeaderCellDef>Status</th>
                <td mat-cell *matCellDef="let row">{{ row.status ?? '-' }}</td>
              </ng-container>
              <ng-container matColumnDef="unitPrice">
                <th mat-header-cell *matHeaderCellDef>Unit Price</th>
                <td mat-cell *matCellDef="let row">{{ row.unitPrice | currency:'INR' }}</td>
              </ng-container>
              <ng-container matColumnDef="discount">
                <th mat-header-cell *matHeaderCellDef>Discount</th>
                <td mat-cell *matCellDef="let row">{{ row.discountAmount | currency:'INR' }}</td>
              </ng-container>
              <ng-container matColumnDef="netPrice">
                <th mat-header-cell *matHeaderCellDef>Net Price</th>
                <td mat-cell *matCellDef="let row" class="font-medium">{{ row.netPrice | currency:'INR' }}</td>
              </ng-container>
              <ng-container matColumnDef="urgent">
                <th mat-header-cell *matHeaderCellDef>Urgent</th>
                <td mat-cell *matCellDef="let row">
                  @if (row.isUrgent) {
                    <mat-icon class="text-red-500 !text-lg">priority_high</mat-icon>
                  } @else {
                    <span class="text-gray-400">—</span>
                  }
                </td>
              </ng-container>
              <tr mat-header-row *matHeaderRowDef="lineItemColumns"></tr>
              <tr mat-row *matRowDef="let row; columns: lineItemColumns"></tr>
            </table>
          } @else {
            <div class="text-center py-8 text-gray-400">
              <mat-icon class="!text-5xl mb-2">science</mat-icon>
              <p>No line items</p>
            </div>
          }
        </mat-card-content>
      </mat-card>
    }
  `,
})
export class OrderDetailComponent implements OnInit {
  private readonly orderService = inject(OrderService);
  private readonly route = inject(ActivatedRoute);
  private readonly notification = inject(NotificationService);

  readonly order = signal<TestOrder | null>(null);
  readonly loading = signal(false);

  readonly lineItemColumns = ['testCode', 'testName', 'sampleType', 'status', 'unitPrice', 'discount', 'netPrice', 'urgent'];
  readonly statusSteps = STATUS_STEPS;

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadOrder(id);
    }
  }

  private loadOrder(id: string): void {
    this.loading.set(true);
    this.orderService.getById(id).subscribe({
      next: (response) => {
        this.order.set(response.data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  currentStepIndex(): number {
    const status = this.order()?.status;
    if (!status || status === 'CANCELLED') return -1;
    return STATUS_STEPS.indexOf(status);
  }

  onPlaceOrder(): void {
    const id = this.order()?.id;
    if (!id) return;
    this.orderService.placeOrder(id).subscribe({
      next: (response) => {
        this.order.set(response.data);
        this.notification.showSuccess('Order placed successfully');
      },
      error: () => this.notification.showError('Failed to place order'),
    });
  }

  onCancelOrder(): void {
    const id = this.order()?.id;
    if (!id) return;
    this.orderService.cancelOrder(id, 'Cancelled by user').subscribe({
      next: (response) => {
        this.order.set(response.data);
        this.notification.showSuccess('Order cancelled successfully');
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
