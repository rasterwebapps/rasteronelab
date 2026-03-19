import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { BillingService } from '../../services/billing.service';
import { Invoice, Payment } from '../../models/billing.model';
import { NotificationService } from '@core/services/notification.service';

@Component({
  selector: 'app-invoice-detail',
  standalone: true,
  imports: [
    RouterLink, MatCardModule, MatButtonModule, MatIconModule,
    MatChipsModule, MatDividerModule, MatProgressSpinnerModule,
    MatTableModule, CurrencyPipe, DatePipe,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (loading()) {
      <div class="flex justify-center p-8">
        <mat-spinner diameter="40" />
      </div>
    } @else if (invoice()) {
      <div class="flex items-center justify-between mb-6">
        <div>
          <h2 class="text-xl font-bold text-gray-800">Invoice {{ invoice()!.invoiceNumber }}</h2>
          <p class="text-gray-500 text-sm">Created on {{ invoice()!.invoiceDate | date:'mediumDate' }}</p>
        </div>
        <div class="flex gap-2">
          <a mat-button routerLink="/billing">
            <mat-icon>arrow_back</mat-icon> Back to List
          </a>
          @if (invoice()!.status !== 'PAID' && invoice()!.status !== 'CANCELLED' && invoice()!.status !== 'REFUNDED') {
            <a mat-raised-button color="primary" [routerLink]="['/billing', invoice()!.id, 'pay']">
              <mat-icon>payment</mat-icon> Record Payment
            </a>
          }
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
        <!-- Invoice Summary -->
        <mat-card>
          <mat-card-header>
            <mat-card-title>Invoice Summary</mat-card-title>
          </mat-card-header>
          <mat-card-content class="grid grid-cols-2 gap-y-3 mt-4">
            <div class="text-gray-500 text-sm">Invoice Number</div>
            <div class="font-medium">{{ invoice()!.invoiceNumber }}</div>
            <div class="text-gray-500 text-sm">Status</div>
            <div>
              <span class="inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium"
                [class]="getStatusClass(invoice()!.status)">
                {{ formatStatus(invoice()!.status) }}
              </span>
            </div>
            <div class="text-gray-500 text-sm">Rate List Type</div>
            <div>{{ formatStatus(invoice()!.rateListType) }}</div>
            <div class="text-gray-500 text-sm">Order ID</div>
            <div class="font-mono text-sm">{{ invoice()!.orderId }}</div>
            <div class="text-gray-500 text-sm">Patient ID</div>
            <div class="font-mono text-sm">{{ invoice()!.patientId }}</div>
            <div class="text-gray-500 text-sm">Invoice Date</div>
            <div>{{ invoice()!.invoiceDate | date:'mediumDate' }}</div>
            @if (invoice()!.dueDate) {
              <div class="text-gray-500 text-sm">Due Date</div>
              <div>{{ invoice()!.dueDate | date:'mediumDate' }}</div>
            }
            @if (invoice()!.notes) {
              <div class="text-gray-500 text-sm">Notes</div>
              <div>{{ invoice()!.notes }}</div>
            }
          </mat-card-content>
        </mat-card>

        <!-- Financial Summary -->
        <mat-card>
          <mat-card-header>
            <mat-card-title>Financial Summary</mat-card-title>
          </mat-card-header>
          <mat-card-content class="mt-4">
            <div class="space-y-3">
              <div class="flex justify-between">
                <span class="text-gray-500">Subtotal</span>
                <span class="font-medium">{{ invoice()!.subtotal | currency:'INR' }}</span>
              </div>
              @if (invoice()!.discountAmount > 0) {
                <div class="flex justify-between text-orange-600">
                  <span>Discount
                    @if (invoice()!.discountType) {
                      ({{ invoice()!.discountType }})
                    }
                  </span>
                  <span>- {{ invoice()!.discountAmount | currency:'INR' }}</span>
                </div>
                @if (invoice()!.discountReason) {
                  <div class="text-xs text-gray-400 pl-2">{{ invoice()!.discountReason }}</div>
                }
              }
              @if (invoice()!.taxAmount > 0) {
                <div class="flex justify-between">
                  <span class="text-gray-500">Tax</span>
                  <span>{{ invoice()!.taxAmount | currency:'INR' }}</span>
                </div>
              }
              <mat-divider />
              <div class="flex justify-between text-lg font-bold">
                <span>Total</span>
                <span>{{ invoice()!.totalAmount | currency:'INR' }}</span>
              </div>
              <mat-divider />
              <div class="flex justify-between text-green-700">
                <span>Paid</span>
                <span>{{ invoice()!.paidAmount | currency:'INR' }}</span>
              </div>
              <div class="flex justify-between" [class]="invoice()!.balanceAmount > 0 ? 'text-red-600 font-bold' : 'text-gray-500'">
                <span>Balance</span>
                <span>{{ invoice()!.balanceAmount | currency:'INR' }}</span>
              </div>
            </div>
          </mat-card-content>
        </mat-card>
      </div>

      <!-- Line Items -->
      <mat-card class="mb-6">
        <mat-card-header>
          <mat-card-title>Line Items ({{ invoice()!.lineItems?.length ?? 0 }})</mat-card-title>
        </mat-card-header>
        <mat-card-content class="mt-4">
          @if (invoice()!.lineItems && invoice()!.lineItems.length > 0) {
            <table mat-table [dataSource]="invoice()!.lineItems" class="w-full">
              <ng-container matColumnDef="testCode">
                <th mat-header-cell *matHeaderCellDef>Test Code</th>
                <td mat-cell *matCellDef="let row" class="font-medium">{{ row.testCode }}</td>
              </ng-container>
              <ng-container matColumnDef="testName">
                <th mat-header-cell *matHeaderCellDef>Test Name</th>
                <td mat-cell *matCellDef="let row">{{ row.testName }}</td>
              </ng-container>
              <ng-container matColumnDef="quantity">
                <th mat-header-cell *matHeaderCellDef>Qty</th>
                <td mat-cell *matCellDef="let row" class="text-center">{{ row.quantity }}</td>
              </ng-container>
              <ng-container matColumnDef="unitPrice">
                <th mat-header-cell *matHeaderCellDef>Unit Price</th>
                <td mat-cell *matCellDef="let row">{{ row.unitPrice | currency:'INR' }}</td>
              </ng-container>
              <ng-container matColumnDef="discount">
                <th mat-header-cell *matHeaderCellDef>Discount</th>
                <td mat-cell *matCellDef="let row">{{ row.discountAmount | currency:'INR' }}</td>
              </ng-container>
              <ng-container matColumnDef="netAmount">
                <th mat-header-cell *matHeaderCellDef>Net Amount</th>
                <td mat-cell *matCellDef="let row" class="font-medium">{{ row.netAmount | currency:'INR' }}</td>
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

      <!-- Payment History -->
      <mat-card>
        <mat-card-header>
          <mat-card-title>Payment History ({{ payments().length }})</mat-card-title>
        </mat-card-header>
        <mat-card-content class="mt-4">
          @if (payments().length > 0) {
            <table mat-table [dataSource]="payments()" class="w-full">
              <ng-container matColumnDef="receiptNumber">
                <th mat-header-cell *matHeaderCellDef>Receipt #</th>
                <td mat-cell *matCellDef="let row" class="font-medium">{{ row.receiptNumber }}</td>
              </ng-container>
              <ng-container matColumnDef="amount">
                <th mat-header-cell *matHeaderCellDef>Amount</th>
                <td mat-cell *matCellDef="let row" class="text-green-700 font-medium">{{ row.amount | currency:'INR' }}</td>
              </ng-container>
              <ng-container matColumnDef="paymentMethod">
                <th mat-header-cell *matHeaderCellDef>Method</th>
                <td mat-cell *matCellDef="let row">{{ row.paymentMethod }}</td>
              </ng-container>
              <ng-container matColumnDef="transactionRef">
                <th mat-header-cell *matHeaderCellDef>Transaction Ref</th>
                <td mat-cell *matCellDef="let row">{{ row.transactionRef ?? '—' }}</td>
              </ng-container>
              <ng-container matColumnDef="paymentDate">
                <th mat-header-cell *matHeaderCellDef>Date</th>
                <td mat-cell *matCellDef="let row">{{ row.paymentDate | date:'medium' }}</td>
              </ng-container>
              <ng-container matColumnDef="receivedBy">
                <th mat-header-cell *matHeaderCellDef>Received By</th>
                <td mat-cell *matCellDef="let row">{{ row.receivedBy ?? '—' }}</td>
              </ng-container>
              <tr mat-header-row *matHeaderRowDef="paymentColumns"></tr>
              <tr mat-row *matRowDef="let row; columns: paymentColumns"></tr>
            </table>
          } @else {
            <div class="text-center py-8 text-gray-400">
              <mat-icon class="!text-5xl mb-2">payments</mat-icon>
              <p>No payments recorded yet</p>
            </div>
          }
        </mat-card-content>
      </mat-card>
    }
  `,
})
export class InvoiceDetailComponent implements OnInit {
  private readonly billingService = inject(BillingService);
  private readonly route = inject(ActivatedRoute);
  private readonly notification = inject(NotificationService);

  readonly invoice = signal<Invoice | null>(null);
  readonly payments = signal<Payment[]>([]);
  readonly loading = signal(false);

  readonly lineItemColumns = ['testCode', 'testName', 'quantity', 'unitPrice', 'discount', 'netAmount'];
  readonly paymentColumns = ['receiptNumber', 'amount', 'paymentMethod', 'transactionRef', 'paymentDate', 'receivedBy'];

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadInvoice(id);
    }
  }

  private loadInvoice(id: string): void {
    this.loading.set(true);
    this.billingService.getInvoiceById(id).subscribe({
      next: (response) => {
        this.invoice.set(response.data);
        this.loading.set(false);
        this.loadPayments(id);
      },
      error: () => {
        this.notification.showError('Failed to load invoice');
        this.loading.set(false);
      },
    });
  }

  private loadPayments(invoiceId: string): void {
    this.billingService.getPaymentsByInvoice(invoiceId).subscribe({
      next: (response) => this.payments.set(response.data),
      error: () => this.notification.showError('Failed to load payment history'),
    });
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
