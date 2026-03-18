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
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatTooltipModule } from '@angular/material/tooltip';
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationService } from '@core/services/notification.service';
import { NotificationTemplate } from '../../models/admin.models';

@Component({
  selector: 'app-notification-template-list',
  standalone: true,
  imports: [
    RouterLink, MatTableModule, MatPaginatorModule, MatFormFieldModule,
    MatInputModule, MatSelectModule, MatButtonModule, MatIconModule, MatChipsModule,
    MatProgressSpinnerModule, MatSlideToggleModule, MatTooltipModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex items-center justify-between mb-6">
      <div>
        <h2 class="text-xl font-bold text-gray-800">Notification Templates</h2>
        <p class="text-gray-500 text-sm">Manage SMS, Email, and WhatsApp templates</p>
      </div>
      <a mat-raised-button color="primary" routerLink="new">
        <mat-icon>add</mat-icon> Add Template
      </a>
    </div>

    <div class="bg-white rounded-lg shadow">
      <div class="flex flex-wrap gap-4 p-4 border-b">
        <mat-form-field appearance="outline" class="flex-1 min-w-[200px]">
          <mat-label>Search templates</mat-label>
          <input matInput placeholder="Search by name or code..." (input)="onSearch($event)" />
          <mat-icon matSuffix>search</mat-icon>
        </mat-form-field>
        <mat-form-field appearance="outline" class="w-48">
          <mat-label>Channel</mat-label>
          <mat-select (selectionChange)="onChannelFilter($event.value)">
            <mat-option value="">All Channels</mat-option>
            <mat-option value="SMS">SMS</mat-option>
            <mat-option value="EMAIL">Email</mat-option>
            <mat-option value="WHATSAPP">WhatsApp</mat-option>
          </mat-select>
        </mat-form-field>
      </div>

      @if (loading()) {
        <div class="flex justify-center p-8">
          <mat-spinner diameter="40" />
        </div>
      } @else {
        <table mat-table [dataSource]="items()" class="w-full">
          <ng-container matColumnDef="templateCode">
            <th mat-header-cell *matHeaderCellDef>Code</th>
            <td mat-cell *matCellDef="let row">{{ row.templateCode }}</td>
          </ng-container>
          <ng-container matColumnDef="templateName">
            <th mat-header-cell *matHeaderCellDef>Template Name</th>
            <td mat-cell *matCellDef="let row" class="font-medium">{{ row.templateName }}</td>
          </ng-container>
          <ng-container matColumnDef="channel">
            <th mat-header-cell *matHeaderCellDef>Channel</th>
            <td mat-cell *matCellDef="let row">
              <span class="px-2 py-1 rounded-full text-xs font-medium"
                [class]="getChannelClass(row.channel)">
                {{ row.channel }}
              </span>
            </td>
          </ng-container>
          <ng-container matColumnDef="eventTrigger">
            <th mat-header-cell *matHeaderCellDef>Event Trigger</th>
            <td mat-cell *matCellDef="let row">{{ row.eventTrigger }}</td>
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
            <mat-icon class="!text-5xl mb-2">notifications_off</mat-icon>
            <p>No notification templates found</p>
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
export class NotificationTemplateListComponent implements OnInit {
  private readonly api = inject(AdminApiService);
  private readonly notification = inject(NotificationService);

  readonly items = signal<NotificationTemplate[]>([]);
  readonly loading = signal(false);
  readonly totalElements = signal(0);
  readonly pageSize = signal(10);
  readonly currentPage = signal(0);
  readonly searchTerm = signal('');
  readonly channelFilter = signal('');

  readonly displayedColumns = ['templateCode', 'templateName', 'channel', 'eventTrigger', 'isActive', 'actions'];

  ngOnInit(): void {
    this.loadItems();
  }

  loadItems(): void {
    this.loading.set(true);
    this.api.list<NotificationTemplate>('config/notification-templates', {
      page: this.currentPage(),
      size: this.pageSize(),
      search: this.searchTerm() || undefined,
      channel: this.channelFilter() || undefined,
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

  onChannelFilter(value: string): void {
    this.channelFilter.set(value);
    this.currentPage.set(0);
    this.loadItems();
  }

  onPage(event: PageEvent): void {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadItems();
  }

  toggleActive(item: NotificationTemplate): void {
    this.api.update<NotificationTemplate>('config/notification-templates', item.id, { isActive: !item.isActive }).subscribe({
      next: () => {
        this.notification.showSuccess(`Template ${item.isActive ? 'deactivated' : 'activated'} successfully`);
        this.loadItems();
      },
    });
  }

  getChannelClass(channel: string): string {
    const classes: Record<string, string> = {
      'SMS': 'bg-green-100 text-green-800',
      'EMAIL': 'bg-blue-100 text-blue-800',
      'WHATSAPP': 'bg-emerald-100 text-emerald-800',
    };
    return classes[channel] ?? 'bg-gray-100 text-gray-800';
  }
}
