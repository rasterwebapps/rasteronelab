import { Routes } from '@angular/router';

export const PATIENT_PORTAL_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./patient-portal.component').then(m => m.PatientPortalComponent),
  },
];
