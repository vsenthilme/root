import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SetupRoutingModule } from './setup-routing.module';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';

import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';
import { UserManagementComponent } from './user-management/user-management.component';
import { CurrencyComponent } from './currency/currency.component';
import { CountriesComponent } from './countries/countries.component';
import { NumberingSeriesComponent } from './numbering-series/numbering-series.component';
import { UsermanagementNewComponent } from './user-management/usermanagement-new/usermanagement-new.component';
import { StorageUnitComponent } from './SBU/storage-unit/storage-unit.component';
import { EmployeeComponent } from './SBU/employee/employee.component';
import { ItemGroupComponent } from './SBU/item-group/item-group.component';
import { StorageTypeComponent } from './setup-masters/storageunit/storage-type/storage-type.component';
import { DoorTypeComponent } from './setup-masters/storageunit/door-type/door-type.component';
import { UomComponent } from './setup-masters/material master/uom/uom.component';
import { WarehousematerialComponent } from './setup-masters/material master/warehousematerial/warehousematerial.component';
import { PaymentModeComponent } from './setup-masters/client-master/payment-mode/payment-mode.component';
import { PaymentTermComponent } from './setup-masters/client-master/payment-term/payment-term.component';
import { NationalityComponent } from './setup-masters/client-master/nationality/nationality.component';
import { StoragetypeNewComponent } from './setup-masters/storageunit/storage-type/storagetype-new/storagetype-new.component';
import { DoortypeComponent } from './setup-masters/storageunit/door-type/doortype/doortype.component';
import { UomNewComponent } from './setup-masters/material master/uom/uom-new/uom-new.component';
import { WarehouseNewComponent } from './setup-masters/material master/warehousematerial/warehouse-new/warehouse-new.component';
import { NationalityNewComponent } from './setup-masters/client-master/nationality/nationality-new/nationality-new.component';
import { PaymenttermNewComponent } from './setup-masters/client-master/payment-term/paymentterm-new/paymentterm-new.component';
import { PaymentmodeNewComponent } from './setup-masters/client-master/payment-mode/paymentmode-new/paymentmode-new.component';
import { ItemgroupNewComponent } from './SBU/item-group/itemgroup-new/itemgroup-new.component';
import { CurrencyNewComponent } from './currency/currency-new/currency-new.component';
import { CountryNewComponent } from './countries/country-new/country-new.component';
import { NumberingseriesNewComponent } from './numbering-series/numberingseries-new/numberingseries-new.component';
import { SbuNewComponent } from './SBU/storage-unit/sbu-new/sbu-new.component';
import { EmployeeNewComponent } from './SBU/employee/employee-new/employee-new.component';
import { TextMaskModule } from 'angular2-text-mask';
@NgModule({
  declarations: [
    UserManagementComponent,
    CurrencyComponent,
    CountriesComponent,
    NumberingSeriesComponent,
    UsermanagementNewComponent,
    StorageUnitComponent,
    EmployeeComponent,
    ItemGroupComponent,
    StorageTypeComponent,
    DoorTypeComponent,
    UomComponent,
    WarehousematerialComponent,
    PaymentModeComponent,
    PaymentTermComponent,
    NationalityComponent,
    StoragetypeNewComponent,
    DoortypeComponent,
    UomNewComponent,
    WarehouseNewComponent,
    NationalityNewComponent,
    PaymenttermNewComponent,
    PaymentmodeNewComponent,
    ItemgroupNewComponent,
    CurrencyNewComponent,
    CountryNewComponent,
    NumberingseriesNewComponent,
    SbuNewComponent,
    EmployeeNewComponent,
  ],
  imports: [
    CommonModule,
    SetupRoutingModule,
    SharedModule,
    TextMaskModule,
    CommonFieldModule, AngularMultiSelectModule,
    NgMultiSelectDropDownModule.forRoot(),
  ],
  exports: [
  ],
})
export class SetupModule { }
