
  import { SelectionModel } from "@angular/cdk/collections";
  import { Component, OnInit } from "@angular/core";
  import { FormBuilder, FormControl, Validators } from "@angular/forms";
  import { MatTableDataSource } from "@angular/material/table";
  import { ActivatedRoute, Router } from "@angular/router";
  import { NgxSpinnerService } from "ngx-spinner";
  import { ToastrService } from "ngx-toastr";
  import { Location } from '@angular/common';
  import { Subscription } from "rxjs";
  import { CommonService } from "src/app/common-service/common-service.service";
  import { AuthService } from "src/app/core/core";
import { FlRentService } from "../fl-rent.service";
import { CommonApiService } from "src/app/common-service/common-api.service";
  

  export interface PeriodicElement {
    usercode: string;
    name: string;
    admin: string;
    role: string;
    userid: string;
    password: string;
    status: string;
    email: string;
    phoneno: string;
    Agreement: string;
    created: string;
    processed: string;
    leadtime: string;
  }

  export interface PeriodicElement2 {
    usercode: string;
    name: string;
    admin: string;
    role: string;
    userid: string;
    password: string;
    status: string;
    email: string;
    phoneno: string;
    Agreement: string;
    created: string;
    processed: string;
    leadtime: string;
  }
  export interface PeriodicElement1 {
    usercode: string;
    name: string;
    admin: string;
    role: string;
    userid: string;
    password: string;
    status: string;
    email: string;
    phoneno: string;
    Agreement: string;
    created: string;
    processed: string;
    leadtime: string;
  }
  
  const ELEMENT_DATA: PeriodicElement[] = [
    {usercode: "test", name: 'test', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test', phoneno: 'test',Agreement: 'test', created: 'test', processed: 'test', leadtime: 'test'},
  ];
 
  const ELEMENT_DATA1: PeriodicElement1[] = [
    {usercode: "test", name: 'test', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test', phoneno: 'test',Agreement: 'test', created: 'test', processed: 'test', leadtime: 'test'},
  ];
  const ELEMENT_DATA2: PeriodicElement2[] = [
    {usercode: "test", name: 'test', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test', phoneno: 'test',Agreement: 'test', created: 'test', processed: 'test', leadtime: 'test'},
  ];
  @Component({
    selector: 'app-fl-rent-new',
    templateUrl: './fl-rent-new.component.html',
    styleUrls: ['./fl-rent-new.component.scss']
  })
  export class FlRentNewComponent implements OnInit {

    form = this.fb.group({
      deletionIndicator: [],
      description:  [, Validators.required],
      itemCode: [],
      itemGroup: [],
      itemType: [],
      referenceField1:  [, Validators.required],
      referenceField10: [],
      referenceField2: [],
      referenceField3: [],
      referenceField4: [],
      referenceField5: [],
      referenceField6: [],
      referenceField7: [],
      referenceField8: [],
      referenceField9: [],
      saleUnitOfMeasure: [],
      status: [],
      unitPrice: [, Validators.required],
    });
    
    screenid: 1043 | undefined;
    isShowDiv = false;
    public icon = 'expand_more';
    showFloatingButtons: any;
    toggle = true;
    pageflowupdated: any;
    toggleFloat() {
      this.isShowDiv = !this.isShowDiv;
      this.toggle = !this.toggle;
  
      if (this.icon === 'expand_more') {
        this.icon = 'chevron_left';
      } else {
        this.icon = 'expand_more'
      }
      this.showFloatingButtons = !this.showFloatingButtons;
      console.log('show:' + this.showFloatingButtons);
    }
    constructor(private fb: FormBuilder,
      private auth: AuthService,
      public toastr: ToastrService,
      // private cas: CommonApiService,
      private spin: NgxSpinnerService,
      private location: Location,
      private route: ActivatedRoute, private router: Router,
      private cs: CommonService,
      private service: FlRentService,
      private cas: CommonApiService, ) {}
  
    displayedColumns: string[] = ['select', 'usercode', 'name', 'admin', 'role', 'userid', 'password'];
    dataSource = new MatTableDataSource<any>(ELEMENT_DATA);

    displayedColumns1: string[] = ['select', 'usercode', 'name', 'admin', 'role', 'userid', 'password', 'status', 'email'];
    dataSource1 = new MatTableDataSource<PeriodicElement1>(ELEMENT_DATA1);
    selection = new SelectionModel<PeriodicElement1>(true, []);

    
    displayedColumns2: string[] = ['select', 'usercode', 'name', 'admin', 'role', 'userid', 'password', 'status', 'email', 'phoneno', 'Agreement', 'created', 'processed', 'leadtime', 'totalamount', 'lastbilled', 'voucher'];
    dataSource2 = new MatTableDataSource<PeriodicElement2>(ELEMENT_DATA2);
  
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

    toggleAllRows() {
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
      return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.no + 1}`;
    }
  
      
    clearselection(row: any) {
      this.selection.clear();
      this.selection.toggle(row);
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
    js: any = {}
    ngOnInit(): void {
      this.dropdownlist();
    let code = this.route.snapshot.params.code;
    this.js = this.cs.decrypt(code);
    console.log(this.js)
     if (this.js.pageflow != 'New') {
       this.form.controls.itemCode.disable();
       if (this.js.pageflow == 'Display')
         this.form.disable();
        this.fill();
     }
    }

    uomList: any[] = [];
    itemGroupList: any[] = [];
    dropdownlist() {
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.setup.uomType.url,
        this.cas.dropdownlist.setup.itemgroup.url,
      ]).subscribe((results) => {
        this.uomList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.uomType.key);
        this.itemGroupList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.itemgroup.key);
      }, (err) => {
        this.toastr.error(err, "");
        this.spin.hide();
      });
    }

    back() {
      this.location.back();
  
    }

    fill() {
      this.spin.show();
      this.sub.add(this.service.Get(this.js.code).subscribe(res => {
        this.form.patchValue(res, { emitEvent: false });
        this.spin.hide();
      },
       err => {
      this.cs.commonerror(err);
        this.spin.hide();
      }
      ));
    }


    sub = new Subscription();
  submitted = false;
  
  submit() {
    this.submitted = true;
    if (this.form.invalid) {
      this.toastr.error(
        "Please fill required fields to continue",
        "Notification",{
          timeOut: 2000,
          progressBar: false,
        }
      );
  
      this.cs.notifyOther(true);
      return;
    }
    
  this.cs.notifyOther(false);
  this.spin.show();
  
  if (this.js.code) {
    this.sub.add(this.service.Update(this.form.getRawValue(), this.js.code).subscribe(res => {
      this.toastr.success(this.js.code + " updated successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/materialmasters/flrent']);

      this.spin.hide();
  
    }, err => {
  
      this.cs.commonerror(err);
      this.spin.hide();
  
    }));
  }else{
    this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
      this.toastr.success(res.itemCode + " Saved Successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/materialmasters/flrent']);
      this.spin.hide();
  
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
  
    }));
   }
  
  }

  email = new FormControl('', [Validators.required, Validators.email]);
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
  }
  }
  
  
