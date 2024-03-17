import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreatepartnerComponent } from './createpartner/createpartner.component';
import { PartnerConfigurationComponent } from './partner-configuration/partner-configuration.component';
import { PartnerMonitoringComponent } from './partner-monitoring/partner-monitoring.component';
import { PartnerSubscriptionComponent } from './partner-subscription/partner-subscription.component';

const routes: Routes = [
  {
    path: 'createPartner',
    component: CreatepartnerComponent
  },
  {
    path: 'configuration',
    component: PartnerConfigurationComponent
  },
  {
    path: 'subscription',
    component: PartnerSubscriptionComponent
  },
  {
    path: 'monitoring',
    component: PartnerMonitoringComponent
  },
  {
    path: '',
    redirectTo: 'createPartner'
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PartnerRoutingModule { }
