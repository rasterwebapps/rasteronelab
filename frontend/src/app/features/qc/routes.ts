import { Routes } from '@angular/router';

export const QC_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./qc.component').then(m => m.QcComponent),
  },
];
