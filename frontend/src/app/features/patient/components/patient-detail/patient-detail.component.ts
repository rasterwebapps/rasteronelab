import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { PatientService } from '../../services/patient.service';
import { Patient, PatientVisit } from '../../models/patient.model';
import { OrderService } from '../../../order/services/order.service';
import { BillingService } from '../../../billing/services/billing.service';
import { ResultService } from '../../../result/services/result.service';
import { TestOrder, OrderStatus } from '../../../order/models/order.model';
import { Invoice, InvoiceStatus } from '../../../billing/models/billing.model';
import { TestResult, RESULT_STATUS_COLORS, ResultStatus } from '../../../result/models/result.model';

@Component({
  selector: 'app-patient-detail',
  standalone: true,
  imports: [
    RouterLink, CurrencyPipe, DatePipe,
    MatCardModule, MatTabsModule, MatButtonModule,
    MatIconModule, MatChipsModule, MatDividerModule,
    MatProgressSpinnerModule, MatTableModule, MatTooltipModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (loading()) {
      <div class="flex justify-center p-8">
        <mat-spinner diameter="40" />
      </div>
    } @else if (patient()) {
      <div class="flex items-center justify-between mb-6">
        <div>
          <h2 class="text-xl font-bold text-gray-800">{{ patient()!.firstName }} {{ patient()!.lastName }}</h2>
          <p class="text-gray-500 text-sm">UHID: {{ patient()!.uhid }}</p>
        </div>
        <div class="flex gap-2">
          <a mat-button routerLink="/patient">
            <mat-icon>arrow_back</mat-icon> Back to List
          </a>
          <a mat-raised-button color="primary" [routerLink]="['/patient', patient()!.id, 'edit']">
            <mat-icon>edit</mat-icon> Edit
          </a>
        </div>
      </div>

      <mat-tab-group animationDuration="200ms">
        <mat-tab label="Demographics">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mt-4">
            <mat-card>
              <mat-card-header>
                <mat-card-title>Personal Information</mat-card-title>
              </mat-card-header>
              <mat-card-content class="grid grid-cols-2 gap-y-3 mt-4">
                <div class="text-gray-500 text-sm">Name</div>
                <div>{{ patient()!.firstName }} {{ patient()!.lastName }}</div>
                <div class="text-gray-500 text-sm">Gender</div>
                <div>{{ patient()!.gender }}</div>
                <div class="text-gray-500 text-sm">Date of Birth</div>
                <div>{{ patient()!.dateOfBirth ?? '-' }}</div>
                <div class="text-gray-500 text-sm">Age</div>
                <div>{{ formatAge(patient()!) }}</div>
                <div class="text-gray-500 text-sm">Blood Group</div>
                <div>{{ patient()!.bloodGroup ?? '-' }}</div>
                <div class="text-gray-500 text-sm">Nationality</div>
                <div>{{ patient()!.nationality ?? '-' }}</div>
              </mat-card-content>
            </mat-card>

            <mat-card>
              <mat-card-header>
                <mat-card-title>Contact Information</mat-card-title>
              </mat-card-header>
              <mat-card-content class="grid grid-cols-2 gap-y-3 mt-4">
                <div class="text-gray-500 text-sm">Phone</div>
                <div>{{ patient()!.phone }}</div>
                <div class="text-gray-500 text-sm">Email</div>
                <div>{{ patient()!.email ?? '-' }}</div>
                <div class="text-gray-500 text-sm">Address</div>
                <div>
                  {{ patient()!.addressLine1 ?? '' }}
                  @if (patient()!.addressLine2) { , {{ patient()!.addressLine2 }} }
                </div>
                <div class="text-gray-500 text-sm">City / State</div>
                <div>{{ patient()!.city ?? '' }} {{ patient()!.state ? ', ' + patient()!.state : '' }}</div>
                <div class="text-gray-500 text-sm">Postal Code</div>
                <div>{{ patient()!.postalCode ?? '-' }}</div>
              </mat-card-content>
            </mat-card>

            <mat-card>
              <mat-card-header>
                <mat-card-title>Emergency Contact</mat-card-title>
              </mat-card-header>
              <mat-card-content class="grid grid-cols-2 gap-y-3 mt-4">
                <div class="text-gray-500 text-sm">Name</div>
                <div>{{ patient()!.emergencyContactName ?? '-' }}</div>
                <div class="text-gray-500 text-sm">Phone</div>
                <div>{{ patient()!.emergencyContactPhone ?? '-' }}</div>
              </mat-card-content>
            </mat-card>

            <mat-card>
              <mat-card-header>
                <mat-card-title>Identification</mat-card-title>
              </mat-card-header>
              <mat-card-content class="grid grid-cols-2 gap-y-3 mt-4">
                <div class="text-gray-500 text-sm">ID Type</div>
                <div>{{ patient()!.idType ?? '-' }}</div>
                <div class="text-gray-500 text-sm">ID Number</div>
                <div>{{ patient()!.idNumber ?? '-' }}</div>
                <div class="text-gray-500 text-sm">Notes</div>
                <div>{{ patient()!.notes ?? '-' }}</div>
              </mat-card-content>
            </mat-card>
          </div>
        </mat-tab>

        <mat-tab label="Visit History">
          <div class="mt-4">
            <mat-card>
              <mat-card-content>
                @if (visits().length > 0) {
                  <table mat-table [dataSource]="visits()" class="w-full">
                    <ng-container matColumnDef="visitNumber">
                      <th mat-header-cell *matHeaderCellDef>Visit #</th>
                      <td mat-cell *matCellDef="let row">{{ row.visitNumber }}</td>
                    </ng-container>
                    <ng-container matColumnDef="visitDate">
                      <th mat-header-cell *matHeaderCellDef>Date</th>
                      <td mat-cell *matCellDef="let row">{{ row.visitDate }}</td>
                    </ng-container>
                    <ng-container matColumnDef="visitType">
                      <th mat-header-cell *matHeaderCellDef>Type</th>
                      <td mat-cell *matCellDef="let row">{{ row.visitType }}</td>
                    </ng-container>
                    <ng-container matColumnDef="clinicalNotes">
                      <th mat-header-cell *matHeaderCellDef>Notes</th>
                      <td mat-cell *matCellDef="let row">{{ row.clinicalNotes ?? '-' }}</td>
                    </ng-container>
                    <tr mat-header-row *matHeaderRowDef="visitColumns"></tr>
                    <tr mat-row *matRowDef="let row; columns: visitColumns"></tr>
                  </table>
                } @else {
                  <div class="text-center py-8 text-gray-400">
                    <mat-icon class="!text-5xl mb-2">event_note</mat-icon>
                    <p>No visits recorded</p>
                  </div>
                }
              </mat-card-content>
            </mat-card>
          </div>
        </mat-tab>

        <mat-tab label="Orders">
          <div class="mt-4">
            <mat-card>
              <mat-card-content>
                @if (loadingOrders()) {
                  <div class="flex justify-center p-8">
                    <mat-spinner diameter="40" />
                  </div>
                } @else if (orders().length > 0) {
                  <table mat-table [dataSource]="orders()" class="w-full">
                    <ng-container matColumnDef="orderNumber">
                      <th mat-header-cell *matHeaderCellDef>Order #</th>
                      <td mat-cell *matCellDef="let row" class="font-medium">{{ row.orderNumber }}</td>
                    </ng-container>
                    <ng-container matColumnDef="orderDate">
                      <th mat-header-cell *matHeaderCellDef>Date</th>
                      <td mat-cell *matCellDef="let row">{{ row.orderDate | date:'dd MMM yyyy' }}</td>
                    </ng-container>
                    <ng-container matColumnDef="status">
                      <th mat-header-cell *matHeaderCellDef>Status</th>
                      <td mat-cell *matCellDef="let row">
                        <span class="px-2 py-1 rounded-full text-xs font-medium" [class]="getOrderStatusClass(row.status)">
                          {{ row.status }}
                        </span>
                      </td>
                    </ng-container>
                    <ng-container matColumnDef="priority">
                      <th mat-header-cell *matHeaderCellDef>Priority</th>
                      <td mat-cell *matCellDef="let row">{{ row.priority }}</td>
                    </ng-container>
                    <ng-container matColumnDef="actions">
                      <th mat-header-cell *matHeaderCellDef>Actions</th>
                      <td mat-cell *matCellDef="let row">
                        <a mat-icon-button [routerLink]="['/order', row.id]" matTooltip="View Order">
                          <mat-icon>visibility</mat-icon>
                        </a>
                      </td>
                    </ng-container>
                    <tr mat-header-row *matHeaderRowDef="orderColumns"></tr>
                    <tr mat-row *matRowDef="let row; columns: orderColumns"></tr>
                  </table>
                } @else {
                  <div class="text-center py-8 text-gray-400">
                    <mat-icon class="!text-5xl mb-2">science</mat-icon>
                    <p>No orders found for this patient</p>
                  </div>
                }
              </mat-card-content>
            </mat-card>
          </div>
        </mat-tab>

        <mat-tab label="Lab Results">
          <div class="mt-4">
            <mat-card>
              <mat-card-content>
                @if (loadingResults()) {
                  <div class="flex justify-center p-8">
                    <mat-spinner diameter="40" />
                  </div>
                } @else if (results().length > 0) {
                  <table mat-table [dataSource]="results()" class="w-full">
                    <ng-container matColumnDef="testCode">
                      <th mat-header-cell *matHeaderCellDef>Code</th>
                      <td mat-cell *matCellDef="let row" class="font-medium">{{ row.testCode }}</td>
                    </ng-container>
                    <ng-container matColumnDef="testName">
                      <th mat-header-cell *matHeaderCellDef>Test Name</th>
                      <td mat-cell *matCellDef="let row">{{ row.testName }}</td>
                    </ng-container>
                    <ng-container matColumnDef="resultStatus">
                      <th mat-header-cell *matHeaderCellDef>Status</th>
                      <td mat-cell *matCellDef="let row">
                        <span class="px-2 py-1 rounded-full text-xs font-medium" [class]="getResultStatusClass(row.status)">
                          {{ row.status }}
                        </span>
                      </td>
                    </ng-container>
                    <ng-container matColumnDef="enteredAt">
                      <th mat-header-cell *matHeaderCellDef>Entered At</th>
                      <td mat-cell *matCellDef="let row">{{ row.enteredAt ? (row.enteredAt | date:'dd MMM yyyy') : '-' }}</td>
                    </ng-container>
                    <ng-container matColumnDef="authorizedAt">
                      <th mat-header-cell *matHeaderCellDef>Authorized At</th>
                      <td mat-cell *matCellDef="let row">{{ row.authorizedAt ? (row.authorizedAt | date:'dd MMM yyyy') : '-' }}</td>
                    </ng-container>
                    <ng-container matColumnDef="resultActions">
                      <th mat-header-cell *matHeaderCellDef>Actions</th>
                      <td mat-cell *matCellDef="let row">
                        <a mat-icon-button [routerLink]="['/result', row.id]" matTooltip="View Result">
                          <mat-icon>visibility</mat-icon>
                        </a>
                      </td>
                    </ng-container>
                    <tr mat-header-row *matHeaderRowDef="resultColumns"></tr>
                    <tr mat-row *matRowDef="let row; columns: resultColumns"></tr>
                  </table>
                } @else {
                  <div class="text-center py-8 text-gray-400">
                    <mat-icon class="!text-5xl mb-2">description</mat-icon>
                    <p>No lab results found for this patient</p>
                  </div>
                }
              </mat-card-content>
            </mat-card>
          </div>
        </mat-tab>

        <mat-tab label="Billing">
          <div class="mt-4">
            <mat-card>
              <mat-card-content>
                @if (loadingInvoices()) {
                  <div class="flex justify-center p-8">
                    <mat-spinner diameter="40" />
                  </div>
                } @else if (invoices().length > 0) {
                  <table mat-table [dataSource]="invoices()" class="w-full">
                    <ng-container matColumnDef="invoiceNumber">
                      <th mat-header-cell *matHeaderCellDef>Invoice #</th>
                      <td mat-cell *matCellDef="let row" class="font-medium">{{ row.invoiceNumber }}</td>
                    </ng-container>
                    <ng-container matColumnDef="invoiceDate">
                      <th mat-header-cell *matHeaderCellDef>Date</th>
                      <td mat-cell *matCellDef="let row">{{ row.invoiceDate | date:'dd MMM yyyy' }}</td>
                    </ng-container>
                    <ng-container matColumnDef="totalAmount">
                      <th mat-header-cell *matHeaderCellDef>Total</th>
                      <td mat-cell *matCellDef="let row">{{ row.totalAmount | currency:'INR':'symbol':'1.2-2' }}</td>
                    </ng-container>
                    <ng-container matColumnDef="paidAmount">
                      <th mat-header-cell *matHeaderCellDef>Paid</th>
                      <td mat-cell *matCellDef="let row">{{ row.paidAmount | currency:'INR':'symbol':'1.2-2' }}</td>
                    </ng-container>
                    <ng-container matColumnDef="balanceAmount">
                      <th mat-header-cell *matHeaderCellDef>Balance</th>
                      <td mat-cell *matCellDef="let row">{{ row.balanceAmount | currency:'INR':'symbol':'1.2-2' }}</td>
                    </ng-container>
                    <ng-container matColumnDef="invoiceStatus">
                      <th mat-header-cell *matHeaderCellDef>Status</th>
                      <td mat-cell *matCellDef="let row">
                        <span class="px-2 py-1 rounded-full text-xs font-medium" [class]="getInvoiceStatusClass(row.status)">
                          {{ row.status }}
                        </span>
                      </td>
                    </ng-container>
                    <ng-container matColumnDef="billingActions">
                      <th mat-header-cell *matHeaderCellDef>Actions</th>
                      <td mat-cell *matCellDef="let row">
                        <a mat-icon-button [routerLink]="['/billing', row.id]" matTooltip="View Invoice">
                          <mat-icon>visibility</mat-icon>
                        </a>
                      </td>
                    </ng-container>
                    <tr mat-header-row *matHeaderRowDef="invoiceColumns"></tr>
                    <tr mat-row *matRowDef="let row; columns: invoiceColumns"></tr>
                  </table>
                } @else {
                  <div class="text-center py-8 text-gray-400">
                    <mat-icon class="!text-5xl mb-2">receipt_long</mat-icon>
                    <p>No billing history found for this patient</p>
                  </div>
                }
              </mat-card-content>
            </mat-card>
          </div>
        </mat-tab>
      </mat-tab-group>
    }
  `,
})
export class PatientDetailComponent implements OnInit {
  private readonly patientService = inject(PatientService);
  private readonly orderService = inject(OrderService);
  private readonly billingService = inject(BillingService);
  private readonly resultService = inject(ResultService);
  private readonly route = inject(ActivatedRoute);

  readonly patient = signal<Patient | null>(null);
  readonly visits = signal<PatientVisit[]>([]);
  readonly orders = signal<TestOrder[]>([]);
  readonly invoices = signal<Invoice[]>([]);
  readonly results = signal<TestResult[]>([]);
  readonly loading = signal(false);
  readonly loadingOrders = signal(false);
  readonly loadingInvoices = signal(false);
  readonly loadingResults = signal(false);

  readonly visitColumns = ['visitNumber', 'visitDate', 'visitType', 'clinicalNotes'];
  readonly orderColumns = ['orderNumber', 'orderDate', 'status', 'priority', 'actions'];
  readonly resultColumns = ['testCode', 'testName', 'resultStatus', 'enteredAt', 'authorizedAt', 'resultActions'];
  readonly invoiceColumns = ['invoiceNumber', 'invoiceDate', 'totalAmount', 'paidAmount', 'balanceAmount', 'invoiceStatus', 'billingActions'];

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadPatient(id);
      this.loadVisits(id);
      this.loadOrders(id);
      this.loadResults(id);
      this.loadInvoices(id);
    }
  }

  private loadPatient(id: string): void {
    this.loading.set(true);
    this.patientService.getById(id).subscribe({
      next: (response) => {
        this.patient.set(response.data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  private loadVisits(patientId: string): void {
    this.patientService.getVisits(patientId).subscribe({
      next: (response) => this.visits.set(response.data ?? []),
    });
  }

  private loadOrders(patientId: string): void {
    this.loadingOrders.set(true);
    this.orderService.getByPatient(patientId).subscribe({
      next: (response) => {
        this.orders.set(response.data ?? []);
        this.loadingOrders.set(false);
      },
      error: () => this.loadingOrders.set(false),
    });
  }

  private loadInvoices(patientId: string): void {
    this.loadingInvoices.set(true);
    this.billingService.getInvoicesByPatient(patientId).subscribe({
      next: (response) => {
        this.invoices.set(response.data ?? []);
        this.loadingInvoices.set(false);
      },
      error: () => this.loadingInvoices.set(false),
    });
  }

  private loadResults(patientId: string): void {
    this.loadingResults.set(true);
    this.resultService.getByPatient(patientId).subscribe({
      next: (response) => {
        this.results.set(response.data ?? []);
        this.loadingResults.set(false);
      },
      error: () => this.loadingResults.set(false),
    });
  }

  formatAge(patient: Patient): string {
    const parts: string[] = [];
    if (patient.ageYears != null && patient.ageYears > 0) parts.push(`${patient.ageYears}Y`);
    if (patient.ageMonths != null && patient.ageMonths > 0) parts.push(`${patient.ageMonths}M`);
    if (patient.ageDays != null && patient.ageDays > 0) parts.push(`${patient.ageDays}D`);
    return parts.length > 0 ? parts.join(' ') : '-';
  }

  private static readonly ORDER_STATUS_COLORS: Record<OrderStatus, string> = {
    DRAFT:            'bg-gray-100 text-gray-700',
    PLACED:           'bg-blue-100 text-blue-700',
    PAID:             'bg-green-100 text-green-700',
    SAMPLE_COLLECTED: 'bg-cyan-100 text-cyan-700',
    IN_PROGRESS:      'bg-yellow-100 text-yellow-700',
    RESULTED:         'bg-purple-100 text-purple-700',
    AUTHORISED:       'bg-indigo-100 text-indigo-700',
    COMPLETED:        'bg-green-100 text-green-700',
    CANCELLED:        'bg-red-100 text-red-700',
  };

  private static readonly INVOICE_STATUS_COLORS: Record<InvoiceStatus, string> = {
    DRAFT:         'bg-gray-100 text-gray-700',
    GENERATED:     'bg-blue-100 text-blue-700',
    PARTIALLY_PAID: 'bg-yellow-100 text-yellow-700',
    PAID:          'bg-green-100 text-green-700',
    REFUNDED:      'bg-orange-100 text-orange-700',
    CANCELLED:     'bg-red-100 text-red-700',
  };

  getOrderStatusClass(status: OrderStatus): string {
    return PatientDetailComponent.ORDER_STATUS_COLORS[status] ?? 'bg-gray-100 text-gray-700';
  }

  getInvoiceStatusClass(status: InvoiceStatus): string {
    return PatientDetailComponent.INVOICE_STATUS_COLORS[status] ?? 'bg-gray-100 text-gray-700';
  }

  getResultStatusClass(status: ResultStatus): string {
    return RESULT_STATUS_COLORS[status] ?? 'bg-gray-100 text-gray-700';
  }
}
