import { Routes } from '@angular/router';

export const RESULT_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./result.component').then(m => m.ResultComponent),
  },
];
