import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CountriesComponent } from './countries/countries.component';
import { CurrencyComponent } from './currency/currency.component';
import { NumberingSeriesComponent } from './numbering-series/numbering-series.component';
import { EmployeeComponent } from './SBU/employee/employee.component';
import { ItemGroupComponent } from './SBU/item-group/item-group.component';
import { StorageUnitComponent } from './SBU/storage-unit/storage-unit.component';
import { NationalityComponent } from './setup-masters/client-master/nationality/nationality.component';
import { PaymentModeComponent } from './setup-masters/client-master/payment-mode/payment-mode.component';
import { PaymentTermComponent } from './setup-masters/client-master/payment-term/payment-term.component';
import { UomComponent } from './setup-masters/material master/uom/uom.component';
import { WarehousematerialComponent } from './setup-masters/material master/warehousematerial/warehousematerial.component';
import { DoorTypeComponent } from './setup-masters/storageunit/door-type/door-type.component';
import { StorageTypeComponent } from './setup-masters/storageunit/storage-type/storage-type.component';
import { UserManagementComponent } from './user-management/user-management.component';
const routes: Routes = [
  


// {
//   path: 'warehouse',
//   component: WarehouseComponent
// },
// {
//   path: 'warehouse/:companyId/:plantId',
//   component: WarehouseComponent
// },




//ustorage
{
  path: 'User-management',
  component: UserManagementComponent
},
{
  path: 'currency',
  component: CurrencyComponent
},
{
  path: 'countries',
  component: CountriesComponent
},
{
  path: 'numbering-series',
  component: NumberingSeriesComponent
},

{
  path: 'storage-unit',
  component: StorageUnitComponent
},

{
  path: 'exployee',
  component: EmployeeComponent
},
{
  path: 'uitemgroup',
  component: ItemGroupComponent
},

{
  path: 'doortype',
  component: DoorTypeComponent
},

{
  path: 'storagetype',
  component: StorageTypeComponent
},



{
  path: 'uom',
  component: UomComponent
},
{
  path: 'warehouse',
  component: WarehousematerialComponent
},

{
  path: 'payment',
  component: PaymentModeComponent
},

{
  path: 'paymentterm',
  component: PaymentTermComponent
},

{
  path: 'nationality',
  component: NationalityComponent
},

{
  path: '',
  redirectTo: 'UserManagementComponent'
},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SetupRoutingModule { }
