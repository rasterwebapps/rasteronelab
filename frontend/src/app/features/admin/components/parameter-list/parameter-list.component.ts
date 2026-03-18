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
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatTooltipModule } from '@angular/material/tooltip';
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationService } from '@core/services/notification.service';
import { ParameterMaster } from '../../models/admin.models';

@Component({
  selector: 'app-parameter-list',
  standalone: true,
  imports: [
    RouterLink, MatTableModule, MatPaginatorModule, MatFormFieldModule,
    MatInputModule, MatButtonModule, MatIconModule, MatChipsModule,
    MatProgressSpinnerModule, MatSlideToggleModule, MatTooltipModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Parameter Master</h2>
        <p class="text-gray-500 text-sm">Manage test parameters and analytes</p>
      </div>
      <a mat-raised-button color="primary" routerLink="new">
        <mat-icon>add</mat-icon> Add Parameter
      </a>
    </div>

    <div class="bg-white rounded-lg shadow">
      <div class="flex flex-wrap gap-4 p-4 border-b">
        <mat-form-field appearance="outline" class="flex-1 min-w-[200px]">
          <mat-label>Search parameters</mat-label>
          <input matInput placeholder="Search by name or code..." (input)="onSearch($event)" />
          <mat-icon matSuffix>search</mat-icon>
        </mat-form-field>
      </div>

      @if (loading()) {
        <div class="flex justify-center p-8">
          <mat-spinner diameter="40" />
        </div>
      } @else {
        <table mat-table [dataSource]="parameters()" class="w-full">
          <ng-container matColumnDef="paramCode">
            <th mat-header-cell *matHeaderCellDef>Code</th>
            <td mat-cell *matCellDef="let row">{{ row.paramCode }}</td>
          </ng-container>
          <ng-container matColumnDef="paramName">
            <th mat-header-cell *matHeaderCellDef>Parameter Name</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ row.paramName }}</td>
          </ng-container>
          <ng-container matColumnDef="testName">
            <th mat-header-cell *matHeaderCellDef>Test</th>
            <td mat-cell *matCellDef="let row">{{ row.testName }}</td>
          </ng-container>
          <ng-container matColumnDef="dataType">
            <th mat-header-cell *matHeaderCellDef>Data Type</th>
            <td mat-cell *matCellDef="let row">
              <span class="px-2 py-1 rounded-full text-xs font-medium"
                [class]="getDataTypeClass(row.dataType)">
                {{ formatType(row.dataType) }}
              </span>
            </td>
          </ng-container>
          <ng-container matColumnDef="unitCode">
            <th mat-header-cell *matHeaderCellDef>Unit</th>
            <td mat-cell *matCellDef="let row">{{ row.unitCode ?? '-' }}</td>
          </ng-container>
          <ng-container matColumnDef="isActive">
            <th mat-header-cell *matHeaderCellDef>Active</th>
            <td mat-cell *matCellDef="let row">
              <mat-slide-toggle [checked]="row.isActive" (change)="toggleActive(row)" />
            </td>
          </ng-container>
          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef>Actions</th>
            <td mat-cell *matCellDef="let row">
              <a mat-icon-button [routerLink]="[row.id, 'edit']" matTooltip="Edit">
                <mat-icon>edit</mat-icon>
              </a>
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
        </table>

        @if (parameters().length === 0) {
          <div class="text-center py-8 text-gray-400">
            <mat-icon class="!text-5xl mb-2">tune</mat-icon>
            <p>No parameters found</p>
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
export class ParameterListComponent implements OnInit {
  private readonly api = inject(AdminApiService);
  private readonly notification = inject(NotificationService);
  private readonly router = inject(Router);

  readonly parameters = signal<ParameterMaster[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(10);
  readonly currentPage = signal(0);
  readonly searchTerm = signal('');

  readonly displayedColumns = ['paramCode', 'paramName', 'testName', 'dataType', 'unitCode', 'isActive', 'actions'];

  ngOnInit(): void {
    this.loadParameters();
  }

  loadParameters(): void {
    this.loading.set(true);
    this.api.list<ParameterMaster>('parameters', {
      page: this.currentPage(),
      size: this.pageSize(),
      search: this.searchTerm() || undefined,
    }).subscribe({
      next: (response) => {
        this.parameters.set(response.data);
        this.totalElements.set(response.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  onSearch(event: Event): void {
    this.searchTerm.set((event.target as HTMLInputElement).value);
    this.currentPage.set(0);
    this.loadParameters();
  }

  onPage(event: PageEvent): void {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadParameters();
  }

  toggleActive(param: ParameterMaster): void {
    this.api.update<ParameterMaster>('parameters', param.id, { isActive: !param.isActive }).subscribe({
      next: () => {
        this.notification.showSuccess(`Parameter ${param.isActive ? 'deactivated' : 'activated'} successfully`);
        this.loadParameters();
      },
    });
  }

  formatType(type: string): string {
    return type.replace(/_/g, ' ').replace(/\b\w/g, c => c.toUpperCase());
  }

  getDataTypeClass(type: string): string {
    const classes: Record<string, string> = {
      'NUMERIC': 'bg-indigo-100 text-indigo-800',
      'TEXT': 'bg-blue-100 text-blue-800',
      'OPTION': 'bg-green-100 text-green-800',
      'FORMULA': 'bg-amber-100 text-amber-800',
      'SEMI_QUANTITATIVE': 'bg-purple-100 text-purple-800',
    };
    return classes[type] ?? 'bg-gray-100 text-gray-800';
  }
}
