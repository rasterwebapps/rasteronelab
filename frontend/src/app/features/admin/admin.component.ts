import { Component, ChangeDetectionStrategy } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

interface AdminSection {
  icon: string;
  title: string;
  description: string;
  route: string;
}

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatIconModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="mb-6">
      <h1 class="text-2xl font-bold text-gray-800">Administration</h1>
      <p class="text-gray-500 mt-1">Manage master data, users, and system configuration</p>
    </div>
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
      @for (section of sections; track section.route) {
        <a [routerLink]="section.route" class="no-underline">
          <mat-card class="hover:shadow-lg transition-shadow cursor-pointer h-full">
            <mat-card-header>
              <mat-icon mat-card-avatar class="!text-indigo-600">{{ section.icon }}</mat-icon>
              <mat-card-title class="!text-base">{{ section.title }}</mat-card-title>
            </mat-card-header>
            <mat-card-content>
              <p class="text-gray-500 text-sm mt-2">{{ section.description }}</p>
            </mat-card-content>
          </mat-card>
        </a>
      }
    </div>
  `,
})
export class AdminComponent {
  readonly sections: AdminSection[] = [
    { icon: 'business', title: 'Branches', description: 'Branch configuration and working hours', route: 'branches' },
    { icon: 'biotech', title: 'Tests', description: 'Test master and panel management', route: 'tests' },
    { icon: 'tune', title: 'Parameters', description: 'Parameter master and reference ranges', route: 'parameters' },
    { icon: 'local_hospital', title: 'Doctors', description: 'Doctor management and referrals', route: 'doctors' },
    { icon: 'people', title: 'Users', description: 'User accounts and access control', route: 'users' },
    { icon: 'security', title: 'Roles', description: 'Roles and permission management', route: 'roles' },
    { icon: 'event', title: 'Holidays', description: 'Holiday calendar management', route: 'holidays' },
    { icon: 'notifications', title: 'Notifications', description: 'Notification template management', route: 'notification-templates' },
    { icon: 'description', title: 'Report Templates', description: 'Report layout configuration', route: 'report-templates' },
    { icon: 'schedule', title: 'Working Hours', description: 'Branch operating hours', route: 'working-hours' },
    { icon: 'format_list_numbered', title: 'Number Series', description: 'ID sequence configuration', route: 'number-series' },
    { icon: 'local_offer', title: 'Discounts', description: 'Discount scheme management', route: 'discount-schemes' },
    { icon: 'health_and_safety', title: 'Insurance Tariffs', description: 'Insurance provider rates', route: 'insurance-tariffs' },
    { icon: 'medication', title: 'Antibiotics', description: 'Antibiotic master data', route: 'antibiotics' },
    { icon: 'bug_report', title: 'Microorganisms', description: 'Microorganism master data', route: 'microorganisms' },
    { icon: 'warning', title: 'Critical Values', description: 'Critical value alert thresholds', route: 'critical-values' },
    { icon: 'compare_arrows', title: 'Delta Checks', description: 'Delta check configuration', route: 'delta-checks' },
    { icon: 'verified', title: 'Auto-Validation', description: 'Auto-validation rules', route: 'auto-validation' },
    { icon: 'timer', title: 'TAT Config', description: 'Turn-around time settings', route: 'tat-config' },
  ];
}
