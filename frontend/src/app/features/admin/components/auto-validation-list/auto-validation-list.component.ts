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
import { AutoValidationRule } from '../../models/admin.models';

@Component({
  selector: 'app-auto-validation-list',
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
        <h2 class="text-xl font-bold text-gray-800">Auto-Validation Rules</h2>
        <p class="text-gray-500 text-sm">Configure automatic result validation rules</p>
      </div>
      <a mat-raised-button color="primary" routerLink="new">
        <mat-icon>add</mat-icon> Add Rule
      </a>
    </div>

    <div class="bg-white rounded-lg shadow">
      @if (loading()) {
        <div class="flex justify-center p-8">
          <mat-spinner diameter="40" />
        </div>
      } @else {
        <table mat-table [dataSource]="rules()" class="w-full">
          <ng-container matColumnDef="ruleCode">
            <th mat-header-cell *matHeaderCellDef>Rule Code</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ row.ruleCode }}</td>
          </ng-container>
          <ng-container matColumnDef="ruleName">
            <th mat-header-cell *matHeaderCellDef>Rule Name</th>
            <td mat-cell *matCellDef="let row">{{ row.ruleName }}</td>
          </ng-container>
          <ng-container matColumnDef="rulePriority">
            <th mat-header-cell *matHeaderCellDef>Priority</th>
            <td mat-cell *matCellDef="let row">
              <span class="px-2 py-1 rounded-full text-xs font-medium bg-indigo-100 text-indigo-800">
                {{ row.rulePriority }}
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
              <button mat-icon-button (click)="deleteRule(row)" matTooltip="Delete" color="warn">
                <mat-icon>delete</mat-icon>
              </button>
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
        </table>

        @if (rules().length === 0) {
          <div class="text-center py-8 text-gray-400">
            <mat-icon class="!text-5xl mb-2">verified</mat-icon>
            <p>No auto-validation rules configured</p>
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
export class AutoValidationListComponent implements OnInit {
  private readonly api = inject(AdminApiService);
  private readonly notification = inject(NotificationService);
  private readonly router = inject(Router);

  readonly rules = signal<AutoValidationRule[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(10);
  readonly currentPage = signal(0);

  readonly displayedColumns = [
    'ruleCode', 'ruleName', 'rulePriority', 'isActive', 'actions',
  ];

  ngOnInit(): void {
    this.loadRules();
  }

  loadRules(): void {
    this.loading.set(true);
    this.api.list<AutoValidationRule>('config/auto-validation', {
      page: this.currentPage(),
      size: this.pageSize(),
    }).subscribe({
      next: (response) => {
        this.rules.set(response.data);
        this.totalElements.set(response.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  onPage(event: PageEvent): void {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadRules();
  }

  deleteRule(rule: AutoValidationRule): void {
    if (!confirm(`Are you sure you want to delete rule "${rule.ruleName}"?`)) {
      return;
    }
    this.api.remove('config/auto-validation', rule.id).subscribe({
      next: () => {
        this.notification.showSuccess('Auto-validation rule deleted successfully');
        this.loadRules();
      },
    });
  }
}
