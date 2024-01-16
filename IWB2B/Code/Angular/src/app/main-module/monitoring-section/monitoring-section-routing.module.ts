import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ActiviyDeckComponent } from './activiy-deck/activiy-deck.component';
import { ApiConsoleComponent } from './api-console/api-console.component';
import { SystemMonitoringComponent } from './system-monitoring/system-monitoring.component';

const routes: Routes = [
  {
    path: 'apiconsole',
    component: ApiConsoleComponent
  },
  {
    path: 'systemmonitoring',
    component: SystemMonitoringComponent
  },
  {
    path: 'activitydeck',
    component: ActiviyDeckComponent
  },
  {
    path: '',
    redirectTo: 'apiconsole'
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MonitoringSectionRoutingModule { }
