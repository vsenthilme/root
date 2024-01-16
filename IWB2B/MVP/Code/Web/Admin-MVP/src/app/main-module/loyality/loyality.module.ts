import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LoyalityRoutingModule } from './loyality-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { LoyaltyCategoryComponent } from './loyalty-category/loyalty-category.component';
import { LoyaltySetupComponent } from './loyalty-setup/loyalty-setup.component';
import { CategoryNewComponent } from './loyalty-category/category-new/category-new.component';
import { SetupNewComponent } from './loyalty-setup/setup-new/setup-new.component';
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
    LoyaltyCategoryComponent,
    LoyaltySetupComponent,
    CategoryNewComponent,
    SetupNewComponent
  ],
  imports: [
    CommonModule,
    LoyalityRoutingModule,
    SharedModule,
    CurrencyMaskModule
  ]
})
export class LoyalityModule { }
