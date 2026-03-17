import { Routes } from '@angular/router';

export const SAMPLE_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./sample.component').then(m => m.SampleComponent),
  },
];
