import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProfileComponent } from '../profile/profile/profile.component';
import { DashboardComponent } from './dashboard/dashboard.component';

const routes: Routes = [{
  path: '',
  component: DashboardComponent,
    children: [
    {
      path: 'profile',
      component: ProfileComponent,
      pathMatch: 'full', data: { title: 'dd', module: 'Hoddme' } 

    },
  ]
  
   
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule { }
