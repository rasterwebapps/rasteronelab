import { Component, ChangeDetectionStrategy } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';

@Component({
  selector: 'app-patient-portal',
  standalone: true,
  imports: [MatCardModule, MatIconModule, MatListModule, MatDividerModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="grid gap-6">
      <!-- Header Card -->
      <mat-card>
        <mat-card-header>
          <div mat-card-avatar class="flex items-center justify-center w-10 h-10 rounded-full bg-indigo-100">
            <mat-icon class="text-indigo-600">person_outline</mat-icon>
          </div>
          <mat-card-title>Patient Portal</mat-card-title>
          <mat-card-subtitle>Patient self-service for orders, reports &amp; appointments</mat-card-subtitle>
        </mat-card-header>
        <mat-card-content>
          <div class="mt-4 flex flex-wrap gap-3 items-center">
            <span class="px-3 py-1 rounded-full text-sm font-medium bg-gray-100 text-gray-700">
              Phase 8 — Not Started
            </span>
            <span class="text-gray-500 text-sm">Expected after Phase 7 (Reports &amp; QC) completion</span>
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
                  <mat-icon matListItemIcon class="text-indigo-500">check_circle_outline</mat-icon>
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
              <div class="flex items-center gap-3 p-3 rounded-lg bg-gray-50">
                <mat-icon class="text-gray-400">schedule</mat-icon>
                <div>
                  <div class="text-sm font-medium text-gray-800">Not Yet Started</div>
                  <div class="text-xs text-gray-500">Planned for Phase 8 development cycle</div>
                </div>
              </div>
              <div class="flex items-center gap-3 p-3 rounded-lg bg-blue-50">
                <mat-icon class="text-blue-600">info</mat-icon>
                <div>
                  <div class="text-sm font-medium text-gray-800">Prerequisites</div>
                  <div class="text-xs text-gray-500">Doctor Portal and full Reports module required</div>
                </div>
              </div>
            </div>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `,
})
export class PatientPortalComponent {
  readonly features = [
    'Test Order Tracking',
    'Lab Report Download',
    'Appointment Scheduling',
    'Account and Profile Management',
    'Result Notifications',
  ];
}
