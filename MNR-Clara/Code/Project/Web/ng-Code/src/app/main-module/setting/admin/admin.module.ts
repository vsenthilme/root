import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { CommonFieldModule } from "src/app/common-field/common-field.module";
import { SharedModule } from "src/app/shared/shared.module";
import { AdminRoutingModule } from "./admin-routing.module";
import { AdminComponent } from "./admin.component";
import { ClassDisplayComponent } from "./class/class-display/class-display.component";
import { ClassComponent } from "./class/class.component";
import { ClientCatgComponent } from "./client-catg/client-catg.component";
import { ClientcatEditComponent } from "./client-catg/clientcat-edit/clientcat-edit.component";
import { ClientEditComponent } from "./client-id/client-edit/client-edit.component";
import { ClientIdComponent } from "./client-id/client-id.component";
import { ClientUserComponent } from "./client-user/client-user.component";
import { ClientuserDisplayComponent } from "./client-user/clientuser-display/clientuser-display.component";
import { CompanyEditComponent } from "./company-id/company-edit/company-edit.component";
import { CompanyIdComponent } from "./company-id/company-id.component";
import { IntakeIdComponent } from "./intake-id/intake-id.component";
import { IntakeidEditComponent } from "./intake-id/intakeid-edit/intakeid-edit.component";
import { NotificationComponent } from "./notification/notification.component";
import { NotifyEditComponent } from "./notification/notify-edit/notify-edit.component";
import { TabbarComponent } from "./tabbar/tabbar.component";
import { UserProfileComponent } from "./user-profile/user-profile.component";
import { UserRoleComponent } from "./user-role/user-role.component";
import { UsertypeEditComponent } from "./usertype-id/usertype-edit/usertype-edit.component";
import { UsertypeIdComponent } from "./usertype-id/usertype-id.component";
import { UserprofileCopyComponent } from './user-profile/userprofile-copy/userprofile-copy.component';
import { UserRoleEditComponent } from './user-role/user-role-edit/user-role-edit.component';
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";

@NgModule({
  declarations: [
    ClientCatgComponent,
    ClientcatEditComponent,
    ClientIdComponent,
    ClientEditComponent,
    CompanyIdComponent,
    CompanyEditComponent,
    IntakeIdComponent,
    IntakeidEditComponent,

    NotificationComponent,
    NotifyEditComponent,

    TabbarComponent,

    UsertypeIdComponent,
    UsertypeEditComponent,
    AdminComponent,
    UserRoleComponent,
    UserProfileComponent,
    ClassComponent,
    ClassDisplayComponent,
    ClientUserComponent,
    ClientuserDisplayComponent,
    UserprofileCopyComponent,
    UserRoleEditComponent,

  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    SharedModule, CommonFieldModule,AngularMultiSelectModule,
  ]
  , exports: [
  ]
})
export class AdminModule { }
