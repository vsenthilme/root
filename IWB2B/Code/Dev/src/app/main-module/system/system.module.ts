import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SystemRoutingModule } from './system-routing.module';
import { SystemTab4Component } from './system-tab4/system-tab4.component';
import { CreateNewsystemComponent } from './create-newsystem/create-newsystem.component';
import { CreatesystemNewComponent } from './create-newsystem/createsystem-new/createsystem-new.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { ConfigurationComponent } from './configuration/configuration.component';
import { ConfigurationNewComponent } from './configuration/configuration-new/configuration-new.component';
import { SubsManagementComponent } from './subs-management/subs-management.component';
import { SubsmanagementNewComponent } from './subs-management/subsmanagement-new/subsmanagement-new.component';
import { MonitoringControlComponent } from './monitoring-control/monitoring-control.component';
import { MonitoringNewComponent } from './monitoring-control/monitoring-new/monitoring-new.component';


@NgModule({
  declarations: [
    SystemTab4Component,
    CreateNewsystemComponent,
    CreatesystemNewComponent,
    ConfigurationComponent,
    ConfigurationNewComponent,
    SubsManagementComponent,
    SubsmanagementNewComponent,
    MonitoringControlComponent,
    MonitoringNewComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    SystemRoutingModule
  ]
})
export class SystemModule { }
