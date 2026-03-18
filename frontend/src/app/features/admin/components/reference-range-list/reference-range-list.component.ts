import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationService } from '@core/services/notification.service';
import { ReferenceRange, ParameterMaster } from '../../models/admin.models';

@Component({
  selector: 'app-reference-range-list',
  standalone: true,
  imports: [
    RouterLink, MatTableModule, MatPaginatorModule, MatFormFieldModule,
    MatInputModule, MatSelectModule, MatButtonModule, MatIconModule,
    MatChipsModule, MatProgressSpinnerModule, MatTooltipModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Reference Ranges</h2>
        <p class="text-gray-500 text-sm">Configure normal and critical ranges per parameter</p>
      </div>
      <a mat-raised-button color="primary" routerLink="new">
        <mat-icon>add</mat-icon> Add Range
      </a>
    </div>

    <div class="bg-white rounded-lg shadow">
      <div class="flex flex-wrap gap-4 p-4 border-b">
        <mat-form-field appearance="outline" class="flex-1 min-w-[250px]">
          <mat-label>Filter by Parameter</mat-label>
          <mat-select (selectionChange)="onParameterFilter($event.value)">
            <mat-option value="">All Parameters</mat-option>
            @for (param of parameterOptions(); track param.id) {
              <mat-option [value]="param.id">{{ param.paramName }} ({{ param.paramCode }})</mat-option>
            }
          </mat-select>
        </mat-form-field>
      </div>

      @if (loading()) {
        <div class="flex justify-center p-8">
          <mat-spinner diameter="40" />
        </div>
      } @else {
        <table mat-table [dataSource]="ranges()" class="w-full">
          <ng-container matColumnDef="parameterName">
            <th mat-header-cell *matHeaderCellDef>Parameter</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ row.parameterName }}</td>
          </ng-container>
          <ng-container matColumnDef="gender">
            <th mat-header-cell *matHeaderCellDef>Gender</th>
            <td mat-cell *matCellDef="let row">
              <span class="px-2 py-1 rounded-full text-xs font-medium"
                [class]="getGenderClass(row.gender)">
                {{ row.gender }}
              </span>
            </td>
          </ng-container>
          <ng-container matColumnDef="ageMinDays">
            <th mat-header-cell *matHeaderCellDef>Age Min (days)</th>
            <td mat-cell *matCellDef="let row">{{ row.ageMinDays }}</td>
          </ng-container>
          <ng-container matColumnDef="ageMaxDays">
            <th mat-header-cell *matHeaderCellDef>Age Max (days)</th>
            <td mat-cell *matCellDef="let row">{{ row.ageMaxDays }}</td>
          </ng-container>
          <ng-container matColumnDef="normalLow">
            <th mat-header-cell *matHeaderCellDef>Normal Low</th>
            <td mat-cell *matCellDef="let row">{{ row.normalLow ?? '-' }}</td>
          </ng-container>
          <ng-container matColumnDef="normalHigh">
            <th mat-header-cell *matHeaderCellDef>Normal High</th>
            <td mat-cell *matCellDef="let row">{{ row.normalHigh ?? '-' }}</td>
          </ng-container>
          <ng-container matColumnDef="criticalLow">
            <th mat-header-cell *matHeaderCellDef>Critical Low</th>
            <td mat-cell *matCellDef="let row" class="text-red-600">{{ row.criticalLow ?? '-' }}</td>
          </ng-container>
          <ng-container matColumnDef="criticalHigh">
            <th mat-header-cell *matHeaderCellDef>Critical High</th>
            <td mat-cell *matCellDef="let row" class="text-red-600">{{ row.criticalHigh ?? '-' }}</td>
          </ng-container>
          <ng-container matColumnDef="interpretation">
            <th mat-header-cell *matHeaderCellDef>Interpretation</th>
            <td mat-cell *matCellDef="let row">{{ row.interpretation ?? '-' }}</td>
          </ng-container>
          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef>Actions</th>
            <td mat-cell *matCellDef="let row">
              <a mat-icon-button [routerLink]="[row.id, 'edit']" matTooltip="Edit">
                <mat-icon>edit</mat-icon>
              </a>
              <button mat-icon-button (click)="deleteRange(row)" matTooltip="Delete" color="warn">
                <mat-icon>delete</mat-icon>
              </button>
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
        </table>

        @if (ranges().length === 0) {
          <div class="text-center py-8 text-gray-400">
            <mat-icon class="!text-5xl mb-2">straighten</mat-icon>
            <p>No reference ranges found</p>
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
export class ReferenceRangeListComponent implements OnInit {
  private readonly api = inject(AdminApiService);
  private readonly notification = inject(NotificationService);
  private readonly router = inject(Router);

  readonly ranges = signal<ReferenceRange[]>([]);
  readonly parameterOptions = signal<ParameterMaster[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(10);
  readonly currentPage = signal(0);
  readonly parameterFilter = signal('');

  readonly displayedColumns = [
    'parameterName', 'gender', 'ageMinDays', 'ageMaxDays',
    'normalLow', 'normalHigh', 'criticalLow', 'criticalHigh',
    'interpretation', 'actions',
  ];

  ngOnInit(): void {
    this.loadParameterOptions();
    this.loadRanges();
  }

  loadParameterOptions(): void {
    this.api.list<ParameterMaster>('parameters', { page: 0, size: 500 }).subscribe({
      next: (response) => this.parameterOptions.set(response.data),
    });
  }

  loadRanges(): void {
    this.loading.set(true);
    this.api.list<ReferenceRange>('reference-ranges', {
      page: this.currentPage(),
      size: this.pageSize(),
      parameterId: this.parameterFilter() || undefined,
    }).subscribe({
      next: (response) => {
        this.ranges.set(response.data);
        this.totalElements.set(response.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  onParameterFilter(parameterId: string): void {
    this.parameterFilter.set(parameterId);
    this.currentPage.set(0);
    this.loadRanges();
  }

  onPage(event: PageEvent): void {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadRanges();
  }

  deleteRange(range: ReferenceRange): void {
    if (!confirm('Are you sure you want to delete this reference range? This may affect test result interpretation.')) {
      return;
    }
    this.api.remove('reference-ranges', range.id).subscribe({
      next: () => {
        this.notification.showSuccess('Reference range deleted successfully');
        this.loadRanges();
      },
    });
  }

  getGenderClass(gender: string): string {
    const classes: Record<string, string> = {
      'MALE': 'bg-blue-100 text-blue-800',
      'FEMALE': 'bg-pink-100 text-pink-800',
      'ALL': 'bg-gray-100 text-gray-800',
    };
    return classes[gender] ?? 'bg-gray-100 text-gray-800';
  }
}
