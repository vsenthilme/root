import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TranserrorComponent } from './transerror/transerror.component';

const routes: Routes = [
  {
    path: 'error',
    component: TranserrorComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TranserrorRoutingModule { }
