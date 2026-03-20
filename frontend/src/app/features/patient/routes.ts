import { Routes } from '@angular/router';

export const PATIENT_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./components/patient-list/patient-list.component').then(m => m.PatientListComponent),
  },
  {
    path: 'register',
    loadComponent: () => import('./components/patient-form/patient-form.component').then(m => m.PatientFormComponent),
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./components/patient-form/patient-form.component').then(m => m.PatientFormComponent),
  },
  {
    path: ':id',
    loadComponent: () => import('./components/patient-detail/patient-detail.component').then(m => m.PatientDetailComponent),
  },
];
