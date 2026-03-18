import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationService } from '@core/services/notification.service';
import { DeltaCheck } from '../../models/admin.models';

@Component({
  selector: 'app-delta-check-list',
  standalone: true,
  imports: [
    RouterLink, MatTableModule, MatPaginatorModule, MatFormFieldModule,
    MatInputModule, MatButtonModule, MatIconModule, MatChipsModule,
    MatProgressSpinnerModule, MatTooltipModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Delta Check Configuration</h2>
        <p class="text-gray-500 text-sm">Configure delta check rules for result comparison</p>
      </div>
      <a mat-raised-button color="primary" routerLink="new">
        <mat-icon>add</mat-icon> Add Delta Check
      </a>
    </div>

    <div class="bg-white rounded-lg shadow">
      @if (loading()) {
        <div class="flex justify-center p-8">
          <mat-spinner diameter="40" />
        </div>
      } @else {
        <table mat-table [dataSource]="deltaChecks()" class="w-full">
          <ng-container matColumnDef="parameterName">
            <th mat-header-cell *matHeaderCellDef>Parameter</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ row.parameterName }}</td>
          </ng-container>
          <ng-container matColumnDef="percentageThreshold">
            <th mat-header-cell *matHeaderCellDef>Threshold (%)</th>
            <td mat-cell *matCellDef="let row">{{ row.percentageThreshold }}%</td>
          </ng-container>
          <ng-container matColumnDef="timeWindowDays">
            <th mat-header-cell *matHeaderCellDef>Time Window (days)</th>
            <td mat-cell *matCellDef="let row">{{ row.timeWindowDays }}</td>
          </ng-container>
          <ng-container matColumnDef="flagAction">
            <th mat-header-cell *matHeaderCellDef>Flag Action</th>
            <td mat-cell *matCellDef="let row">
              <span class="px-2 py-1 rounded-full text-xs font-medium"
                [class]="getFlagActionClass(row.flagAction)">
                {{ row.flagAction }}
              </span>
            </td>
          </ng-container>
          <ng-container matColumnDef="isActive">
            <th mat-header-cell *matHeaderCellDef>Status</th>
            <td mat-cell *matCellDef="let row">
              <span class="px-2 py-1 rounded-full text-xs font-medium"
                [class]="row.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'">
                {{ row.isActive ? 'Active' : 'Inactive' }}
              </span>
            </td>
          </ng-container>
          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef>Actions</th>
            <td mat-cell *matCellDef="let row">
              <a mat-icon-button [routerLink]="[row.id, 'edit']" matTooltip="Edit">
                <mat-icon>edit</mat-icon>
              </a>
              <button mat-icon-button (click)="deleteDeltaCheck(row)" matTooltip="Delete" color="warn">
                <mat-icon>delete</mat-icon>
              </button>
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
        </table>

        @if (deltaChecks().length === 0) {
          <div class="text-center py-8 text-gray-400">
            <mat-icon class="!text-5xl mb-2">compare_arrows</mat-icon>
            <p>No delta checks configured</p>
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
export class DeltaCheckListComponent implements OnInit {
  private readonly api = inject(AdminApiService);
  private readonly notification = inject(NotificationService);
  private readonly router = inject(Router);

  readonly deltaChecks = signal<DeltaCheck[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(10);
  readonly currentPage = signal(0);

  readonly displayedColumns = [
    'parameterName', 'percentageThreshold', 'timeWindowDays',
    'flagAction', 'isActive', 'actions',
  ];

  ngOnInit(): void {
    this.loadDeltaChecks();
  }

  loadDeltaChecks(): void {
    this.loading.set(true);
    this.api.list<DeltaCheck>('config/delta-checks', {
      page: this.currentPage(),
      size: this.pageSize(),
    }).subscribe({
      next: (response) => {
        this.deltaChecks.set(response.data);
        this.totalElements.set(response.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  onPage(event: PageEvent): void {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadDeltaChecks();
  }

  deleteDeltaCheck(dc: DeltaCheck): void {
    if (!confirm(`Are you sure you want to delete this delta check for "${dc.parameterName}"?`)) {
      return;
    }
    this.api.remove('config/delta-checks', dc.id).subscribe({
      next: () => {
        this.notification.showSuccess('Delta check deleted successfully');
        this.loadDeltaChecks();
      },
    });
  }

  getFlagActionClass(action: string): string {
    const classes: Record<string, string> = {
      'WARN': 'bg-amber-100 text-amber-800',
      'BLOCK': 'bg-red-100 text-red-800',
      'REQUIRE_APPROVAL': 'bg-blue-100 text-blue-800',
    };
    return classes[action] ?? 'bg-gray-100 text-gray-800';
  }
}
