import { Routes } from '@angular/router';

export const SAMPLE_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./components/sample-list/sample-list.component').then(m => m.SampleListComponent),
  },
  {
    path: 'collect',
    loadComponent: () => import('./components/sample-collect/sample-collect.component').then(m => m.SampleCollectComponent),
  },
  {
    path: 'pending-receipt',
    loadComponent: () => import('./components/pending-receipt/pending-receipt.component').then(m => m.PendingReceiptComponent),
  },
  {
    path: ':id',
    loadComponent: () => import('./components/sample-detail/sample-detail.component').then(m => m.SampleDetailComponent),
  },
  {
    path: ':id/receive',
    loadComponent: () => import('./components/sample-receive/sample-receive.component').then(m => m.SampleReceiveComponent),
  },
  {
    path: ':id/tracking',
    loadComponent: () => import('./components/sample-tracking/sample-tracking.component').then(m => m.SampleTrackingComponent),
  },
  {
    path: ':id/aliquot',
    loadComponent: () => import('./components/sample-aliquot/sample-aliquot.component').then(m => m.SampleAliquotComponent),
  },
  {
    path: ':id/transfer',
    loadComponent: () => import('./components/sample-transfer/sample-transfer.component').then(m => m.SampleTransferComponent),
  },
];

