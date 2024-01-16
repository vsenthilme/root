import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { CommonFieldModule } from "src/app/common-field/common-field.module";
import { SharedModule } from "src/app/shared/shared.module";
import { ConfigTabbarComponent } from "./config-tabbar/config-tabbar.component";
import { ConfigurationRoutingModule } from "./configuration-routing.module";
import { LanguageEditComponent } from "./language-id/language-edit/language-edit.component";
import { LanguageIdComponent } from "./language-id/language-id.component";
import { MessageEditComponent } from "./message-id/message-edit/message-edit.component";
import { MessageIdComponent } from "./message-id/message-id.component";
import { NumberRangeComponent } from "./number-range/number-range.component";
import { NumberrangeDisplayComponent } from "./number-range/numberrange-display/numberrange-display.component";
import { ScreenEditComponent } from "./screen-id/screen-edit/screen-edit.component";
import { ScreenIdComponent } from "./screen-id/screen-id.component";
import { StatusEditComponent } from "./status-id/status-edit/status-edit.component";
import { StatusIdComponent } from "./status-id/status-id.component";
import { TransactionEditComponent } from "./transaction-id/transaction-edit/transaction-edit.component";
import { TransactionIdComponent } from "./transaction-id/transaction-id.component";



@NgModule({
  declarations: [
    ConfigTabbarComponent,
    LanguageIdComponent,
    LanguageEditComponent,
    TransactionIdComponent,
    TransactionEditComponent,
    ScreenIdComponent,
    ScreenEditComponent,
    StatusIdComponent,
    StatusEditComponent,
    MessageIdComponent,
    MessageEditComponent,
    NumberRangeComponent,
    NumberrangeDisplayComponent,
  ],
  imports: [
    CommonModule,
    ConfigurationRoutingModule,
    SharedModule, CommonFieldModule,AngularMultiSelectModule
  ],
  exports: [
  ]
})


export class ConfigurationModule { }