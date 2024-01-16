import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AdminModule } from './admin/admin.module';
import { BusinessModule } from './business/business.module';
import { ConfigurationModule } from './configuration/configuration.module';
import { UploadDataComponent } from './upload-data/upload-data.component';


const routes: Routes = [{
  path: 'admin',
  loadChildren: () => import('./admin/admin.module').then(m => AdminModule)
},
{
  path: 'configuration',
  loadChildren: () => import('./configuration/configuration.module').then(m => ConfigurationModule)
},
{
  path: 'business',
  loadChildren: () => import('./business/business.module').then(m => BusinessModule)
},
{ path: 'upload-data', component: UploadDataComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SettingRoutingModule { }
