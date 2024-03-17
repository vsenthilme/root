import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { JntReportComponent } from './jnt-report/jnt-report.component';
import { BoutiqaatReportComponent } from './boutiqaat-report/boutiqaat-report.component';
import { TrackingComponent } from './tracking/tracking.component';
import { IwintComponent } from './iwint/iwint.component';

const routes: Routes = [
  {
    path: 'jntReport',
    component: JntReportComponent
  },
  {
    path: 'boutiqaatReport',
    component: BoutiqaatReportComponent
  },
  {
    path: 'tracking',
    component: TrackingComponent
  },
  {
    path: 'iwint',
    component: IwintComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReportRoutingModule { }
