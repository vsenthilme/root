import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MaterialMasterRoutingModule } from './material-master-routing.module';
import { StorageUnitComponent } from './storage-unit/storage-unit.component';
import { MaterialTabComponent } from './material-tab/material-tab.component';
import { ConsumablesComponent } from './consumables/consumables.component';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { TripsComponent } from './trips/trips.component';
import { FlRentComponent } from './fl-rent/fl-rent.component';
import { HandlingchargesComponent } from './handlingcharges/handlingcharges.component';
import { MaterialNewComponent } from './material-new/material-new.component';
import { StorageNewComponent } from './storage-unit/storage-new/storage-new.component';
import { ConsumablesNewComponent } from './consumables/consumables-new/consumables-new.component';
import { FlRentNewComponent } from './fl-rent/fl-rent-new/fl-rent-new.component';
import { HandlingChargesNewComponent } from './handlingcharges/handling-charges-new/handling-charges-new.component';
import { TripsNewComponent } from './trips/trips-new/trips-new.component';
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
    StorageUnitComponent,
    MaterialTabComponent,
    ConsumablesComponent,
    TripsComponent,
    FlRentComponent,
    HandlingchargesComponent,
    MaterialNewComponent,
    StorageNewComponent,
    ConsumablesNewComponent,
    FlRentNewComponent,
    HandlingChargesNewComponent,
    TripsNewComponent
  ],
  imports: [
    CommonModule,
    MaterialMasterRoutingModule,
    CommonModule,
    SharedModule,
    CommonFieldModule,
    NgMultiSelectDropDownModule.forRoot(),
    AngularMultiSelectModule,
    CurrencyMaskModule
  ],
  exports: [
    StorageUnitComponent,
    MaterialTabComponent,
    ConsumablesComponent
  ]
})
export class MaterialMasterModule { }
