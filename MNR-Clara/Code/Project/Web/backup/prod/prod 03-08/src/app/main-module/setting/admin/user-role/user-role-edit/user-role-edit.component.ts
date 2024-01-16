import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { ClientGeneralService } from 'src/app/main-module/client/client-general/client-general.service';

import { Location } from "@angular/common";
import { SelectionModel } from '@angular/cdk/collections';
import { MatTableDataSource } from '@angular/material/table';
import { UserRoleService } from '../user-role.service';
@Component({
  selector: 'app-user-role-edit',
  templateUrl: './user-role-edit.component.html',
  styleUrls: ['./user-role-edit.component.scss']
})
export class UserRoleEditComponent implements OnInit {
  menuList: any[] = [];
  menuListraw: any[] =
    [
      {
        mainMenu: "Shortcuts", Menu: [  
        { screenId: 7, subScreenId: 1173, referenceField2: 'Dashboard', referenceField1: "Dashboard", createUpdate: true, delete: true, view: true, },
        { screenId: 7, subScreenId: 1117, referenceField2: 'Time Ticket', referenceField1: "Time Ticket - New", createUpdate: true, delete: true, view: true, },
        { screenId: 7, subScreenId: 1115, referenceField2: 'Task', referenceField1: "Task - New", createUpdate: true, delete: true, view: true, },
        { screenId: 7, subScreenId: 1065, referenceField2: 'Inquiry', referenceField1: "Inquiry - New", createUpdate: true, delete: true, view: true, },
        { screenId: 7, subScreenId: 1137, referenceField2: 'Prebill Approval', referenceField1: "Prebill Approval", createUpdate: true, delete: true, view: true, },
        { screenId: 7, subScreenId: 1080, referenceField2: 'Conflict Check', referenceField1: "Conflict Check", createUpdate: true, delete: true, view: true, },
        ]
      },

      // {
      //   mainMenu: "Home", Menu: [  
      //   { screenId: 8, subScreenId: 1175, referenceField2: 'Time Ticket Summary', referenceField1: "Time Ticket Summary", createUpdate: true, delete: true, view: true, },
      //   ]
      // },

      {
      mainMenu: "Settings",
      Menu: [
        { screenId: 1, subScreenId: 1003, referenceField2: 'Configuration', referenceField1: "	Language ID	", createUpdate: true, delete: true, view: true, },
        { screenId: 1, subScreenId: 1008, referenceField2: 'Configuration', referenceField1: "	Status ID	", createUpdate: true, delete: true, view: true, },
        { screenId: 1, subScreenId: 1006, referenceField2: 'Configuration', referenceField1: "	Transaction ID	", createUpdate: true, delete: true, view: true, },

        { screenId: 1, subScreenId: 1045, referenceField2: 'Configuration', referenceField1: "	Number Range - List	", createUpdate: true, delete: true, view: true, },



        { screenId: 1, subScreenId: 1021, referenceField2: 'Business', referenceField1: "	Case Category - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 1, subScreenId: 1023, referenceField2: 'Business', referenceField1: "	Case Sub Category - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 1, subScreenId: 1047, referenceField2: 'Business', referenceField1: "	Agreement Template - List	", createUpdate: true, delete: true, view: true, },

        { screenId: 1, subScreenId: 1053, referenceField2: 'Business', referenceField1: "	Inquiry Mode - List	", createUpdate: true, delete: true, view: true, },

        { screenId: 1, subScreenId: 1055, referenceField2: 'Business', referenceField1: "	Referral - List	", createUpdate: true, delete: true, view: true, },

        { screenId: 1, subScreenId: 1057, referenceField2: 'Business', referenceField1: "	Note Type - List	", createUpdate: true, delete: true, view: true, },

        { screenId: 1, subScreenId: 1017, referenceField2: 'Business', referenceField1: "	Time Keeper - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 1, subScreenId: 1025, referenceField2: 'Business', referenceField1: "	Activity Code - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 1, subScreenId: 1033, referenceField2: 'Business', referenceField1: "	Task Type - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 1, subScreenId: 1027, referenceField2: 'Business', referenceField1: "	Task based Code - List	", createUpdate: true, delete: true, view: true, },

        { screenId: 1, subScreenId: 1031, referenceField2: 'Business', referenceField1: "	Expense Codes - List	", createUpdate: true, delete: true, view: true, },

        { screenId: 1, subScreenId: 1043, referenceField2: 'Business', referenceField1: "	Deadline Calculator - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 1, subScreenId: 1049, referenceField2: 'Business', referenceField1: "	Documents Template - List	", createUpdate: true, delete: true, view: true, },

        { screenId: 1, subScreenId: 1128, referenceField2: 'Business', referenceField1: "	 Checklist Template - List	", createUpdate: true, delete: true, view: true, },

        { screenId: 1, subScreenId: 1029, referenceField2: 'Business', referenceField1: "	Chart of Account - List	", createUpdate: true, delete: true, view: true, },


        { screenId: 1, subScreenId: 1035, referenceField2: 'Business', referenceField1: "	Business Cost - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 1, subScreenId: 1037, referenceField2: 'Business', referenceField1: "	Billing Mode - List	", createUpdate: true, delete: true, view: true, },



        { screenId: 1, subScreenId: 1039, referenceField2: 'Business', referenceField1: "	Billing Frequency - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 1, subScreenId: 1041, referenceField2: 'Business', referenceField1: "	Billing Format - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 1, subScreenId: 1129, referenceField2: 'Business', referenceField1: "	Expiration Document Type - List	", createUpdate: true, delete: true, view: true, },

        { screenId: 1, subScreenId: 1138, referenceField2: 'Business', referenceField1: "	GL Mapping - List	", createUpdate: true, delete: true, view: true, },


        { screenId: 1, subScreenId: 1127, referenceField2: 'Upload Data', referenceField1: "Upload Data", createUpdate: true, delete: true, view: true, },






        { screenId: 1, subScreenId: 1082, referenceField2: 'Admin', referenceField1: "	Company - List	", createUpdate: true, delete: true, view: true, },

        { screenId: 1, subScreenId: 1019, referenceField2: 'Business', referenceField1: "	Class - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 1, subScreenId: 1011, referenceField2: 'Admin', referenceField1: "	Intake Form ID	", createUpdate: true, delete: true, view: true, },


        { screenId: 1, subScreenId: 1059, referenceField2: 'Admin', referenceField1: "	Notification ID	", createUpdate: true, delete: true, view: true, },
        { screenId: 1, subScreenId: 1010, referenceField2: 'Admin', referenceField1: "	Client Category ID	", createUpdate: true, delete: true, view: true, },
        { screenId: 1, subScreenId: 1007, referenceField2: 'Admin', referenceField1: "	Client Type ID	", createUpdate: true, delete: true, view: true, },


        { screenId: 1, subScreenId: 1005, referenceField2: 'Admin', referenceField1: "	User Type ID	", createUpdate: true, delete: true, view: true, },

        { screenId: 1, subScreenId: 1015, referenceField2: 'Admin', referenceField1: "	User Profile - List	", createUpdate: true, delete: true, view: true, },


        { screenId: 1, subScreenId: 1013, referenceField2: 'Admin', referenceField1: "	User Role - List	", createUpdate: true, delete: true, view: true, },



        { screenId: 1, subScreenId: 1170, referenceField2: 'QB sync', referenceField1: "QB sync", createUpdate: true, delete: true, view: true, },
        { screenId: 1, subScreenId: 1174, referenceField2: 'Docketwise sync', referenceField1: "Docketwise sync", createUpdate: true, delete: true, view: true, },


      ]
    },
    {
      mainMenu: "CRM", Menu: [
        { screenId: 2, subScreenId: 1060, referenceField1: "	Inquiry New - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 2, subScreenId: 1062, referenceField1: "	Inquiry Assign - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 2, subScreenId: 1064, referenceField1: "	Inquiry Validation - Snapshot / List	", createUpdate: true, delete: true, view: true, },

        { screenId: 2, subScreenId: 1073, referenceField1: "	Intake Form - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 2, subScreenId: 1075, referenceField1: "	Prospective Client - List	", createUpdate: true, delete: true, view: true, },

        { screenId: 2, subScreenId: 1079, referenceField1: "	Agreement - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 2, subScreenId: 1080, referenceField1: "	Conflict Check - Parameters	", createUpdate: true, delete: true, view: true, },



      ]
    },
    {
      mainMenu: "Client", Menu: [
        { screenId: 3, subScreenId: 1085, referenceField1: "	Client - New	", createUpdate: true, delete: true, view: true, },
        { screenId: 3, subScreenId: 1084, referenceField1: "	Client - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 3, subScreenId: 1088, referenceField1: "	Client - Matters List	", createUpdate: true, delete: true, view: true, },

        { screenId: 3, subScreenId: 1086, referenceField1: "	Client - Notes List	", createUpdate: true, delete: true, view: true, },

        { screenId: 3, subScreenId: 1091, referenceField1: "	Client - Documents List	", createUpdate: true, delete: true, view: true, },

        { screenId: 3, subScreenId: 1156, referenceField1: "	Client Portal", createUpdate: true, delete: true, view: true, }
      ]
    },
    {
      mainMenu: "Matter", Menu: [
        { screenId: 4, subScreenId: 1094, referenceField1: "	Case Information - L&E	", createUpdate: true, delete: true, view: true, },
        { screenId: 4, subScreenId: 1096, referenceField1: "	Case Information - Immigration	", createUpdate: true, delete: true, view: true, },

        { screenId: 4, subScreenId: 1098, referenceField1: "	Matter - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 4, subScreenId: 1099, referenceField1: "	Matter - General	", createUpdate: true, delete: true, view: true, },


        { screenId: 4, subScreenId: 1107, referenceField1: "	Matter - Billing	", createUpdate: true, delete: true, view: true, },

        { screenId: 4, subScreenId: 1100, referenceField1: "	Matter - Notes List	", createUpdate: true, delete: true, view: true, },
        { screenId: 4, subScreenId: 1104, referenceField1: "	Matter - Documents - List	", createUpdate: true, delete: true, view: true, },

        { screenId: 4, subScreenId: 1108, referenceField1: "	Matter - Rates - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 4, subScreenId: 1110, referenceField1: "	Matter - Fee Sharing - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 4, subScreenId: 1112, referenceField1: "	Matter - Expenses - List	", createUpdate: true, delete: true, view: true, },

        { screenId: 4, subScreenId: 1126, referenceField1: "	Matter - Intake Form List	", createUpdate: true, delete: true, view: true, },

        { screenId: 4, subScreenId: 1114, referenceField1: "	Matter - Task - List	", createUpdate: true, delete: true, view: true, },


        { screenId: 4, subScreenId: 1116, referenceField1: "	Matter - Time - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 4, subScreenId: 1132, referenceField1: "	Matter - Receipt - List	", createUpdate: true, delete: true, view: true, }, 
        { screenId: 4, subScreenId: 1130, referenceField1: "	Matter - Expiration Date - List	", createUpdate: true, delete: true, view: true, },

        { screenId: 4, subScreenId: 1118, referenceField1: "	Case Assignment - List	", createUpdate: true, delete: true, view: true, },
       
//documents
        { screenId: 4, subScreenId: 1157, referenceField1: "Client Portal Documents	", createUpdate: true, delete: true, view: true, },
        { screenId: 4, subScreenId: 1168, referenceField1: "Client Portal Checklist	", createUpdate: true, delete: true, view: true, },


      ]
    },
    {
      mainMenu: "Billing", Menu: [
        { screenId: 5, subScreenId: 1134, referenceField1: "	Quotation - List	", createUpdate: true, delete: true, view: true, },
        
        { screenId: 5, subScreenId: 1188, referenceField1: "Payment", createUpdate: true, delete: true, view: true, },
        { screenId: 5, subScreenId: 1136, referenceField1: "	Payment Plan - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 5, subScreenId: 1137, referenceField1: "	Prebill Generate   ", createUpdate: true, delete: true, view: true, },
        { screenId: 5, subScreenId: 1139, referenceField1: "	Prebill Approve   ", createUpdate: true, delete: true, view: true, },
        { screenId: 5, subScreenId: 1140, referenceField1: "	Invoice - List	", createUpdate: true, delete: true, view: true, },
        { screenId: 5, subScreenId: 1088, referenceField1: "	Client - Matters List	", createUpdate: true, delete: true, view: true, },

        { screenId: 5, subScreenId: 1086, referenceField1: "	Client - Notes List	", createUpdate: true, delete: true, view: true, },

        { screenId: 5, subScreenId: 1091, referenceField1: "	Client - Documents List	", createUpdate: true, delete: true, view: true, },

        { screenId: 5, subScreenId: 1167, referenceField1: "Matter Billing Activity", createUpdate: true, delete: true, view: true, },
        { screenId: 5, subScreenId: 1186, referenceField1: "Transfer Matter Billing Activity", createUpdate: true, delete: true, view: true, },                  
        { screenId: 5, subScreenId: 1169, referenceField1: "Matter Expense", createUpdate: true, delete: true, view: true, },
        { screenId: 5, subScreenId: 1171, referenceField1: "Time Ticket Summary", createUpdate: true, delete: true, view: true, },
        //{ screenId: 5, subScreenId: 1175, referenceField2: 'Time Ticket Summary - Restricted Access', referenceField1: "Time Ticket Summary - Restricted Access", createUpdate: true, delete: true, view: true, },
      ]
    },

    {
      mainMenu: "Reports", Menu: [
        { screenId: 6, subScreenId: 1158, referenceField2: 'CRM', referenceField1: "Prospective Client", createUpdate: false, delete: false, view: true, },
        { screenId: 6, subScreenId: 1159, referenceField2: 'CRM', referenceField1: "Lead Conversion", createUpdate: false, delete: false, view: true, },
        { screenId: 6, subScreenId: 1160, referenceField2: 'CRM', referenceField1: "Referral", createUpdate: false, delete: false, view: true, },

        { screenId: 6, subScreenId: 1161, referenceField2: 'Client', referenceField1: "New Client - L&E", createUpdate: false, delete: false, view: true, },
        { screenId: 6, subScreenId: 1162, referenceField2: 'Client', referenceField1: "New Client - Immigration", createUpdate: false, delete: false, view: true, },
        
        { screenId: 6, subScreenId: 1163, referenceField2: 'Matter', referenceField1: "Matter L&E", createUpdate: false, delete: false, view: true, },
        { screenId: 6, subScreenId: 1164, referenceField2: 'Matter', referenceField1: "Matter Immigration", createUpdate: false, delete: false, view: true, },

        { screenId: 6, subScreenId: 1165, referenceField2: 'Accounting', referenceField1: "AR Aging", createUpdate: false, delete: false, view: true, },

        { screenId: 6, subScreenId: 1166, referenceField2: 'Accounting', referenceField1: "WIP Aged Report for Prebills Validation", createUpdate: false, delete: false, view: true, },
        
        { screenId: 6, subScreenId: 1172, referenceField2: 'Accounting', referenceField1: "Expiration", createUpdate: false, delete: false, view: true, },
        
        { screenId: 6, subScreenId: 1176, referenceField2: 'Accounting', referenceField1: "AR Report", createUpdate: false, delete: false, view: true, },
        
        { screenId: 6, subScreenId: 1177, referenceField2: 'Accounting', referenceField1: "Billed Paid Fees", createUpdate: false, delete: false, view: true, },

        { screenId: 6, subScreenId: 1178, referenceField2: 'Accounting', referenceField1: "Billed Unbilled", createUpdate: false, delete: false, view: true, },
        
        { screenId: 6, subScreenId: 1179, referenceField2: 'Accounting', referenceField1: "Billed Hours Paid", createUpdate: false, delete: false, view: true, },
                
        { screenId: 6, subScreenId: 1180, referenceField2: 'Accounting', referenceField1: "Client Cash Receipt", createUpdate: false, delete: false, view: true, },
        
        { screenId: 6, subScreenId: 1181, referenceField2: 'Accounting', referenceField1: "Client Income Summary", createUpdate: false, delete: false, view: true, },
        
        { screenId: 6, subScreenId: 1182, referenceField2: 'Accounting', referenceField1: "Write Off", createUpdate: false, delete: false, view: true, },
        
        { screenId: 6, subScreenId: 1183, referenceField2: 'Accounting', referenceField1: "Client Matter", createUpdate: false, delete: false, view: true, },
        
        
        { screenId: 6, subScreenId: 1184, referenceField2: 'Accounting', referenceField1: "Matter Rates", createUpdate: false, delete: false, view: true, },
        
        { screenId: 6, subScreenId: 1185, referenceField2: 'Accounting', referenceField1: "Billing", createUpdate: false, delete: false, view: true, },
        { screenId: 6, subScreenId: 1192, referenceField2: 'Accounting', referenceField1: "L&E Billing Hours", createUpdate: false, delete: false, view: true, },

        { screenId: 6, subScreenId: 1187, referenceField2: 'Matter', referenceField1: "Task", createUpdate: false, delete: false, view: true, },
        { screenId: 6, subScreenId: 1189, referenceField2: 'Matter', referenceField1: "Attorney Productivity", createUpdate: false, delete: false, view: true, },
        { screenId: 6, subScreenId: 1190, referenceField2: 'Matter', referenceField1: "Matter P&L", createUpdate: false, delete: false, view: true, },
//newly added for QB Sync
  

        //dashboard
        
      ]
    },
    ];

  input: any;
  isbtntext = true;
  public icon = 'expand_more';
  btntext = "Save";
  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);
  form = this.fb.group({
    roleStatus: ["Active", [Validators.required]],
    userRoleId: [,],
    userRoleName: [, [Validators.required]],


  });


  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
  }
  roleid = true
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  toggleFloat() {

    this.isShowDiv = !this.isShowDiv;
    this.toggle = !this.toggle;

    if (this.icon === 'expand_more') {
      this.icon = 'chevron_left';
    } else {
      this.icon = 'expand_more'
    }
    this.showFloatingButtons = !this.showFloatingButtons;

  }



  disabled = false;
  step = 0;

  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
  }
 
  prevStep() {
    this.step--;
  }

  panelOpenState = false;

  constructor(private fb: FormBuilder,
    private auth: AuthService,
    private service: UserRoleService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService, private location: Location) { }

  ngOnInit(): void {
    this.reset();
    this.auth.isuserdata();
  
    // this.dropdownlist();
    let code = this.route.snapshot.params.code;
    if (code != 'new') {
      let js = this.cs.decrypt(code);
      this.fill(js);

      this.form.controls.userRoleId.disable();
     
    }
  }
  reset() {
    this.menuList = this.menuListraw;
  }

  languageIdList: any[] = [];
  classIdList: any[] = [];
  clientCategoryIdList: any[] = [];
  statusIdList: any[] = [];
  referenceList: any[] = [];
  clientIdList: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.languageId.url,
    this.cas.dropdownlist.setup.classId.url,

    this.cas.dropdownlist.setup.clientCategoryId.url,
    this.cas.dropdownlist.setup.statusId.url,
    this.cas.dropdownlist.setup.referralId.url,
    this.cas.dropdownlist.client.clientId.url,
    ]).subscribe((results) => {
      this.languageIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.languageId.key);
      this.classIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.classId.key).filter(x => x.key != 3);
      this.clientCategoryIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.clientCategoryId.key);
      this.statusIdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.statusId.key).filter(s => [18, 21, this.form.controls.statusId.value].includes(s.key));
      this.referenceList = this.cas.foreachlist(results[4], this.cas.dropdownlist.setup.referralId.key);
      this.clientIdList = this.cas.foreachlist(results[5], this.cas.dropdownlist.client.clientId.key, { clientCategoryId: '1' }, true);

    }, (err) => {
      this.toastr.error(err, "");
    });
    if (this.auth.classId != '3')
      this.form.controls.classId.patchValue(this.auth.classId);
    this.spin.hide();
  }
  pageflow = "New";
  fill(data: any) {
    this.pageflow = data.pageflow;

    if(data.pageflow == 'New'){
      this.roleid = false;
    }
    if (data.pageflow != 'New') {
      this.btntext = "Update";
      if (data.pageflow == 'Display') {
        this.form.disable();
        this.isbtntext = false;
      }

      this.spin.show();
      this.sub.add(this.service.Get(data.code).subscribe(res => {
        this.form.patchValue(res[0]);

        this.form.controls.roleStatus.patchValue(this.form.controls.roleStatus.value ? 'Active' : 'Inactive');
        // debugger
        this.menuList = [];
        this.menuList.push({
          mainMenu: "Shortcuts",
          Menu: res.filter((x: any) => x.screenId == 7) //done
        });
        this.menuList.push({
          mainMenu: "Home",
          Menu: res.filter((x: any) => x.screenId == 8)
        });
        this.menuList.push({
          mainMenu: "Settings",
          Menu: res.filter((x: any) => x.screenId == 1)
        });
        this.menuList.push({
          mainMenu: "CRM",
          Menu: res.filter((x: any) => x.screenId == 2)
        });
        this.menuList.push({
          mainMenu: "Client",
          Menu: res.filter((x: any) => x.screenId == 3)
        });
        this.menuList.push({
          mainMenu: "Matter",
          Menu: res.filter((x: any) => x.screenId == 4)
        });
        this.menuList.push({
          mainMenu: "Billing",
          Menu: res.filter((x: any) => x.screenId == 5)
        });
        this.menuList.push({
          mainMenu: "Reports",
          Menu: res.filter((x: any) => x.screenId == 6)
        });
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }
  }

  submit() {
    this.form.controls.roleStatus.patchValue(this.form.controls.roleStatus.value == 'Active' ? true : false);

    this.submitted = true;
    if (this.form.invalid) {
      this.toastr.error(
        "Please fill the required fields to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );

      this.cs.notifyOther(true);
      return;
    }

    this.form.updateValueAndValidity;
    let data: any[] = [];
    this.menuList.forEach((x: any) => {
      x.Menu.forEach((y: any) => {
        y.languageId = "en"
        y.roleStatus = this.form.controls.roleStatus.value
        // if (this.pageflow != "New")
        //   y.userRoleId = this.form.controls.userRoleId.value
        y.userRoleName = this.form.controls.userRoleName.value

        data.push(y);
      });
    })
    console.log(JSON.stringify(this.menuList));

    this.cs.notifyOther(false);
    this.spin.show();

    if (this.pageflow != "New") {
      this.sub.add(this.service.Update(data, this.form.controls.userRoleId.value).subscribe(res => {
        this.toastr.success(this.form.controls.userRoleId.value + " updated successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        this.location.back();
      }, err => {

        this.form.controls.roleStatus.patchValue(this.form.controls.roleStatus.value ? 'Active' : 'Inactive');
        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }
    else {
      this.sub.add(this.service.Create(data).subscribe(res => {
        this.toastr.success(res[0].userRoleId + " saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        this.location.back();

      }, err => {
        this.form.controls.roleStatus.patchValue(this.form.controls.roleStatus.value ? 'Active' : 'Inactive');

        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }
  };
  back() {
    this.location.back();
  }
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }


  displayedColumns: string[] = ['select', 'role', 'name', 'status', 'by', 'on'];
  dataSource = new MatTableDataSource<any>([]);
  selection = new SelectionModel<any>(true, []);

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    if (this.isAllSelected()) {
      this.selection.clear();
      return;
    }

    this.selection.select(...this.dataSource.data);
  }

  /** The label for the checkbox on the passed row */
  checkboxLabel(row?: any): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.role + 1}`;
  }
    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }


  checkUncheckAll(menu: any, event: any) {
    console.log(menu)
    if (event.checked) { this.menuList.find(x => x.mainMenu == menu).Menu.forEach((x: any) => { x.createUpdate = true, x.delete = true, x.view = true }) }
    else { this.menuList.find(x => x.mainMenu == menu).Menu.forEach((x: any) => { x.createUpdate = false, x.delete = false, x.view = false }) }
  }



  onChange(menu: any, event: any) {
    menu.view = event.checked; menu.createUpdate = event.checked; menu.delete = event.checked;

  }

  resetAll() {
    this.menuList.forEach((x: any) => { 
      x.Menu.forEach(element => {
        element.createUpdate = false, element.delete = false, element.view = false
      });
    })
  }
}

