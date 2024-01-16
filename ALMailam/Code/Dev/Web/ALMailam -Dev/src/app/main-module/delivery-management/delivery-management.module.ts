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
import { DeliveryOpenComponent } from './delivery/delivery-open/delivery-open.component';
import { DeliverypopComponent } from './pickup/deliverypop/deliverypop.component';
import { ManifestComponent } from './manifest/manifest/manifest.component';
import { ManifestEditComponent } from './manifest/manifest/manifest-edit/manifest-edit.component';
import { PickupEditComponent } from './pickup/pickup-edit/pickup-edit.component';

@NgModule({
  declarations: [
    ConsignmentsComponent,
    PickupComponent,
    DeliveryComponent,
    DeliveryMannagementTabComponent,
    ConsignmentsEditComponent,
    ConsignmentsOpenComponent,
    DeliveryOpenComponent,
    DeliverypopComponent,
    ManifestComponent,
    ManifestEditComponent,
    PickupEditComponent
  ],
  imports: [
    CommonModule,
    DeliveryManagementRoutingModule,
    SharedModule,
    CommonFieldModule,
  ]
})
export class DeliveryManagementModule { }
