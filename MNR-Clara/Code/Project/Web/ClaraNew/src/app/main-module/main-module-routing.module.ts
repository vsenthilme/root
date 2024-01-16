import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AccountsModule } from './accounts/accounts.module';
import { ClientModule } from './client/client.module';
import { CrmModule } from './crm/crm.module';
import { DashboardModule } from './dashboard/dashboard.module';
import { HeaderComponent } from './header/header.component';
import { MattersModule } from './matters/matters.module';
import { ReportsModule } from './reports/reports.module';
import { SettingModule } from './setting/setting.module';
import { ControlgroupModule } from './controlgroup/controlgroup.module';


const routes: Routes = [{
  path: '',
  component: HeaderComponent,
  children: [
    {
      path: 'dashboard',
      loadChildren: () => import('./dashboard/dashboard.module').then(m => DashboardModule)

    },
    {
      path: 'crm',
      loadChildren: () => import('./crm/crm.module').then(m => CrmModule)
    },
    {
      path: 'setting',
      loadChildren: () => import('./setting/setting.module').then(m => SettingModule)
    },
    {
      path: 'client',
      loadChildren: () => import('./client/client.module').then(m => ClientModule)
    },
    {
      path: 'matters',
      loadChildren: () => import('./matters/matters.module').then(m => MattersModule)
    },
    {
      path: 'accounts',
      loadChildren: () => import('./accounts/accounts.module').then(m => AccountsModule)
    },
    {
      path: 'reports',
      loadChildren: () => import('./reports/reports.module').then(m => ReportsModule)
    },
    {
      path: 'controlgroup',
      loadChildren: () => import('./controlgroup/controlgroup.module').then(m => ControlgroupModule)
    },
  ]
}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MainModuleRoutingModule { }
