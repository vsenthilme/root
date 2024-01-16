import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { GlMappingComponent } from "../business/gl-mapping/gl-mapping.component";
import { ClassComponent } from "./class/class.component";
import { ClientCatgComponent } from "./client-catg/client-catg.component";
import { ClientIdComponent } from "./client-id/client-id.component";
import { CompanyIdComponent } from "./company-id/company-id.component";
import { IntakeIdComponent } from "./intake-id/intake-id.component";
import { NotificationComponent } from "./notification/notification.component";
import { TabbarComponent } from "./tabbar/tabbar.component";
import { UserProfileComponent } from "./user-profile/user-profile.component";
import { UserRoleEditComponent } from "./user-role/user-role-edit/user-role-edit.component";
import { UserRoleComponent } from "./user-role/user-role.component";
import { UsertypeIdComponent } from "./usertype-id/usertype-id.component";




const routes: Routes = [{
  path: '',
  component: TabbarComponent,
  children: [

    { path: 'company', component: CompanyIdComponent, pathMatch: 'full', data: { title: 'Company', module: 'Admin' } },
    { path: 'userrole', component: UserRoleComponent, pathMatch: 'full', data: { title: 'User Role', module: 'Admin' } },
    { path: 'class', component: ClassComponent, pathMatch: 'full', data: { title: 'Class', module: 'Admin' } },
    { path: 'intake', component: IntakeIdComponent, pathMatch: 'full', data: { title: 'Intake', module: 'Admin' } },

    { path: 'notification', component: NotificationComponent, pathMatch: 'full', data: { title: 'Notification', module: 'Admin' } },

    { path: 'clientcategory', component: ClientCatgComponent, pathMatch: 'full', data: { title: 'Client Category', module: 'Admin' } },
    { path: 'clienttype', component: ClientIdComponent, pathMatch: 'full', data: { title: 'Client Type', module: 'Admin' } },

    { path: 'userprofile', component: UserProfileComponent, pathMatch: 'full', data: { title: 'User Profile', module: 'Admin' } },
    { path: 'usertype', component: UsertypeIdComponent, pathMatch: 'full', data: { title: 'User Type', module: 'Admin' } },
    { path: 'userrole', component: UserRoleComponent, pathMatch: 'full', data: { title: 'User Type', module: 'Admin' } },
    { path: 'glmapping', component: GlMappingComponent, pathMatch: 'full', data: { title: 'GL Mapping', module: 'Admin' } },
  ]
},
{ path: 'userroleedit/:code', component: UserRoleEditComponent, pathMatch: 'full', data: { title: 'User Type', module: 'Admin' } },

];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
