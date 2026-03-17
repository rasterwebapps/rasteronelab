import { Component, ChangeDetectionStrategy } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [MatCardModule, MatIconModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="grid gap-4">
      <mat-card>
        <mat-card-header>
          <mat-icon mat-card-avatar>admin_panel_settings</mat-icon>
          <mat-card-title>Admin Module</mat-card-title>
          <mat-card-subtitle>Master data &amp; configuration</mat-card-subtitle>
        </mat-card-header>
        <mat-card-content>
          <p class="text-gray-500 mt-4">Coming soon — administration features will appear here.</p>
        </mat-card-content>
      </mat-card>
    </div>
  `,
})
export class AdminComponent {}
