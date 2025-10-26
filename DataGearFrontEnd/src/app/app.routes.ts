import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/statistics',
    pathMatch: 'full'
  },
  {
    path: 'statistics',
    loadComponent: () => import('./components/statistics/statistics.component').then(m => m.StatisticsComponent)
  },
  {
    path: 'transactions',
    loadComponent: () => import('./components/transactions/transactions.component').then(m => m.TransactionsComponent)
  },
  {
    path: 'transactions/create',
    loadComponent: () => import('./components/transaction-form/transaction-form.component').then(m => m.TransactionFormComponent)
  },
  {
    path: '**',
    redirectTo: '/statistics'
  }
];
