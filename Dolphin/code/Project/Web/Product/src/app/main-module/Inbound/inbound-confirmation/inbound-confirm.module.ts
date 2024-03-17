import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { InboundConfirmRoutingModule } from './inbound-confirm-routing.module';
import { PutawayDetailsComponent } from './inbound-create/putaway-details/putaway-details.component';
import { PalletdetailsComponent } from './inbound-create/palletdetails/palletdetails.component';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { ReplaceASNComponent } from './replace-asn/replace-asn.component';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';


@NgModule({
  declarations: [
    PutawayDetailsComponent,
    PalletdetailsComponent,
    ReplaceASNComponent
  ],
  imports: [
    CommonModule,
    InboundConfirmRoutingModule,
    SharedModule,
    CommonFieldModule,  NgMultiSelectDropDownModule.forRoot(),
    AngularMultiSelectModule
  ],
  exports:[
    PutawayDetailsComponent,
    PalletdetailsComponent
  ]
})
export class InboundConfirmModule { }
