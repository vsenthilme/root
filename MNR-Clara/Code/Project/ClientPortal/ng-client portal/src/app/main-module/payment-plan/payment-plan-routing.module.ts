import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PaymentPlanComponent } from './payment-plan/payment-plan.component';
import { PaymentplanDetailsComponent } from './paymentplan-details/paymentplan-details.component';

const routes: Routes = [
  { path: 'payment', component: PaymentPlanComponent,  },
  { path: 'payment-details/:code', component: PaymentplanDetailsComponent,  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PaymentPlanRoutingModule { }
