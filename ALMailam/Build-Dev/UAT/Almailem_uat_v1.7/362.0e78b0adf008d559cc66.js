(self.webpackChunkclassic_wms_angular_ui=self.webpackChunkclassic_wms_angular_ui||[]).push([[362],{1362:(e,t,o)=>{"use strict";o.r(t),o.d(t,{LoginModule:()=>P});var i=o(8583),r=o(4655),s=o(3679),n=o(2340),a=o(7716),l=o(2238),u=o(8309),d=o(9699),c=o(1841),m=o(9866),h=o(1786),g=o(3738),p=o(8295),f=o(9983),w=o(6627),b=o(1095),Z=o(103);function x(e,t){if(1&e){const e=a.EpF();a.TgZ(0,"mat-card",8),a.TgZ(1,"form",9),a.NdJ("ngSubmit",function(){return a.CHM(e),a.oxw().login()}),a.TgZ(2,"div",10),a._UZ(3,"img",11),a.qZA(),a.TgZ(4,"h4",12),a._uU(5,"Sign In"),a.qZA(),a.TgZ(6,"div",13),a.TgZ(7,"h5",14),a._uU(8,"User ID"),a.qZA(),a.TgZ(9,"mat-form-field",15),a._UZ(10,"input",16),a.TgZ(11,"mat-icon",17),a._uU(12,"person"),a.qZA(),a.qZA(),a.qZA(),a.TgZ(13,"div",18),a.TgZ(14,"h5",14),a._uU(15,"Password"),a.qZA(),a.TgZ(16,"mat-form-field",15),a._UZ(17,"input",19),a.TgZ(18,"button",20),a.NdJ("click",function(){a.CHM(e);const t=a.oxw();return t.hide=!t.hide}),a.TgZ(19,"mat-icon"),a._uU(20),a.qZA(),a.qZA(),a.qZA(),a.qZA(),a.TgZ(21,"button",21),a._uU(22,"Sign In"),a.qZA(),a.qZA(),a.qZA()}if(2&e){const e=a.oxw();a.xp6(1),a.Q6J("formGroup",e.lgForm),a.xp6(16),a.Q6J("type",e.hide?"password":"text"),a.xp6(1),a.uIk("aria-label","Hide password")("aria-pressed",e.hide),a.xp6(2),a.Oqu(e.hide?"visibility_off":"visibility")}}function v(e,t){if(1&e){const e=a.EpF();a.TgZ(0,"mat-card",8),a.TgZ(1,"form",22),a.TgZ(2,"div",10),a._UZ(3,"img",11),a.qZA(),a.TgZ(4,"h4",12),a._uU(5,"Sign In"),a.qZA(),a.TgZ(6,"div",13),a.TgZ(7,"h5",14),a._uU(8,"User ID"),a.qZA(),a.TgZ(9,"mat-form-field",15),a._UZ(10,"input",23),a.TgZ(11,"button",20),a.NdJ("click",function(){a.CHM(e);const t=a.oxw();return t.hide=!t.hide}),a.TgZ(12,"mat-icon"),a._uU(13),a.qZA(),a.qZA(),a.qZA(),a.qZA(),a.TgZ(14,"div",18),a.TgZ(15,"h5",14),a._uU(16,"Password"),a.qZA(),a.TgZ(17,"mat-form-field",15),a._UZ(18,"input",24),a.TgZ(19,"button",20),a.NdJ("click",function(){a.CHM(e);const t=a.oxw();return t.hide=!t.hide}),a.TgZ(20,"mat-icon"),a._uU(21),a.qZA(),a.qZA(),a.qZA(),a.qZA(),a.TgZ(22,"button",25),a.NdJ("click",function(){return a.CHM(e),a.oxw().confirmPassword()}),a._uU(23,"Confirm"),a.qZA(),a.qZA(),a.qZA()}if(2&e){const e=a.oxw();a.xp6(1),a.Q6J("formGroup",e.lgForm),a.xp6(9),a.Q6J("type",e.hide?"password":"text"),a.xp6(1),a.uIk("aria-label","Hide password")("aria-pressed",e.hide),a.xp6(2),a.Oqu(e.hide?"visibility_off":"visibility"),a.xp6(5),a.Q6J("type",e.hide?"password":"text"),a.xp6(1),a.uIk("aria-label","Hide password")("aria-pressed",e.hide),a.xp6(2),a.Oqu(e.hide?"visibility_off":"visibility")}}const T=function(){return{width:"100%","margin-top":"0.3rem","margin-bottom":"0.3rem"}},y=function(){return{width:"20%"}};function I(e,t){if(1&e){const e=a.EpF();a.TgZ(0,"mat-card",8),a.TgZ(1,"form",22),a.TgZ(2,"div",10),a._UZ(3,"img",11),a.qZA(),a.TgZ(4,"h4",12),a._uU(5,"Sign In"),a.qZA(),a.TgZ(6,"div",26),a.TgZ(7,"h5",27),a._uU(8,"Select Branch"),a.qZA(),a.TgZ(9,"p-dropdown",28,29),a.NdJ("onChange",function(){a.CHM(e);const t=a.MAs(10);return a.oxw().onPlantChange(t.selectedOption)}),a.qZA(),a.qZA(),a.TgZ(11,"button",25),a.NdJ("click",function(){return a.CHM(e),a.oxw().warehouseConfirm()}),a._uU(12,"Confirm"),a.qZA(),a.qZA(),a.qZA()}if(2&e){const e=a.oxw();a.xp6(1),a.Q6J("formGroup",e.lgForm),a.xp6(8),a.Akn(a.DdM(6,T)),a.Q6J("filter",!0)("options",e.plantList)("panelStyle",a.DdM(7,y))}}const _=[{path:"",component:(()=>{class e{constructor(e,t,o,i,r,s,a,l){this.fb=e,this.router=t,this.dialog=o,this.auth=i,this.toastr=r,this.http=s,this.spin=a,this.usermanagement=l,this.hide=!0,this.title=n.N.title,this.plantList=[],this.authResult={},this.currentEnv=n.N.name}ngOnInit(){this.prod="prod"==this.currentEnv,sessionStorage.clear(),this.inForm()}inForm(){this.lgForm=this.fb.group({userName:["",s.kI.required],password:["",s.kI.required],newPassword:[""],confirmPassword:[""],selectedPlant:[""]}),this.loginTrue=!0,this.resetTrue=!1,this.warehouseSelection=!1}login(){if(this.spin.show(),sessionStorage.clear(),sessionStorage.clear(),this.lgForm.invalid)return this.error="Please fill required fields to continue",void this.toastr.error("Invalid User name or Password!","Login Failed",{timeOut:2e3,progressBar:!1});this.http.get(`/wms-idmaster-service/login?name=${this.lgForm.controls.userName.value}&password=${this.lgForm.controls.password.value}`).subscribe(e=>2==e.statusId?(this.toastr.error("User "+e.userName+" is currently inactive","Notification"),this.spin.hide(),void setTimeout(()=>{window.location.reload()},1500)):5==e.userTypeId?(this.toastr.error("User not authorized to login","Notification"),this.spin.hide(),void setTimeout(()=>{window.location.reload()},1500)):(this.userResult=e,sessionStorage.setItem("user",JSON.stringify(e)),void this.usermanagement.findProfile({userId:[this.lgForm.controls.userName.value]}).subscribe(e=>{1==e.length?1==e[0].resetPassword?(this.resetTrue=!0,this.loginTrue=!1,this.warehouseSelection=!1,this.userResult=null,this.userResult=e[0],this.spin.hide()):(this.auth.login(this.lgForm.value),this.spin.hide()):e.length>1?(this.warehouseSelection=!0,this.resetTrue=!1,this.loginTrue=!1,e.forEach(e=>{this.plantList.push({value:e.plantId,label:e.plantIdAndDescription,result:e})}),this.spin.hide()):(this.auth.login(this.lgForm.value),this.spin.hide())})),e=>{this.spin.hide(),this.toastr.error(e.error.error,"Notification"),setTimeout(()=>{window.location.reload()},1e3)})}confirmPassword(){return this.spin.show(),this.lgForm.controls.newPassword.value!=this.lgForm.controls.confirmPassword.value?(this.toastr.error("Password didn't match","Login Failed",{timeOut:2e3,progressBar:!1}),void this.spin.hide()):this.lgForm.controls.newPassword.value&&this.lgForm.controls.newPassword.value?void this.http.patch(`/wms-idmaster-service/usermanagement/${this.userResult.userId}?warehouseId=${this.userResult.warehouseId}&companyCode=${this.userResult.companyCode}&plantId=${this.userResult.plantId}&languageId=${this.userResult.languageId}&userRoleId=${this.userResult.userRoleId}`,{resetPassword:!1,password:this.lgForm.controls.confirmPassword.value,companyCode:this.userResult.companyId,plantId:this.userResult.plantId,languageId:this.userResult.languageId,userRoleId:this.userResult.userRoleId}).subscribe(e=>{this.toastr.success("Password changed Successfully","Notification"),this.resetTrue=!1,this.loginTrue=!0,this.spin.hide(),setTimeout(()=>{window.location.reload()},1e3)}):(this.toastr.error("Please fill the fields to continue","Login Failed",{timeOut:2e3,progressBar:!1}),void this.spin.hide())}onPlantChange(e){this.authResult=e.result}warehouseConfirm(){1==this.authResult.resetPassword?(this.resetTrue=!0,this.loginTrue=!1,this.warehouseSelection=!1,this.userResult=null,this.userResult=this.authResult,this.spin.hide()):(console.log(this.authResult),this.auth.login1(this.lgForm.value,this.authResult))}}return e.\u0275fac=function(t){return new(t||e)(a.Y36(s.qu),a.Y36(r.F0),a.Y36(l.uw),a.Y36(u.e),a.Y36(d._W),a.Y36(c.eN),a.Y36(m.t2),a.Y36(h.I))},e.\u0275cmp=a.Xpm({type:e,selectors:[["app-login"]],decls:10,vars:3,consts:[[1,"container","primaryblue","vw-100","vh-100","mw-100","mx-0","px-0"],[1,"row","no-gutters","fullBackground"],[1,"truevalue"],["src","./assets/img/almailem.png","alt","","srcset","",1,"",2,"max-width","80%","position","relative","right","-10%"],[1,"col-lg-8","col-md-7","img-fluid","d-flex","flex-column","justify-content-center","align-items-center"],[1,"col-lg-4","col-md-5","vh-100","d-flex","align-items-center","justify-content-center","border-secondary"],["style","background-color: transparent;border-radius: 2%;","class","d-flex align-self-center px-4 shadow-none py-4",4,"ngIf"],["src","./assets/img/icons/awslogo.png","alt","","srcset","",2,"position","absolute","bottom","1rem","right","1rem","width","4.5rem","height","4.5rem"],[1,"d-flex","align-self-center","px-4","shadow-none","py-4",2,"background-color","transparent","border-radius","2%"],[3,"formGroup","ngSubmit"],[1,"d-flex","justify-content-center","align-items-center"],["src","./assets/img/icons/logozigzag3.png","alt","","srcset","",1,"mb-0",2,"max-width","90%"],[1,"text-center","mt-2","invisible",2,"color","#ffff"],[1,"form-group","mb-1","mt-4","whs-text-grey"],[1,"mb-1","d-none",2,"font-size","1rem"],["appearance","outline",2,"width","17rem"],["matInput","","type","text","formControlName","userName","id","exampleInputEmail1","aria-describedby","emailHelp","placeholder","User ID","autocomplete","off",1,"my-auto"],["matSuffix","",1,"my-auto"],[1,"form-group","mb-1","whs-text-grey"],["matInput","","type","password","formControlName","password","id","exampleInputPassword1","placeholder","Password","aria-describedby","forgot","autocomplete","off",3,"type"],["mat-icon-button","","matSuffix","",3,"click"],["mat-button","","type","Submit",1,"btn-md","btn-block","py-1",2,"background-color","#ec830a","color","#ffff"],[3,"formGroup"],["matInput","","type","password","formControlName","newPassword","id","exampleInputPassword1","placeholder","New Password","aria-describedby","forgot","autocomplete","off",3,"type"],["matInput","","type","password","formControlName","confirmPassword","id","exampleInputPassword1","placeholder","Confirm Password","aria-describedby","forgot","autocomplete","off",3,"type"],["mat-button","",1,"btn-md","btn-block","py-1",2,"background-color","#ec830a","color","#ffff",3,"click"],[1,"form-group","mb-4","whs-text-grey"],[1,"mb-1",2,"font-size","1rem"],["placeholder","Select","formControlName","selectedPlant","appendTo","body",1,"w-100",3,"filter","options","panelStyle","onChange"],["plant",""]],template:function(e,t){1&e&&(a.TgZ(0,"div",0),a.TgZ(1,"div",1),a.TgZ(2,"div",2),a._UZ(3,"img",3),a.qZA(),a._UZ(4,"div",4),a.TgZ(5,"div",5),a.YNc(6,x,23,5,"mat-card",6),a.YNc(7,v,24,9,"mat-card",6),a.YNc(8,I,13,8,"mat-card",6),a._UZ(9,"img",7),a.qZA(),a.qZA(),a.qZA()),2&e&&(a.xp6(6),a.Q6J("ngIf",t.loginTrue),a.xp6(1),a.Q6J("ngIf",t.resetTrue),a.xp6(1),a.Q6J("ngIf",t.warehouseSelection))},directives:[i.O5,g.a8,s._Y,s.JL,s.sg,p.KE,f.Nt,s.Fj,s.JJ,s.u,w.Hw,p.R9,b.lW,Z.Lt],styles:[".footer[_ngcontent-%COMP%]{position:fixed;left:0;bottom:0;width:100%;background-color:#e0e8f1d1;text-align:center}hr.dashed[_ngcontent-%COMP%]{border-top:1px dashed #324e7bab}.form-control[_ngcontent-%COMP%], .form-select[_ngcontent-%COMP%]{width:100%;height:100%;border-color:#dee2e6;font-size:.8rem}  .mat-form-field-appearance-outline .mat-form-field-infix{padding:.7em 0}.input_field[_ngcontent-%COMP%]{width:17rem;border-color:#dee2e6}  .mat-form-field-appearance-outline .mat-form-field-outline{background-color:#fff!important;border-radius:4px;border-color:red!important}.backgroundbg[_ngcontent-%COMP%]{background-image:url(original.4a0af2baf110999aacb4.jpg)}.backgroundbg[_ngcontent-%COMP%], .backgroundbg1[_ngcontent-%COMP%]{background-repeat:no-repeat;position:relative}.backgroundbg1[_ngcontent-%COMP%]{background-image:url(smallbg_original.41b5231c4f50f8e2188f.jpg)}.fullBackground[_ngcontent-%COMP%]{background-image:url(fullBackground1.bff6399b757dd7b393c9.jpg);background-repeat:no-repeat;position:relative;width:100%;height:100%}  .mat-form-field-appearance-outline .mat-form-field-infix{padding:.7em 0 1.1em!important}.truevalue[_ngcontent-%COMP%]{position:absolute;right:-15px!important;z-index:100!important;top:5px!important}"]}),e.\u0275prov=a.Yz7({token:e,factory:e.\u0275fac,providedIn:"root"}),e})(),pathMatch:"full"}];let q=(()=>{class e{}return e.\u0275fac=function(t){return new(t||e)},e.\u0275mod=a.oAB({type:e}),e.\u0275inj=a.cJS({imports:[[r.Bz.forChild(_)],r.Bz]}),e})();var A=o(6893);let P=(()=>{class e{}return e.\u0275fac=function(t){return new(t||e)},e.\u0275mod=a.oAB({type:e}),e.\u0275inj=a.cJS({imports:[[i.ez,q,A.m]]}),e})()}}]);