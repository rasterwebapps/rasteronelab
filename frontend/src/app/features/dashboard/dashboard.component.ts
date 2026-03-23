import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe, SlicePipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { ResultService } from '../result/services/result.service';
import { OrderService } from '../order/services/order.service';
import { TestResult, RESULT_STATUS_COLORS, ResultStatus } from '../result/models/result.model';
import { TestOrder } from '../order/models/order.model';

interface QuickAccessCard {
  label: string;
  route: string;
  icon: string;
  colorClass: string;
  bgClass: string;
}

interface ModuleStatus {
  name: string;
  icon: string;
  phase: number;
  status: 'Complete' | 'In Progress' | 'Not Started';
  detail?: string;
  statusClass: string;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    RouterLink, DatePipe, SlicePipe,
    MatCardModule, MatIconModule, MatListModule,
    MatProgressSpinnerModule, MatDividerModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="mb-6">
      <h2 class="text-xl font-bold text-gray-800">Dashboard</h2>
      <p class="text-gray-500 text-sm">Overview of lab operations</p>
    </div>

    <!-- Quick Access Cards -->
    <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4 mb-6">
      @for (card of quickAccessCards; track card.route) {
        <a [routerLink]="card.route"
           class="flex flex-col items-center justify-center p-4 rounded-lg shadow bg-white hover:shadow-md transition-shadow cursor-pointer no-underline">
          <div class="rounded-full p-3 mb-2" [class]="card.bgClass">
            <mat-icon [class]="card.colorClass">{{ card.icon }}</mat-icon>
          </div>
          <span class="text-sm font-medium text-gray-700 text-center">{{ card.label }}</span>
        </a>
      }
    </div>

    <!-- Module Status Overview -->
    <mat-card class="mb-6">
      <mat-card-header>
        <mat-icon mat-card-avatar>view_module</mat-icon>
        <mat-card-title>Module Status Overview</mat-card-title>
        <mat-card-subtitle>Development progress by phase</mat-card-subtitle>
      </mat-card-header>
      <mat-card-content>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
          @for (mod of moduleStatuses; track mod.name) {
            <div class="flex items-center gap-3 p-3 rounded-lg border border-gray-100 bg-gray-50">
              <div class="rounded-full p-2 bg-white shadow-sm">
                <mat-icon class="text-gray-600">{{ mod.icon }}</mat-icon>
              </div>
              <div class="flex-1 min-w-0">
                <div class="flex items-center gap-2 flex-wrap">
                  <span class="text-sm font-medium text-gray-800 truncate">{{ mod.name }}</span>
                  <span class="px-2 py-0.5 rounded-full text-xs font-medium" [class]="mod.statusClass">
                    {{ mod.status }}
                  </span>
                </div>
                <div class="text-xs text-gray-500 mt-0.5">
                  Phase {{ mod.phase }} @if (mod.detail) { · {{ mod.detail }}}
                </div>
              </div>
            </div>
          }
        </div>
      </mat-card-content>
    </mat-card>

    <!-- Bottom Section: Recent Results + Pending Orders -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <!-- Recent Results -->
      <mat-card>
        <mat-card-header>
          <mat-icon mat-card-avatar>analytics</mat-icon>
          <mat-card-title>Recent Results</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          @if (loadingResults()) {
            <div class="flex justify-center py-6">
              <mat-spinner diameter="32" />
            </div>
          } @else if (recentResults().length === 0) {
            <div class="text-center py-6 text-gray-400">
              <mat-icon class="!text-4xl mb-1">analytics</mat-icon>
              <p class="text-sm">No recent results</p>
            </div>
          } @else {
            <mat-list class="mt-2">
              @for (result of recentResults(); track result.id) {
                <mat-list-item class="!h-auto !py-2">
                  <div class="flex items-center justify-between gap-2 w-full py-1">
                    <div class="flex-1 min-w-0">
                      <div class="text-sm font-medium text-gray-800 truncate">{{ result.testName }}</div>
                      <div class="text-xs text-gray-500">
                        Patient: {{ result.patientId | slice:0:8 }}… · {{ result.enteredAt ? (result.enteredAt | date:'dd MMM') : '-' }}
                      </div>
                    </div>
                    <span class="px-2 py-0.5 rounded-full text-xs font-medium shrink-0" [class]="getResultStatusClass(result.status)">
                      {{ result.status }}
                    </span>
                  </div>
                </mat-list-item>
                <mat-divider />
              }
            </mat-list>
          }
        </mat-card-content>
      </mat-card>

      <!-- Pending Orders -->
      <mat-card>
        <mat-card-header>
          <mat-icon mat-card-avatar>science</mat-icon>
          <mat-card-title>Pending Orders</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          @if (loadingOrders()) {
            <div class="flex justify-center py-6">
              <mat-spinner diameter="32" />
            </div>
          } @else if (pendingOrders().length === 0) {
            <div class="text-center py-6 text-gray-400">
              <mat-icon class="!text-4xl mb-1">science</mat-icon>
              <p class="text-sm">No pending orders</p>
            </div>
          } @else {
            <mat-list class="mt-2">
              @for (order of pendingOrders(); track order.id) {
                <mat-list-item class="!h-auto !py-2">
                  <div class="flex items-center justify-between gap-2 w-full py-1">
                    <div class="flex-1 min-w-0">
                      <div class="text-sm font-medium text-gray-800 truncate">{{ order.orderNumber }}</div>
                      <div class="text-xs text-gray-500">{{ order.orderDate | date:'dd MMM yyyy' }}</div>
                    </div>
                    <span class="px-2 py-0.5 rounded-full text-xs font-medium shrink-0" [class]="getPriorityClass(order.priority)">
                      {{ order.priority }}
                    </span>
                  </div>
                </mat-list-item>
                <mat-divider />
              }
            </mat-list>
          }
        </mat-card-content>
      </mat-card>
    </div>
  `,
})
export class DashboardComponent implements OnInit {
  private readonly resultService = inject(ResultService);
  private readonly orderService = inject(OrderService);

  readonly recentResults = signal<TestResult[]>([]);
  readonly pendingOrders = signal<TestOrder[]>([]);
  readonly loadingResults = signal(false);
  readonly loadingOrders = signal(false);

  readonly quickAccessCards: QuickAccessCard[] = [
    { label: 'Patients',  route: '/patient',  icon: 'person',      colorClass: 'text-blue-600',   bgClass: 'bg-blue-50' },
    { label: 'Orders',    route: '/order',    icon: 'science',     colorClass: 'text-green-600',  bgClass: 'bg-green-50' },
    { label: 'Billing',   route: '/billing',  icon: 'receipt',     colorClass: 'text-orange-600', bgClass: 'bg-orange-50' },
    { label: 'Samples',   route: '/sample',   icon: 'biotech',     colorClass: 'text-purple-600', bgClass: 'bg-purple-50' },
    { label: 'Results',   route: '/result',   icon: 'analytics',   colorClass: 'text-teal-600',   bgClass: 'bg-teal-50' },
    { label: 'Reports',   route: '/report',   icon: 'description', colorClass: 'text-indigo-600', bgClass: 'bg-indigo-50' },
  ];

  readonly moduleStatuses: ModuleStatus[] = [
    { name: 'Administration',       icon: 'settings',     phase: 2, status: 'In Progress', detail: '~97% complete',  statusClass: 'bg-yellow-100 text-yellow-700' },
    { name: 'Patient & Ordering',   icon: 'person_add',   phase: 3, status: 'Complete',                              statusClass: 'bg-green-100 text-green-700' },
    { name: 'Sample Management',    icon: 'biotech',      phase: 4, status: 'Complete',                              statusClass: 'bg-green-100 text-green-700' },
    { name: 'Result Entry',         icon: 'analytics',    phase: 5, status: 'Complete',                              statusClass: 'bg-green-100 text-green-700' },
    { name: 'Instrument Interface', icon: 'device_hub',   phase: 6, status: 'Not Started',                           statusClass: 'bg-gray-100 text-gray-700' },
    { name: 'Reports & QC',         icon: 'description',  phase: 7, status: 'In Progress', detail: '~15% complete',  statusClass: 'bg-yellow-100 text-yellow-700' },
  ];

  ngOnInit(): void {
    this.loadRecentResults();
    this.loadPendingOrders();
  }

  private loadRecentResults(): void {
    this.loadingResults.set(true);
    this.resultService.getAll(0, 5).subscribe({
      next: (response) => {
        this.recentResults.set(response.data ?? []);
        this.loadingResults.set(false);
      },
      error: () => this.loadingResults.set(false),
    });
  }

  private loadPendingOrders(): void {
    this.loadingOrders.set(true);
    this.orderService.getByStatus('PLACED', 0, 5).subscribe({
      next: (response) => {
        this.pendingOrders.set(response.data ?? []);
        this.loadingOrders.set(false);
      },
      error: () => this.loadingOrders.set(false),
    });
  }

  getResultStatusClass(status: ResultStatus): string {
    return RESULT_STATUS_COLORS[status] ?? 'bg-gray-100 text-gray-700';
  }

  getPriorityClass(priority: string): string {
    const map: Record<string, string> = {
      STAT:    'bg-red-100 text-red-700',
      URGENT:  'bg-orange-100 text-orange-700',
      ROUTINE: 'bg-blue-100 text-blue-700',
    };
    return map[priority] ?? 'bg-gray-100 text-gray-700';
  }
}
