import { Routes } from '@angular/router';

export const RESULT_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./components/result-list/result-list.component').then(m => m.ResultListComponent),
  },
  {
    path: 'worklist',
    loadComponent: () =>
      import('./components/result-worklist/result-worklist.component').then(m => m.ResultWorklistComponent),
  },
  {
    path: 'authorize',
    loadComponent: () =>
      import('./components/result-authorization/result-authorization.component').then(m => m.ResultAuthorizationComponent),
  },
  {
    path: ':id',
    loadComponent: () =>
      import('./components/result-detail/result-detail.component').then(m => m.ResultDetailComponent),
  },
  {
    path: ':id/enter',
    loadComponent: () =>
      import('./components/result-entry/result-entry.component').then(m => m.ResultEntryComponent),
  },
];
