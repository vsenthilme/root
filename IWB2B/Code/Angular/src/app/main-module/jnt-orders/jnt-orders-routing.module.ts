import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { JntOrdersComponent } from './jnt-orders/jnt-orders.component';
import { InternalOrderComponent } from './internal-order/internal-order.component';

const routes: Routes = [
  {
    path: 'jntOrdersList',
    component: JntOrdersComponent
  },
  {
    path: 'internalOrdersList',
    component: InternalOrderComponent
  },
  {
    path: '',
    redirectTo: 'apiconsole'
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class JntOrdersRoutingModule { }
