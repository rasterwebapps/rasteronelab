import { Component, ChangeDetectionStrategy } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-inventory',
  standalone: true,
  imports: [MatCardModule, MatIconModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="grid gap-4">
      <mat-card>
        <mat-card-header>
          <mat-icon mat-card-avatar>inventory_2</mat-icon>
          <mat-card-title>Inventory Module</mat-card-title>
          <mat-card-subtitle>Reagents &amp; consumables</mat-card-subtitle>
        </mat-card-header>
        <mat-card-content>
          <p class="text-gray-500 mt-4">Coming soon — inventory management features will appear here.</p>
        </mat-card-content>
      </mat-card>
    </div>
  `,
})
export class InventoryComponent {}
