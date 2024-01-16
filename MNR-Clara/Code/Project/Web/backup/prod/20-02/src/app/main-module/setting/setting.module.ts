import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { CommonFieldModule } from "src/app/common-field/common-field.module";
import { SharedModule } from "src/app/shared/shared.module";
import { SettingRoutingModule } from "./setting-routing.module";
import { UploadDataComponent } from './upload-data/upload-data.component';


@NgModule({
  declarations: [

  
    UploadDataComponent
  ],
  imports: [
    CommonModule,
    SettingRoutingModule,

    SharedModule, CommonFieldModule, AngularMultiSelectModule,

  ],
  exports: [


  ]
})
export class SettingModule { }
