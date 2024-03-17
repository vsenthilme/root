import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AnalyticalRoutingModule } from './analytical-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { ArchivesLogComponent } from './archives-log/archives-log.component';
import { AnalyticalTabComponent } from './analytical-tab/analytical-tab.component';
import { DatatrafficComponent } from './datatraffic/datatraffic.component';


@NgModule({
  declarations: [
    ArchivesLogComponent,
    AnalyticalTabComponent,
    DatatrafficComponent
  ],
  imports: [
    CommonModule,
    AnalyticalRoutingModule,
    SharedModule,
  ]
})
export class AnalyticalModule { }
