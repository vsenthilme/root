import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { CommonFieldModule } from "../common-field/common-field.module";
import { SharedModule } from "../shared/shared.module";
import { HeaderComponent } from "./header/header.component";
import { MainModuleRoutingModule } from "./main-module-routing.module";



@NgModule({
  declarations: [
    HeaderComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    MainModuleRoutingModule,
    CommonFieldModule
  ],
  exports: []
})
export class MainModuleModule { }
