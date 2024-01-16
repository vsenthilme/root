import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OrdersListComponent } from './orders-list/orders-list.component';
import { PostOrderComponent } from './post-order/post-order.component';

const routes: Routes = [
  {
    path: 'ordersMain',
    component: OrdersListComponent
  },
  {
    path: 'postORder',
    component: PostOrderComponent
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
export class OrdersRoutingModule { }
