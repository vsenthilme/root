import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DomSanitizer } from '@angular/platform-browser';
import { NgxSpinnerService } from 'ngx-spinner';
import { MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { JntOrdersService } from '../../jnt-orders/jnt-orders.service';
import { TrackingPopupComponent } from './tracking-popup/tracking-popup.component';

@Component({
  selector: 'app-tracking',
  templateUrl: './tracking.component.html',
  styleUrls: ['./tracking.component.scss']
})
export class TrackingComponent implements OnInit {

 
  @ViewChild('userProfile') userProfile: Table | undefined;
  jntOrders: any[] = [];
  selectedjntOrders : any[] = [];
  advanceFilterShow: boolean;

  constructor(public dialog: MatDialog, private service: JntOrdersService,  private spin: NgxSpinnerService, private cs: CommonService,
    private messageService: MessageService,    private sanitizer: DomSanitizer,) { 
    
  }
  sub = new Subscription();
  ngOnInit(): void {
    this.getAll();
  }

  filterResult: any[] = [];
  getAll(){
    this.spin.show();
    this.sub.add(this.service.getAll().subscribe((res: any[]) => {

      res.forEach(x => {
       x['printStatus'] = x.is_awb_printed == true && x.awb_3rd_Party ? 'Y' : x.is_awb_printed == null && x.awb_3rd_Party ? 'N' : '';

       if(x.orderType == "1"){
        this.filterResult.push(x);
       }

      })
      
      this.jntOrders = this.filterResult;
      this.selectedjntOrders = [];
      this.spin.hide();
    }, err => {
      this.spin.hide();      
this.cs.commonerror(err);
    }));

  }

  



  advanceFilter(){
    this.advanceFilterShow = !this.advanceFilterShow;
  }
  

  onChange() {
    const choosen= this.selectedjntOrders[this.selectedjntOrders.length - 1];   
    this.selectedjntOrders.length = 0;
    this.selectedjntOrders.push(choosen);
  } 


  applyFilterGlobal($event: any, stringVal: any) {
    this.userProfile!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }


  
  downloadExcel() {
    var res: any = [];
    this.jntOrders.forEach(x => {
      res.push({
        "Shipsy AWB No":x.reference_number,
        "Customer Code ":x.customer_code,
        "Hub Code ": 'JNT',
        "JNT Bill Code ":x.awb_3rd_Party,
        "Created On": this.cs.dateapi(x.created_at),
        "Event":x.scanType,
      });
  
    })
    this.cs.exportAsExcel(res, "Tracking");
  }


  docurl: any;
  fileUrldownload: any;


  showTracking(data){
      const dialogRef = this.dialog.open(TrackingPopupComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '35%',
        position: { top: '6.5%' },
        data: { code: data ? data : null }
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
  
  }
  
}

