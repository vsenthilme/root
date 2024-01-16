
  import { SelectionModel } from '@angular/cdk/collections';
  import { DatePipe } from '@angular/common';
  import { Component, Inject, OnInit, ViewChild } from '@angular/core';
    import { FormArray, FormBuilder, FormControl, Validators } from '@angular/forms';
    import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
  import { MatPaginator } from '@angular/material/paginator';
  import { MatSort } from '@angular/material/sort';
  import { MatTableDataSource } from '@angular/material/table';
    import { NgxSpinnerService } from 'ngx-spinner';
    import { ToastrService } from 'ngx-toastr';
  import { Subscription } from 'rxjs';
  import { CommonApiService } from 'src/app/common-service/common-api.service';
    import { CommonService } from 'src/app/common-service/common-service.service';
    import { AuthService } from 'src/app/core/core';
  import { ConsumablesService } from 'src/app/main-module/Masters -1/material-master/consumables/consumables.service';
  import { FlRentService } from 'src/app/main-module/Masters -1/material-master/fl-rent/fl-rent.service';
  import { HandlingChargesService } from 'src/app/main-module/Masters -1/material-master/handlingcharges/handling-charges.service';
  import { TripsService } from 'src/app/main-module/Masters -1/material-master/trips/trips.service';
  import { WorkorderService } from '../workorder.service';
  
  
    export interface PeriodicElement {
      usercode: string,
      name: string,
      admin: string,
      role: string,
      userid:string,
      password: string,
      status: string,
      email: string,
    }
    
    const ELEMENT_DATA: PeriodicElement[] = [
      {usercode: "Moving", name: 'War', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test'},
      {usercode: "Wrapping", name: 'War', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test'},
      {usercode: "Dismantling and Assembling", name: 'War', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test'},
      {usercode: "Box Size 45x45x45 cm(S) (with packing)", name: 'War', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test'},
      {usercode: "Box Size 45x45x45 cm(S) (without packing)", name: 'War', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test'},
      {usercode: "Box Size 45x45x45 cm(L) (with packing)", name: 'War', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test'},
      {usercode: "Box Size 45x45x45 cm(L) (without packing)", name: 'War', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test'},
      {usercode: "Corrugated Roll", name: 'War', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test'},
      {usercode: "Bubble Wrap", name: 'War', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test'},
      {usercode: "Nylon Roll", name: 'War', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test'},
      {usercode: "Stretch Film", name: 'War', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test'},
      {usercode: "Tape", name: 'War', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test'},
      {usercode: "Other", name: 'War', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test'},
    ];
  
  
    @Component({
      selector: 'app-workorder-new',
      templateUrl: './workorder-new.component.html',
      styleUrls: ['./workorder-new.component.scss']
    })
    export class WorkorderNewComponent implements OnInit {
  
  
  
      WorkOrderLine = this.fb.group({
        serialNumber: [1],
        itemDescription: [],
        itemNumber: [, [Validators.required]],
        quantity: [],
        rateperHour: [],
        totalAmount: [, [Validators.required]],
      });
  
      rows: FormArray = this.fb.array([this.WorkOrderLine]);
  
      form = this.fb.group({
        codeId: [],
       created: [],
       createdBy: [, Validators.required],
       createdOn: [],
       customerId:  [, Validators.required],
       deletionIndicator: [],
       endDate: [],
       endTime: [],
       workOrderLine: this.rows,
       jobCard: [],
       jobCardType: [],
       leadTime: [],
       plannedHours: [],
       processedBy:  [, Validators.required],
       processedTime: [],
       referenceField1: [],
       referenceField10: [],
       referenceField2: [],
       referenceField3: [],
       referenceField4: [],
       referenceField5: [],
       referenceField6: [],
       referenceField7: [],
       referenceField8: [],
       referenceField9: [],
       remarks: [],
       startDate: [],
       startTime: [],
       status:   ['Planned', ],
       updatedBy: [],
       updatedOn: [],
       workOrderDate: [],
       workOrderId: [],
       workOrderNumber: [],
       workOrderSbu: [],
      });
  
    
      constructor(
        public dialogRef: MatDialogRef<any>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        public toastr: ToastrService,
        private spin: NgxSpinnerService,public dialog: MatDialog,
        private auth: AuthService,
        private fb: FormBuilder,
        public cs: CommonService,
        private service: WorkorderService,
        private consumablesService: ConsumablesService,
        private handlingsService: HandlingChargesService,
        private flService: FlRentService,
        private tripService: TripsService,
        private cas: CommonApiService,
        public datePipe: DatePipe,) { }
    
  
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
      
      sub = new Subscription();
      submitted = false;
  
      ngOnInit(): void {
        this.dropdownlist();
      //  this.workOrderList();
      this.unitPriceList();
       this.form.controls.createdBy.disable();
        this.form.controls.createdOn.disable();
        this.form.controls.updatedBy.disable();
        this.form.controls.updatedOn.disable();
        if (this.data.pageflow != 'New') {
          
        this.form.controls.workOrderId.disable();
          if (this.data.pageflow == 'Display')
            this.form.disable();
          this.fill();
        }
  
        this.form.controls.createdOn1.valueChanges.subscribe(x =>{
          let leadTime = this.form.controls.endDate.value ? this.cs.getDataDiff(this.form.controls.createdOn1.value,this.form.controls.endDate.value) : '';
          this.form.controls.leadTime.patchValue(leadTime);
         }) 
  
         this.form.controls.endDate.valueChanges.subscribe(x =>{
          let processedTime = this.form.controls.endDate.value ? this.cs.getDataDiff(this.form.controls.startDate.value,this.form.controls.endDate.value) : '';
          this.form.controls.processedTime.patchValue(processedTime);
         }) 
        //this.Qtycalculation()
  
  if (this.data.pageflow == "Copy From Quote") {
    this.form.controls.customerId.patchValue(this.data.customerName);
  }
      }
  
      customerIDList: any[] = [];
      processedByList: any[] = [];
      statusList: any[] = [];
      createdList: any[] = [];
      workOrderSbuList: any[] = [];
  
      dropdownlist() {
       this.cas.getalldropdownlist([
         this.cas.dropdownlist.trans.customerID.url,
         this.cas.dropdownlist.setup.workorderprocessedby.url,
         this.cas.dropdownlist.setup.workorderstatus.url,
         this.cas.dropdownlist.setup.workordercreatedby.url,
         this.cas.dropdownlist.setup.sbu.url,
       ]).subscribe((results) => {
        this.customerIDList = this.cas.foreachlist2(results[0], this.cas.dropdownlist.trans.customerID.key);
        this.processedByList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.workorderprocessedby.key);
        this.statusList = this.cas.foreachlist1(results[2], this.cas.dropdownlist.setup.workorderstatus.key);
        this.createdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.workordercreatedby.key);
        this.workOrderSbuList = this.cas.foreachlist(results[4], this.cas.dropdownlist.setup.sbu.key);
       }, (err) => {
         this.toastr.error(err, "");
         this.spin.hide();
       });
     }
  
    
      displayedColumns: string[] = ['select', 'usercode', 'name', 'admin', 'role'];
      dataSource = new MatTableDataSource<any>(ELEMENT_DATA);
      selection = new SelectionModel<any>(true, []);
      
    
      /** Whether the number of selected elements matches the total number of rows. */
      isAllSelected() {
        const numSelected = this.selection.selected.length;
        const numRows = this.dataSource.data.length;
        return numSelected === numRows;
      }
    
      /** Selects all rows if they are not all selected; otherwise clear selection. */
      toggleAllRows() {
        if (this.isAllSelected()) {
          this.selection.clear();
          return;
        }
    
        this.selection.select(...this.dataSource.data);
      }
    
      /** The label for the checkbox on the passed row */
      checkboxLabel(row?: PeriodicElement): string {
        if (!row) {
          return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
        }
        return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.usercode + 1}`;
      }
    
      clearselection(row: any) {
        this.selection.clear();
        this.selection.toggle(row);
      }
    
    
      @ViewChild(MatPaginator) paginator: MatPaginator;
      @ViewChild(MatSort) sort: MatSort;
    
      
      ngAfterViewInit() {
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
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
    
      fill() {
        this.spin.show();
        this.sub.add(this.service.Get(this.data.code).subscribe(res => {
          this.form.patchValue(res, { emitEvent: false });
  
       this.form.controls.createdOn1.patchValue(this.form.controls.createdOn.value);
       this.form.controls.endDate.patchValue(this.form.controls.endDate.value);
  
       this.form.controls.createdOn.patchValue(this.cs.dateapiutc0(this.form.controls.createdOn.value));
       this.form.controls.updatedOn.patchValue(this.cs.dateapiutc0(this.form.controls.updatedOn.value));
        //this.form.controls.updatedOn.patchValue(this.cs.dateapiutc0(this.form.controls.updatedOn.value));
  
  
    
         
          this.spin.hide();
        },
         err => {
        this.cs.commonerror(err);
          this.spin.hide();
        }
        ));
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
  
     let startTime = this.datePipe.transform(this.form.controls.startDate.value, "yyyy-MM-dd");
     let setStartDate = startTime + "T" + this.form.controls.startTime.value + ':00.000Z';
     this.form.controls.startDate.patchValue(setStartDate);
  
     let endTime = this.datePipe.transform(this.form.controls.endDate.value, "yyyy-MM-dd");
     let setEndDate = endTime + "T" + this.form.controls.endTime.value + ':00.000Z';
     this.form.controls.endDate.patchValue(setEndDate);
  
      if (this.data.code) {
        this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code).subscribe(res => {
          this.toastr.success(this.data.code + " updated successfully!","Notification",{
            timeOut: 2000,
            progressBar: false,
          });
          this.spin.hide();
          this.dialogRef.close();
  
        }, err => {
  
          this.cs.commonerror(err);
          this.spin.hide();
  
        }));
      }else{
        this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
          this.toastr.success(res.workOrderId  + " Saved Successfully!","Notification",{
            timeOut: 2000,
            progressBar: false,
          });
          this.spin.hide();
          this.dialogRef.close();
  
        }, err => {
          this.cs.commonerror(err);
          this.spin.hide();
  
        }));
      }
  
  
     
      }
  
      lineData: any[] = [];
  
      workOrderList(){
          this.consumablesService.search({}).subscribe(res => {
            res.forEach(element => {
              this.lineData.push(element);
            });
            console.log(this.lineData)
          });
          this.handlingsService.search({}).subscribe(res => {
            res.forEach(element => {
              this.lineData.push(element);
            });
            console.log(this.lineData)
        });
        this.flService.search({}).subscribe(res => {
          res.forEach(element => {
            this.lineData.push(element);
          });
          console.log(this.lineData)
      });
      this.tripService.search({}).subscribe(res => {
        res.forEach(element => {
          this.lineData.push(element);
        });
        console.log(this.lineData)
    })
  
     
      }
  
      unitPriceList(){
        this.consumablesService.search({}).subscribe(res => {
          res.forEach(element => {
          if(element.itemCode == "C00000000011"){
            this.form.controls.sboxSizeWithPackingUnitPrice.patchValue(Number(element.unitPrice));
          };  
          if(element.itemCode == "C00000000012"){
            this.form.controls.sboxSizeWithoutPackingUnitPrice.patchValue(Number(element.unitPrice));
          }; 
          if(element.itemCode == "C00000000013"){
            this.form.controls.lboxSizeWithPackingUnitPrice.patchValue(Number(element.unitPrice));
          };
          if(element.itemCode == "C00000000014"){
            this.form.controls.lboxSizeWithoutPackingUnitPrice.patchValue(Number(element.unitPrice));
          };
          if(element.itemCode == "C00000000015"){
            this.form.controls.corrugatedRollUnitPrice.patchValue(Number(element.unitPrice));
          };
          if(element.itemCode == "C00000000016"){
            this.form.controls.bubbleWrapUnitPrice.patchValue(Number(element.unitPrice));
          };
          if(element.itemCode == "C00000000017"){
            this.form.controls.nylonRollUnitPrice.patchValue(Number(element.unitPrice));
          };
          if(element.itemCode == "C00000000018"){
            this.form.controls.stretchFilmUnitPrice.patchValue(Number(element.unitPrice));
          };
          if(element.itemCode == "C00000000019"){
            this.form.controls.tapeUnitPrice.patchValue(Number(element.unitPrice));
          };
          if(element.itemCode == "C00000000020"){
            this.form.controls.otherUnitPrice.patchValue(Number(element.unitPrice));
          };
          });
          this.tripService.search({}).subscribe(res => {
            res.forEach(element => {
              if(element.itemCode == "TP0000000006"){
                this.form.controls.movingUnitPrice.patchValue(Number(element.unitPrice));
              }; 
            });
          });
          this.handlingsService.search({}).subscribe(res => {
            res.forEach(element => {
              if(element.itemCode == "HC0000000006"){
                this.form.controls.wrappingUnitPrice.patchValue(Number(element.unitPrice));
              }; 
              if(element.itemCode == "HC0000000007"){
                this.form.controls.dismantlingAssemblingUnitPrice.patchValue(Number(element.unitPrice));
              }; 
            });
          });
        });
      
      }
  Qtycalculation(){
  
    this.form.controls.movingQuantity.valueChanges.subscribe(x => {
      this.form.controls.movingTotal.patchValue(x as number * this.form.controls.movingUnitPrice.value as number);
    });
    this.form.controls.wrappingQuantity.valueChanges.subscribe(x => {
      this.form.controls.wrappingTotal.patchValue(x as number * this.form.controls.wrappingUnitPrice.value as number);
    });
    this.form.controls.dismantlingAssemblingQuantity.valueChanges.subscribe(x => {
      this.form.controls.dismantlingAssemblingTotal.patchValue(x as number * this.form.controls.dismantlingAssemblingUnitPrice.value as number);
    });
    this.form.controls.sboxSizeWithPackingQuantity.valueChanges.subscribe(x => {
      this.form.controls.sboxSizeWithPackingTotal.patchValue(x as number * this.form.controls.sboxSizeWithPackingUnitPrice.value as number);
    });
    this.form.controls.sboxSizeWithoutPackingQuantity.valueChanges.subscribe(x => {
      this.form.controls.sboxSizeWithoutPackingTotal.patchValue(x as number * this.form.controls.sboxSizeWithoutPackingUnitPrice.value as number);
    });
    this.form.controls.lboxSizeWithPackingQuantity.valueChanges.subscribe(x => {
      this.form.controls.lboxSizeWithPackingTotal.patchValue(x as number * this.form.controls.lboxSizeWithPackingUnitPrice.value as number);
    });
    this.form.controls.lboxSizeWithoutPackingQuantity.valueChanges.subscribe(x => {
      this.form.controls.lboxSizeWithoutPackingTotal.patchValue(x as number * this.form.controls.lboxSizeWithoutPackingUnitPrice.value as number);
    });
    this.form.controls.corrugatedRollQuantity.valueChanges.subscribe(x => {
      this.form.controls.corrugatedRollTotal.patchValue(x as number * this.form.controls.corrugatedRollUnitPrice.value as number);
    });
    this.form.controls.bubbleWrapQuantity.valueChanges.subscribe(x => {
      this.form.controls.bubbleWrapTotal.patchValue(x as number * this.form.controls.bubbleWrapUnitPrice.value as number);
    });
    this.form.controls.nylonRollQuantity.valueChanges.subscribe(x => {
      this.form.controls.nylonRollTotal.patchValue(x as number * this.form.controls.nylonRollUnitPrice.value as number);
    });
    this.form.controls.stretchFilmQuantity.valueChanges.subscribe(x => {
      this.form.controls.stretchFilmTotal.patchValue(x as number * this.form.controls.stretchFilmUnitPrice.value as number);
    });
    this.form.controls.tapeQuantity.valueChanges.subscribe(x => {
      this.form.controls.tapeTotal.patchValue(x as number * this.form.controls.tapeUnitPrice.value as number);
    });
    this.form.controls.otherQuantity.valueChanges.subscribe(x => {
      this.form.controls.otherTotal.patchValue(x as number * this.form.controls.otherUnitPrice.value as number);
    });
  
  
  
    this.form.controls.movingUnitPrice.valueChanges.subscribe(x => {
      this.form.controls.movingTotal.patchValue(x as number * this.form.controls.movingQuantity.value as number);
    });
    this.form.controls.wrappingUnitPrice.valueChanges.subscribe(x => {
      this.form.controls.wrappingTotal.patchValue(x as number * this.form.controls.wrappingQuantity.value as number);
    });
    this.form.controls.dismantlingAssemblingUnitPrice.valueChanges.subscribe(x => {
      this.form.controls.dismantlingAssemblingTotal.patchValue(x as number * this.form.controls.dismantlingAssemblingQuantity.value as number);
    });
    this.form.controls.sboxSizeWithPackingUnitPrice.valueChanges.subscribe(x => {
      this.form.controls.sboxSizeWithPackingTotal.patchValue(x as number * this.form.controls.sboxSizeWithPackingQuantity.value as number);
    });
    this.form.controls.sboxSizeWithoutPackingUnitPrice.valueChanges.subscribe(x => {
      this.form.controls.sboxSizeWithoutPackingTotal.patchValue(x as number * this.form.controls.sboxSizeWithoutPackingQuantity.value as number);
    });
    this.form.controls.lboxSizeWithPackingUnitPrice.valueChanges.subscribe(x => {
      this.form.controls.lboxSizeWithPackingTotal.patchValue(x as number * this.form.controls.lboxSizeWithPackingQuantity.value as number);
    });
    this.form.controls.lboxSizeWithoutPackingUnitPrice.valueChanges.subscribe(x => {
      this.form.controls.lboxSizeWithoutPackingTotal.patchValue(x as number * this.form.controls.lboxSizeWithoutPackingQuantity.value as number);
    });
    this.form.controls.corrugatedRollUnitPrice.valueChanges.subscribe(x => {
      this.form.controls.corrugatedRollTotal.patchValue(x as number * this.form.controls.corrugatedRollQuantity.value as number);
    });
    this.form.controls.bubbleWrapUnitPrice.valueChanges.subscribe(x => {
      this.form.controls.bubbleWrapTotal.patchValue(x as number * this.form.controls.bubbleWrapQuantity.value as number);
    });
    this.form.controls.nylonRollUnitPrice.valueChanges.subscribe(x => {
      this.form.controls.nylonRollTotal.patchValue(x as number * this.form.controls.nylonRollQuantity.value as number);
    });
    this.form.controls.stretchFilmUnitPrice.valueChanges.subscribe(x => {
      this.form.controls.stretchFilmTotal.patchValue(x as number * this.form.controls.stretchFilmQuantity.value as number);
    });
    this.form.controls.tapeUnitPrice.valueChanges.subscribe(x => {
      this.form.controls.tapeTotal.patchValue(x as number * this.form.controls.tapeQuantity.value as number);
    });
    this.form.controls.otherUnitPrice.valueChanges.subscribe(x => {
      this.form.controls.otherTotal.patchValue(x as number * this.form.controls.otherQuantity.value as number);
    });
    
  }
  
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    