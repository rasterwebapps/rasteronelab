import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs';
import { AuthService } from '@core/services/auth.service';
import { BranchService } from '@core/services/branch.service';
import { BranchSelectorComponent } from '@shared/components/branch-selector/branch-selector.component';
import { BreadcrumbComponent } from '@shared/components/breadcrumb/breadcrumb.component';

interface NavItem {
  icon: string;
  label: string;
  route: string;
}

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    MatSidenavModule,
    MatToolbarModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    MatDividerModule,
    BranchSelectorComponent,
    BreadcrumbComponent,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-sidenav-container class="h-screen">
      <mat-sidenav
        #sidenav
        [mode]="isMobile() ? 'over' : 'side'"
        [opened]="!isMobile()"
        class="w-64 border-r"
      >
        <div class="flex items-center gap-3 px-4 py-5 border-b">
          <mat-icon class="!text-3xl text-indigo-600">biotech</mat-icon>
          @if (!sidebarCollapsed()) {
            <span class="text-lg font-bold text-gray-800">RasterOneLab</span>
          }
        </div>

        <mat-nav-list>
          @for (item of navItems; track item.route) {
            <a
              mat-list-item
              [routerLink]="item.route"
              routerLinkActive="!bg-indigo-50 !text-indigo-700"
              class="!rounded-lg !mx-2 !my-0.5"
            >
              <mat-icon matListItemIcon>{{ item.icon }}</mat-icon>
              @if (!sidebarCollapsed()) {
                <span matListItemTitle>{{ item.label }}</span>
              }
            </a>
          }
        </mat-nav-list>
      </mat-sidenav>

      <mat-sidenav-content class="flex flex-col">
        <mat-toolbar color="primary" class="!sticky !top-0 z-10 shadow-sm">
          <button mat-icon-button (click)="toggleSidenav(sidenav)">
            <mat-icon>menu</mat-icon>
          </button>

          <span class="ml-2 text-base font-medium hidden sm:inline">
            RasterOneLab LIS
          </span>

          <span class="flex-1"></span>

          <app-branch-selector class="mr-2 hidden md:block" />

          <button mat-icon-button [matMenuTriggerFor]="userMenu">
            <mat-icon>account_circle</mat-icon>
          </button>

          <mat-menu #userMenu="matMenu">
            <div class="px-4 py-2 border-b">
              <p class="font-medium text-gray-800">{{ authService.fullName() }}</p>
              <p class="text-xs text-gray-500">{{ authService.user()?.email }}</p>
              <p class="text-xs text-gray-400 mt-1">
                {{ authService.user()?.roles?.join(', ') }}
              </p>
            </div>
            <button mat-menu-item (click)="logout()">
              <mat-icon>logout</mat-icon>
              <span>Sign out</span>
            </button>
          </mat-menu>
        </mat-toolbar>

        <app-breadcrumb />

        <main class="flex-1 overflow-auto p-4 bg-gray-50">
          <router-outlet />
        </main>
      </mat-sidenav-content>
    </mat-sidenav-container>
  `,
})
export class MainLayoutComponent implements OnInit {
  readonly authService = inject(AuthService);
  private readonly branchService = inject(BranchService);
  private readonly breakpointObserver = inject(BreakpointObserver);

  readonly sidebarCollapsed = signal(false);

  readonly isMobile = toSignal(
    this.breakpointObserver
      .observe([Breakpoints.Handset])
      .pipe(map(result => result.matches)),
    { initialValue: false },
  );

  readonly navItems: NavItem[] = [
    { icon: 'dashboard', label: 'Dashboard', route: '/dashboard' },
    { icon: 'people', label: 'Patients', route: '/patients' },
    { icon: 'assignment', label: 'Orders', route: '/orders' },
    { icon: 'science', label: 'Samples', route: '/samples' },
    { icon: 'fact_check', label: 'Results', route: '/results' },
    { icon: 'description', label: 'Reports', route: '/reports' },
    { icon: 'receipt_long', label: 'Billing', route: '/billing' },
    { icon: 'inventory_2', label: 'Inventory', route: '/inventory' },
    { icon: 'verified', label: 'QC', route: '/qc' },
    { icon: 'admin_panel_settings', label: 'Admin', route: '/admin' },
  ];

  ngOnInit(): void {
    this.branchService.loadBranches();
  }

  toggleSidenav(sidenav: { toggle: () => void }): void {
    if (this.isMobile()) {
      sidenav.toggle();
    } else {
      this.sidebarCollapsed.update(v => !v);
    }
  }

  logout(): void {
    this.authService.logout();
  }
}
