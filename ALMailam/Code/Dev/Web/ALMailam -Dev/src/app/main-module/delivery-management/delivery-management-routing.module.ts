import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ConsignmentsComponent } from './consignments/consignments.component';
import { PickupComponent } from './pickup/pickup.component';
import { DeliveryComponent } from './delivery/delivery.component';
import { ConsignmentsEditComponent } from './consignments/consignments-edit/consignments-edit.component';
import { ConsignmentsOpenComponent } from './consignments/consignments-open/consignments-open.component';
import { DeliveryOpenComponent } from './delivery/delivery-open/delivery-open.component';
import { ManifestComponent } from './manifest/manifest/manifest.component';
import { ManifestEditComponent } from './manifest/manifest/manifest-edit/manifest-edit.component';
import { PickupEditComponent } from './pickup/pickup-edit/pickup-edit.component';



const routes: Routes = [
  {
    path: 'consignment',
    component: ConsignmentsComponent
  },
  {
    path: 'pickup',
    component: PickupComponent
  },
  {
    path: 'pickupEdit/:code',
    component: PickupEditComponent
  },
  {
    path: 'delivery',
    component: DeliveryComponent
  },
  {
    path: 'consignmentEdit/:code',
    component: ConsignmentsEditComponent
  },
  {
    path: 'consignment-open',
    component: ConsignmentsOpenComponent
  }, 
  {
    path: 'delivery-open/:code',
    component: DeliveryOpenComponent
  },
  
  {
    path: 'manifestEdit/:code',
    component: ManifestEditComponent
  },
  {
    path: 'manifest',
    component: ManifestComponent
  },


];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DeliveryManagementRoutingModule { }
