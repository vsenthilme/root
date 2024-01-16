import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CustomerMasterRoutingModule } from './customer-master-routing.module';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { CustomermaterNewComponent } from './customermater-new/customermater-new.component';
import { CustomerListComponent } from './customer-list/customer-list.component';
import { TextMaskModule } from 'angular2-text-mask';
import { DownloadComponent } from './customermater-new/download/download.component';
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
    CustomermaterNewComponent,
    CustomerListComponent,
    DownloadComponent
  ],
  imports: [
    CommonModule,
    CustomerMasterRoutingModule,
    CommonModule,
    SharedModule,
    CommonFieldModule,
    NgMultiSelectDropDownModule.forRoot(),
    AngularMultiSelectModule,
    TextMaskModule,
    CurrencyMaskModule
  ]
})
export class CustomerMasterModule { }
