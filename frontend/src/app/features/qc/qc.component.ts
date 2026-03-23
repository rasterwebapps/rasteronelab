import { Component, ChangeDetectionStrategy } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';

@Component({
  selector: 'app-qc',
  standalone: true,
  imports: [MatCardModule, MatIconModule, MatListModule, MatDividerModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="grid gap-6">
      <!-- Header Card -->
      <mat-card>
        <mat-card-header>
          <div mat-card-avatar class="flex items-center justify-center w-10 h-10 rounded-full bg-purple-100">
            <mat-icon class="text-purple-600">verified</mat-icon>
          </div>
          <mat-card-title>Quality Control</mat-card-title>
          <mat-card-subtitle>Westgard rules, Levey-Jennings charts &amp; EQA tracking</mat-card-subtitle>
        </mat-card-header>
        <mat-card-content>
          <div class="mt-4 flex flex-wrap gap-3 items-center">
            <span class="px-3 py-1 rounded-full text-sm font-medium bg-yellow-100 text-yellow-700">
              Phase 7 — In Progress
            </span>
            <span class="text-gray-500 text-sm">Domain layer complete · Service layer pending</span>
          </div>
        </mat-card-content>
      </mat-card>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <!-- Features Planned -->
        <mat-card>
          <mat-card-header>
            <mat-card-title class="text-base">Features Planned</mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <mat-list>
              @for (feature of features; track feature) {
                <mat-list-item>
                  <mat-icon matListItemIcon class="text-purple-500">check_circle_outline</mat-icon>
                  <span matListItemTitle>{{ feature }}</span>
                </mat-list-item>
                <mat-divider />
              }
            </mat-list>
          </mat-card-content>
        </mat-card>

        <!-- Development Status -->
        <mat-card>
          <mat-card-header>
            <mat-card-title class="text-base">Development Status</mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <div class="mt-4 space-y-4">
              <div class="flex items-center gap-3 p-3 rounded-lg bg-green-50">
                <mat-icon class="text-green-600">check_circle</mat-icon>
                <div>
                  <div class="text-sm font-medium text-gray-800">Domain Layer</div>
                  <div class="text-xs text-gray-500">Entities, repositories, enums complete</div>
                </div>
              </div>
              <div class="flex items-center gap-3 p-3 rounded-lg bg-yellow-50">
                <mat-icon class="text-yellow-600">pending</mat-icon>
                <div>
                  <div class="text-sm font-medium text-gray-800">Service Layer</div>
                  <div class="text-xs text-gray-500">Westgard rules engine in development</div>
                </div>
              </div>
              <div class="flex items-center gap-3 p-3 rounded-lg bg-gray-50">
                <mat-icon class="text-gray-400">hourglass_empty</mat-icon>
                <div>
                  <div class="text-sm font-medium text-gray-800">REST API &amp; Frontend</div>
                  <div class="text-xs text-gray-500">Pending service layer completion</div>
                </div>
              </div>
            </div>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `,
})
export class QcComponent {
  readonly features = [
    'QC Lot Management (reagent lots, expiry tracking)',
    'Westgard Rules Engine (1-2s, 1-3s, 2-2s, R-4s, 4-1s, 10-x rules)',
    'Levey-Jennings Control Charts',
    'EQA (External Quality Assurance) Tracking',
    'QC Results Entry and Review',
  ];
}
