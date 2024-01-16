import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CrmRoutingModule } from './crm-routing.module';
import { EnquiryComponent } from './enquiry/enquiry.component';
import { QuoteComponent } from './quote/quote.component';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { CrmTabComponent } from './crm-tab/crm-tab.component';
import { QuoteNewComponent } from './quote/quote-new/quote-new.component';
import { EnquiryNewComponent } from './enquiry/enquiry-new/enquiry-new.component';

import { TextMaskModule } from 'angular2-text-mask';
import { StorageavailablityComponent } from './enquiry/enquiry-new/storageavailablity/storageavailablity.component';
import { CustomerDetailsComponent } from './enquiry/enquiry-new/customer-details/customer-details.component';
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
    EnquiryComponent,
    QuoteComponent,
    CrmTabComponent,
    QuoteNewComponent,
    EnquiryNewComponent,
    StorageavailablityComponent,
    CustomerDetailsComponent,
  ],
  imports: [
    CommonModule,
    CrmRoutingModule,
    SharedModule,
    CommonFieldModule,
    NgMultiSelectDropDownModule.forRoot(),
    AngularMultiSelectModule,
    TextMaskModule,
    CurrencyMaskModule
  ]
})
export class CrmModule { }
