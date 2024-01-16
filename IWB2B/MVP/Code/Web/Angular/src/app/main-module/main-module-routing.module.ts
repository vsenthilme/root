import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ConsignmentsModule } from './consignments/consignments.module';
import { CustomersModule } from './customers/customers.module';
import { DashboardModule } from './dashboard/dashboard.module';
import { HomeComponent } from './home/home.component';
import { LoyalityModule } from './loyality/loyality.module';


const routes: Routes = [{
  path: '',
  component: HomeComponent,
  children: [
    {
      path: 'dashboard',
      loadChildren: () => import('./dashboard/dashboard.module').then(m => DashboardModule)
    },
    {
      path: 'customer',
      loadChildren: () => import('./customers/customers.module').then(m => CustomersModule)

    },
    {
      path: 'loyalty',
      loadChildren: () => import('./loyality/loyality.module').then(m => LoyalityModule)
    },
    {
      path: 'consignment',
      loadChildren: () => import('./consignments/consignments.module').then(m => ConsignmentsModule)
    },
  ]
}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MainModuleRoutingModule { }
