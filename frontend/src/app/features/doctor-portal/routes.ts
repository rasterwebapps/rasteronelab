import { Routes } from '@angular/router';

export const DOCTOR_PORTAL_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./doctor-portal.component').then(m => m.DoctorPortalComponent),
  },
];
