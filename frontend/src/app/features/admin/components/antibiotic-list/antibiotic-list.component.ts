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
import { Antibiotic } from '../../models/admin.models';

@Component({
  selector: 'app-antibiotic-list',
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
        <h2 class="text-xl font-bold text-gray-800">Antibiotic Master</h2>
        <p class="text-gray-500 text-sm">Manage antibiotics and CLSI breakpoints</p>
      </div>
      <a mat-raised-button color="primary" routerLink="new">
        <mat-icon>add</mat-icon> Add Antibiotic
      </a>
    </div>

    <div class="bg-white rounded-lg shadow">
      <div class="flex flex-wrap gap-4 p-4 border-b">
        <mat-form-field appearance="outline" class="flex-1 min-w-[250px]">
          <mat-label>Search by name or code</mat-label>
          <input matInput (input)="onSearch($event)" placeholder="Type to search..." />
          <mat-icon matSuffix>search</mat-icon>
        </mat-form-field>
      </div>

      @if (loading()) {
        <div class="flex justify-center p-8">
          <mat-spinner diameter="40" />
        </div>
      } @else {
        <table mat-table [dataSource]="antibiotics()" class="w-full">
          <ng-container matColumnDef="antibioticCode">
            <th mat-header-cell *matHeaderCellDef>Code</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ row.antibioticCode }}</td>
          </ng-container>
          <ng-container matColumnDef="antibioticName">
            <th mat-header-cell *matHeaderCellDef>Name</th>
            <td mat-cell *matCellDef="let row">{{ row.antibioticName }}</td>
          </ng-container>
          <ng-container matColumnDef="group">
            <th mat-header-cell *matHeaderCellDef>Group</th>
            <td mat-cell *matCellDef="let row">
              <span class="px-2 py-1 rounded-full text-xs font-medium bg-indigo-100 text-indigo-800">
                {{ row.group }}
              </span>
            </td>
          </ng-container>
          <ng-container matColumnDef="clsiBreakpointS">
            <th mat-header-cell *matHeaderCellDef>CLSI S</th>
            <td mat-cell *matCellDef="let row" class="text-green-600">{{ row.clsiBreakpointS ?? '-' }}</td>
          </ng-container>
          <ng-container matColumnDef="clsiBreakpointI">
            <th mat-header-cell *matHeaderCellDef>CLSI I</th>
            <td mat-cell *matCellDef="let row" class="text-amber-600">{{ row.clsiBreakpointI ?? '-' }}</td>
          </ng-container>
          <ng-container matColumnDef="clsiBreakpointR">
            <th mat-header-cell *matHeaderCellDef>CLSI R</th>
            <td mat-cell *matCellDef="let row" class="text-red-600">{{ row.clsiBreakpointR ?? '-' }}</td>
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
              <button mat-icon-button (click)="deleteAntibiotic(row)" matTooltip="Delete" color="warn">
                <mat-icon>delete</mat-icon>
              </button>
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
        </table>

        @if (antibiotics().length === 0) {
          <div class="text-center py-8 text-gray-400">
            <mat-icon class="!text-5xl mb-2">medication</mat-icon>
            <p>No antibiotics found</p>
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
export class AntibioticListComponent implements OnInit {
  private readonly api = inject(AdminApiService);
  private readonly notification = inject(NotificationService);
  private readonly router = inject(Router);

  readonly antibiotics = signal<Antibiotic[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(10);
  readonly currentPage = signal(0);
  readonly searchTerm = signal('');

  readonly displayedColumns = [
    'antibioticCode', 'antibioticName', 'group',
    'clsiBreakpointS', 'clsiBreakpointI', 'clsiBreakpointR',
    'isActive', 'actions',
  ];

  ngOnInit(): void {
    this.loadAntibiotics();
  }

  loadAntibiotics(): void {
    this.loading.set(true);
    this.api.list<Antibiotic>('antibiotics', {
      page: this.currentPage(),
      size: this.pageSize(),
      search: this.searchTerm() || undefined,
    }).subscribe({
      next: (response) => {
        this.antibiotics.set(response.data);
        this.totalElements.set(response.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  onSearch(event: Event): void {
    this.searchTerm.set((event.target as HTMLInputElement).value);
    this.currentPage.set(0);
    this.loadAntibiotics();
  }

  onPage(event: PageEvent): void {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadAntibiotics();
  }

  deleteAntibiotic(antibiotic: Antibiotic): void {
    if (!confirm(`Are you sure you want to delete antibiotic "${antibiotic.antibioticName}"?`)) {
      return;
    }
    this.api.remove('antibiotics', antibiotic.id).subscribe({
      next: () => {
        this.notification.showSuccess('Antibiotic deleted successfully');
        this.loadAntibiotics();
      },
    });
  }
}
