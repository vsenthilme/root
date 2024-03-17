import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AdminModule } from './admin/admin.module';
import { AnalyticalModule } from './analytical/analytical.module';
import { DashboardModule } from './dashboard/dashboard.module';
import { HomeComponent } from './home/home.component';
import { LmdModule } from './lmd/lmd.module';
import { MonitoringSectionModule } from './monitoring-section/monitoring-section.module';
import { PartnerModule } from './partner/partner.module';
import { SystemModule } from './system/system.module';
import { OrdersModule } from './orders/orders.module';
import { JntOrdersModule } from './jnt-orders/jnt-orders.module';
import { SettingModule } from './setting/setting.module';
import { ReportModule } from './report/report.module';


const routes: Routes = [{
  path: '',
  component: HomeComponent,
  children: [
    {
      path: 'dashboard',
      loadChildren: () => import('./dashboard/dashboard.module').then(m => DashboardModule)
    },
    {
      path: 'admin',
      loadChildren: () => import('./admin/admin.module').then(m => AdminModule)
    },
    {
      path: 'system',
      loadChildren: () => import('./system/system.module').then(m => SystemModule)
    },
    {
      path: 'partner',
      loadChildren: () => import('./partner/partner.module').then(m => PartnerModule)
    },
    {
      path: 'montoring',
      loadChildren: () => import('./monitoring-section/monitoring-section.module').then(m => MonitoringSectionModule)
    },
    {
      path: 'analytical',
      loadChildren: () => import('./analytical/analytical.module').then(m => AnalyticalModule)
    },
    {
      path: 'lmd',
      loadChildren: () => import('./lmd/lmd.module').then(m => LmdModule)
    },
    {
      path: 'orders',
      loadChildren: () => import('./orders/orders.module').then(m => OrdersModule)
    },
    {
      path: 'jntOrders',
      loadChildren: () => import('./jnt-orders/jnt-orders.module').then(m => JntOrdersModule)
    },
    
    {
      path: 'settings',
      loadChildren: () => import('./setting/setting.module').then(m => SettingModule)
    },

    {
      path: 'report',
      loadChildren: () => import('./report/report.module').then(m => ReportModule)
    },
  ]
}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MainModuleRoutingModule { }
