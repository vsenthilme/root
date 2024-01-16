import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomdialogComponent } from './customdialog/customdialog.component';
import { SharedModule } from '../shared/shared.module';
import { DatepickerComponent } from './datepicker/datepicker.component';
import { StylePaginationDirective } from '../common-directive/style-pagination.directive';
import { RouterModule } from '@angular/router';
import { DeleteComponent } from './delete/delete.component';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';



@NgModule({
  declarations: [
    CustomdialogComponent,
    DatepickerComponent,
    StylePaginationDirective,
    DeleteComponent
  ],
  imports: [
    CommonModule, SharedModule, RouterModule,   NgMultiSelectDropDownModule.forRoot(),    AngularMultiSelectModule,
  ], 
  exports: [CustomdialogComponent]
})
export class CommonFieldModule { }
