import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MonitoringSectionRoutingModule } from './monitoring-section-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { ApiConsoleComponent } from './api-console/api-console.component';
import { MonitoringTabComponent } from './monitoring-tab/monitoring-tab.component';
import { SystemMonitoringComponent } from './system-monitoring/system-monitoring.component';
import { ActiviyDeckComponent } from './activiy-deck/activiy-deck.component';


@NgModule({
  declarations: [
    ApiConsoleComponent,
    MonitoringTabComponent,
    SystemMonitoringComponent,
    ActiviyDeckComponent
  ],
  imports: [
    CommonModule,
    MonitoringSectionRoutingModule,
    SharedModule,
  ]
})
export class MonitoringSectionModule { }
