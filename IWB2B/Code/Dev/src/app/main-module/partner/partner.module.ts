import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PartnerRoutingModule } from './partner-routing.module';
import { CreatepartnerComponent } from './createpartner/createpartner.component';
import { PartnerNewComponent } from './createpartner/partner-new/partner-new.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { PartnerTabComponent } from './partner-tab/partner-tab.component';
import { PartnerConfigurationComponent } from './partner-configuration/partner-configuration.component';
import { PartnerconfigrationNewComponent } from './partner-configuration/partnerconfigration-new/partnerconfigration-new.component';
import { PartnerSubscriptionComponent } from './partner-subscription/partner-subscription.component';
import { SubscriptionNewComponent } from './partner-subscription/subscription-new/subscription-new.component';
import { PartnerMonitoringComponent } from './partner-monitoring/partner-monitoring.component';
import { MonitoringNewComponent } from './partner-monitoring/monitoring-new/monitoring-new.component';


@NgModule({
  declarations: [
    CreatepartnerComponent,
    PartnerNewComponent,
    PartnerTabComponent,
    PartnerConfigurationComponent,
    PartnerconfigrationNewComponent,
    PartnerSubscriptionComponent,
    SubscriptionNewComponent,
    PartnerMonitoringComponent,
    MonitoringNewComponent
  ],
  imports: [
    CommonModule,
    PartnerRoutingModule,
    SharedModule,
  ]
})
export class PartnerModule { }
