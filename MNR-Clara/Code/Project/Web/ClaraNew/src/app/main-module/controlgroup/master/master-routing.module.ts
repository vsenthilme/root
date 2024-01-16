import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IdmasterlistComponent } from '../idmaster/idmasterlist/idmasterlist.component';
import { ClientComponent } from './client/client.component';
import { ControlgroupComponent } from './controlgroup/controlgroup.component';
import { ClientcontrolgroupComponent } from './clientcontrolgroup/clientcontrolgroup.component';
import { CliententityassignmentComponent } from './cliententityassignment/cliententityassignment.component';

const routes: Routes = [{
  path: '',
  component: IdmasterlistComponent,
  children:[
  { path: 'client', component: ClientComponent, pathMatch: 'full', data: { title: 'Client', module: 'Client' } },
  { path: 'controlgroup', component: ControlgroupComponent, pathMatch: 'full', data: { title: 'Controlled Groups', module: 'Controlled Groups' } },
  { path: 'clientcontrolgroup', component: ClientcontrolgroupComponent, pathMatch: 'full', data: { title: 'Controlled Groups', module: 'Controlled Groups' } },
  { path: 'cliententityassignment', component: CliententityassignmentComponent, pathMatch: 'full', data: { title: 'Controlled Groups', module: 'Controlled Groups' } },
 
 
]} 
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MasterRoutingModule { }
