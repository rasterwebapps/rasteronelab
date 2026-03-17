import { Component, ChangeDetectionStrategy } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [MatCardModule, MatIconModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="grid gap-4">
      <mat-card>
        <mat-card-header>
          <mat-icon mat-card-avatar>dashboard</mat-icon>
          <mat-card-title>Dashboard</mat-card-title>
          <mat-card-subtitle>Overview of lab operations</mat-card-subtitle>
        </mat-card-header>
        <mat-card-content>
          <p class="text-gray-500 mt-4">Coming soon — analytics, charts, and KPIs will appear here.</p>
        </mat-card-content>
      </mat-card>
    </div>
  `,
})
export class DashboardComponent {}
