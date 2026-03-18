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
import { DiscountScheme } from '../../models/admin.models';

@Component({
  selector: 'app-discount-scheme-list',
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
        <h2 class="text-xl font-bold text-gray-800">Discount Schemes</h2>
        <p class="text-gray-500 text-sm">Manage pricing discounts and promotional schemes</p>
      </div>
      <a mat-raised-button color="primary" routerLink="new">
        <mat-icon>add</mat-icon> Add Scheme
      </a>
    </div>

    <div class="bg-white rounded-lg shadow">
      <div class="flex flex-wrap gap-4 p-4 border-b">
        <mat-form-field appearance="outline" class="flex-1 min-w-[200px]">
          <mat-label>Search schemes</mat-label>
          <input matInput placeholder="Search by name or code..." (input)="onSearch($event)" />
          <mat-icon matSuffix>search</mat-icon>
        </mat-form-field>
      </div>

      @if (loading()) {
        <div class="flex justify-center p-8">
          <mat-spinner diameter="40" />
        </div>
      } @else {
        <table mat-table [dataSource]="items()" class="w-full">
          <ng-container matColumnDef="schemeCode">
            <th mat-header-cell *matHeaderCellDef>Code</th>
            <td mat-cell *matCellDef="let row">{{ row.schemeCode }}</td>
          </ng-container>
          <ng-container matColumnDef="schemeName">
            <th mat-header-cell *matHeaderCellDef>Scheme Name</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ row.schemeName }}</td>
          </ng-container>
          <ng-container matColumnDef="applicableTo">
            <th mat-header-cell *matHeaderCellDef>Applicable To</th>
            <td mat-cell *matCellDef="let row">
              <span class="px-2 py-1 rounded-full text-xs font-medium"
                [class]="getApplicableClass(row.applicableTo)">
                {{ formatType(row.applicableTo) }}
              </span>
            </td>
          </ng-container>
          <ng-container matColumnDef="discountType">
            <th mat-header-cell *matHeaderCellDef>Type</th>
            <td mat-cell *matCellDef="let row">{{ formatType(row.discountType) }}</td>
          </ng-container>
          <ng-container matColumnDef="discountValue">
            <th mat-header-cell *matHeaderCellDef>Value</th>
            <td mat-cell *matCellDef="let row">
              {{ row.discountType === 'PERCENTAGE' ? row.discountValue + '%' : (row.discountValue | currency) }}
            </td>
          </ng-container>
          <ng-container matColumnDef="startDate">
            <th mat-header-cell *matHeaderCellDef>Start Date</th>
            <td mat-cell *matCellDef="let row">{{ row.startDate | date:'mediumDate' }}</td>
          </ng-container>
          <ng-container matColumnDef="endDate">
            <th mat-header-cell *matHeaderCellDef>End Date</th>
            <td mat-cell *matCellDef="let row">{{ row.endDate | date:'mediumDate' }}</td>
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
            <mat-icon class="!text-5xl mb-2">local_offer</mat-icon>
            <p>No discount schemes found</p>
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
export class DiscountSchemeListComponent implements OnInit {
  private readonly api = inject(AdminApiService);
  private readonly notification = inject(NotificationService);

  readonly items = signal<DiscountScheme[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(10);
  readonly currentPage = signal(0);
  readonly searchTerm = signal('');

  readonly displayedColumns = ['schemeCode', 'schemeName', 'applicableTo', 'discountType', 'discountValue', 'startDate', 'endDate', 'isActive', 'actions'];

  ngOnInit(): void {
    this.loadItems();
  }

  loadItems(): void {
    this.loading.set(true);
    this.api.list<DiscountScheme>('config/discount-schemes', {
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

  toggleActive(item: DiscountScheme): void {
    this.api.update<DiscountScheme>('config/discount-schemes', item.id, { isActive: !item.isActive }).subscribe({
      next: () => {
        this.notification.showSuccess(`Scheme ${item.isActive ? 'deactivated' : 'activated'} successfully`);
        this.loadItems();
      },
    });
  }

  formatType(type: string): string {
    return type.replace(/_/g, ' ').replace(/\b\w/g, c => c.toUpperCase());
  }

  getApplicableClass(type: string): string {
    const classes: Record<string, string> = {
      'WALK_IN': 'bg-blue-100 text-blue-800',
      'CORPORATE': 'bg-purple-100 text-purple-800',
      'INSURANCE': 'bg-amber-100 text-amber-800',
    };
    return classes[type] ?? 'bg-gray-100 text-gray-800';
  }
}
