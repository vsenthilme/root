import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LmdRoutingModule } from './lmd-routing.module';
import { DeliveryComponent } from './delivery/delivery.component';
import { LmdTabComponent } from './lmd-tab/lmd-tab.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { BulkAssignComponent } from './delivery/bulk-assign/bulk-assign.component';
import { ExcelAssignComponent } from './delivery/excel-assign/excel-assign.component';
import { PrintLabelComponent } from './delivery/print-label/print-label.component';
import { PickingComponent } from './picking/picking.component';
import { PickupNewComponent } from './picking/pickup-new/pickup-new.component';
import { UploadExcelComponent } from './picking/upload-excel/upload-excel.component';
import { ConsignmentComponent } from './consignment/consignment.component';
import { NewComponent } from './consignment/new/new.component';
import { MediumsComponent } from './mediums/mediums.component';
import { HubopsComponent } from './hubops/hubops.component';
import { PrintComponent } from './consignment/print/print.component';
import { UploadoutscanComponent } from './consignment/outscan/uploadoutscan/uploadoutscan.component';
import { BulkOutscanComponent } from './consignment/outscan/bulk-outscan/bulk-outscan.component';
import { UploadInscanComponent } from './consignment/Inscan/upload-inscan/upload-inscan.component';
import { BulkInscanComponent } from './consignment/Inscan/bulk-inscan/bulk-inscan.component';
import { UploadArrivalComponent } from './consignment/Arrival/upload-arrival/upload-arrival.component';
import { BulkArrivalComponent } from './consignment/Arrival/bulk-arrival/bulk-arrival.component';
import { MarkNprComponent } from './consignment/mark-npr/mark-npr.component';
import { UploadCancelComponent } from './consignment/cancel/upload-cancel/upload-cancel.component';


@NgModule({
  declarations: [
    DeliveryComponent,
    LmdTabComponent,
    BulkAssignComponent,
    ExcelAssignComponent,
    PrintLabelComponent,
    PickingComponent,
    PickupNewComponent,
    UploadExcelComponent,
    ConsignmentComponent,
    NewComponent,
    MediumsComponent,
    HubopsComponent,
    PrintComponent,
    UploadoutscanComponent,
    BulkOutscanComponent,
    UploadInscanComponent,
    BulkInscanComponent,
    UploadArrivalComponent,
    BulkArrivalComponent,
    MarkNprComponent,
    UploadCancelComponent,
  ],
  imports: [
    CommonModule,
    LmdRoutingModule,
    SharedModule,
  ]
})
export class LmdModule { }
