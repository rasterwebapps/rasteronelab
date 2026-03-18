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
  // LIS-030: Test Master
  {
    path: 'tests',
    loadComponent: () => import('./components/test-list/test-list.component').then(m => m.TestListComponent),
  },
  {
    path: 'tests/new',
    loadComponent: () => import('./components/test-form/test-form.component').then(m => m.TestFormComponent),
  },
  {
    path: 'tests/:id/edit',
    loadComponent: () => import('./components/test-form/test-form.component').then(m => m.TestFormComponent),
  },
  // LIS-030: Parameter Master
  {
    path: 'parameters',
    loadComponent: () => import('./components/parameter-list/parameter-list.component').then(m => m.ParameterListComponent),
  },
  {
    path: 'parameters/new',
    loadComponent: () => import('./components/parameter-form/parameter-form.component').then(m => m.ParameterFormComponent),
  },
  {
    path: 'parameters/:id/edit',
    loadComponent: () => import('./components/parameter-form/parameter-form.component').then(m => m.ParameterFormComponent),
  },
  // LIS-030: Reference Ranges
  {
    path: 'reference-ranges',
    loadComponent: () => import('./components/reference-range-list/reference-range-list.component').then(m => m.ReferenceRangeListComponent),
  },
  {
    path: 'reference-ranges/new',
    loadComponent: () => import('./components/reference-range-form/reference-range-form.component').then(m => m.ReferenceRangeFormComponent),
  },
  {
    path: 'reference-ranges/:id/edit',
    loadComponent: () => import('./components/reference-range-form/reference-range-form.component').then(m => m.ReferenceRangeFormComponent),
  },
  // LIS-030: Test Panels
  {
    path: 'panels',
    loadComponent: () => import('./components/panel-list/panel-list.component').then(m => m.PanelListComponent),
  },
  {
    path: 'panels/new',
    loadComponent: () => import('./components/panel-form/panel-form.component').then(m => m.PanelFormComponent),
  },
  {
    path: 'panels/:id/edit',
    loadComponent: () => import('./components/panel-form/panel-form.component').then(m => m.PanelFormComponent),
  },
];
