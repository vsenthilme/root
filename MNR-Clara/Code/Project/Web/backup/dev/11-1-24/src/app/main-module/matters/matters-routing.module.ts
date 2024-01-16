import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CaseAssignmentModule } from './case-assignment/case-assignment.module';
import { CaseInfoModule } from './case-info/case-info.module';
import { CaseManagementModule } from './case-management/case-management.module';

const routes: Routes = [{
  path: 'case-info',
  loadChildren: () => import('./case-info/case-info.module').then(m => CaseInfoModule)
},
{
  path: 'case-management',
  loadChildren: () => import('./case-management/case-management.module').then(m => CaseManagementModule)
}, {
  path: 'case-assignment',
  loadChildren: () => import('./case-assignment/case-assignment.module').then(m => CaseAssignmentModule)
},


];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MattersRoutingModule { }
