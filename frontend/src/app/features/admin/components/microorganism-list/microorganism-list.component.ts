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
import { Microorganism } from '../../models/admin.models';

@Component({
  selector: 'app-microorganism-list',
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
        <h2 class="text-xl font-bold text-gray-800">Microorganism Master</h2>
        <p class="text-gray-500 text-sm">Manage microorganisms for culture & sensitivity</p>
      </div>
      <a mat-raised-button color="primary" routerLink="new">
        <mat-icon>add</mat-icon> Add Microorganism
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
        <table mat-table [dataSource]="microorganisms()" class="w-full">
          <ng-container matColumnDef="organismCode">
            <th mat-header-cell *matHeaderCellDef>Code</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ row.organismCode }}</td>
          </ng-container>
          <ng-container matColumnDef="organismName">
            <th mat-header-cell *matHeaderCellDef>Name</th>
            <td mat-cell *matCellDef="let row">{{ row.organismName }}</td>
          </ng-container>
          <ng-container matColumnDef="gramType">
            <th mat-header-cell *matHeaderCellDef>Gram Type</th>
            <td mat-cell *matCellDef="let row">
              <span class="px-2 py-1 rounded-full text-xs font-medium"
                [class]="getGramTypeClass(row.gramType)">
                {{ row.gramType }}
              </span>
            </td>
          </ng-container>
          <ng-container matColumnDef="clinicalSignificance">
            <th mat-header-cell *matHeaderCellDef>Clinical Significance</th>
            <td mat-cell *matCellDef="let row">{{ row.clinicalSignificance ?? '-' }}</td>
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
              <button mat-icon-button (click)="deleteMicroorganism(row)" matTooltip="Delete" color="warn">
                <mat-icon>delete</mat-icon>
              </button>
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
        </table>

        @if (microorganisms().length === 0) {
          <div class="text-center py-8 text-gray-400">
            <mat-icon class="!text-5xl mb-2">biotech</mat-icon>
            <p>No microorganisms found</p>
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
export class MicroorganismListComponent implements OnInit {
  private readonly api = inject(AdminApiService);
  private readonly notification = inject(NotificationService);
  private readonly router = inject(Router);

  readonly microorganisms = signal<Microorganism[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(10);
  readonly currentPage = signal(0);
  readonly searchTerm = signal('');

  readonly displayedColumns = [
    'organismCode', 'organismName', 'gramType',
    'clinicalSignificance', 'isActive', 'actions',
  ];

  ngOnInit(): void {
    this.loadMicroorganisms();
  }

  loadMicroorganisms(): void {
    this.loading.set(true);
    this.api.list<Microorganism>('microorganisms', {
      page: this.currentPage(),
      size: this.pageSize(),
      search: this.searchTerm() || undefined,
    }).subscribe({
      next: (response) => {
        this.microorganisms.set(response.data);
        this.totalElements.set(response.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  onSearch(event: Event): void {
    this.searchTerm.set((event.target as HTMLInputElement).value);
    this.currentPage.set(0);
    this.loadMicroorganisms();
  }

  onPage(event: PageEvent): void {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadMicroorganisms();
  }

  deleteMicroorganism(organism: Microorganism): void {
    if (!confirm(`Are you sure you want to delete microorganism "${organism.organismName}"?`)) {
      return;
    }
    this.api.remove('microorganisms', organism.id).subscribe({
      next: () => {
        this.notification.showSuccess('Microorganism deleted successfully');
        this.loadMicroorganisms();
      },
    });
  }

  getGramTypeClass(gramType: string): string {
    const classes: Record<string, string> = {
      'GRAM_POSITIVE': 'bg-purple-100 text-purple-800',
      'GRAM_NEGATIVE': 'bg-red-100 text-red-800',
      'FUNGAL': 'bg-green-100 text-green-800',
      'ACID_FAST': 'bg-amber-100 text-amber-800',
    };
    return classes[gramType] ?? 'bg-gray-100 text-gray-800';
  }
}
