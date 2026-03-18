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
  // LIS-031: Doctor Management
  {
    path: 'doctors',
    loadComponent: () => import('./components/doctor-list/doctor-list.component').then(m => m.DoctorListComponent),
  },
  {
    path: 'doctors/new',
    loadComponent: () => import('./components/doctor-form/doctor-form.component').then(m => m.DoctorFormComponent),
  },
  {
    path: 'doctors/:id/edit',
    loadComponent: () => import('./components/doctor-form/doctor-form.component').then(m => m.DoctorFormComponent),
  },
  // LIS-031: User Management
  {
    path: 'users',
    loadComponent: () => import('./components/user-list/user-list.component').then(m => m.UserListComponent),
  },
  {
    path: 'users/new',
    loadComponent: () => import('./components/user-form/user-form.component').then(m => m.UserFormComponent),
  },
  {
    path: 'users/:id/edit',
    loadComponent: () => import('./components/user-form/user-form.component').then(m => m.UserFormComponent),
  },
  // LIS-031: Role Management
  {
    path: 'roles',
    loadComponent: () => import('./components/role-list/role-list.component').then(m => m.RoleListComponent),
  },
  {
    path: 'roles/new',
    loadComponent: () => import('./components/role-form/role-form.component').then(m => m.RoleFormComponent),
  },
  {
    path: 'roles/:id/edit',
    loadComponent: () => import('./components/role-form/role-form.component').then(m => m.RoleFormComponent),
  },
  // LIS-032: Holiday Management
  {
    path: 'holidays',
    loadComponent: () => import('./components/holiday-list/holiday-list.component').then(m => m.HolidayListComponent),
  },
  {
    path: 'holidays/new',
    loadComponent: () => import('./components/holiday-form/holiday-form.component').then(m => m.HolidayFormComponent),
  },
  {
    path: 'holidays/:id/edit',
    loadComponent: () => import('./components/holiday-form/holiday-form.component').then(m => m.HolidayFormComponent),
  },
  // LIS-032: Notification Templates
  {
    path: 'notification-templates',
    loadComponent: () => import('./components/notification-template-list/notification-template-list.component').then(m => m.NotificationTemplateListComponent),
  },
  {
    path: 'notification-templates/new',
    loadComponent: () => import('./components/notification-template-form/notification-template-form.component').then(m => m.NotificationTemplateFormComponent),
  },
  {
    path: 'notification-templates/:id/edit',
    loadComponent: () => import('./components/notification-template-form/notification-template-form.component').then(m => m.NotificationTemplateFormComponent),
  },
  // LIS-032: Report Templates
  {
    path: 'report-templates',
    loadComponent: () => import('./components/report-template-list/report-template-list.component').then(m => m.ReportTemplateListComponent),
  },
  {
    path: 'report-templates/new',
    loadComponent: () => import('./components/report-template-form/report-template-form.component').then(m => m.ReportTemplateFormComponent),
  },
  {
    path: 'report-templates/:id/edit',
    loadComponent: () => import('./components/report-template-form/report-template-form.component').then(m => m.ReportTemplateFormComponent),
  },
  // LIS-032: Working Hours
  {
    path: 'working-hours',
    loadComponent: () => import('./components/working-hours-list/working-hours-list.component').then(m => m.WorkingHoursListComponent),
  },
  {
    path: 'working-hours/edit',
    loadComponent: () => import('./components/working-hours-form/working-hours-form.component').then(m => m.WorkingHoursFormComponent),
  },
  // LIS-032: Number Series
  {
    path: 'number-series',
    loadComponent: () => import('./components/number-series-list/number-series-list.component').then(m => m.NumberSeriesListComponent),
  },
  {
    path: 'number-series/new',
    loadComponent: () => import('./components/number-series-form/number-series-form.component').then(m => m.NumberSeriesFormComponent),
  },
  {
    path: 'number-series/:id/edit',
    loadComponent: () => import('./components/number-series-form/number-series-form.component').then(m => m.NumberSeriesFormComponent),
  },
  // LIS-032: Discount Schemes
  {
    path: 'discount-schemes',
    loadComponent: () => import('./components/discount-scheme-list/discount-scheme-list.component').then(m => m.DiscountSchemeListComponent),
  },
  {
    path: 'discount-schemes/new',
    loadComponent: () => import('./components/discount-scheme-form/discount-scheme-form.component').then(m => m.DiscountSchemeFormComponent),
  },
  {
    path: 'discount-schemes/:id/edit',
    loadComponent: () => import('./components/discount-scheme-form/discount-scheme-form.component').then(m => m.DiscountSchemeFormComponent),
  },
  // LIS-032: Insurance Tariffs
  {
    path: 'insurance-tariffs',
    loadComponent: () => import('./components/insurance-tariff-list/insurance-tariff-list.component').then(m => m.InsuranceTariffListComponent),
  },
  {
    path: 'insurance-tariffs/new',
    loadComponent: () => import('./components/insurance-tariff-form/insurance-tariff-form.component').then(m => m.InsuranceTariffFormComponent),
  },
  {
    path: 'insurance-tariffs/:id/edit',
    loadComponent: () => import('./components/insurance-tariff-form/insurance-tariff-form.component').then(m => m.InsuranceTariffFormComponent),
  },
  // LIS-032: Antibiotic Master
  {
    path: 'antibiotics',
    loadComponent: () => import('./components/antibiotic-list/antibiotic-list.component').then(m => m.AntibioticListComponent),
  },
  {
    path: 'antibiotics/new',
    loadComponent: () => import('./components/antibiotic-form/antibiotic-form.component').then(m => m.AntibioticFormComponent),
  },
  {
    path: 'antibiotics/:id/edit',
    loadComponent: () => import('./components/antibiotic-form/antibiotic-form.component').then(m => m.AntibioticFormComponent),
  },
  // LIS-032: Microorganism Master
  {
    path: 'microorganisms',
    loadComponent: () => import('./components/microorganism-list/microorganism-list.component').then(m => m.MicroorganismListComponent),
  },
  {
    path: 'microorganisms/new',
    loadComponent: () => import('./components/microorganism-form/microorganism-form.component').then(m => m.MicroorganismFormComponent),
  },
  {
    path: 'microorganisms/:id/edit',
    loadComponent: () => import('./components/microorganism-form/microorganism-form.component').then(m => m.MicroorganismFormComponent),
  },
  // LIS-032: Critical Value Configuration
  {
    path: 'critical-values',
    loadComponent: () => import('./components/critical-value-list/critical-value-list.component').then(m => m.CriticalValueListComponent),
  },
  {
    path: 'critical-values/new',
    loadComponent: () => import('./components/critical-value-form/critical-value-form.component').then(m => m.CriticalValueFormComponent),
  },
  {
    path: 'critical-values/:id/edit',
    loadComponent: () => import('./components/critical-value-form/critical-value-form.component').then(m => m.CriticalValueFormComponent),
  },
  // LIS-032: Delta Check Configuration
  {
    path: 'delta-checks',
    loadComponent: () => import('./components/delta-check-list/delta-check-list.component').then(m => m.DeltaCheckListComponent),
  },
  {
    path: 'delta-checks/new',
    loadComponent: () => import('./components/delta-check-form/delta-check-form.component').then(m => m.DeltaCheckFormComponent),
  },
  {
    path: 'delta-checks/:id/edit',
    loadComponent: () => import('./components/delta-check-form/delta-check-form.component').then(m => m.DeltaCheckFormComponent),
  },
  // LIS-032: Auto-Validation Rules
  {
    path: 'auto-validation',
    loadComponent: () => import('./components/auto-validation-list/auto-validation-list.component').then(m => m.AutoValidationListComponent),
  },
  {
    path: 'auto-validation/new',
    loadComponent: () => import('./components/auto-validation-form/auto-validation-form.component').then(m => m.AutoValidationFormComponent),
  },
  {
    path: 'auto-validation/:id/edit',
    loadComponent: () => import('./components/auto-validation-form/auto-validation-form.component').then(m => m.AutoValidationFormComponent),
  },
  // LIS-032: TAT Configuration
  {
    path: 'tat-config',
    loadComponent: () => import('./components/tat-config-list/tat-config-list.component').then(m => m.TatConfigListComponent),
  },
  {
    path: 'tat-config/new',
    loadComponent: () => import('./components/tat-config-form/tat-config-form.component').then(m => m.TatConfigFormComponent),
  },
  {
    path: 'tat-config/:id/edit',
    loadComponent: () => import('./components/tat-config-form/tat-config-form.component').then(m => m.TatConfigFormComponent),
  },
];
