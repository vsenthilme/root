import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ConfigurationComponent } from './configuration/configuration.component';
import { CreateNewsystemComponent } from './create-newsystem/create-newsystem.component';
import { MonitoringControlComponent } from './monitoring-control/monitoring-control.component';
import { SubsManagementComponent } from './subs-management/subs-management.component';

const routes: Routes = [
  {
    path: 'createnewsystem',
    component: CreateNewsystemComponent
  },
  {
    path: 'configuration',
    component: ConfigurationComponent
  },
  {
    path: 'subsmanagement',
    component: SubsManagementComponent
  },
  {
    path: 'monitoringControl',
    component: MonitoringControlComponent
  },

  {
    path: '',
    redirectTo: 'userprofile'
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SystemRoutingModule { }
