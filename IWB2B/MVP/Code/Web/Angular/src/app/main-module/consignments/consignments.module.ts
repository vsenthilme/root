import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ConsignmentsRoutingModule } from './consignments-routing.module';
import { ConsignmentNewComponent } from './consignment-new/consignment-new.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { ConsignmentHistoryComponent } from './consignment-history/consignment-history.component';
import { ConsignmentNewPopupComponent } from './consignment-new/consignment-new-popup/consignment-new-popup.component';
import { TrackHistoryComponent } from './consignment-history/track-history/track-history.component';


@NgModule({
  declarations: [
    ConsignmentNewComponent,
    ConsignmentHistoryComponent,
    ConsignmentNewPopupComponent,
    TrackHistoryComponent
  ],
  imports: [
    CommonModule,
    ConsignmentsRoutingModule,
    SharedModule,
  ]
})
export class ConsignmentsModule { }
