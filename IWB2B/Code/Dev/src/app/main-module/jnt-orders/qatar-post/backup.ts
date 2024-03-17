import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DomSanitizer } from '@angular/platform-browser';
import { NgxSpinnerService } from 'ngx-spinner';
import { MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { JntOrdersService } from '../jnt-orders.service';

@Component({
  selector: 'app-qatar-post',
  templateUrl: './qatar-post.component.html',
  styleUrls: ['./qatar-post.component.scss']
})
export class QatarPostComponent implements OnInit {

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
    this.jntOrders = [];
    this.sub.add(this.service.getAllOrder('QATARPOST').subscribe((res: any[]) => {

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

  genrateLabel(jntOrders){
    // if (this.selectedjntOrders.length === 0) {
    //   this.messageService.add({key: 'br', severity:'info', summary:'Warning', detail:  "Kindly Select any row"});
    //   return;
    // }
    this.spin.show();
    this.service.getLabel(jntOrders.awb_3rd_Party).subscribe(res => {
      window.open(res.data, '_blank');
      this.spin.hide();
    },err => {
      this.spin.hide();      
this.cs.commonerror(err);
    })
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
        "Event":x.action_name,
        "Event Time": this.cs.dateapi(x.action_time),
      });
  
    })
    this.cs.exportAsExcel(res, "JNT Orders");
  }


  docurl: any;
  fileUrldownload: any;


  async bulkLabel(){
    let  selectedLabel : any[] = [];
    this.selectedjntOrders.forEach(x => {
      if(x.awb_3rd_Party != null){
        selectedLabel.push(x.awb_3rd_Party);
      }
    })
    let obj: any= {};
    obj.billCodes = selectedLabel;
    this.spin.show()
    const blob = await this.service.getBulkLabel(obj)
      .catch((err: HttpErrorResponse) => {
        this.cs.commonerror(err);
      });
    this.spin.hide();
    if (blob) {
      const blobOb = new Blob([blob], {
        type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
      });
      this.fileUrldownload = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blobOb));
      // http://remote.url.tld/path/to/document.doc&embedded=true
      this.docurl = window.URL.createObjectURL(blob);
      const a = document.createElement('a')
      a.href = this.docurl
      a.download = 'JNTBillCode' + `_${new Date().getDate() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getFullYear()  + '_' +this.cs.timeFormat(new Date())}`;
      a.click();
      URL.revokeObjectURL(this.docurl);
    }
   // window.location.reload();
    this.spin.hide();
  }
  
  onTableHeaderCheckboxToggle(E){
    console.log(E);
    //console.log(Paginator)
  }
}
