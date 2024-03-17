import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ConsignmentComponent } from './consignment/consignment.component';
import { DeliveryComponent } from './delivery/delivery.component';
import { HubopsComponent } from './hubops/hubops.component';
import { MediumsComponent } from './mediums/mediums.component';
import { PickingComponent } from './picking/picking.component';

const routes: Routes = [
  {
    path: 'delivery',
    component: DeliveryComponent
  }, 
  {
    path: 'picking',
    component: PickingComponent
  }, 
  {
    path: 'consignment',
    component: ConsignmentComponent
  },
  {
    path: 'mediums',
    component: MediumsComponent
  },
  {
    path: 'hubops',
    component: HubopsComponent
  },
  {
    path: '',
    redirectTo: 'delivery'
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LmdRoutingModule { }
