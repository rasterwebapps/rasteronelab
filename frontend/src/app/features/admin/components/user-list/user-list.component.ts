import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatTooltipModule } from '@angular/material/tooltip';
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationService } from '@core/services/notification.service';
import { User } from '../../models/admin.models';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [
    RouterLink, MatTableModule, MatPaginatorModule, MatFormFieldModule,
    MatInputModule, MatSelectModule, MatButtonModule, MatIconModule,
    MatProgressSpinnerModule, MatSlideToggleModule, MatTooltipModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">User Management</h2>
        <p class="text-gray-500 text-sm">Manage system users and their access</p>
      </div>
      <a mat-raised-button color="primary" routerLink="new">
        <mat-icon>add</mat-icon> Add User
      </a>
    </div>

    <div class="bg-white rounded-lg shadow">
      <div class="flex flex-wrap gap-4 p-4 border-b">
        <mat-form-field appearance="outline" class="flex-1 min-w-[200px]">
          <mat-label>Search users</mat-label>
          <input matInput placeholder="Search by name or email..." (input)="onSearch($event)" />
          <mat-icon matSuffix>search</mat-icon>
        </mat-form-field>
        <mat-form-field appearance="outline" class="w-56">
          <mat-label>Role</mat-label>
          <mat-select (selectionChange)="onRoleFilter($event.value)">
            <mat-option value="">All Roles</mat-option>
            @for (role of roles; track role) {
              <mat-option [value]="role">{{ formatRole(role) }}</mat-option>
            }
          </mat-select>
        </mat-form-field>
      </div>

      @if (loading()) {
        <div class="flex justify-center p-8">
          <mat-spinner diameter="40" />
        </div>
      } @else {
        <table mat-table [dataSource]="users()" class="w-full">
          <ng-container matColumnDef="username">
            <th mat-header-cell *matHeaderCellDef>Username</th>
            <td mat-cell *matCellDef="let row">{{ row.username }}</td>
          </ng-container>
          <ng-container matColumnDef="name">
            <th mat-header-cell *matHeaderCellDef>Name</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ row.firstName }} {{ row.lastName }}</td>
          </ng-container>
          <ng-container matColumnDef="email">
            <th mat-header-cell *matHeaderCellDef>Email</th>
            <td mat-cell *matCellDef="let row">{{ row.email }}</td>
          </ng-container>
          <ng-container matColumnDef="role">
            <th mat-header-cell *matHeaderCellDef>Role</th>
            <td mat-cell *matCellDef="let row">
              <span class="px-2 py-1 rounded-full text-xs font-medium" [class]="getRoleClass(row.role)">
                {{ formatRole(row.role) }}
              </span>
            </td>
          </ng-container>
          <ng-container matColumnDef="designation">
            <th mat-header-cell *matHeaderCellDef>Designation</th>
            <td mat-cell *matCellDef="let row">{{ row.designation ?? '—' }}</td>
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

        @if (users().length === 0) {
          <div class="text-center py-8 text-gray-400">
            <mat-icon class="!text-5xl mb-2">people</mat-icon>
            <p>No users found</p>
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
export class UserListComponent implements OnInit {
  private readonly api = inject(AdminApiService);
  private readonly notification = inject(NotificationService);
  private readonly router = inject(Router);

  readonly users = signal<User[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(10);
  readonly currentPage = signal(0);
  readonly searchTerm = signal('');
  readonly roleFilter = signal('');

  readonly displayedColumns = ['username', 'name', 'email', 'role', 'designation', 'isActive', 'actions'];

  readonly roles = [
    'SUPER_ADMIN', 'ORG_ADMIN', 'BRANCH_ADMIN', 'PATHOLOGIST',
    'LAB_TECHNICIAN', 'RECEPTIONIST', 'PHLEBOTOMIST', 'BILLING_STAFF',
  ];

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading.set(true);
    this.api.list<User>('users', {
      page: this.currentPage(),
      size: this.pageSize(),
      search: this.searchTerm() || undefined,
      role: this.roleFilter() || undefined,
    }).subscribe({
      next: (response) => {
        this.users.set(response.data);
        this.totalElements.set(response.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  onSearch(event: Event): void {
    this.searchTerm.set((event.target as HTMLInputElement).value);
    this.currentPage.set(0);
    this.loadUsers();
  }

  onRoleFilter(role: string): void {
    this.roleFilter.set(role);
    this.currentPage.set(0);
    this.loadUsers();
  }

  onPage(event: PageEvent): void {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadUsers();
  }

  toggleActive(user: User): void {
    this.api.update<User>('users', user.id, { isActive: !user.isActive }).subscribe({
      next: () => {
        this.notification.showSuccess(`User ${user.isActive ? 'deactivated' : 'activated'} successfully`);
        this.loadUsers();
      },
    });
  }

  formatRole(role: string): string {
    return role.replace(/_/g, ' ').replace(/\b\w/g, c => c.toUpperCase());
  }

  getRoleClass(role: string): string {
    const classes: Record<string, string> = {
      'SUPER_ADMIN': 'bg-red-100 text-red-800',
      'ORG_ADMIN': 'bg-purple-100 text-purple-800',
      'BRANCH_ADMIN': 'bg-indigo-100 text-indigo-800',
      'PATHOLOGIST': 'bg-blue-100 text-blue-800',
      'LAB_TECHNICIAN': 'bg-cyan-100 text-cyan-800',
      'RECEPTIONIST': 'bg-green-100 text-green-800',
      'PHLEBOTOMIST': 'bg-teal-100 text-teal-800',
      'BILLING_STAFF': 'bg-amber-100 text-amber-800',
    };
    return classes[role] ?? 'bg-gray-100 text-gray-800';
  }
}
