import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { CommonFieldModule } from "../common-field/common-field.module";
import { SharedModule } from "../shared/shared.module";
import { HeaderComponent } from "./header/header.component";
import { MenuItemComponent } from "./header/menu-item/menu-item.component";
import { MainModuleRoutingModule } from "./main-module-routing.module";
import { NotificationComponent } from './header/notification/notification.component';



@NgModule({
  declarations: [
    HeaderComponent, MenuItemComponent, NotificationComponent,
  ],
  imports: [
    CommonModule,
    SharedModule,
    MainModuleRoutingModule,
    CommonFieldModule,
    AngularMultiSelectModule
  ],
  exports: []
})
export class MainModuleModule { }
