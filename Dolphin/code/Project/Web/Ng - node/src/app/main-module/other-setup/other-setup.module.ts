import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OtherSetupRoutingModule } from './other-setup-routing.module';
import { VerticalComponent } from './vertical/vertical.component';
import { SetupTabComponent } from './setup-tab/setup-tab.component';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { CommonFieldModule } from 'src/app/common-field/common-field.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { CompanyComponent } from './company/company.component';
import { WarehouseComponent } from './warehouse/warehouse.component';
import { PlantComponent } from './plant/plant.component';
import { StorageSectionComponent } from './storage-section/storage-section.component';
import { RowComponent } from './row/row.component';
import { RowNewComponent } from './row/row-new/row-new.component';
import { VerticalNewComponent } from './vertical/vertical-new/vertical-new.component';
import { PlantNewComponent } from './plant/plant-new/plant-new.component';
import { WarehouseNewComponent } from './warehouse/warehouse-new/warehouse-new.component';
import { StorageSectionNewComponent } from './storage-section/storage-section-new/storage-section-new.component';
import { StatusidComponent } from './statusid/statusid.component';
import { StatusidNewComponent } from './statusid/statusid-new/statusid-new.component';
import { CompanyNewComponent } from './company/company-new/company-new.component';
import { AisleComponent } from './aisle/aisle.component';
import { StorageclassComponent } from './storageclass/storageclass.component';
import { StorageclassNewComponent } from './storageclass/storageclass-new/storageclass-new.component';
import { StoragetypeComponent } from './storagetype/storagetype.component';
import { StoragetypeNewComponent } from './storagetype/storagetype-new/storagetype-new.component';
import { StoragebintypeComponent } from './storagebintype/storagebintype.component';
import { StoragebintypeNewComponent } from './storagebintype/storagebintype-new/storagebintype-new.component';
import { ProcessComponent } from './process/process.component';
import { ProcessNewComponent } from './process/process-new/process-new.component';
import { UsertypeComponent } from './usertype/usertype.component';
import { UsertypeNewComponent } from './usertype/usertype-new/usertype-new.component';
import { UomComponent } from './uom/uom.component';
import { UomNewComponent } from './uom/uom-new/uom-new.component';
import { LevelComponent } from './level/level.component';
import { LevelNewComponent } from './level/level-new/level-new.component';
import { CurrencyComponent } from './currency/currency.component';
import { CurrencyNewComponent } from './currency/currency-new/currency-new.component';
import { BarcodetypeidComponent } from './barcodetypeid/barcodetypeid.component';
import { BarcodetypeidNewComponent } from './barcodetypeid/barcodetypeid-new/barcodetypeid-new.component';
import { BarcodesubtypeidComponent } from './barcodesubtypeid/barcodesubtypeid.component';
import { BarcodesubtypeidNewComponent } from './barcodesubtypeid/barcodesubtypeid-new/barcodesubtypeid-new.component';
import { StratergyComponent } from './stratergy/stratergy.component';
import { StratergyNewComponent } from './stratergy/stratergy-new/stratergy-new.component';
import { WarehousetypeComponent } from './warehousetype/warehousetype.component';
import { WarehousetypeNewComponent } from './warehousetype/warehousetype-new/warehousetype-new.component';
import { MenuComponent } from './menu/menu.component';
import { MenuNewComponent } from './menu/menu-new/menu-new.component';
import { CountryComponent } from './country/country.component';
import { CountryNewComponent } from './country/country-new/country-new.component';
import { StateComponent } from './state/state.component';
import { StateNewComponent } from './state/state-new/state-new.component';
import { CityComponent } from './city/city.component';
import { CityNewComponent } from './city/city-new/city-new.component';
import { ItemtypeComponent } from './itemtype/itemtype.component';
import { ItemtypeNewComponent } from './itemtype/itemtype-new/itemtype-new.component';
import { ItemgroupComponent } from './itemgroup/itemgroup.component';
import { ItemgroupNewComponent } from './itemgroup/itemgroup-new/itemgroup-new.component';
import { FloorComponent } from './floor/floor.component';
import { FloorNewComponent } from './floor/floor-new/floor-new.component';
import { SubitemgroupComponent } from './subitemgroup/subitemgroup.component';
import { SubitemgroupNewComponent } from './subitemgroup/subitemgroup-new/subitemgroup-new.component';



@NgModule({
  declarations: [
    VerticalComponent,
    SetupTabComponent,
    CompanyComponent,
    WarehouseComponent,
    PlantComponent,
    StorageSectionComponent,
    RowComponent,
    RowNewComponent,
    VerticalNewComponent,
    PlantNewComponent,
    WarehouseNewComponent,
    StorageSectionNewComponent,
    StatusidComponent,
    StatusidNewComponent,
    CompanyNewComponent,
    AisleComponent,
    StorageclassComponent,
    StorageclassNewComponent,
    StoragetypeComponent,
    StoragetypeNewComponent,
    StoragebintypeComponent,
    StoragebintypeNewComponent,
    ProcessComponent,
    ProcessNewComponent,
    UsertypeComponent,
    UsertypeNewComponent,
    UomComponent,
    UomNewComponent,
    LevelComponent,
    LevelNewComponent,
    CurrencyComponent,
    CurrencyNewComponent,
    BarcodetypeidComponent,
    BarcodetypeidNewComponent,
    BarcodesubtypeidComponent,
    BarcodesubtypeidNewComponent,
    StratergyComponent,
    StratergyNewComponent,
    WarehousetypeComponent,
    WarehousetypeNewComponent,
    MenuComponent,
    MenuNewComponent,
    CountryComponent,
    CountryNewComponent,
    StateComponent,
    StateNewComponent,
    CityComponent,
    CityNewComponent,
    ItemtypeComponent,
    ItemtypeNewComponent,
    ItemgroupComponent,
    ItemgroupNewComponent,
    FloorComponent,
    FloorNewComponent,
    SubitemgroupComponent,
    SubitemgroupNewComponent
  ],
  imports: [
    CommonModule,
    OtherSetupRoutingModule,
    SharedModule,
    CommonFieldModule,
    NgMultiSelectDropDownModule.forRoot(),
    AngularMultiSelectModule,
  ],
  exports: [
    VerticalComponent,
    SetupTabComponent
  ]
})
export class OtherSetupModule { }
