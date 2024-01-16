import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomdialogComponent } from './customdialog/customdialog.component';
import { SharedModule } from '../shared/shared.module';
import { StylePaginationDirective } from '../common-directive/style-pagination.directive';
import { SubsidenavComponent } from './subsidenav/subsidenav.component';
import { RouterModule } from '@angular/router';
import { DeleteComponent } from './delete/delete.component';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';



@NgModule({
  declarations: [
    CustomdialogComponent,
    StylePaginationDirective,
    SubsidenavComponent,
    DeleteComponent
  ],
  imports: [
    CommonModule, SharedModule, RouterModule,   NgMultiSelectDropDownModule.forRoot(),    AngularMultiSelectModule,
  ], 
  exports: [CustomdialogComponent, SubsidenavComponent,]
})
export class CommonFieldModule { }
