import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DeliveryManagementRoutingModule } from './delivery-management-routing.module';
import { ConsignmentsComponent } from './consignments/consignments.component';
import { PickupComponent } from './pickup/pickup.component';
import { DeliveryComponent } from './delivery/delivery.component';
import { DeliveryMannagementTabComponent } from './delivery-mannagement-tab/delivery-mannagement-tab.component';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { ConsignmentsEditComponent } from './consignments/consignments-edit/consignments-edit.component';
import { ConsignmentsOpenComponent } from './consignments/consignments-open/consignments-open.component';
import { PickupOpenComponent } from './pickup/pickup-open/pickup-open.component';
import { DeliveryOpenComponent } from './delivery/delivery-open/delivery-open.component';

@NgModule({
  declarations: [
    ConsignmentsComponent,
    PickupComponent,
    DeliveryComponent,
    DeliveryMannagementTabComponent,
    ConsignmentsEditComponent,
    ConsignmentsOpenComponent,
    PickupOpenComponent,
    DeliveryOpenComponent
  ],
  imports: [
    CommonModule,
    DeliveryManagementRoutingModule,
    SharedModule,
    CommonFieldModule,
  ]
})
export class DeliveryManagementModule { }
