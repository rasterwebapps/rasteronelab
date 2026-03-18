import { Routes } from '@angular/router';

export const ADMIN_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./admin.component').then(m => m.AdminComponent),
  },
  {
    path: 'branches',
    loadComponent: () => import('./components/branch-list/branch-list.component').then(m => m.BranchListComponent),
  },
  {
    path: 'branches/new',
    loadComponent: () => import('./components/branch-form/branch-form.component').then(m => m.BranchFormComponent),
  },
  {
    path: 'branches/:id/edit',
    loadComponent: () => import('./components/branch-form/branch-form.component').then(m => m.BranchFormComponent),
  },
];
