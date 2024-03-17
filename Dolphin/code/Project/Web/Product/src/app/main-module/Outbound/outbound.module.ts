import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OutboundRoutingModule } from './outbound-routing.module';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';
import { AssignmentModule } from './assignment/assignment.module';
import { DeliveryModule } from './delivery/delivery.module';
import { PickingModule } from './picking/picking.module';
import { QualityModule } from './quality/quality.module';
import { ReversalOutboundModule } from './reversal-outbound/reversal-outbound.module';
import { PreoutboundModule } from './preoutbound/preoutbound.module';
import { OrderManagementModule } from './order-management/order-management.module';


@NgModule({
  declarations: [
  
  ],
  imports: [
    CommonModule,
    OutboundRoutingModule,
    SharedModule,
    CommonFieldModule,AngularMultiSelectModule,
    AssignmentModule,
    PickingModule,
    QualityModule,
    DeliveryModule,
    ReversalOutboundModule,
    PreoutboundModule,
    OrderManagementModule
  ]
})
export class OutboundModule { }
