import { Component, ChangeDetectionStrategy, inject, signal, computed, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatCardModule } from '@angular/material/card';
import { SlicePipe } from '@angular/common';
import { ResultService } from '../../services/result.service';
import { NotificationService } from '@core/services/notification.service';
import { TestResult, RESULT_STATUS_COLORS } from '../../models/result.model';

@Component({
  selector: 'app-result-authorization',
  standalone: true,
  imports: [
    RouterLink, MatTableModule, MatPaginatorModule, MatButtonModule,
    MatIconModule, MatCheckboxModule, MatChipsModule,
    MatProgressSpinnerModule, MatTooltipModule, MatCardModule, SlicePipe,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Authorization Queue</h2>
        <p class="text-gray-500 text-sm">Review and authorize validated results</p>
      </div>
      <div class="flex gap-2">
        <a mat-button routerLink="/results">
          <mat-icon>arrow_back</mat-icon> Back to Results
        </a>
        <button mat-raised-button color="primary"
          [disabled]="selectedIds().size === 0 || authorizing()"
          (click)="onBatchAuthorize()">
          @if (authorizing()) {
            <mat-icon class="animate-spin">refresh</mat-icon>
          }
          <mat-icon>verified</mat-icon>
          Authorize Selected ({{ selectedIds().size }})
        </button>
      </div>
    </div>

    @if (loading()) {
      <div class="flex justify-center p-8">
        <mat-spinner diameter="40" />
      </div>
    } @else {
      @if (hasCriticalUnacknowledged()) {
        <mat-card class="mb-4 !bg-red-50 border border-red-200">
          <mat-card-content class="flex items-center gap-3 py-2">
            <mat-icon class="text-red-600">warning</mat-icon>
            <span class="text-red-700 text-sm font-medium">
              Some selected results have unacknowledged critical values. These cannot be authorized until critical values are acknowledged.
            </span>
          </mat-card-content>
        </mat-card>
      }

      <div class="bg-white rounded-lg shadow">
        <table mat-table [dataSource]="results()" multiTemplateDataRows class="w-full">
          <ng-container matColumnDef="select">
            <th mat-header-cell *matHeaderCellDef>
              <mat-checkbox
                [checked]="allSelected()"
                [indeterminate]="someSelected()"
                (change)="toggleAll($event.checked)" />
            </th>
            <td mat-cell *matCellDef="let row">
              <mat-checkbox
                [checked]="selectedIds().has(row.id)"
                [disabled]="row.isCritical && !row.criticalAcknowledged"
                (change)="toggleRow(row.id, $event.checked)" />
            </td>
          </ng-container>

          <ng-container matColumnDef="testCode">
            <th mat-header-cell *matHeaderCellDef>Test Code</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ row.testCode }}</td>
          </ng-container>

          <ng-container matColumnDef="testName">
            <th mat-header-cell *matHeaderCellDef>Test Name</th>
            <td mat-cell *matCellDef="let row">{{ row.testName }}</td>
          </ng-container>

          <ng-container matColumnDef="patientId">
            <th mat-header-cell *matHeaderCellDef>Patient</th>
            <td mat-cell *matCellDef="let row">{{ row.patientId | slice:0:8 }}…</td>
          </ng-container>

          <ng-container matColumnDef="departmentName">
            <th mat-header-cell *matHeaderCellDef>Department</th>
            <td mat-cell *matCellDef="let row">{{ row.departmentName ?? '-' }}</td>
          </ng-container>

          <ng-container matColumnDef="flags">
            <th mat-header-cell *matHeaderCellDef>Flags</th>
            <td mat-cell *matCellDef="let row">
              @if (row.isCritical) {
                <span class="px-2 py-1 rounded-full text-xs font-medium bg-red-100 text-red-700 mr-1">CRITICAL</span>
              }
              @if (row.isCritical && !row.criticalAcknowledged) {
                <mat-icon class="text-amber-500 !text-base align-middle" matTooltip="Critical not acknowledged">warning</mat-icon>
              }
              @if (row.hasDeltaCheckFailure) {
                <span class="px-2 py-1 rounded-full text-xs font-medium bg-amber-100 text-amber-700">DELTA</span>
              }
            </td>
          </ng-container>

          <ng-container matColumnDef="validatedBy">
            <th mat-header-cell *matHeaderCellDef>Validated By</th>
            <td mat-cell *matCellDef="let row">{{ row.validatedBy ?? '-' }}</td>
          </ng-container>

          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef>Actions</th>
            <td mat-cell *matCellDef="let row">
              <button mat-icon-button (click)="toggleExpand(row.id)" matTooltip="View values">
                <mat-icon>{{ expandedId() === row.id ? 'expand_less' : 'expand_more' }}</mat-icon>
              </button>
              <a mat-icon-button [routerLink]="['/results', row.id]" matTooltip="Full details">
                <mat-icon>visibility</mat-icon>
              </a>
            </td>
          </ng-container>

          <!-- Expanded row -->
          <ng-container matColumnDef="expandedDetail">
            <td mat-cell *matCellDef="let row" [attr.colspan]="displayedColumns.length">
              @if (expandedId() === row.id) {
                <div class="p-4 bg-gray-50">
                  <table class="w-full text-sm">
                    <thead>
                      <tr class="text-left text-gray-500 border-b">
                        <th class="p-1">Parameter</th>
                        <th class="p-1">Value</th>
                        <th class="p-1">Unit</th>
                        <th class="p-1">Reference Range</th>
                        <th class="p-1">Flag</th>
                      </tr>
                    </thead>
                    <tbody>
                      @for (rv of row.resultValues; track rv.id) {
                        <tr class="border-b"
                          [class.bg-red-50]="rv.isCritical"
                          [class.bg-orange-50]="rv.abnormalFlag === 'HIGH' || rv.abnormalFlag === 'LOW'">
                          <td class="p-1">{{ rv.parameterName }}</td>
                          <td class="p-1 font-medium">{{ rv.numericValue ?? rv.textValue ?? '-' }}</td>
                          <td class="p-1 text-gray-500">{{ rv.unit ?? '' }}</td>
                          <td class="p-1 text-gray-500">
                            @if (rv.referenceRangeLow != null && rv.referenceRangeHigh != null) {
                              {{ rv.referenceRangeLow }} – {{ rv.referenceRangeHigh }}
                            } @else if (rv.referenceRangeText) {
                              {{ rv.referenceRangeText }}
                            } @else {
                              -
                            }
                          </td>
                          <td class="p-1">
                            @if (rv.isCritical) {
                              <span class="text-red-700 font-bold text-xs">CRITICAL</span>
                            } @else if (rv.abnormalFlag === 'HIGH' || rv.abnormalFlag === 'LOW') {
                              <span class="text-orange-700 font-medium text-xs">{{ rv.abnormalFlag }}</span>
                            } @else if (rv.abnormalFlag === 'NORMAL') {
                              <span class="text-green-700 text-xs">NORMAL</span>
                            }
                          </td>
                        </tr>
                      }
                    </tbody>
                  </table>
                </div>
              }
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns"
            class="cursor-pointer" (click)="toggleExpand(row.id)"></tr>
          <tr mat-row *matRowDef="let row; columns: ['expandedDetail']"
            class="!h-0"></tr>
        </table>

        @if (results().length === 0) {
          <div class="text-center py-8 text-gray-400">
            <mat-icon class="!text-5xl mb-2">verified</mat-icon>
            <p>No results awaiting authorization</p>
          </div>
        }

        <mat-paginator
          [length]="totalElements()"
          [pageSize]="pageSize()"
          [pageSizeOptions]="[10, 25, 50]"
          (page)="onPage($event)"
          showFirstLastButtons
        />
      </div>
    }
  `,
})
export class ResultAuthorizationComponent implements OnInit {
  private readonly resultService = inject(ResultService);
  private readonly notification = inject(NotificationService);

  readonly results = signal<TestResult[]>([]);
  readonly loading = signal(false);
  readonly authorizing = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(20);
  readonly currentPage = signal(0);
  readonly selectedIds = signal<Set<string>>(new Set());
  readonly expandedId = signal<string | null>(null);

  readonly displayedColumns = [
    'select', 'testCode', 'testName', 'patientId',
    'departmentName', 'flags', 'validatedBy', 'actions',
  ];

  readonly allSelected = computed(() => {
    const selectableRows = this.results().filter(r => !(r.isCritical && !r.criticalAcknowledged));
    return selectableRows.length > 0 && selectableRows.every(r => this.selectedIds().has(r.id));
  });

  readonly someSelected = computed(() => {
    const ids = this.selectedIds();
    return ids.size > 0 && !this.allSelected();
  });

  readonly hasCriticalUnacknowledged = computed(() => {
    const ids = this.selectedIds();
    return this.results().some(r => ids.has(r.id) && r.isCritical && !r.criticalAcknowledged);
  });

  ngOnInit(): void {
    this.loadResults();
  }

  loadResults(): void {
    this.loading.set(true);
    this.resultService.getAll(this.currentPage(), this.pageSize()).subscribe({
      next: (response) => {
        const validated = response.data.filter(r => r.status === 'VALIDATED');
        this.results.set(validated);
        this.totalElements.set(validated.length);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  toggleRow(id: string, checked: boolean): void {
    this.selectedIds.update(ids => {
      const next = new Set(ids);
      if (checked) {
        next.add(id);
      } else {
        next.delete(id);
      }
      return next;
    });
  }

  toggleAll(checked: boolean): void {
    if (checked) {
      const selectableIds = this.results()
        .filter(r => !(r.isCritical && !r.criticalAcknowledged))
        .map(r => r.id);
      this.selectedIds.set(new Set(selectableIds));
    } else {
      this.selectedIds.set(new Set());
    }
  }

  toggleExpand(id: string): void {
    this.expandedId.update(current => current === id ? null : id);
  }

  onPage(event: PageEvent): void {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.selectedIds.set(new Set());
    this.loadResults();
  }

  onBatchAuthorize(): void {
    const ids = Array.from(this.selectedIds());
    if (ids.length === 0) return;

    this.authorizing.set(true);
    this.resultService.batchAuthorize(ids).subscribe({
      next: () => {
        this.notification.showSuccess(`${ids.length} result(s) authorized successfully`);
        this.selectedIds.set(new Set());
        this.authorizing.set(false);
        this.loadResults();
      },
      error: () => {
        this.notification.showError('Failed to authorize results');
        this.authorizing.set(false);
      },
    });
  }

  getStatusClass(status: string): string {
    return RESULT_STATUS_COLORS[status as keyof typeof RESULT_STATUS_COLORS] ?? 'bg-gray-100 text-gray-700';
  }
}
