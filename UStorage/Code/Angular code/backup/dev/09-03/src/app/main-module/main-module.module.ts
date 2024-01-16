import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainModuleRoutingModule } from './main-module-routing.module';
import { HomeComponent } from './home/home.component';
import { SharedModule } from '../shared/shared.module';
import { CommonFieldModule } from '../common-field/common-field.module';
import { SetupModule } from './setup/setup.module';
import { ReportstabComponent } from './reports/reportstab/reportstab.component';
import { ReportsModule } from './reports/reports.module';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';
import { NotificationComponent } from './home/notification/notification.component';



@NgModule({
  declarations: [
    HomeComponent,
    ReportstabComponent,
    NotificationComponent,


  ],
  imports: [
    CommonModule,
    SharedModule,
    MainModuleRoutingModule,
    CommonFieldModule,
    SetupModule,
    ReportsModule,
    NgMultiSelectDropDownModule.forRoot(),
    AngularMultiSelectModule,
  ]
})
export class MainModuleModule { }
