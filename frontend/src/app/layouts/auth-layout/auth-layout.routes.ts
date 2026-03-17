import { Routes } from '@angular/router';

export const AUTH_LAYOUT_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./auth-layout.component').then(m => m.AuthLayoutComponent),
    children: [
      { path: 'login', loadComponent: () => import('./login/login.component').then(m => m.LoginComponent) },
      { path: 'callback', loadComponent: () => import('./callback/callback.component').then(m => m.CallbackComponent) },
      { path: '', redirectTo: 'login', pathMatch: 'full' },
    ],
  },
];
