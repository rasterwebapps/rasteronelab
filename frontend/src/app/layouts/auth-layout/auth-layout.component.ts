import { Component, ChangeDetectionStrategy } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-auth-layout',
  standalone: true,
  imports: [RouterOutlet, MatCardModule, MatIconModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex min-h-screen items-center justify-center bg-gray-100">
      <mat-card class="w-full max-w-md p-8">
        <div class="mb-6 text-center">
          <mat-icon class="!text-5xl !h-12 !w-12 text-indigo-600 mb-2">biotech</mat-icon>
          <h1 class="text-2xl font-bold text-gray-800">RasterOneLab LIS</h1>
          <p class="text-sm text-gray-500">Laboratory Information System</p>
        </div>
        <router-outlet />
      </mat-card>
    </div>
  `,
})
export class AuthLayoutComponent {}
