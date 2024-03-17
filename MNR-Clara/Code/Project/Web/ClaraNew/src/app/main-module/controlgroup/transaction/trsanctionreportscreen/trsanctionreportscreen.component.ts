import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router, ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { ApprovalService } from '../approval/approval.service';
import { OwnershipService } from '../ownership/ownership.service';
import { SummaryService } from '../summary/summary.service';
import { Subscription } from 'rxjs';
import { SelectionModel } from '@angular/cdk/collections';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Table } from 'primeng/table';

@Component({
  selector: 'app-trsanctionreportscreen',
  templateUrl: './trsanctionreportscreen.component.html',
  styleUrls: ['./trsanctionreportscreen.component.scss']
})
export class TrsanctionreportscreenComponent implements OnInit {
  expandedElement: any | null;

  toggleExpansion(element: any): void {
    console.log(element);
      this.expandedElement = element;
  }
  Transaction: any[] = [];
  selectedTransaction : any[] = [];
  @ViewChild('transactionTag') transactionTag: Table | any;
  public icon = 'expand_more';
  constructor(public dialog: MatDialog,
    private service: OwnershipService,
   // private storePartnerListring: ApprovalService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private router: Router,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService,
    private summary: SummaryService,
    private route: ActivatedRoute) {}
    sub = new Subscription();
    ELEMENT_DATA: any[] = [];
   
    @ViewChild(MatSort, {
      static: true
    })
    sort: MatSort;
    @ViewChild(MatPaginator, {
      static: true
    })
    
    paginator: MatPaginator; // Pagination
    isShowDiv = false;
    showFloatingButtons: any;
    toggle = true;
    displayedColumns: string[] = ['sno', 'groupId', 'groupName'];
    displayedColumns1: string[] = ['storeId', 'storeName'];
    noOfStores:any;
    ngOnInit(): void {
      this.spin.show();
      let obj: any = {};
      this.summary.reportstorepartner(obj).subscribe(
          (res) => {
              console.log(res);
              if (res ) {
      
                  this.Transaction =res;
               
              } else {

                  console.error('groupId is null in the response.');
          
              }
  
              this.spin.hide();
          },
          (err) => {
              this.cs.commonerrorNew(err);
              this.spin.hide();
          }
      );
  }
  onChange() {
    const choosen= this.selectedTransaction[this.selectedTransaction.length - 1];   
    this.selectedTransaction.length = 0;
    this.selectedTransaction.push(choosen);
  }
  toggleFloat() {
    this.isShowDiv = !this.isShowDiv;
    this.toggle = !this.toggle;

    if (this.icon === 'expand_more') {
      this.icon = 'chevron_left';
    } else {
      this.icon = 'expand_more';
    }
    this.showFloatingButtons = !this.showFloatingButtons;
  }
  showFiller = false;
  animal: string | undefined;
}