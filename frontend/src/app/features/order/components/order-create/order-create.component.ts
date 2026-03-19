import { Component, ChangeDetectionStrategy, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormArray, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { OrderService } from '../../services/order.service';
import { NotificationService } from '@core/services/notification.service';
import { TestOrderRequest } from '../../models/order.model';

@Component({
  selector: 'app-order-create',
  standalone: true,
  imports: [
    RouterLink, ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatButtonModule, MatIconModule, MatCardModule,
    MatDividerModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Create Test Order</h2>
        <p class="text-gray-500 text-sm">Place a new laboratory test order</p>
      </div>
      <a mat-button routerLink="/order">
        <mat-icon>arrow_back</mat-icon> Back to List
      </a>
    </div>

    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="grid gap-6">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Order Information</mat-card-title>
        </mat-card-header>
        <mat-card-content class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          <mat-form-field appearance="outline">
            <mat-label>Patient ID</mat-label>
            <input matInput formControlName="patientId" placeholder="UUID" />
            @if (form.controls.patientId.hasError('required')) {
              <mat-error>Patient ID is required</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Visit ID</mat-label>
            <input matInput formControlName="visitId" placeholder="Optional UUID" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Referring Doctor ID</mat-label>
            <input matInput formControlName="referringDoctorId" placeholder="Optional UUID" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Priority</mat-label>
            <mat-select formControlName="priority">
              <mat-option value="ROUTINE">Routine</mat-option>
              <mat-option value="STAT">STAT</mat-option>
              <mat-option value="URGENT">Urgent</mat-option>
            </mat-select>
          </mat-form-field>
          <mat-form-field appearance="outline" class="md:col-span-2">
            <mat-label>Clinical History</mat-label>
            <textarea matInput formControlName="clinicalHistory" rows="3"></textarea>
          </mat-form-field>
          <mat-form-field appearance="outline" class="md:col-span-2">
            <mat-label>Special Instructions</mat-label>
            <textarea matInput formControlName="specialInstructions" rows="2"></textarea>
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Line Items</mat-card-title>
        </mat-card-header>
        <mat-card-content class="mt-4">
          @for (item of lineItems.controls; track $index; let i = $index) {
            <div class="grid grid-cols-1 md:grid-cols-6 gap-3 mb-4 p-4 border rounded-lg" [formGroupName]="'lineItems'">
              <ng-container [formGroupName]="i">
                <mat-form-field appearance="outline">
                  <mat-label>Test ID</mat-label>
                  <input matInput formControlName="testId" placeholder="UUID" />
                  @if (lineItems.at(i).get('testId')?.hasError('required')) {
                    <mat-error>Required</mat-error>
                  }
                </mat-form-field>
                <mat-form-field appearance="outline">
                  <mat-label>Test Code</mat-label>
                  <input matInput formControlName="testCode" />
                  @if (lineItems.at(i).get('testCode')?.hasError('required')) {
                    <mat-error>Required</mat-error>
                  }
                </mat-form-field>
                <mat-form-field appearance="outline">
                  <mat-label>Test Name</mat-label>
                  <input matInput formControlName="testName" />
                  @if (lineItems.at(i).get('testName')?.hasError('required')) {
                    <mat-error>Required</mat-error>
                  }
                </mat-form-field>
                <mat-form-field appearance="outline">
                  <mat-label>Sample Type</mat-label>
                  <input matInput formControlName="sampleType" />
                </mat-form-field>
                <mat-form-field appearance="outline">
                  <mat-label>Unit Price</mat-label>
                  <input matInput formControlName="unitPrice" type="number" min="0" />
                </mat-form-field>
                <div class="flex items-center">
                  <button mat-icon-button color="warn" type="button" (click)="removeLineItem(i)"
                    [disabled]="lineItems.length === 1" matTooltip="Remove">
                    <mat-icon>delete</mat-icon>
                  </button>
                </div>
              </ng-container>
            </div>
          }
          <button mat-stroked-button type="button" color="primary" (click)="addLineItem()">
            <mat-icon>add</mat-icon> Add Test
          </button>
        </mat-card-content>
      </mat-card>

      <div class="flex justify-end gap-3">
        <a mat-button routerLink="/order">Cancel</a>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || saving()">
          @if (saving()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          Create Order
        </button>
      </div>
    </form>
  `,
})
export class OrderCreateComponent {
  private readonly fb = inject(FormBuilder);
  private readonly orderService = inject(OrderService);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly saving = signal(false);

  readonly form = this.fb.nonNullable.group({
    patientId: ['', Validators.required],
    visitId: [''],
    referringDoctorId: [''],
    priority: ['ROUTINE'],
    clinicalHistory: [''],
    specialInstructions: [''],
    lineItems: this.fb.array([this.createLineItem()]),
  });

  get lineItems(): FormArray {
    return this.form.get('lineItems') as FormArray;
  }

  createLineItem() {
    return this.fb.nonNullable.group({
      testId: ['', Validators.required],
      testCode: ['', Validators.required],
      testName: ['', Validators.required],
      sampleType: [''],
      unitPrice: [0],
    });
  }

  addLineItem(): void {
    this.lineItems.push(this.createLineItem());
  }

  removeLineItem(index: number): void {
    this.lineItems.removeAt(index);
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const raw = this.form.getRawValue();
    const request: TestOrderRequest = {
      patientId: raw.patientId,
      visitId: raw.visitId || undefined,
      referringDoctorId: raw.referringDoctorId || undefined,
      priority: raw.priority || undefined,
      clinicalHistory: raw.clinicalHistory || undefined,
      specialInstructions: raw.specialInstructions || undefined,
      lineItems: raw.lineItems.map(item => ({
        testId: item.testId,
        testCode: item.testCode,
        testName: item.testName,
        sampleType: item.sampleType || undefined,
        unitPrice: item.unitPrice || undefined,
      })),
    };

    this.orderService.create(request).subscribe({
      next: () => {
        this.notification.showSuccess('Order created successfully');
        this.router.navigate(['/order']);
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
