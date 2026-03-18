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
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatTooltipModule } from '@angular/material/tooltip';
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationService } from '@core/services/notification.service';
import { TestMaster, Department } from '../../models/admin.models';

@Component({
  selector: 'app-test-list',
  standalone: true,
  imports: [
    RouterLink, MatTableModule, MatPaginatorModule, MatFormFieldModule,
    MatInputModule, MatSelectModule, MatButtonModule, MatIconModule,
    MatChipsModule, MatProgressSpinnerModule, MatSlideToggleModule,
    MatTooltipModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Test Master</h2>
        <p class="text-gray-500 text-sm">Manage laboratory tests and profiles</p>
      </div>
      <a mat-raised-button color="primary" routerLink="new">
        <mat-icon>add</mat-icon> Add Test
      </a>
    </div>

    <div class="bg-white rounded-lg shadow">
      <div class="flex flex-wrap gap-4 p-4 border-b">
        <mat-form-field appearance="outline" class="flex-1 min-w-[200px]">
          <mat-label>Search tests</mat-label>
          <input matInput placeholder="Search by name or code..." (input)="onSearch($event)" />
          <mat-icon matSuffix>search</mat-icon>
        </mat-form-field>
        <mat-form-field appearance="outline" class="w-56">
          <mat-label>Department</mat-label>
          <mat-select (selectionChange)="onDepartmentFilter($event.value)">
            <mat-option value="">All Departments</mat-option>
            @for (dept of departments(); track dept.id) {
              <mat-option [value]="dept.id">{{ dept.departmentName }}</mat-option>
            }
          </mat-select>
        </mat-form-field>
      </div>

      @if (loading()) {
        <div class="flex justify-center p-8">
          <mat-spinner diameter="40" />
        </div>
      } @else {
        <table mat-table [dataSource]="tests()" class="w-full">
          <ng-container matColumnDef="testCode">
            <th mat-header-cell *matHeaderCellDef>Code</th>
            <td mat-cell *matCellDef="let row">{{ row.testCode }}</td>
          </ng-container>
          <ng-container matColumnDef="testName">
            <th mat-header-cell *matHeaderCellDef>Test Name</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ row.testName }}</td>
          </ng-container>
          <ng-container matColumnDef="departmentName">
            <th mat-header-cell *matHeaderCellDef>Department</th>
            <td mat-cell *matCellDef="let row">{{ row.departmentName }}</td>
          </ng-container>
          <ng-container matColumnDef="testType">
            <th mat-header-cell *matHeaderCellDef>Type</th>
            <td mat-cell *matCellDef="let row">
              <span class="px-2 py-1 rounded-full text-xs font-medium"
                [class]="getTypeClass(row.testType)">
                {{ formatType(row.testType) }}
              </span>
            </td>
          </ng-container>
          <ng-container matColumnDef="tatHours">
            <th mat-header-cell *matHeaderCellDef>TAT (hrs)</th>
            <td mat-cell *matCellDef="let row">{{ row.tatHours ?? '-' }}</td>
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

        @if (tests().length === 0) {
          <div class="text-center py-8 text-gray-400">
            <mat-icon class="!text-5xl mb-2">science</mat-icon>
            <p>No tests found</p>
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
export class TestListComponent implements OnInit {
  private readonly api = inject(AdminApiService);
  private readonly notification = inject(NotificationService);
  private readonly router = inject(Router);

  readonly tests = signal<TestMaster[]>([]);
  readonly departments = signal<Department[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(10);
  readonly currentPage = signal(0);
  readonly searchTerm = signal('');
  readonly departmentFilter = signal('');

  readonly displayedColumns = ['testCode', 'testName', 'departmentName', 'testType', 'tatHours', 'isActive', 'actions'];

  ngOnInit(): void {
    this.loadDepartments();
    this.loadTests();
  }

  loadDepartments(): void {
    this.api.list<Department>('admin/departments', { page: 0, size: 200 }).subscribe({
      next: (response) => this.departments.set(response.data),
    });
  }

  loadTests(): void {
    this.loading.set(true);
    this.api.list<TestMaster>('tests', {
      page: this.currentPage(),
      size: this.pageSize(),
      search: this.searchTerm() || undefined,
      departmentId: this.departmentFilter() || undefined,
    }).subscribe({
      next: (response) => {
        this.tests.set(response.data);
        this.totalElements.set(response.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  onSearch(event: Event): void {
    this.searchTerm.set((event.target as HTMLInputElement).value);
    this.currentPage.set(0);
    this.loadTests();
  }

  onDepartmentFilter(departmentId: string): void {
    this.departmentFilter.set(departmentId);
    this.currentPage.set(0);
    this.loadTests();
  }

  onPage(event: PageEvent): void {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadTests();
  }

  toggleActive(test: TestMaster): void {
    this.api.update<TestMaster>('tests', test.id, { isActive: !test.isActive }).subscribe({
      next: () => {
        this.notification.showSuccess(`Test ${test.isActive ? 'deactivated' : 'activated'} successfully`);
        this.loadTests();
      },
    });
  }

  formatType(type: string): string {
    return type.replace(/_/g, ' ').replace(/\b\w/g, c => c.toUpperCase());
  }

  getTypeClass(type: string): string {
    const classes: Record<string, string> = {
      'SINGLE': 'bg-indigo-100 text-indigo-800',
      'PANEL': 'bg-blue-100 text-blue-800',
      'PROFILE': 'bg-green-100 text-green-800',
      'CALCULATED': 'bg-amber-100 text-amber-800',
    };
    return classes[type] ?? 'bg-gray-100 text-gray-800';
  }
}
