import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AisleComponent } from './aisle/aisle.component';
import { BarcodesubtypeidComponent } from './barcodesubtypeid/barcodesubtypeid.component';
import { BarcodetypeidComponent } from './barcodetypeid/barcodetypeid.component';
import { CityComponent } from './city/city.component';
import { CompanyComponent } from './company/company.component';
import { CountryComponent } from './country/country.component';
import { CurrencyComponent } from './currency/currency.component';
import { FloorComponent } from './floor/floor.component';
import { ItemgroupComponent } from './itemgroup/itemgroup.component';
import { ItemtypeComponent } from './itemtype/itemtype.component';
import { LevelComponent } from './level/level.component';
import { MenuNewComponent } from './menu/menu-new/menu-new.component';
import { MenuComponent } from './menu/menu.component';
import { PlantComponent } from './plant/plant.component';
import { ProcessComponent } from './process/process.component';
import { RowComponent } from './row/row.component';
import { RowService } from './row/row.service';
import { StateComponent } from './state/state.component';
import { StatusidComponent } from './statusid/statusid.component';
import { StorageSectionComponent } from './storage-section/storage-section.component';
import { StoragebintypeComponent } from './storagebintype/storagebintype.component';
import { StorageclassComponent } from './storageclass/storageclass.component';
import { StoragetypeComponent } from './storagetype/storagetype.component';
import { StratergyComponent } from './stratergy/stratergy.component';
import { SubitemgroupComponent } from './subitemgroup/subitemgroup.component';
import { UomComponent } from './uom/uom.component';
import { UsertypeComponent } from './usertype/usertype.component';

import { VerticalComponent } from './vertical/vertical.component';
import { WarehouseComponent } from './warehouse/warehouse.component';
import { WarehousetypeComponent } from './warehousetype/warehousetype.component';

const routes: Routes = [
{
  path: 'vertical',
  component: VerticalComponent
},
{
  path: 'company',
  component: CompanyComponent
},
{
  path: 'plant',
  component: PlantComponent
},
{
  path: 'warehouse',
  component: WarehouseComponent
},
{
  path: 'storagesection',
  component: StorageSectionComponent
},

{
  path: 'status',
  component: StatusidComponent
},
{
  path: 'aisle',
  component: AisleComponent
},
{
  path: 'storageclass',
  component: StorageclassComponent
},
{
  path: 'storagetype',
  component: StoragetypeComponent
},
{
  path: 'storagebintype',
  component: StoragebintypeComponent
},
{
  path: 'processsequence',
  component:  ProcessComponent
},
{
  path: 'usertype',
  component:  UsertypeComponent
},
{
  path: 'uom',
  component: UomComponent
},
{
  path: 'level',
  component: LevelComponent
},
{
  path: 'row',
  component: RowComponent
},
{
  path: 'currency',
  component: CurrencyComponent
},
{
  path: 'barcodetypeid',
  component: BarcodetypeidComponent
},
{
  path: 'barcodesubtypeid',
  component: BarcodesubtypeidComponent 
},
{
  path: 'stratergy',
  component: StratergyComponent 
},
{
  path: 'warehousetype',
  component: WarehousetypeComponent 
},
{
  path: 'menu',
  component:  MenuComponent
},
{
  path: 'country',
  component:  CountryComponent
},
{
  path: 'state',
  component:  StateComponent
},
{
  path: 'city',
  component:  CityComponent
},
{
  path: 'itemtype',
  component:  ItemtypeComponent
},
{
  path: 'itemgroup',
  component:  ItemgroupComponent
},
{
  path: 'floor',
  component:  FloorComponent
},
{
  path: 'subitemgroup',
  component:  SubitemgroupComponent
},


{
  path: '',
  redirectTo: 'vertical'
},];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OtherSetupRoutingModule { }
