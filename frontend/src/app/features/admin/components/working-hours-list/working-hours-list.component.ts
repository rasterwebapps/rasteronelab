import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AdminApiService } from '../../services/admin-api.service';
import { WorkingHours } from '../../models/admin.models';

@Component({
  selector: 'app-working-hours-list',
  standalone: true,
  imports: [
    RouterLink, MatTableModule, MatButtonModule, MatIconModule,
    MatChipsModule, MatProgressSpinnerModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Working Hours</h2>
        <p class="text-gray-500 text-sm">Configure branch operating hours for each day of the week</p>
      </div>
      <a mat-raised-button color="primary" routerLink="edit">
        <mat-icon>edit</mat-icon> Edit All
      </a>
    </div>

    <div class="bg-white rounded-lg shadow">
      @if (loading()) {
        <div class="flex justify-center p-8">
          <mat-spinner diameter="40" />
        </div>
      } @else {
        <table mat-table [dataSource]="items()" class="w-full">
          <ng-container matColumnDef="dayOfWeek">
            <th mat-header-cell *matHeaderCellDef>Day</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ formatDay(row.dayOfWeek) }}</td>
          </ng-container>
          <ng-container matColumnDef="openTime">
            <th mat-header-cell *matHeaderCellDef>Open Time</th>
            <td mat-cell *matCellDef="let row">{{ row.isClosed ? '-' : row.openTime }}</td>
          </ng-container>
          <ng-container matColumnDef="closeTime">
            <th mat-header-cell *matHeaderCellDef>Close Time</th>
            <td mat-cell *matCellDef="let row">{{ row.isClosed ? '-' : row.closeTime }}</td>
          </ng-container>
          <ng-container matColumnDef="isClosed">
            <th mat-header-cell *matHeaderCellDef>Status</th>
            <td mat-cell *matCellDef="let row">
              <span class="px-2 py-1 rounded-full text-xs font-medium"
                [class]="row.isClosed ? 'bg-red-100 text-red-800' : 'bg-green-100 text-green-800'">
                {{ row.isClosed ? 'Closed' : 'Open' }}
              </span>
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
        </table>

        @if (items().length === 0) {
          <div class="text-center py-8 text-gray-400">
            <mat-icon class="!text-5xl mb-2">schedule</mat-icon>
            <p>No working hours configured</p>
          </div>
        }
      }
    </div>
  `,
})
export class WorkingHoursListComponent implements OnInit {
  private readonly api = inject(AdminApiService);

  readonly items = signal<WorkingHours[]>([]);
  readonly loading = signal(false);

  readonly displayedColumns = ['dayOfWeek', 'openTime', 'closeTime', 'isClosed'];

  ngOnInit(): void {
    this.loadItems();
  }

  loadItems(): void {
    this.loading.set(true);
    this.api.list<WorkingHours>('config/working-hours', { page: 0, size: 7 }).subscribe({
      next: (response) => {
        this.items.set(response.data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  formatDay(day: string): string {
    return day.charAt(0) + day.slice(1).toLowerCase();
  }
}
