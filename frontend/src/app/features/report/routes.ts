import { Routes } from '@angular/router';

export const REPORT_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./components/report-list/report-list.component').then(m => m.ReportListComponent),
  },
];
