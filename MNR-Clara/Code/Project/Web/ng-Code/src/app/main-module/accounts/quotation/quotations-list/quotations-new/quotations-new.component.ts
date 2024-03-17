import { SelectionModel } from "@angular/cdk/collections";
import { ChangeDetectorRef, Component, OnChanges, OnInit, SimpleChanges, ViewChild } from "@angular/core";
import { AbstractControl, FormArray, FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { BehaviorSubject, Observable, Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { GeneralMatterService } from "src/app/main-module/matters/case-management/General/general-matter.service";
import { map, startWith } from 'rxjs/operators';
import { QuotationService } from "../quotation.service";
import { ThrowStmt } from "@angular/compiler";
import { ChangeDetectionStrategy } from "@angular/compiler/src/core";
// import pdfMake from "pdfmake/build/pdfmake";
// import pdfFonts from "pdfmake/build/vfs_fonts";
// pdfMake.vfs = pdfFonts.pdfMake.vfs;
interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-quotations-new',
  templateUrl: './quotations-new.component.html',
  styleUrls: ['./quotations-new.component.scss'],
})
export class QuotationsNewComponent implements OnInit {
  screenid: 1135 | undefined;
  displayedColumns: string[] = ['sno', 'timeKeeperCode', 'date', 'itemNumber', 'itemDescription', 'quantity', 'rateperHour', 'totalAmount', 'delete'];
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  clientList: any;
  matterList: any;
  filtersmatter: any;
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


  myControl = new FormControl();
  options: string[] = ['10008-01', '10009-01', '10010-01', '10011-01', '10012-01', '10013-01', '10014-01', '10015-01', '10016-01', '10017-01', '10018-01'];
  filteredOptions: Observable<string[]>;


  ELEMENT_DATA: any[] = [];
  dataSource = new BehaviorSubject<AbstractControl[]>([]);


  input: any;
  isbtntext = true;

  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);

  isbtntexts = false;
  isbtntextr = false;

  QuotationLine = this.fb.group({
    serialNumber: [1],
    itemDescription: [],
    itemNumber: [, [Validators.required]],
    quantity: [],
    rateperHour: [],
    referenceField1: [],
    referenceField2: [],
    totalAmount: [, [Validators.required]],
  });

  rows: FormArray = this.fb.array([this.QuotationLine]);

  form = this.fb.group({
    approvedDate: [],
    caseCategoryId: [],
    caseSubCategoryId: [],
    classId: [, [Validators.required]],
    clientId: [, [Validators.required]],
    corporation: [],
    createdBy: [this.auth.userID],
    createdOn: [],
    currency: ['$'],
    deletionIndicator: [],
    dueDate: [],
    languageId: [, [Validators.required]],
    matterNumber: [, [Validators.required]],
    paymentPlanNumber: [],
    quotationAmount: [, [Validators.required]],
    quotationDate: [new Date(), [Validators.required]],
    quotationLine: this.rows,
    quotationNo: [],
    quotationRevisionNo: [],
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
    sentDate: [],
    statusId: [],
    termDetails: [],
    updatedBy: [this.auth.userID],
    updatedOn: [],

  });


  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {

    if (control.includes('.')) {
      const controls = this.form.get(control);
      return controls ? controls.hasError(error) : false && this.submitted;

    }
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
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
    private service: QuotationService,
    private serviceMatter: GeneralMatterService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,) { }

  ngOnInit(): void {
    this.ngdropdown();
    this.form.controls.referenceField10.disable();
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    this.form.controls.quotationNo.disable();
    this.form.controls.quotationAmount.disable();
    this.form.controls.sentDate.disable();

    this.form.controls.approvedDate.disable();
    this.form.controls.caseCategoryId.disable();
    this.form.controls.caseSubCategoryId.disable();
    this.form.controls.referenceField9.disable();
    // this.form.controls.statusId.disable();
    this.QuotationLine.controls.quantity.valueChanges.subscribe(x => {
      this.QuotationLine.controls.totalAmount.patchValue(0);
      if (x)
        this.QuotationLine.controls.totalAmount.patchValue(this.QuotationLine.controls.quantity.value as number * this.QuotationLine.controls.rateperHour.value as number);
    }
    );

    this.QuotationLine.controls.rateperHour.valueChanges.subscribe(x => {
      this.QuotationLine.controls.totalAmount.patchValue(0);
      if (x)
        this.QuotationLine.controls.totalAmount.patchValue(this.QuotationLine.controls.quantity.value as number * this.QuotationLine.controls.rateperHour.value as number);
    }
    );
    this.QuotationLine.controls.totalAmount.valueChanges.subscribe(x => {
      this.form.controls.quotationAmount.patchValue(0);
      if (x)
        this.calculatamount();
    }
    );

    this.auth.isuserdata();

    let code = this.route.snapshot.params.code;
    if (code != 'new') {


      let js = this.cs.decrypt(code);
      this.fill(js);
    }
    this.dropdownlist();
    this.form.controls.clientId.valueChanges.subscribe(x => {

      this.apiClientfileter = { clientId: x };
   //   this.form.controls.matterNumber.patchValue('');
      this.form.controls.referenceField9.patchValue('');

      if (x)
        this.get_clientetails(x);
    }
    );

    this.form.controls.matterNumber.valueChanges.subscribe(x => {
      this.form.controls.caseCategoryId.patchValue('');
      this.form.controls.caseSubCategoryId.patchValue('');
      if (this.form.controls.matterNumber.value)
        this.get_matterdetails(this.form.controls.matterNumber.value);
    }
    );

    this.QuotationLine.controls.totalAmount.valueChanges.subscribe(x => {
      this.form.controls.quotationAmount.patchValue(0);
      if (x)
      this.calculatamount();
  }
    );
    this.filteredOptions = this.myControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value)),
    );
    this.updateView();
  }
  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }
  calculatamount() {
    // row.controls.totalAmount.patchValue(row.controls.rateperHour.value as number * row.controls.quantity.value as number);
    ;
    let totalAmount = 0;
    this.rows.controls.forEach((x: any) => {
      let newAmount: number = +x.controls.totalAmount.value;
      totalAmount = totalAmount + newAmount;

    });
    this.form.controls.quotationAmount.patchValue(totalAmount);
    console.log(totalAmount);
  }

  statusIdList: any[] = [];
  clientIdList: any[] = [];
  caseSubCategoryIdList: any[] = [];
  caseCategoryIdList: any[] = [];
  clientIdCorporationList: any[] = [];
  apiClientfileter: any = { clientId: null };
  glList: any[] = [];


  selectedItems3: SelectItem[] = [];
  multiselectclientList: any[] = [];
  multiclientList: any[] = [];


  selectedItems4: SelectItem[] = [];
  multiselectcorporationList: any[] = [];
  multicorporationList: any[] = [];


  selectedItems2: SelectItem[] = [];
  multiselectmatterList: any[] = [];
  multimatterList: any[] = [];

  dropdownSettings = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  
  dropdownSettings1 = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };


  ngdropdown() {
    this.sub.add(this.service.GetClientdetails().subscribe(res => {
      this.clientList = res;
      this.clientList.forEach((x: { clientId: string; firstNameLastName: string; }) => this.multiclientList.push({ value: x.clientId, label: x.clientId + '-' + x.firstNameLastName }))
      this.multiselectclientList = this.multiclientList;

      this.spin.hide();

    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    this.sub.add(this.service.getmatter().subscribe(res => {
      this.matterList = res;
      // this.filtersmatter = this.matterList.filter((element: { clientId: string; }) => {
      //   return element.clientId == this.selectedItems3[0].id  as string; 

      // });

      this.matterList.forEach((x: { matterNumber: string }) => this.multimatterList.push({ value: x.matterNumber, label: x.matterNumber }))
      this.multiselectmatterList = this.multimatterList;

      this.spin.hide();

    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
  }
  timeKeeperCodelist: any[] = [];
  multiTimeKeeperCodelist: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas
      .getalldropdownlist([
        this.cas.dropdownlist.setup.statusId.url,
        this.cas.dropdownlist.client.clientId.url,

        this.cas.dropdownlist.setup.caseSubcategoryId.url,

        this.cas.dropdownlist.setup.caseCategoryId.url,
        this.cas.dropdownlist.setup.itemNumber.url,
        this.cas.dropdownlist.setup.timeKeeperCode.url,
      ])
      .subscribe(
        (results) => {
          let statusL =  [1,54];  // [10, 1,52]
          if (this.form.controls.statusId.value == 1) statusL =  [1,54]; //[10, 1,52];
          // else if (this.form.controls.statusId.value == 12)
          //   statusL = [10];
          // else if (this.form.controls.statusId.value == 13)
          //   statusL = [10];
          console.log(results[2]);
          this.statusIdList = this.cas.foreachlist(
            results[0],
            this.cas.dropdownlist.setup.statusId.key,
            { statusId: statusL }
          );
          console.log(this.statusIdList);
          this.clientIdList = this.cas.foreachlist(
            results[1],
            this.cas.dropdownlist.client.clientId.key
          );
          this.caseSubCategoryIdList = this.cas.foreachlist(
            results[2],
            this.cas.dropdownlist.setup.caseSubcategoryId.key
          );
          this.caseCategoryIdList = this.cas.foreachlist(
            results[3],
            this.cas.dropdownlist.setup.caseCategoryId.key
          );
          this.clientIdCorporationList = this.cas.foreachlist(
            results[1],
            this.cas.dropdownlist.client.clientId.key,
            { clientCategoryId: '1' }
          );
          this.clientIdCorporationList.forEach(
            (x: { key: string; value: string }) =>
              this.multicorporationList.push({ value: x.key, label: x.value })
          );
          this.multiselectcorporationList = this.multicorporationList;

          this.glList = this.cas.foreachlist(
            results[4],
            this.cas.dropdownlist.setup.itemNumber.key
          );

          this.timeKeeperCodelist = this.cas.foreachlist(
            results[5],
            this.cas.dropdownlist.setup.timeKeeperCode.key
          );
          this.timeKeeperCodelist.forEach((x: { key: string; value: string }) =>
            this.multiTimeKeeperCodelist.push({
              value: x.key,
              label: x.value,
            })
          );
          if(this.pageflow != 'New'){
            
            console.log(this.QuotationLine.controls.referenceField1.value)
            console.log(this.formResult)
            this.formResult.quotationLine.forEach((d: any) => this.addRow(d, false));
            console.log(this.QuotationLine.controls.referenceField1.value)
          }
        },
        (err) => {
          this.toastr.error(err, '');
        }
      );

    this.spin.hide();
  }

  get_clientetails(code: any) {
    console.log("code", code)
    this.spin.show();
    this.sub.add(this.serviceMatter.GetClient(this.form.controls.clientId.value).subscribe(res => {
      this.form.controls.referenceField9.patchValue(res.corporationClientId);
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  get_matterdetails(code: any) {
    this.spin.show();
    this.sub.add(this.serviceMatter.Get(this.form.controls.matterNumber.value).subscribe(res => {
      console.log(this.caseSubCategoryIdList)
      this.form.controls.caseCategoryId.patchValue(res.caseCategoryId);
      this.form.controls.caseSubCategoryId.patchValue(res.caseSubCategoryId);
      this.form.controls.classId.patchValue(res.classId);
      this.form.controls.languageId.patchValue(res.languageId);
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  list: any = []
  path = "New";
  btntext = "Save";
  pageflow = "New";
  formResult: any;
  fill(data: any) {
    this.formResult = [];
    this.pageflow = data.pageflow;

    if (data.pageflow != 'New') {
      this.path = 'Edit';
      this.form.controls.clientId.disable();
      this.dropdownSettings1.disabled = true;
      this.form.controls.matterNumber.disable();

      this.btntext = 'Update';
      this.pageflow = data.code.quotationNo;
      if (data.pageflow == 'Display') {
        this.path = 'Display';
        this.form.disable();
        this.dropdownSettings.disabled = true;
        this.isbtntext = false;
      }
      this.spin.show();
      this.sub.add(
        this.service
          .Get(data.code.quotationNo, data.code.quotationRevisionNo)
          .subscribe(
            (res) => {
              console.log(res);
              this.form.patchValue(res);
              this.formResult = res;
              // this.multiselectcorporationList.forEach(element => {
              //   if(element.id == res.corporation){
              //     console.log(this.form)
              //     this.form.controls.corporation.patchValue([element]);
              //   }
              // });
              this.form.controls.quotationAmount.patchValue(
                res.quotationAmount
              );
              this.apiClientfileter = { clientId: res.clientId };
              // this.form.controls.clientId.patchValue([{ id: res.clientId, itemName: res.clientId }]);
              // this.form.controls.matterNumber.patchValue([{ id: res.matterNumber, itemName: res.matterNumber }]);
              // this.form.controls.corporation.patchValue([{ id: res.corporation, itemName: res.corporation }]);
              // this.form.controls.matterNumber.patchValue(res.matterNumber);
              // this.form.controls.clientId.patchValue(res.clientId);
              // this.form.controls.corporation.patchValue(res.corporation);
              this.form.controls.createdOn.patchValue(
                this.cs.dateapi(this.form.controls.createdOn.value)
              );
              this.form.controls.updatedOn.patchValue(
                this.cs.dateapi(this.form.controls.updatedOn.value)
              );

              if (res.statusId == 12) {
                this.isbtntextr = true;
              }
              if (res.statusId != 13 && res.statusId != 12) {
                this.isbtntexts = true;
              }
              if (this.pageflow == 'New') {
                this.form.controls.statusId.patchValue(1);
              }

              this.rows.clear();
             // res.quotationLine.forEach((d: any) => this.addRow(d, false));
              this.updateView();
              this.spin.hide();
            },
            (err) => {
              this.cs.commonerror(err);
              this.spin.hide();
            }
          )
      );
    } else this.ELEMENT_DATA.forEach((d: any) => this.addRow(d, false));
  }

  submit(x: any = null) {
    //if (this.selectedItems2 && this.selectedItems2.length > 0)
    // {
    //   let multiplematter: any[]=[]
    //   this.selectedItems2.forEach((a: any)=> multiplematter.push(a.id))
    //   this.form.patchValue({matterNumber: multiplematter });
    // }

    // if (this.selectedItems3 && this.selectedItems3.length > 0)
    // {
    //   let multipleclient: any[]=[]
    //   this.selectedItems3.forEach((a: any)=> multipleclient.push(a.id))
    //   this.form.patchValue({clientId: multipleclient });
    // }

    // if (this.selectedItems2 != null && this.selectedItems2 != undefined && this.selectedItems2.length > 0) {
    //   this.form.patchValue({ matterNumber: this.selectedItems2[0].id });
    // }
    // else{
    //   this.form.patchValue({ matterNumber: null });
    // }
    // if (this.selectedItems3 != null && this.selectedItems3 != undefined && this.selectedItems3.length > 0) {
    //   this.form.patchValue({ clientId: this.selectedItems3[0].id });
    // }
    // else{
    //   this.form.patchValue({ clientId: null });
    // }

    // if (this.selectedItems4 != null && this.selectedItems4 != undefined && this.selectedItems4.length > 0) {
    //   this.form.patchValue({ corporation: this.selectedItems4[0].id });
    // }
    // else{
    //   this.form.patchValue({ corporation: null });
    // }

    if (x && this.form.controls.statusId.value != 10 && this.pageflow != 'New') {
     
     
    
      this.form.controls.statusId.patchValue(x);
      if (x == 12)
        this.form.controls.sentDate.patchValue(this.cs.todayCallApi());
      if (x == 13)
        this.form.controls.approvedDate.patchValue(this.cs.todayCallApi());

    }

    this.submitted = true; this.form.updateValueAndValidity;


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
    if (this.rows.controls.length == 0) {
      this.toastr.error(
        "Please add line data.",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );

      this.cs.notifyOther(true);
      return;
    }
    this.cs.notifyOther(false);
    this.spin.show();
    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');
    this.form.patchValue({ updatedby: this.auth.username });
    if (this.form.controls.quotationNo.value) {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.form.controls.quotationNo.value, this.form.controls.quotationRevisionNo.value).subscribe(res => {
        this.toastr.success(this.form.controls.quotationNo.value + " updated successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        this.router.navigate(['/main/accounts/quotationslist']);



      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }
    else {
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {




        this.toastr.success(res.quotationNo + " saved successfully!",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );
        this.spin.hide();
        this.router.navigate(['/main/accounts/quotationslist']);

      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }
  };
  back() {
    this.router.navigate(['/main/accounts/quotationslist']);
  }
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
  @ViewChild(MatSort)
  sort: MatSort;
  @ViewChild(MatPaginator)
  paginator: MatPaginator; // Pagination
  // addData() {
  //   this.ELEMENT_DATA.push({
  //     itemDescription: [],
  //     itemNumber: [],
  //     quantity: [],
  //     rateperHour: [],
  //     totalAmount: [],
  //   })
  //   this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  //   // this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA.sort((a, b) => (a.updatedOn > b.updatedOn) ? -1 : 1));
  //   this.dataSource.sort = this.sort;
  //   this.dataSource.paginator = this.paginator;
  // }

  addRow(d?: any, noUpdate?: boolean) {
    const row = this.fb.group({
      serialNumber: [this.rows.controls.length + 1],
      itemDescription: [],
      itemNumber: [, [Validators.required]],
      quantity: [],
      rateperHour: [],
      referenceField1: [],
      referenceField2: [],
      totalAmount: [, [Validators.required]],
    });
    if (d)
      row.patchValue(d);


    row.controls.quantity.valueChanges.subscribe(x => {
      row.controls.totalAmount.patchValue(0);
      if (x)
        row.controls.totalAmount.patchValue(row.controls.quantity.value as number * row.controls.rateperHour.value as number);
    }
    );

    row.controls.rateperHour.valueChanges.subscribe(x => {
      row.controls.totalAmount.patchValue(0);
      if (x)
        row.controls.totalAmount.patchValue(row.controls.quantity.value as number * row.controls.rateperHour.value as number);
    }
    );
    row.controls.totalAmount.valueChanges.subscribe(x => {
      this.form.controls.quotationAmount.patchValue(0);
      if (x)
        this.calculatamount();
    }
    );

    this.rows.push(row);
    if (!noUpdate) { this.updateView(); }
  }
  updateView() {
    this.dataSource.next(this.rows.controls);
  }
  removerow(x: any) {
    this.rows.removeAt(x)
    this.reset();
    this.dataSource.next(this.rows.controls);
  }

  reset() {
    let i = 0;

    this.rows.value.forEach((ro: any) => { ro.serialNumber = ++i });
    console.log(this.rows.value);
    this.rows.patchValue(this.rows.value);
  }

 


  onItemSelect(item: any, type: string) {
    if (type == 'CLIENT') {
      this.multimatterList = [];
      this.form.controls.matterNumber.patchValue('');
      console.log(this.multiselectmatterList)
      this.multiselectmatterList.forEach((data: any) => {
        if (data['value'].includes(this.form.controls.clientId.value)) {
          this.multimatterList.push(data)
        }
      })
    } else {
      this.get_matterdetails(this.form.controls.matterNumber.value);
    }
    console.log(this.multimatterList);
  }
  OnItemDeSelect(item: any) {
    console.log(item);
    console.log(this.selectedItems3);
  }
  onSelectAll(items: any) {
    console.log(items);
  }
  onDeSelectAll(items: any) {
    console.log(items);
  }
  change(item: any) {
    console.log(item)
  }

}
