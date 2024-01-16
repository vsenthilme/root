import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { InhouseTransferService } from "../inhouse-transfer.service";
import { InhouseLinesComponent } from "./inhouse-lines/inhouse-lines.component";
import { Table } from "primeng/table";

export interface variant {


  no: string;
  actions: string;
  status: string;
  warehouseno: string;
  preinboundno: string;
  refdocno: string;
  type: string;
  ttype: string;
}
const ELEMENT_DATA: variant[] = [
  { no: "1", warehouseno: 'readonly', type: 'readonly', ttype: 'readonly', refdocno: 'readonly', preinboundno: 'readonly', status: 'readonly', actions: 's' },
  { no: "2", warehouseno: 'readonly', type: 'readonly', ttype: 'readonly', refdocno: 'readonly', preinboundno: 'readonly', status: 'readonly', actions: 's' },
  { no: "3", warehouseno: 'readonly', type: 'readonly', ttype: 'readonly', refdocno: 'readonly', preinboundno: 'readonly', status: 'readonly', actions: 's' },
  { no: "4", warehouseno: 'readonly', type: 'readonly', ttype: 'readonly', refdocno: 'readonly', preinboundno: 'readonly', status: 'readonly', actions: 's' },
  { no: "5", warehouseno: 'readonly', type: 'readonly', ttype: 'readonly', refdocno: 'readonly', preinboundno: 'readonly', status: 'readonly', actions: 's' },
  { no: "6", warehouseno: 'readonly', type: 'readonly', ttype: 'readonly', refdocno: 'readonly', preinboundno: 'readonly', status: 'readonly', actions: 's' },
  { no: "7", warehouseno: 'readonly', type: 'readonly', ttype: 'readonly', refdocno: 'readonly', preinboundno: 'readonly', status: 'readonly', actions: 's' },
  { no: "8", warehouseno: 'readonly', type: 'readonly', ttype: 'readonly', refdocno: 'readonly', preinboundno: 'readonly', status: 'readonly', actions: 's' },
  { no: "9", warehouseno: 'readonly', type: 'readonly', ttype: 'readonly', refdocno: 'readonly', preinboundno: 'readonly', status: 'readonly', actions: 's' },
  { no: "10", warehouseno: 'readonly', type: 'readonly', ttype: 'readonly', refdocno: 'readonly', preinboundno: 'readonly', status: 'readonly', actions: 's' },

];
@Component({
  selector: 'app-inhouse-main',
  templateUrl: './inhouse-main.component.html',
  styleUrls: ['./inhouse-main.component.scss']
})
export class InhouseMainComponent implements OnInit {
  screenid= 3057 ;
  inhouse: any[] = [];
  selectedinhouse : any[] = [];
  @ViewChild('inhouseTag') inhouseTag: Table | any;
  constructor(private service: InhouseTransferService,
    public toastr: ToastrService, public dialog: MatDialog,
    private spin: NgxSpinnerService, private router: Router,
    private auth: AuthService,
    private fb: FormBuilder,
    public cs: CommonService,) { }
  sub = new Subscription();
  RA: any = {};
  ngOnInit(): void {
    this.RA = this.auth.getRoleAccess(this.screenid);
    this.search();
  }

  isShowDiv = false;
  public icon = 'expand_more';
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
    console.log('show:' + this.showFloatingButtons);
  }

 



 
  

  
 
  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.inhouse.forEach(x => {
      res.push( {
  
        "Status ": this.cs.getstatus_text(x.statusId),
          'Transfer No': x.transferNumber,
          ' Transfer Date': this.cs.dateExcel(x.createdOn),
          "Transfer Type": x.transferTypeId,
          " Transferred By ": x.createdBy,
          // 'Created By': x.createdBy,
          // 'Date': this.cs.dateapi(x.createdOn),
        });

    })
    this.cs.exportAsExcel(res, "Inhouse Transfer");
  }


  searhform = this.fb.group({

  });

  warehouseId=this.auth.warehouseId
  search() {
    this.sub.add(this.service.GetAll().subscribe(res => {
      let result = res.filter((x: any) =>   x.warehouseId == this.warehouseId);
      this.spin.hide();

      this.inhouse =result;
    

    }, err => {

      this.cs.commonerrorNew(err);
      this.spin.hide();

    }));
  }
  openDialog(data: any = 'new'): void {
    if (data != 'New')
      if (this.selectedinhouse.length === 0) {
        this.toastr.error("Kindly select any row", "Notification",{
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }

    if (this.selectedinhouse[0].statusId == 24) {
      this.toastr.error("Order can't be edited as it's already confirmed.", "",{
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    let paramdata = "";

    if (this.selectedinhouse.length > 0) {
      paramdata = this.cs.encrypt({ code: this.selectedinhouse[0], pageflow: data });
      this.router.navigate(['/main/inbound/inbound-create/' + paramdata]);
    }
    else {
      paramdata = this.cs.encrypt({ pageflow: data });
      this.router.navigate(['/main/inbound/cinbound-create/' + paramdata]);
    }
  }


  openLines(element): void {
    const dialogRef = this.dialog.open(InhouseLinesComponent, {
      disableClose: true,
      width: '82%',
      maxWidth: '90%',
      data: element.transferNumber,
    });
  
    dialogRef.afterClosed().subscribe(result => {
    });
  }
  onChange() {
    const choosen= this.selectedinhouse[this.selectedinhouse.length - 1];   
    this.selectedinhouse.length = 0;
    this.selectedinhouse.push(choosen);
  }
}

