
//import { MainModuleModule } from './main-module/main-module.module';

import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from './core/Auth/auth.guard';
import { CustomPreloadingService } from './custom-preloading.service';

const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./login/login.module').then(m => m.LoginModule),

  },
  {
    path: 'main', data: {preload: true},
    //  loadChildren: () => import('./main-module/main-module.module').then(m => m.MainModuleModule),
    canActivate: [AuthGuard],
    loadChildren: () => import('./main-module/main-module.module').then(m => m.MainModuleModule),

  },
  {
    path: 'mr',data: {preload: false},
    //  loadChildren: () => import('./main-module/main-module.module').then(m => m.MainModuleModule),
    loadChildren: () => import('./customerform/customerform.module').then(m => m.CustomerformModule),

  },
  // otherwise redirect to home
  //{ path: '**', redirectTo: '' }

];




@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    [RouterModule.forRoot(routes, { preloadingStrategy: CustomPreloadingService })],
  ]
})
export class AppRoutingModule { }
