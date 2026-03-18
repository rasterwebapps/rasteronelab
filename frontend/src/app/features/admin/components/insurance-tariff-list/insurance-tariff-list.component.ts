import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
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
import { DatePipe, CurrencyPipe } from '@angular/common';
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationService } from '@core/services/notification.service';
import { InsuranceTariff } from '../../models/admin.models';

@Component({
  selector: 'app-insurance-tariff-list',
  standalone: true,
  imports: [
    RouterLink, MatTableModule, MatPaginatorModule, MatFormFieldModule,
    MatInputModule, MatButtonModule, MatIconModule, MatChipsModule,
    MatProgressSpinnerModule, MatSlideToggleModule, MatTooltipModule,
    DatePipe, CurrencyPipe,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Insurance Tariffs</h2>
        <p class="text-gray-500 text-sm">Manage insurance company tariff rates</p>
      </div>
      <a mat-raised-button color="primary" routerLink="new">
        <mat-icon>add</mat-icon> Add Tariff
      </a>
    </div>

    <div class="bg-white rounded-lg shadow">
      <div class="flex flex-wrap gap-4 p-4 border-b">
        <mat-form-field appearance="outline" class="flex-1 min-w-[200px]">
          <mat-label>Search tariffs</mat-label>
          <input matInput placeholder="Search by insurance or plan name..." (input)="onSearch($event)" />
          <mat-icon matSuffix>search</mat-icon>
        </mat-form-field>
      </div>

      @if (loading()) {
        <div class="flex justify-center p-8">
          <mat-spinner diameter="40" />
        </div>
      } @else {
        <table mat-table [dataSource]="items()" class="w-full">
          <ng-container matColumnDef="insuranceName">
            <th mat-header-cell *matHeaderCellDef>Insurance</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ row.insuranceName }}</td>
          </ng-container>
          <ng-container matColumnDef="planName">
            <th mat-header-cell *matHeaderCellDef>Plan</th>
            <td mat-cell *matCellDef="let row">{{ row.planName }}</td>
          </ng-container>
          <ng-container matColumnDef="testName">
            <th mat-header-cell *matHeaderCellDef>Test</th>
            <td mat-cell *matCellDef="let row">{{ row.testName ?? '-' }}</td>
          </ng-container>
          <ng-container matColumnDef="tariffRate">
            <th mat-header-cell *matHeaderCellDef>Tariff Rate</th>
            <td mat-cell *matCellDef="let row">{{ row.tariffRate | currency }}</td>
          </ng-container>
          <ng-container matColumnDef="effectiveFrom">
            <th mat-header-cell *matHeaderCellDef>Effective From</th>
            <td mat-cell *matCellDef="let row">{{ row.effectiveFrom | date:'mediumDate' }}</td>
          </ng-container>
          <ng-container matColumnDef="effectiveTo">
            <th mat-header-cell *matHeaderCellDef>Effective To</th>
            <td mat-cell *matCellDef="let row">{{ row.effectiveTo ? (row.effectiveTo | date:'mediumDate') : 'Ongoing' }}</td>
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

        @if (items().length === 0) {
          <div class="text-center py-8 text-gray-400">
            <mat-icon class="!text-5xl mb-2">health_and_safety</mat-icon>
            <p>No insurance tariffs found</p>
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
export class InsuranceTariffListComponent implements OnInit {
  private readonly api = inject(AdminApiService);
  private readonly notification = inject(NotificationService);

  readonly items = signal<InsuranceTariff[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(10);
  readonly currentPage = signal(0);
  readonly searchTerm = signal('');

  readonly displayedColumns = ['insuranceName', 'planName', 'testName', 'tariffRate', 'effectiveFrom', 'effectiveTo', 'isActive', 'actions'];

  ngOnInit(): void {
    this.loadItems();
  }

  loadItems(): void {
    this.loading.set(true);
    this.api.list<InsuranceTariff>('config/insurance-tariffs', {
      page: this.currentPage(),
      size: this.pageSize(),
      search: this.searchTerm() || undefined,
    }).subscribe({
      next: (response) => {
        this.items.set(response.data);
        this.totalElements.set(response.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  onSearch(event: Event): void {
    this.searchTerm.set((event.target as HTMLInputElement).value);
    this.currentPage.set(0);
    this.loadItems();
  }

  onPage(event: PageEvent): void {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadItems();
  }

  toggleActive(item: InsuranceTariff): void {
    this.api.update<InsuranceTariff>('config/insurance-tariffs', item.id, { isActive: !item.isActive }).subscribe({
      next: () => {
        this.notification.showSuccess(`Tariff ${item.isActive ? 'deactivated' : 'activated'} successfully`);
        this.loadItems();
      },
    });
  }
}
