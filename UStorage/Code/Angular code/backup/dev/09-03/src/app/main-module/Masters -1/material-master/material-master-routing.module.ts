import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ConsumablesNewComponent } from './consumables/consumables-new/consumables-new.component';
import { ConsumablesComponent } from './consumables/consumables.component';
import { FlRentNewComponent } from './fl-rent/fl-rent-new/fl-rent-new.component';
import { FlRentComponent } from './fl-rent/fl-rent.component';
import { HandlingChargesNewComponent } from './handlingcharges/handling-charges-new/handling-charges-new.component';
import { HandlingchargesComponent } from './handlingcharges/handlingcharges.component';
import { MaterialNewComponent } from './material-new/material-new.component';
import { MaterialTabComponent } from './material-tab/material-tab.component';
import { StorageNewComponent } from './storage-unit/storage-new/storage-new.component';
import { StorageUnitComponent } from './storage-unit/storage-unit.component';
import { TripsNewComponent } from './trips/trips-new/trips-new.component';
import { TripsComponent } from './trips/trips.component';

const routes: Routes = [
  {
    path: 'storageunit',
    component: StorageUnitComponent
  },
  {
    path: 'storageunitNew/:code',
    component: StorageNewComponent
  },
  {
    path: 'consumables-new/:code',
    component: ConsumablesNewComponent
  },
  {
    path: 'consumables',
    component: ConsumablesComponent
  },
  {
    path: 'flrent',
    component: FlRentComponent
  }, 
  {
    path: 'flrent-new/:code',
    component: FlRentNewComponent
  },
  {
    path: 'trip',
    component: TripsComponent
  },
  {
    path: 'trip-new/:code',
    component: TripsNewComponent
  },
  {
    path: 'handling-charges',
    component: HandlingchargesComponent
  },
  {
    path: 'handling-charges-new/:code',
    component: HandlingChargesNewComponent
  },
  {
    path: 'material-new/:code',
    component: MaterialNewComponent
  },


  {
    path: '',
    redirectTo: 'storageunit'
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MaterialMasterRoutingModule { }
