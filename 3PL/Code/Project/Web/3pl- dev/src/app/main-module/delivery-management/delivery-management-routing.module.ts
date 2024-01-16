import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ConsignmentsComponent } from './consignments/consignments.component';
import { PickupComponent } from './pickup/pickup.component';
import { DeliveryComponent } from './delivery/delivery.component';
import { ConsignmentsEditComponent } from './consignments/consignments-edit/consignments-edit.component';
import { ConsignmentsOpenComponent } from './consignments/consignments-open/consignments-open.component';
import { PickupOpenComponent } from './pickup/pickup-open/pickup-open.component';
import { DeliveryOpenComponent } from './delivery/delivery-open/delivery-open.component';



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
    path: 'delivery',
    component: DeliveryComponent
  },
  {
    path: 'consignment-edit',
    component: ConsignmentsEditComponent
  },
  {
    path: 'consignment-open',
    component: ConsignmentsOpenComponent
  }, 
   {
    path: 'pickup-open',
    component: PickupOpenComponent
  },
  {
    path: 'delivery-open',
    component: DeliveryOpenComponent
  },


];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DeliveryManagementRoutingModule { }
