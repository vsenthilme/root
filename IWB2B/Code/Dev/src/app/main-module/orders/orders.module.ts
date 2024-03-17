import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OrdersRoutingModule } from './orders-routing.module';
import { OrdersListComponent } from './orders-list/orders-list.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { OrdersTabComponent } from './orders-tab/orders-tab.component';
import { PostOrderComponent } from './post-order/post-order.component';


@NgModule({
  declarations: [
    OrdersListComponent,
    OrdersTabComponent,
    PostOrderComponent
  ],
  imports: [
    CommonModule,
    OrdersRoutingModule,
    SharedModule,
  ]
})
export class OrdersModule { }
