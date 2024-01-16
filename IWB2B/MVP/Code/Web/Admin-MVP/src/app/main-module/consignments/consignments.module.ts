import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ConsignmentsRoutingModule } from './consignments-routing.module';
import { ConsignmentNewComponent } from './consignment-new/consignment-new.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { ConsignmentHistoryComponent } from './consignment-history/consignment-history.component';
import { ConsignmentNewPopupComponent } from './consignment-new/consignment-new-popup/consignment-new-popup.component';
import { TrackHistoryComponent } from './consignment-history/track-history/track-history.component';
import { ConsignmentLinesComponent } from './consignment-new/consignment-lines/consignment-lines.component';
import { CurrencyMaskConfig, CurrencyMaskModule, CURRENCY_MASK_CONFIG } from 'ng2-currency-mask';

export const CustomCurrencyMaskConfig: CurrencyMaskConfig = {
  align: "right",
  allowNegative: true,
  decimal: ".",
  precision: 3,
  prefix: "KWD ",
  suffix: "",
  thousands: ","
};


 
@NgModule({
  declarations: [
    ConsignmentNewComponent,
    ConsignmentHistoryComponent,
    ConsignmentNewPopupComponent,
    TrackHistoryComponent,
    ConsignmentLinesComponent
  ],
  providers: [
    { provide: CURRENCY_MASK_CONFIG, useValue: CustomCurrencyMaskConfig }
],
  imports: [
    CommonModule,
    ConsignmentsRoutingModule,
    SharedModule,
    CurrencyMaskModule
  ]
})
export class ConsignmentsModule { }
