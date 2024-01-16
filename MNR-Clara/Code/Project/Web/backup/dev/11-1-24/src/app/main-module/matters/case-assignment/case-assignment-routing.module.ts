import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CaseassignmentListComponent } from './caseassignment-list/caseassignment-list.component';
import { ResouceAssignmentComponent } from './resouce-assignment/resouce-assignment.component';
const routes: Routes = [
  { path: 'caselist', component: CaseassignmentListComponent, pathMatch: 'full', data: { title: 'Case Assignment List', module: 'Case Assignment' } },
  { path: 'resource-assigment/:code', component: ResouceAssignmentComponent, pathMatch: 'full', data: { title: 'Case Assignment', module: 'Case Assignment' } },
  
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CaseAssignmentRoutingModule { }
