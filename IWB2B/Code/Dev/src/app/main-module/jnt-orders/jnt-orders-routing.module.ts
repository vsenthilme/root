import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { JntOrdersComponent } from './jnt-orders/jnt-orders.component';
import { InternalOrderComponent } from './internal-order/internal-order.component';
import { QatarPostComponent } from './qatar-post/qatar-post.component';

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
    path: 'qatarPost',
    component: QatarPostComponent
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
