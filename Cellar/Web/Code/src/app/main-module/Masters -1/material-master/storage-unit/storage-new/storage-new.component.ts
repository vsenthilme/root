

  import { SelectionModel } from "@angular/cdk/collections";
  import { Component, OnInit, ViewChild } from "@angular/core";
  import { FormBuilder, FormControl, Validators } from "@angular/forms";
  import { MatTableDataSource } from "@angular/material/table";
  import { ActivatedRoute, Router } from "@angular/router";
  import { NgxSpinnerService } from "ngx-spinner";
  import { ToastrService } from "ngx-toastr";
  import { Location } from '@angular/common';
  import { Subscription } from "rxjs";
  import { CommonService } from "src/app/common-service/common-service.service";
  import { AuthService } from "src/app/core/core";
import { StorageunitService } from "../storageunit.service";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { AgreementService } from "src/app/main-module/operation/operation/agreement/agreement.service";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
  



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

  

  const ELEMENT_DATA: PeriodicElement2[] = [
    {usercode: "test", name: 'test', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test', phoneno: 'test',Agreement: 'test', created: 'test', processed: 'test', leadtime: 'test'},
  ];

  @Component({
    selector: 'app-storage-new',
    templateUrl: './storage-new.component.html',
    styleUrls: ['./storage-new.component.scss']
  })
  export class StorageNewComponent implements OnInit {


    form = this.fb.group({
      availability:[],
        availableAfter:[],
        aisle: [],
        bin: [],
        createdBy: [],
        codeId:  [, Validators.required],
        createdOn:[],
        deletionIndicator:[],
        description: [, ],
        doorType: [],
        halfYearly: [0,],
        itemCode: [],
        itemGroup: [],
        itemType: [],
        length: [],
        linkedAgreement: [],
        monthly: [0,],
        occupiedFrom:[],
        originalDimention: [],
        phase: [],
        priceMeterSquare: [0,],
        quarterly: [0,],
        rack: [],
        referenceField1: ['KWD', ],
        referenceField10: [],
        referenceField2: [],
        referenceField3: [],
        referenceField4: [],
        referenceField5: [],
        referenceField6: [],
        referenceField7: [],
        referenceField8: [],
        referenceField9: [],
        room: [],
        roundedDimention: [],
        storeSizeMeterSquare: [],
        storageType: [],
        updatedBy: [],
        updatedOn:[],
        weekly: [0,],
        width: [],
        yearly: [0,],
        zone: [],
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
      private service:StorageunitService, 
      private cas: CommonApiService,
      private agreementService: AgreementService) { }

      
  


    
    displayedColumns2: string[] = ['select', 'usercode', 'name', 'admin', 'role', 'password',  'endDate','status', 'phoneno',  'created', 'processed','totalamount'];
    dataSource = new MatTableDataSource<PeriodicElement2>(ELEMENT_DATA);
    selection = new SelectionModel<PeriodicElement2>(true, []);
  
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

    @ViewChild(MatSort, { static: true })
    sort!: MatSort;
    @ViewChild(MatPaginator, { static: true })
    paginator!: MatPaginator; // Pagination
   // Pagination
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
      this.form.controls.createdBy.disable();
      this.form.controls.createdOn.disable();
      this.form.controls.updatedBy.disable();
      this.form.controls.updatedOn.disable();
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
  
    sub = new Subscription();
    submitted = false;
  

    fill() {
      this.spin.show();
      this.sub.add(this.service.Get(this.js.code).subscribe(res => {
        this.form.patchValue(res, { emitEvent: false });
        this.form.controls.linkedAgreement.patchValue('');
        this.form.controls.occupiedFrom.patchValue('');
        this.form.controls.availableAfter.patchValue('');
        this.agreementService.finStoreNumber({storeNumber: [this.form.controls.itemCode.value]}).subscribe(ress => {
         let result =  ress.sort((a, b) => (a.agreementNumber > b.agreementNumber) ? -1 : 1);
         console.log(result.length)
  //  if(result.length > 0){
  //   this.form.controls.linkedAgreement.patchValue(result[0].agreementNumber);
  //   this.form.controls.occupiedFrom.patchValue(result[0].startDate);
  //   this.form.controls.availableAfter.patchValue(result[0].endDate);
  //  }else{
  //   this.form.controls.linkedAgreement.patchValue('');
  //   this.form.controls.occupiedFrom.patchValue('');
  //   this.form.controls.availableAfter.patchValue('');
  //  }
  result.forEach(element => {
    if(element.status == "Open"){
    this.form.controls.linkedAgreement.patchValue(element.agreementNumber);
    this.form.controls.occupiedFrom.patchValue(element.startDate);
    this.form.controls.availableAfter.patchValue(element.endDate);
    }
  });
          this.dataSource = new MatTableDataSource<any>(result);
          this.selection = new SelectionModel<any>(true, []);
          this.dataSource.sort = this.sort;
           this.dataSource.paginator = this.paginator;
           this.spin.hide();
        }, err => {
      
          this.cs.commonerror(err);
          this.spin.hide();
      
        });

        this.spin.hide();
      },
       err => {
      this.cs.commonerror(err);
        this.spin.hide();
      }
      ));
    }

    back() {
      this.location.back();
  
    }
    storageTypeList: any[] = [];
    doorTypeList: any[] = [];
    itemTypeList: any[] = [];
    itemGroupList: any[] = [];
    phaseList: any[] = [];
    zoneList: any[] = [];
    roomList: any[] = [];
    rackList: any[] = [];
    binList: any[] = [];
    statusList: any[] = [];
    currencyList: any[] = [];
    uomTypeList: any[] = [];
    dropdownlist(){
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.setup.storagetype.url,
        this.cas.dropdownlist.setup.doortype.url,
        this.cas.dropdownlist.setup.itemtype.url,
        this.cas.dropdownlist.setup.itemgroup.url,
        this.cas.dropdownlist.setup.phase.url,
        this.cas.dropdownlist.setup.zone.url,
        this.cas.dropdownlist.setup.room.url,
        this.cas.dropdownlist.setup.rack.url,
        this.cas.dropdownlist.setup.bin.url,
        this.cas.dropdownlist.setup.status.url,
        this.cas.dropdownlist.setup.currency.url,
        this.cas.dropdownlist.setup.uomType.url,
      ]).subscribe((results) => {
      this.storageTypeList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.storagetype.key);
      this.doorTypeList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.doortype.key);
      this.itemTypeList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.itemtype.key);
      this.itemGroupList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.itemgroup.key);
      this.phaseList = this.cas.foreachlist(results[4], this.cas.dropdownlist.setup.phase.key);
      this.zoneList = this.cas.foreachlist(results[5], this.cas.dropdownlist.setup.zone.key);
      this.roomList = this.cas.foreachlist(results[6], this.cas.dropdownlist.setup.room.key);
      this.rackList = this.cas.foreachlist(results[7], this.cas.dropdownlist.setup.rack.key);
      this.binList = this.cas.foreachlist(results[8], this.cas.dropdownlist.setup.bin.key);
      this.statusList = this.cas.foreachlist(results[9], this.cas.dropdownlist.setup.status.key).filter(x => ['Occupied','Vacant','Booked','To be vacant','Maintenance','Own Used', 'Double Locked','Legal Conflicts'].includes(x.value));
      this.currencyList = this.cas.foreachlist2(results[10], this.cas.dropdownlist.setup.currency.key);
      this.uomTypeList = this.cas.foreachlist(results[11], this.cas.dropdownlist.setup.uomType.key);
      console.log(this.statusList)
      }, (err) => {
        this.toastr.error(err, "");
        this.spin.hide();
      });
    }

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
    

    this.form.controls.storeSizeMeterSquare.patchValue(Number(this.form.controls.storeSizeMeterSquare.value))
    if (this.js.code) {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.js.code).subscribe(res => {
        this.toastr.success(this.js.code + " updated successfully!","Notification",{
          timeOut: 2000,
          progressBar: false,
        });
        this.router.navigate(['/main/materialmasters/storageunit']);

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
        this.router.navigate(['/main/materialmasters/storageunit']);
        this.spin.hide();
    
      }, err => {
        this.cs.commonerror1(err);
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
  
  
