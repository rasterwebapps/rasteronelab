import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { SlicePipe } from '@angular/common';
import { ResultService } from '../../services/result.service';
import { TestResult, ResultStatus, RESULT_STATUS_COLORS } from '../../models/result.model';

@Component({
  selector: 'app-result-list',
  standalone: true,
  imports: [
    RouterLink, MatTableModule, MatPaginatorModule, MatFormFieldModule,
    MatInputModule, MatButtonModule, MatButtonToggleModule, MatIconModule,
    MatChipsModule, MatProgressSpinnerModule, MatTooltipModule, SlicePipe,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Test Results</h2>
        <p class="text-gray-500 text-sm">Manage test result entries and validations</p>
      </div>
      <div class="flex gap-2">
        <a mat-stroked-button routerLink="worklist">
          <mat-icon>assignment</mat-icon> Worklist
        </a>
        <a mat-raised-button color="primary" routerLink="authorize">
          <mat-icon>verified</mat-icon> Authorization Queue
        </a>
      </div>
    </div>

    <div class="bg-white rounded-lg shadow">
      <div class="flex flex-wrap gap-4 p-4 border-b items-center">
        <mat-form-field appearance="outline" class="flex-1 min-w-[200px]">
          <mat-label>Search results</mat-label>
          <input matInput placeholder="Search by test code or name..." (input)="onSearch($event)" />
          <mat-icon matSuffix>search</mat-icon>
        </mat-form-field>

        <mat-button-toggle-group [value]="statusFilter()" (change)="onStatusFilter($event.value)" appearance="standard">
          <mat-button-toggle value="ALL">All</mat-button-toggle>
          <mat-button-toggle value="PENDING">Pending</mat-button-toggle>
          <mat-button-toggle value="ENTERED">Entered</mat-button-toggle>
          <mat-button-toggle value="VALIDATED">Validated</mat-button-toggle>
          <mat-button-toggle value="AUTHORIZED">Authorized</mat-button-toggle>
        </mat-button-toggle-group>
      </div>

      @if (loading()) {
        <div class="flex justify-center p-8">
          <mat-spinner diameter="40" />
        </div>
      } @else {
        <table mat-table [dataSource]="filteredResults()" class="w-full">
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

          <ng-container matColumnDef="status">
            <th mat-header-cell *matHeaderCellDef>Status</th>
            <td mat-cell *matCellDef="let row">
              <span class="px-2 py-1 rounded-full text-xs font-medium" [class]="getStatusClass(row.status)">
                {{ row.status }}
              </span>
              @if (row.isCritical) {
                <span class="ml-1 px-2 py-1 rounded-full text-xs font-medium bg-red-100 text-red-700">CRITICAL</span>
              }
            </td>
          </ng-container>

          <ng-container matColumnDef="enteredBy">
            <th mat-header-cell *matHeaderCellDef>Entered By</th>
            <td mat-cell *matCellDef="let row">{{ row.enteredBy ?? '-' }}</td>
          </ng-container>

          <ng-container matColumnDef="enteredAt">
            <th mat-header-cell *matHeaderCellDef>Entered At</th>
            <td mat-cell *matCellDef="let row">{{ row.enteredAt ?? '-' }}</td>
          </ng-container>

          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef>Actions</th>
            <td mat-cell *matCellDef="let row">
              <a mat-icon-button [routerLink]="[row.id]" matTooltip="View Details">
                <mat-icon>visibility</mat-icon>
              </a>
              @if (row.status === 'PENDING') {
                <a mat-icon-button [routerLink]="[row.id, 'enter']" matTooltip="Enter Results">
                  <mat-icon>edit_note</mat-icon>
                </a>
              }
              @if (row.status === 'ENTERED') {
                <button mat-icon-button matTooltip="Validate" (click)="onValidate(row)">
                  <mat-icon>check_circle</mat-icon>
                </button>
              }
              @if (row.status === 'VALIDATED') {
                <button mat-icon-button matTooltip="Authorize" (click)="onAuthorize(row)">
                  <mat-icon>verified</mat-icon>
                </button>
              }
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
        </table>

        @if (filteredResults().length === 0) {
          <div class="text-center py-8 text-gray-400">
            <mat-icon class="!text-5xl mb-2">science</mat-icon>
            <p>No results found</p>
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
export class ResultListComponent implements OnInit {
  private readonly resultService = inject(ResultService);

  readonly results = signal<TestResult[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(20);
  readonly currentPage = signal(0);
  readonly searchTerm = signal('');
  readonly statusFilter = signal<string>('ALL');

  readonly displayedColumns = [
    'testCode', 'testName', 'patientId', 'departmentName',
    'status', 'enteredBy', 'enteredAt', 'actions',
  ];

  ngOnInit(): void {
    this.loadResults();
  }

  loadResults(): void {
    this.loading.set(true);
    this.resultService.getAll(this.currentPage(), this.pageSize()).subscribe({
      next: (response) => {
        this.results.set(response.data);
        this.totalElements.set(response.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  filteredResults(): TestResult[] {
    let data = this.results();
    const term = this.searchTerm().toLowerCase();
    if (term) {
      data = data.filter(r =>
        r.testCode.toLowerCase().includes(term) ||
        r.testName.toLowerCase().includes(term),
      );
    }
    const status = this.statusFilter();
    if (status !== 'ALL') {
      data = data.filter(r => r.status === status);
    }
    return data;
  }

  onSearch(event: Event): void {
    this.searchTerm.set((event.target as HTMLInputElement).value);
  }

  onStatusFilter(value: string): void {
    this.statusFilter.set(value);
  }

  onPage(event: PageEvent): void {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadResults();
  }

  onValidate(result: TestResult): void {
    this.resultService.validateResult(result.id).subscribe({
      next: () => this.loadResults(),
    });
  }

  onAuthorize(result: TestResult): void {
    this.resultService.authorizeResult(result.id).subscribe({
      next: () => this.loadResults(),
    });
  }

  getStatusClass(status: ResultStatus): string {
    return RESULT_STATUS_COLORS[status] ?? 'bg-gray-100 text-gray-700';
  }
}
