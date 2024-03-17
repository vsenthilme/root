import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ArchivesLogComponent } from './archives-log/archives-log.component';
import { DatatrafficComponent } from './datatraffic/datatraffic.component';

const routes: Routes = [ 
  {
  path: 'archivesLog',
  component: ArchivesLogComponent
},  {
  path: 'datatraffic',
  component: DatatrafficComponent
},
{
  path: '',
  redirectTo: 'apiconsole'
},];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AnalyticalRoutingModule { }
