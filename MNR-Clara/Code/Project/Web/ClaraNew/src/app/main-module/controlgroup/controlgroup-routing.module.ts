import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IdmasterModule } from './idmaster/idmaster.module';
import { MasterModule } from './master/master.module';
import { TransactionModule } from './transaction/transaction.module';

const routes: Routes = [{
  path: 'idmaster',
  loadChildren: () => import('./idmaster/idmaster.module').then(m => IdmasterModule)
},

{
  path: 'master',
  loadChildren: () => import( './master/master.module').then(m => MasterModule)
},
{
  path: 'transaction',
  loadChildren: () => import('./transaction/transaction.module').then(m => TransactionModule)
},

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ControlgroupRoutingModule { }
