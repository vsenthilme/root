import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReportRoutingModule } from './report-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { JntReportComponent } from './jnt-report/jnt-report.component';
import { BoutiqaatReportComponent } from './boutiqaat-report/boutiqaat-report.component';
import { TrackingComponent } from './tracking/tracking.component';
import { TrackingPopupComponent } from './tracking/tracking-popup/tracking-popup.component';
import { IwintComponent } from './iwint/iwint.component';


@NgModule({
  declarations: [
    JntReportComponent,
    BoutiqaatReportComponent,
    TrackingComponent,
    TrackingPopupComponent,
    IwintComponent
  ],
  imports: [
    CommonModule,
    ReportRoutingModule,
    SharedModule,
  ]
})
export class ReportModule { }
