import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PaymentPlanRoutingModule } from './payment-plan-routing.module';
import { PaymentPlanComponent } from './payment-plan/payment-plan.component';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { PaymentplanDetailsComponent } from './paymentplan-details/paymentplan-details.component';


@NgModule({
  declarations: [
    PaymentPlanComponent,
    PaymentplanDetailsComponent
  ],
  imports: [
    CommonModule,
    PaymentPlanRoutingModule,
    SharedModule,
    CommonFieldModule
  ]
})
export class PaymentPlanModule { }
