import { Routes } from '@angular/router';

export const BILLING_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./components/invoice-list/invoice-list.component').then(m => m.InvoiceListComponent),
  },
  {
    path: 'generate',
    loadComponent: () => import('./components/invoice-detail/invoice-detail.component').then(m => m.InvoiceDetailComponent),
  },
  {
    path: ':id',
    loadComponent: () => import('./components/invoice-detail/invoice-detail.component').then(m => m.InvoiceDetailComponent),
  },
  {
    path: ':id/pay',
    loadComponent: () => import('./components/payment-form/payment-form.component').then(m => m.PaymentFormComponent),
  },
];
