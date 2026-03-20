import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
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
import { SlicePipe } from '@angular/common';
import { ResultService } from '../../services/result.service';
import { TestResult, ResultStatus, RESULT_STATUS_COLORS } from '../../models/result.model';

@Component({
  selector: 'app-result-worklist',
  standalone: true,
  imports: [
    RouterLink, MatTableModule, MatPaginatorModule, MatFormFieldModule,
    MatInputModule, MatSelectModule, MatButtonModule, MatIconModule,
    MatChipsModule, MatProgressSpinnerModule, MatTooltipModule, SlicePipe,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Result Worklist</h2>
        <p class="text-gray-500 text-sm">Pending and in-progress results by department</p>
      </div>
      <a mat-button routerLink="/results">
        <mat-icon>arrow_back</mat-icon> Back to Results
      </a>
    </div>

    <div class="bg-white rounded-lg shadow">
      <div class="flex flex-wrap gap-4 p-4 border-b items-center">
        <mat-form-field appearance="outline" class="w-64">
          <mat-label>Department</mat-label>
          <mat-select (selectionChange)="onDepartmentChange($event.value)" [value]="departmentId()">
            <mat-option value="">All Departments</mat-option>
            <mat-option value="biochemistry">Biochemistry</mat-option>
            <mat-option value="hematology">Hematology</mat-option>
            <mat-option value="microbiology">Microbiology</mat-option>
            <mat-option value="pathology">Pathology</mat-option>
            <mat-option value="immunology">Immunology</mat-option>
          </mat-select>
        </mat-form-field>
      </div>

      @if (loading()) {
        <div class="flex justify-center p-8">
          <mat-spinner diameter="40" />
        </div>
      } @else {
        <table mat-table [dataSource]="results()" class="w-full">
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

          <ng-container matColumnDef="sampleId">
            <th mat-header-cell *matHeaderCellDef>Sample</th>
            <td mat-cell *matCellDef="let row">{{ row.sampleId ?? '-' }}</td>
          </ng-container>

          <ng-container matColumnDef="status">
            <th mat-header-cell *matHeaderCellDef>Status</th>
            <td mat-cell *matCellDef="let row">
              <span class="px-2 py-1 rounded-full text-xs font-medium" [class]="getStatusClass(row.status)">
                {{ row.status }}
              </span>
            </td>
          </ng-container>

          <ng-container matColumnDef="createdAt">
            <th mat-header-cell *matHeaderCellDef>Ordered At</th>
            <td mat-cell *matCellDef="let row">{{ row.createdAt }}</td>
          </ng-container>

          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef>Actions</th>
            <td mat-cell *matCellDef="let row">
              <a mat-icon-button [routerLink]="['/results', row.id]" matTooltip="View">
                <mat-icon>visibility</mat-icon>
              </a>
              @if (row.status === 'PENDING') {
                <a mat-icon-button [routerLink]="['/results', row.id, 'enter']" matTooltip="Enter Results">
                  <mat-icon>edit_note</mat-icon>
                </a>
              }
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
        </table>

        @if (results().length === 0) {
          <div class="text-center py-8 text-gray-400">
            <mat-icon class="!text-5xl mb-2">assignment</mat-icon>
            <p>No results in worklist</p>
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
export class ResultWorklistComponent implements OnInit {
  private readonly resultService = inject(ResultService);

  readonly results = signal<TestResult[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(20);
  readonly currentPage = signal(0);
  readonly departmentId = signal('');

  readonly displayedColumns = [
    'testCode', 'testName', 'patientId', 'sampleId',
    'status', 'createdAt', 'actions',
  ];

  ngOnInit(): void {
    this.loadResults();
  }

  loadResults(): void {
    this.loading.set(true);
    const dept = this.departmentId();
    const request$ = dept
      ? this.resultService.getWorklist(dept, this.currentPage(), this.pageSize())
      : this.resultService.getAll(this.currentPage(), this.pageSize());

    request$.subscribe({
      next: (response) => {
        const pending = dept ? response.data : response.data.filter(r => r.status === 'PENDING' || r.status === 'ENTERED');
        this.results.set(pending);
        this.totalElements.set(dept ? response.totalElements : pending.length);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  onDepartmentChange(departmentId: string): void {
    this.departmentId.set(departmentId);
    this.currentPage.set(0);
    this.loadResults();
  }

  onPage(event: PageEvent): void {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadResults();
  }

  getStatusClass(status: ResultStatus): string {
    return RESULT_STATUS_COLORS[status] ?? 'bg-gray-100 text-gray-700';
  }
}
