import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { JntOrdersRoutingModule } from './jnt-orders-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { JntOrdersComponent } from './jnt-orders/jnt-orders.component';
import { JntTabComponent } from './jnt-tab/jnt-tab.component';
import { InternalOrderComponent } from './internal-order/internal-order.component';


@NgModule({
  declarations: [
    JntOrdersComponent,
    JntTabComponent,
    InternalOrderComponent
  ],
  imports: [
    CommonModule,
    JntOrdersRoutingModule,
    SharedModule,
  ]
})
export class JntOrdersModule { }
