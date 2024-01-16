import { SelectionModel } from "@angular/cdk/collections";
import { DecimalPipe } from "@angular/common";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { ShowStringPopupComponent } from "src/app/common-field/dialog_modules/show-string-popup/show-string-popup.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { GeneralMatterService } from "src/app/main-module/matters/case-management/General/general-matter.service";
import { PrebillService } from "../prebill.service";
import { TimeTicketNewComponent } from "./time-ticket-new/time-ticket-new.component";
export interface expense {
  ecode: string;
  edescription: string;
  eamount: string;
}

const ELEMENT_DATA1: expense[] = [
  { ecode: "test", edescription: 'test', eamount: 'Test1', }
];

@Component({
  selector: 'app-prebill-aaprove',
  templateUrl: './prebill-aaprove.component.html',
  styleUrls: ['./prebill-aaprove.component.scss']
})
export class PrebillAaproveComponent implements OnInit {
  //screenid: 1128 | undefined;
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  //checked = false;
  toggle = true;
  matterdescription: any;
  paymentApprovalData: any;
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
  showFiller = false;
  animal: string | undefined;
  id: string | undefined;

  filter = true;
  expense = false;
  showfilter() {
    this.filter = !this.filter;
  }
  showexpense() {
    this.expense = !this.expense;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  constructor(private fb: FormBuilder,
    private auth: AuthService,
    private service: PrebillService, public dialog: MatDialog,
    public toastr: ToastrService, private serviceMatter: GeneralMatterService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private decimalPipe: DecimalPipe,
    private route: ActivatedRoute, private router: Router,

    private cs: CommonService,) { }

  sub = new Subscription();
  paymentPlan: any = null;
  ngOnInit(): void {

    let code = this.route.snapshot.params.code;

    if (code != 'new') {
      let js = this.cs.decrypt(code);
      this.paymentApprovalData = js
      this.paymentPlan = js.code;
      console.log(this.paymentPlan.matterNumber)
      this.paymentPlan.preBillDate = this.cs.dateapi(this.paymentPlan.preBillDate);
    }

    this.sub.add(this.serviceMatter.Search({ matterNumber: [this.paymentPlan.matterNumber] }).subscribe((res) => {
      this.matterdescription = res[0].matterDescription
      console.log(this.matterdescription)
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
    this.dropdownlist();
    this.gettimeticketpreBillNo();
    this.getexpensepreBillNo();





  }

  taskCodelist: any[] = [];
  activityCodelist: any[] = [];

  multiselectactivityList: any[] = [];
  multiactivityList: any[] = [];

  multiselecttaskList: any[] = [];
  multitaskList: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.taskbasedCode.url,
    this.cas.dropdownlist.setup.activityCode.url,
    ]).subscribe((results: any) => {
      this.spin.hide();
      this.taskCodelist = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.taskbasedCode.key);
      this.taskCodelist.forEach((x: { key: string; value: string; }) => this.multitaskList.push({ value: x.key, label: x.value }))
      this.multiselecttaskList = this.multitaskList;

      this.activityCodelist = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.activityCode.key);
      this.activityCodelist.forEach((x: { key: string; value: string; }) => this.multiactivityList.push({ value: x.key, label: x.value }))
      this.multiselectactivityList = this.multiactivityList;

    })
  }
  displayedColumns1: string[] = ['sno', 'expenseCode', 'expenseno', 'expensedate', 'expenseDescription', 'expenseAmount', 'expenseType'];
  dataSource1 = new MatTableDataSource<any>([]);
  displayedColumns: string[] = ['select', 'timeKeeperCode', 'stimeTicketDate', 'billType', 'taskCode','activityCode','approvedHours', 'approvedAmount', 'statusId', 'approvedDescription',];
  dataSource = new MatTableDataSource<any>([]);
  selection = new SelectionModel<any>(true, []);

  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination
  timekeeperCodelist: any[] = [];
  statuslist: any[] = [];

  gettimeticketpreBillNo() {

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.timeKeeperCode.url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      this.timekeeperCodelist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.timeKeeperCode.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.gettimeticketpreBillNo(this.paymentPlan.preBillNumber).subscribe((res: any[]) => {
        res.forEach((x) => {
          x.billType = x.billType.toLowerCase()
          x.approvedDescription = x.timeTicketDescription;
          x.approvedHours = x.timeTicketHours;
          //x.approvedAmount = (Math.round(x.timeTicketAmount * 100) / 100);
          // x.approvedAmount = this.decimalPipe.transform(x.timeTicketAmount, "1.2-2");
          x.approvedAmount = x.approvedBillableAmount != null ? x.approvedBillableAmount : x.timeTicketAmount,
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
          x.timeKeeperCode = this.timekeeperCodelist.find(y => y.key == x.timeKeeperCode)?.key;
          if (x.billType == "non-billable" || x.billType == "nocharge") {
            x.approvedAmount = 0;
          }

          if (x.statusId == 56) {
           // let description = decodeURIComponent(x.referenceField4);
            x.approvedHours = x.approvedBillableTimeInHours;
            x.approvedAmount = x.approvedBillableAmount;
            x.approvedDescription = x.referenceField4
          }
          if (x.statusId == 46) {
             x.approvedHours = x.approvedBillableTimeInHours;
             x.approvedAmount = x.approvedBillableAmount;
           }

          //trim description
          if (x.timeTicketDescription) {
            x.trimmedTimeTicketDescription = this.trimTimeTicketDescription(x.timeTicketDescription);
          }

          if (x.approvedDescription) {
            x.trimmedApprovedDescription = this.trimApprovedDescription(x.timeTicketDescription);
          }
        })

        this.paymentPlan.statusId = this.statuslist.find(y => y.key == this.paymentPlan.statusId)?.value;
        this.dataSource = new MatTableDataSource<any>(res);
        console.log(this.dataSource.data.length)
        this.selection = new SelectionModel<any>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;

        // this.masterToggle();
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
  }
  getexpensepreBillNo() {
    // alert(2)
    this.spin.show();
    this.sub.add(this.service.getexpensepreBillNo(this.paymentPlan.preBillNumber).subscribe((res: any[]) => {

      res.forEach((element: any) => {
        element['positiveExpenseAmount'] = (element.expenseAmount < 0 ? element.expenseAmount * -1 : element.expenseAmount)
      })


      this.dataSource1 = new MatTableDataSource<any>(res);
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }



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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.position + 1}`;
  }
  clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  back() {
    this.router.navigate(['/main/accounts/prebilllist/Approve']);
  }



  approve() {

    if (this.selection.selected.length === 0 && this.dataSource.data.length > 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    if (this.isAllSelected() != true) {
      this.toastr.error("Kindly select all rows to approve", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }

    //   let i = 1;
    //  if(this.dataSource.data.length == 0){
    //     this.sub.add(this.service.Approve(
    //       this.paymentPlan.preBillNumber,  0,0,null,this.paymentPlan.matterNumber,
    //       this.paymentPlan.preBillNumber, 0,'billable').subscribe(res => {
    //         this.toastr.success(this.paymentPlan.preBillNumber + " approved successfully!", "Notification", {
    //           timeOut: 2000,
    //           progressBar: false,
    //         });
    //         this.spin.hide();

    //           this.router.navigate(['/main/accounts/prebilllist/Approve']);

    //       }, err => {
    //         this.cs.commonerror(err);
    //         this.spin.hide();

    //       }));
    //  }else{
    //   this.selection.selected.forEach((x: any) => {
    //     this.sub.add(this.service.Approve(
    //       this.paymentPlan.preBillNumber,
    //       x.approvedAmount,
    //       x.approvedHours,
    //       x.approvedDescription,
    //       this.paymentPlan.matterNumber,
    //       this.paymentPlan.preBillNumber,
    //       x.timeTicketNumber,x.billType).subscribe(res => {
    //         this.toastr.success(this.paymentPlan.preBillNumber + " approved successfully!", "Notification", {
    //           timeOut: 2000,
    //           progressBar: false,
    //         });
    //         this.spin.hide();

    //         if (i == this.selection.selected.length)
    //           this.router.navigate(['/main/accounts/prebilllist/Approve']);
    //         i++;
    //       }, err => {
    //         this.cs.commonerror(err);
    //         this.spin.hide();

    //       }));
    //   });
    //  }
    let objArray: any[] = [];
    this.selection.selected.forEach((x: any) => {
      let obj: any = {};
      obj.approvedBillAmount = x.approvedAmount;
      obj.approvedBillTime = x.approvedHours;
      obj.approvedDescription = x.approvedDescription;
      obj.billType = x.billType;
      obj.matterNumber = x.matterNumber;
      obj.preBillNumber = this.paymentPlan.preBillNumber;
      obj.timeTicketNumber = x.timeTicketNumber;
      obj.taskCode = x.taskCode;
      obj.activityCode = x.activityCode;
      objArray.push(obj);
    });
    
    //if (objArray.length > 0) {
      this.spin.show();
      this.sub.add(this.service.Approve(objArray).subscribe(res => {
        this.spin.hide();
        this.toastr.success(this.paymentPlan.preBillNumber + " approved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.router.navigate(['/main/accounts/prebilllist/Approve']);
      }, err => {
        this.spin.hide();
        this.cs.commonerror(err);
      }));
 //   }
  }

  save() {

    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }


    // let i = 1;
    // this.selection.selected.forEach((x: any) => {
    //   this.sub.add(this.service.save(
    //     this.paymentPlan.preBillNumber,
    //     x.approvedAmount,
    //     x.approvedHours,
    //     x.approvedDescription,
    //     this.paymentPlan.matterNumber,
    //     this.paymentPlan.preBillNumber,
    //     x.timeTicketNumber, x.billType).subscribe(res => {
    //       this.toastr.success(this.paymentPlan.preBillNumber + " saved successfully!", "Notification", {
    //         timeOut: 2000,
    //         progressBar: false,
    //       });
    //       this.spin.hide();

    //       if (i == this.selection.selected.length)
    //         this.router.navigate(['/main/accounts/prebilllist/Approve']);
    //       i++;
    //     }, err => {
    //       this.cs.commonerror(err);
    //       this.spin.hide();

    //     }));
    // });

    let objArray: any[] = [];
    this.selection.selected.forEach((x: any) => {
      let obj: any = {};
      obj.approvedBillAmount = x.approvedAmount;
      obj.approvedBillTime = x.approvedHours;
      obj.approvedDescription = x.approvedDescription;
      obj.billType = x.billType;
      obj.matterNumber = x.matterNumber;
      obj.preBillNumber = this.paymentPlan.preBillNumber;
      obj.timeTicketNumber = x.timeTicketNumber;
      obj.taskCode = x.taskCode;
      obj.activityCode = x.activityCode;
      objArray.push(obj);
    });

    if (objArray.length > 0) {
      this.sub.add(this.service.save(objArray).subscribe(res => {
        this.spin.hide();
        this.toastr.success(this.paymentPlan.preBillNumber + " saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.router.navigate(['/main/accounts/prebilllist/Approve']);
      }, err => {
        this.spin.hide();
        this.cs.commonerror(err);
      }));
    }
  }



  openStringPopup(description: any, type, title, index?) {
    const dialogRef = this.dialog.open(ShowStringPopupComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { link: description, type: type, title: title }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result.trim() != "" && type == 'TIME_TICKET_APPROVED_DESCRIPTION') {
        this.dataSource.data[index].approvedDescription = result;
        this.dataSource.data[index].trimmedApprovedDescription = this.trimApprovedDescription(result);
      }
    });
  }

  expenseAmount() {
    let total = 0;
    let positiveTotal = 0;
    this.dataSource1.data.forEach(element => {
      // console.log(element)
      total = total + (element.expenseAmount != null ? element.expenseAmount : 0);
      positiveTotal = (total < 0 ? total * -1 : total)
      return positiveTotal;
    })
    return (Math.round(positiveTotal * 100) / 100);
  }

  getapprovedamount() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.approvedAmount != null ? element.approvedAmount : 0);
    })
    return (Math.round(total * 100) / 100);
  }
  getapprovedhrs() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.approvedHours != null ? element.approvedHours : 0);
      //    console.log(total)
    })
    return (Math.round(total * 100) / 100);
  }
  getbookedhrs() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.timeTicketHours != null ? element.timeTicketHours : 0);
    })
    return (Math.round(total * 100) / 100);
  }
  getbookedamt() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.timeTicketAmount != null ? element.timeTicketAmount : 0);
    })
    return (Math.round(total * 100) / 100);
  }
  approvedhrscalculation() {
    let result
    this.dataSource.data.forEach(element => {
      console.log(element.timeTicketHours)
      console.log(element.timeTicketAmount)
      element.approvedAmount = Math.round(((element.approvedHours / element.timeTicketHours) * element.timeTicketAmount) * 100) / 100;
      if (element.billType == "non-billable" || element.billType == "nocharge") {
        element.approvedAmount = 0;
      }
      if (element.billType == "billable" && element.timeTicketHours == 0 && element.timeTicketAmount == 0) {
        element.approvedAmount = (element.approvedHours) * (element.assignedRatePerHour); 
      }
    })
    // this.checked = true;
  }

  trimTimeTicketDescription(timeTicketDescription) {
    let splitTimeTicketDescription = timeTicketDescription.split(" ");
    if (splitTimeTicketDescription.length > 2) {
      return (splitTimeTicketDescription[0] + " " + splitTimeTicketDescription[1]);
    } else {
      return timeTicketDescription;
    }
  }


  trimApprovedDescription(approvedDescription) {
    let splitApprovedDescription = approvedDescription.split(" ");
    if (splitApprovedDescription.length > 2) {
      return (splitApprovedDescription[0] + " " + splitApprovedDescription[1]);
    } else {
      return approvedDescription;
    }
  }

  billtypeChange() {
    this.dataSource.data.forEach(element => {
      console.log(element.approvedBillableTimeInHours);
      if (element.billType == "non-billable" || element.billType == "nocharge") {
        element.approvedAmount = 0;
      }
      if (element.billType == "billable" && element.timeTicketHours != 0 && element.timeTicketAmount != 0) {
        // element.approvedAmount = Math.round(((element.approvedHours / element.timeTicketHours) * element.timeTicketAmount) * 100) / 100;
   //     element.approvedAmount = Math.round((element.assignedRatePerHour != null ? element.assignedRatePerHour : 0) * element.approvedBillableTimeInHours);
       // element.approvedAmount = this.decimalPipe.transform((element.assignedRatePerHour != null ? element.assignedRatePerHour : 0) * element.approvedBillableTimeInHours, "1.2-2");
       element.approvedAmount = ((element.assignedRatePerHour != null ? element.assignedRatePerHour : 0) * element.approvedBillableTimeInHours);
      }
      if (element.billType == "billable" && element.timeTicketHours == 0 && element.timeTicketAmount == 0) {
        element.approvedAmount = (element.approvedHours) * (element.assignedRatePerHour); 
      }
    })
    // this.checked=true;
  }

  ondecscription() {
    this.dataSource.data.forEach(element => {
      if (element.approvedDescription.length > 0) {
        // this.checked=true;
      }
    })
  }


  createTimeTicket(){
    const dialogRef = this.dialog.open(TimeTicketNewComponent, {
      width: '70%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: {approval: this.paymentApprovalData, lines: this.dataSource.data.length >= 0 ? this.dataSource.data[0] : null}
    });
    dialogRef.afterClosed().subscribe(result => {
      if(result){
        let obj: any = {};
        obj.approvedAmount = result.approvedBillableAmount;
        obj.approvedHours = result.approvedBillableTimeInHours;
        obj.approvedBillableTimeInHours = result.approvedBillableTimeInHours;
        obj.approvedDescription = result.timeTicketDescription;
        obj.billType = result.billType;
        obj.matterNumber = result.matterNumber;
        obj.preBillNumber = result.referenceField1;
        obj.timeTicketNumber = result.timeTicketNumber;
        obj.taskCode = result.taskCode;
        obj.stimeTicketDate = result.stimeTicketDate
        obj.activityCode = result.activityCode;
        obj.timeKeeperCode = result.timeKeeperCode;
        obj.statusIddes = this.statuslist.find(y => y.key == result.statusId)?.value;
        obj.assignedRatePerHour = result.defaultRate;
        obj.timeTicketHours =  0;
        obj.timeTicketAmount =  0;
        this.dataSource.data.push(obj);
        this.dataSource._updateChangeSubscription();
      }
    });
  }

}

