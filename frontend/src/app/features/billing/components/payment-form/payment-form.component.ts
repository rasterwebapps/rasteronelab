import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CurrencyPipe } from '@angular/common';
import { BillingService } from '../../services/billing.service';
import { NotificationService } from '@core/services/notification.service';
import { Invoice, PaymentMethod, PaymentRequest } from '../../models/billing.model';

const PAYMENT_METHODS: { label: string; value: PaymentMethod }[] = [
  { label: 'Cash', value: 'CASH' },
  { label: 'Card', value: 'CARD' },
  { label: 'UPI', value: 'UPI' },
  { label: 'Insurance', value: 'INSURANCE' },
  { label: 'Credit', value: 'CREDIT' },
  { label: 'Online', value: 'ONLINE' },
];

@Component({
  selector: 'app-payment-form',
  standalone: true,
  imports: [
    RouterLink, ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatButtonModule, MatIconModule, MatCardModule,
    MatProgressSpinnerModule, CurrencyPipe,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Record Payment</h2>
        @if (invoice()) {
          <p class="text-gray-500 text-sm">Invoice {{ invoice()!.invoiceNumber }} — Balance: {{ invoice()!.balanceAmount | currency:'INR' }}</p>
        }
      </div>
      <a mat-button [routerLink]="invoiceId ? ['/billing', invoiceId] : ['/billing']">
        <mat-icon>arrow_back</mat-icon> Back
      </a>
    </div>

    @if (loadingInvoice()) {
      <div class="flex justify-center p-8">
        <mat-spinner diameter="40" />
      </div>
    } @else {
      <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6 max-w-2xl">
        <mat-card>
          <mat-card-header>
            <mat-card-title>Payment Details</mat-card-title>
          </mat-card-header>
          <mat-card-content class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
            <mat-form-field appearance="outline" class="md:col-span-2">
              <mat-label>Amount</mat-label>
              <input matInput formControlName="amount" type="number" min="0" step="0.01" />
              @if (form.controls.amount.hasError('required')) {
                <mat-error>Amount is required</mat-error>
              }
              @if (form.controls.amount.hasError('min')) {
                <mat-error>Amount must be greater than 0</mat-error>
              }
              <span matTextPrefix>₹&nbsp;</span>
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>Payment Method</mat-label>
              <mat-select formControlName="paymentMethod">
                @for (method of paymentMethods; track method.value) {
                  <mat-option [value]="method.value">{{ method.label }}</mat-option>
                }
              </mat-select>
              @if (form.controls.paymentMethod.hasError('required')) {
                <mat-error>Payment method is required</mat-error>
              }
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>Transaction Reference</mat-label>
              <input matInput formControlName="transactionRef" />
            </mat-form-field>

            <mat-form-field appearance="outline" class="md:col-span-2">
              <mat-label>Notes</mat-label>
              <textarea matInput formControlName="notes" rows="3"></textarea>
            </mat-form-field>
          </mat-card-content>
        </mat-card>

        <div class="flex justify-end gap-3">
          <a mat-button [routerLink]="invoiceId ? ['/billing', invoiceId] : ['/billing']">Cancel</a>
          <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
            @if (saving()) {
              <mat-icon class="animate-spin">refresh</mat-icon>
            }
            Record Payment
          </button>
        </div>
      </form>
    }
  `,
})
export class PaymentFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly billingService = inject(BillingService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly invoice = signal<Invoice | null>(null);
  readonly loadingInvoice = signal(false);
  readonly saving = signal(false);
  readonly paymentMethods = PAYMENT_METHODS;
  invoiceId = '';

  readonly form = this.fb.nonNullable.group({
    amount: [0, [Validators.required, Validators.min(0.01)]],
    paymentMethod: ['CASH' as string, Validators.required],
    transactionRef: [''],
    notes: [''],
  });

  ngOnInit(): void {
    this.invoiceId = this.route.snapshot.paramMap.get('id') ?? '';
    if (this.invoiceId) {
      this.loadInvoice(this.invoiceId);
    }
  }

  private loadInvoice(id: string): void {
    this.loadingInvoice.set(true);
    this.billingService.getInvoiceById(id).subscribe({
      next: (response) => {
        this.invoice.set(response.data);
        this.form.patchValue({ amount: response.data.balanceAmount });
        this.loadingInvoice.set(false);
      },
      error: () => {
        this.notification.showError('Failed to load invoice');
        this.loadingInvoice.set(false);
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid || !this.invoiceId) return;
    this.saving.set(true);
    const raw = this.form.getRawValue();
    const request: PaymentRequest = {
      invoiceId: this.invoiceId,
      amount: raw.amount,
      paymentMethod: raw.paymentMethod,
      transactionRef: raw.transactionRef || undefined,
      notes: raw.notes || undefined,
    };

    this.billingService.recordPayment(request).subscribe({
      next: () => {
        this.notification.showSuccess('Payment recorded successfully');
        this.router.navigate(['/billing', this.invoiceId]);
        this.saving.set(false);
      },
      error: () => {
        this.notification.showError('Failed to record payment');
        this.saving.set(false);
      },
    });
  }
}
