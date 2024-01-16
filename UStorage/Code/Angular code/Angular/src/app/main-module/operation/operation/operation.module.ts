import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OperationRoutingModule } from './operation-routing.module';
import { AgreementComponent } from './agreement/agreement.component';
import { InvoiceComponent } from './invoice/invoice.component';
import { PaymentComponent } from './payment/payment.component';
import { WorkorderComponent } from './workorder/workorder.component';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { OperationTabComponent } from './operation-tab/operation-tab.component';
import { PaymentNewComponent } from './payment/payment-new/payment-new.component';
import { AgreementNewComponent } from './agreement/agreement-new/agreement-new.component';
import { InvoiceNewComponent } from './invoice/invoice-new/invoice-new.component';
import { CopyfromComponent } from './invoice/copyfrom/copyfrom.component';
import { WorkorderNewComponent } from './workorder/workorder-new/workorder-new.component';

import { TextMaskModule } from 'angular2-text-mask';
import { StoreTableComponent } from './agreement/store-table/store-table.component';
import { CurrencyMaskConfig, CurrencyMaskModule, CURRENCY_MASK_CONFIG } from 'ng2-currency-mask';

export const CustomCurrencyMaskConfig: CurrencyMaskConfig = {
  align: "right",
  allowNegative: true,
  decimal: ".",
  precision: 3,
  prefix: "KWD ",
  suffix: "",
  thousands: ","
};
@NgModule({
  providers: [
    { provide: CURRENCY_MASK_CONFIG, useValue: CustomCurrencyMaskConfig }
],
  declarations: [
    AgreementComponent,
    InvoiceComponent,
    PaymentComponent,
    WorkorderComponent,
    OperationTabComponent,
    PaymentNewComponent,
    AgreementNewComponent,
    InvoiceNewComponent,
    CopyfromComponent,
    WorkorderNewComponent,
    StoreTableComponent
  ],
  imports: [
    CommonModule,
    OperationRoutingModule,
    SharedModule,
    CommonFieldModule,
    NgMultiSelectDropDownModule.forRoot(),
    AngularMultiSelectModule,
    TextMaskModule,
    CurrencyMaskModule
  ]
})
export class OperationModule { }
