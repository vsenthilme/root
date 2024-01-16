import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardModule } from './dashboard/dashboard.module';
import { HomeComponent } from './home/home.component';
import { CustomerMasterModule } from './Masters -1/customer-master/customer-master.module';
import { MaterialMasterModule } from './Masters -1/material-master/material-master.module';
import { CrmModule } from './operation/crm/crm.module';
import { OperationModule } from './operation/operation/operation.module';
import { ReportsModule } from './reports/reports.module';
import { SetupModule } from './setup/setup.module';


const routes: Routes = [{
  path: '',
  component: HomeComponent,
  children: [
    {
      path: 'dashboard',
      loadChildren: () => import('./dashboard/dashboard.module').then(m => DashboardModule)
    },
    {
      path: 'setup',
      loadChildren: () => import('./setup/setup.module').then(m => SetupModule)
    },
    {
      path: 'materialmasters',
      loadChildren: () => import('./Masters -1/material-master/material-master.module').then(m => MaterialMasterModule)
    },
    {
      path: 'customermasters',
      loadChildren: () => import('./Masters -1/customer-master/customer-master.module').then(m => CustomerMasterModule)
    },
    {
      path: 'reports',
      loadChildren: () => import('./reports/reports.module').then(m => ReportsModule)
    },

    {
      path: 'operation',
      loadChildren: () => import('./operation/operation/operation.module').then(m => OperationModule)
    },

    {
      path: 'crm',
      loadChildren: () => import('./operation/crm/crm.module').then(m => CrmModule)
    },

  ]
}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MainModuleRoutingModule { }
