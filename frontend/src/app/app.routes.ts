import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },
  {
    path: 'auth',
    loadChildren: () =>
      import('./layouts/auth-layout/auth-layout.routes').then(m => m.AUTH_LAYOUT_ROUTES),
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./layouts/main-layout/main-layout.component').then(m => m.MainLayoutComponent),
    children: [
      {
        path: 'dashboard',
        loadChildren: () =>
          import('./features/dashboard/routes').then(m => m.DASHBOARD_ROUTES),
      },
      {
        path: 'patients',
        loadChildren: () =>
          import('./features/patient/routes').then(m => m.PATIENT_ROUTES),
        data: { roles: ['RECEPTIONIST', 'LAB_TECHNICIAN', 'PATHOLOGIST', 'ADMIN'] },
      },
      {
        path: 'samples',
        loadChildren: () =>
          import('./features/sample/routes').then(m => m.SAMPLE_ROUTES),
        data: { roles: ['LAB_TECHNICIAN', 'PATHOLOGIST', 'ADMIN'] },
      },
      {
        path: 'orders',
        loadChildren: () =>
          import('./features/order/routes').then(m => m.ORDER_ROUTES),
        data: { roles: ['RECEPTIONIST', 'LAB_TECHNICIAN', 'PATHOLOGIST', 'ADMIN'] },
      },
      {
        path: 'results',
        loadChildren: () =>
          import('./features/result/routes').then(m => m.RESULT_ROUTES),
        data: { roles: ['LAB_TECHNICIAN', 'PATHOLOGIST', 'ADMIN'] },
      },
      {
        path: 'reports',
        loadChildren: () =>
          import('./features/report/routes').then(m => m.REPORT_ROUTES),
        data: { roles: ['PATHOLOGIST', 'ADMIN', 'RECEPTIONIST'] },
      },
      {
        path: 'billing',
        loadChildren: () =>
          import('./features/billing/routes').then(m => m.BILLING_ROUTES),
        data: { roles: ['BILLING_STAFF', 'ADMIN'] },
      },
      {
        path: 'inventory',
        loadChildren: () =>
          import('./features/inventory/routes').then(m => m.INVENTORY_ROUTES),
        data: { roles: ['INVENTORY_STAFF', 'ADMIN'] },
      },
      {
        path: 'qc',
        loadChildren: () =>
          import('./features/qc/routes').then(m => m.QC_ROUTES),
        data: { roles: ['LAB_TECHNICIAN', 'PATHOLOGIST', 'ADMIN'] },
      },
      {
        path: 'admin',
        canActivate: [roleGuard],
        loadChildren: () =>
          import('./features/admin/routes').then(m => m.ADMIN_ROUTES),
        data: { roles: ['ADMIN', 'SUPER_ADMIN'] },
      },
    ],
  },
  {
    path: 'portal/doctor',
    loadChildren: () =>
      import('./features/doctor-portal/routes').then(m => m.DOCTOR_PORTAL_ROUTES),
  },
  {
    path: 'portal/patient',
    loadChildren: () =>
      import('./features/patient-portal/routes').then(m => m.PATIENT_PORTAL_ROUTES),
  },
  {
    path: '**',
    redirectTo: 'dashboard',
  },
];
