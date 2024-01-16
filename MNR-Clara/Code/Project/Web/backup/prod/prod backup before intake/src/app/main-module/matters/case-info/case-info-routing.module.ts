import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ImmigrationListComponent } from './Immigiration/immigration-list/immigration-list.component';
import { ImmigrationNewComponent } from './Immigiration/immigration-new/immigration-new.component';
import { LandEListComponent } from './L&E/land-e-list/land-e-list.component';
import { LandENewComponent } from './L&E/land-e-new/land-e-new.component';
const routes: Routes = [
  { path: 'lande', component: LandEListComponent, pathMatch: 'full', data: { title: 'L & E', module: 'Case Info Sheet' } },
  { path: 'landenew/:code', component: LandENewComponent, pathMatch: 'full', data: { title: 'L & E', module: 'Case Info Sheet' } },
  { path: 'immigration', component: ImmigrationListComponent, pathMatch: 'full', data: { title: 'Immigration ', module: 'Case Info Sheet' } },
  { path: 'immigrationnew/:code', component: ImmigrationNewComponent, pathMatch: 'full', data: { title: 'Immigration ', module: 'Case Info Sheet' } },

];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CaseInfoRoutingModule { }
