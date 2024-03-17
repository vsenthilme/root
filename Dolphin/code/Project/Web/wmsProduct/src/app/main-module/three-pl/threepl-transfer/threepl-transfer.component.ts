import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';


const ELEMENT_DATA1:  any[] = 
[
  {
   "customer": 10001,
   "module": "Inbound",
   "orderNo": "ASN000001070",
   "sku": "0057510968",
   "description": "FRESH CONTAINER 1Ltr 3pc RECT055010",
   "transactionQty": 252,
   "transactionDate": "08-05-2023",
   "cbmQty": 0.05,
   "chargeValue": 12.6,
   "chargeUnit": "CBM",
   "serviceType": "2-Handling charges",
   "rateUnit": 80,
   "totalValue": 1008
  },
  {
   "customer": 10001,
   "module": "Inbound",
   "orderNo": "ASN000001070",
   "sku": "0057517341",
   "description": "CONTAINER AROMA VEGETABLES 4Ltr",
   "transactionQty": 540,
   "transactionDate": "11-05-2023",
   "cbmQty": 0.07,
   "chargeValue": 37.8,
   "chargeUnit": "CBM",
   "serviceType": "2-Handling charges",
   "rateUnit": 60,
   "totalValue": 2268
  },
  {
   "customer": 10001,
   "module": "Inbound",
   "orderNo": "ASN000001070",
   "sku": "005751262",
   "description": "ICE LOLLY POP Pk(1)020560\/020520\/020580",
   "transactionQty": 744,
   "transactionDate": "11-05-2023",
   "cbmQty": 0.02,
   "chargeValue": 14.88,
   "chargeUnit": "CBM",
   "serviceType": "2-Handling charges",
   "rateUnit": 80,
   "totalValue": 1190.4
  },
  {
   "customer": 10001,
   "module": "Inbound",
   "orderNo": "ASN000001064",
   "sku": "0057511926",
   "description": "BANANA SAVER021269\/000180",
   "transactionQty": 800,
   "transactionDate": "12-05-2023",
   "cbmQty": 0.01,
   "chargeValue": 8,
   "chargeUnit": "CBM",
   "serviceType": "2-Handling charges",
   "rateUnit": 60,
   "totalValue": 480
  },
  {
   "customer": 10001,
   "module": "Inbound",
   "orderNo": "ASN000001064",
   "sku": "0057517698",
   "description": "SAPONELLO SOAP DISPENSER WHT\/GREY",
   "transactionQty": 900,
   "transactionDate": "14-05-2023",
   "cbmQty": 0.03,
   "chargeValue": 27,
   "chargeUnit": "CBM",
   "serviceType": "2-Handling charges",
   "rateUnit": 80,
   "totalValue": 2160
  },
  {
   "customer": 10001,
   "module": "Inbound",
   "orderNo": "ASN000001064",
   "sku": "0057517055",
   "description": "LEMON KEEPER",
   "transactionQty": 900,
   "transactionDate": "18-05-2023",
   "cbmQty": 0.02,
   "chargeValue": 18,
   "chargeUnit": "CBM",
   "serviceType": "2-Handling charges",
   "rateUnit": 80,
   "totalValue": 1440
  },
  {
   "customer": 10001,
   "module": "Inbound",
   "orderNo": "ASN000001064",
   "sku": "0057517609",
   "description": "SALAD SPINNER WASH&DRY SALAD",
   "transactionQty": 180,
   "transactionDate": "20-05-2023",
   "cbmQty": 0.15,
   "chargeValue": 27,
   "chargeUnit": "CBM",
   "serviceType": "2-Handling charges",
   "rateUnit": 60,
   "totalValue": 1620
  },
  {
   "customer": 10001,
   "module": "Storage",
   "sku": "0057510968",
   "description": "FRESH CONTAINER 1Ltr 3pc RECT055010",
   "transactionQty": 252,
   "cbmQty": 0.05,
   "chargeValue": 12.6,
   "chargeUnit": "CBM",
   "serviceType": "4-storage charges",
   "rateUnit": 25,
   "totalValue": 315
  },
  {
   "customer": 10001,
   "module": "Storage",
   "sku": "0057517341",
   "description": "CONTAINER AROMA VEGETABLES 4Ltr",
   "transactionQty": 540,
   "cbmQty": 0.07,
   "chargeValue": 37.8,
   "chargeUnit": "CBM",
   "serviceType": "4-storage charges",
   "rateUnit": 25,
   "totalValue": 945
  },
  {
   "customer": 10001,
   "module": "Storage",
   "sku": "005751262",
   "description": "ICE LOLLY POP Pk(1)020560\/020520\/020580",
   "transactionQty": 744,
   "cbmQty": 0.02,
   "chargeValue": 14.88,
   "chargeUnit": "CBM",
   "serviceType": "4-storage charges",
   "rateUnit": 25,
   "totalValue": 372
  },
  {
   "customer": 10001,
   "module": "Storage",
   "sku": "0057511926",
   "description": "BANANA SAVER021269\/000180",
   "transactionQty": 804,
   "cbmQty": 0.01,
   "chargeValue": 8.04,
   "chargeUnit": "CBM",
   "serviceType": "4-storage charges",
   "rateUnit": 25,
   "totalValue": 201
  },
  {
   "customer": 10001,
   "module": "Storage",
   "sku": "0057517698",
   "description": "SAPONELLO SOAP DISPENSER WHT\/GREY",
   "transactionQty": 900,
   "cbmQty": 0.03,
   "chargeValue": 27,
   "chargeUnit": "CBM",
   "serviceType": "4-storage charges",
   "rateUnit": 25,
   "totalValue": 675
  },
  {
   "customer": 10001,
   "module": "Storage",
   "sku": "0057517055",
   "description": "LEMON KEEPER",
   "transactionQty": 900,
   "cbmQty": 0.02,
   "chargeValue": 18,
   "chargeUnit": "CBM",
   "serviceType": "4-storage charges",
   "rateUnit": 25,
   "totalValue": 450
  },
  {
   "customer": 10001,
   "module": "Storage",
   "sku": "0057517609",
   "description": "SALAD SPINNER WASH&DRY SALAD",
   "transactionQty": 180,
   "cbmQty": 0.15,
   "chargeValue": 27,
   "chargeUnit": "CBM",
   "serviceType": "4-storage charges",
   "rateUnit": 25,
   "totalValue": 675
  },
  {
   "customer": 10001,
   "module": "Storage",
   "sku": "005759569",
   "description": "CHESTNUT CUTTER020490\/020491\/020493",
   "transactionQty": 200,
   "cbmQty": 0.18,
   "chargeValue": 36,
   "chargeUnit": "CBM",
   "serviceType": "4-storage charges",
   "rateUnit": 25,
   "totalValue": 900
  },
  {
   "customer": 10001,
   "module": "Picking",
   "orderNo": "TO-080857",
   "sku": "020304420",
   "description": "GARBAGE BAG 45x55CM ASSTD COLOR8009",
   "transactionQty": 200,
   "transactionDate": "23-05-2023",
   "chargeUnit": "QTY",
   "serviceType": "9-Picking Charges",
   "rateUnit": 2,
   "totalValue": 400
  },
  {
   "customer": 10001,
   "module": "Picking",
   "orderNo": "TO-080960",
   "sku": "0203013102",
   "description": "NON-WOVEN BAG SMALL",
   "transactionQty": 200,
   "transactionDate": "13-05-2023",
   "chargeUnit": "QTY",
   "serviceType": "9-Picking Charges",
   "rateUnit": 5,
   "totalValue": 1000
  },
  {
   "customer": 10001,
   "module": "Picking",
   "orderNo": "TO-080653",
   "sku": "011671276",
   "description": "TISSUE HOLDER1141",
   "transactionQty": 216,
   "transactionDate": "19-05-2023",
   "chargeUnit": "QTY",
   "serviceType": "9-Picking Charges",
   "rateUnit": 2,
   "totalValue": 432
  },
  {
   "customer": 10001,
   "module": "Picking",
   "orderNo": "TO-081446",
   "sku": "0203013287",
   "description": "5pc KIDS HANGER SET PINK",
   "transactionQty": 240,
   "transactionDate": "24-05-2023",
   "chargeUnit": "QTY",
   "serviceType": "9-Picking Charges",
   "rateUnit": 3,
   "totalValue": 720
  },
  {
   "customer": 10001,
   "module": "Picking",
   "orderNo": "TO-080299",
   "sku": "21300073015",
   "description": "PHILIPS BIT #2 2FRV P2\/98660",
   "transactionQty": 240,
   "transactionDate": "04-05-2023",
   "chargeUnit": "QTY",
   "serviceType": "9-Picking Charges",
   "rateUnit": 5,
   "totalValue": 1200
  },
  {
   "customer": 10001,
   "module": "Picking",
   "orderNo": "TO-081273",
   "sku": "0203011529",
   "description": "30WET SWEEPER CLOTHES REFILLS",
   "transactionQty": 270,
   "transactionDate": "22-05-2023",
   "chargeUnit": "QTY",
   "serviceType": "9-Picking Charges",
   "rateUnit": 5,
   "totalValue": 1350
  },
  {
   "customer": 10001,
   "module": "Picking",
   "orderNo": "TO-080492",
   "sku": "0203012838",
   "description": "METAL STORAGE BOX  ROUND RED",
   "transactionQty": 300,
   "transactionDate": "11-05-2023",
   "chargeUnit": "QTY",
   "serviceType": "9-Picking Charges",
   "rateUnit": 2,
   "totalValue": 600
  },
  {
   "customer": 10001,
   "module": "Picking",
   "orderNo": "TO-081446",
   "sku": "020307402",
   "description": "5pc KIDS HANGER SET-BLUE",
   "transactionQty": 320,
   "transactionDate": "24-05-2023",
   "chargeUnit": "QTY",
   "serviceType": "9-Picking Charges",
   "rateUnit": 2,
   "totalValue": 640
  },
  {
   "customer": 10001,
   "module": "Picking",
   "orderNo": "TO-081057",
   "sku": "020304539",
   "description": "RAIN COAT 0.02MM CLRCX-1",
   "transactionQty": 300,
   "transactionDate": "21-05-2023",
   "chargeUnit": "QTY",
   "serviceType": "9-Picking Charges",
   "rateUnit": 2,
   "totalValue": 600
  },
  {
   "customer": 10001,
   "module": "Picking",
   "orderNo": "TO-080882",
   "sku": "0203013314",
   "description": "FLY FAN",
   "transactionQty": 432,
   "transactionDate": "13-05-2023",
   "chargeUnit": "QTY",
   "serviceType": "9-Picking Charges",
   "rateUnit": 3,
   "totalValue": 1296
  },
  {
   "customer": 10001,
   "module": "Picking",
   "orderNo": "TO-080427",
   "sku": "2000394",
   "description": "1\/8PLASTIC COATED CABLE (1 Roll=75mtr)7000497",
   "transactionQty": 525,
   "transactionDate": "02-05-2023",
   "chargeUnit": "QTY",
   "serviceType": "9-Picking Charges",
   "rateUnit": 2,
   "totalValue": 1050
  },
  {
   "customer": 10001,
   "module": "Packing",
   "orderNo": "TO-081273",
   "sku": "0203011529",
   "description": "30WET SWEEPER CLOTHES REFILLS",
   "transactionQty": 270,
   "transactionDate": "22-05-2023",
   "chargeUnit": "QTY",
   "serviceType": "10-Packing Charges",
   "rateUnit": 4,
   "totalValue": 1080
  },
  {
   "customer": 10001,
   "module": "Packing",
   "orderNo": "TO-080492",
   "sku": "0203012838",
   "description": "METAL STORAGE BOX  ROUND RED",
   "transactionQty": 300,
   "transactionDate": "11-05-2023",
   "chargeUnit": "QTY",
   "serviceType": "10-Packing Charges",
   "rateUnit": 3,
   "totalValue": 900
  },
  {
   "customer": 10001,
   "module": "Packing",
   "orderNo": "TO-081446",
   "sku": "020307402",
   "description": "5pc KIDS HANGER SET-BLUE",
   "transactionQty": 320,
   "transactionDate": "24-05-2023",
   "chargeUnit": "QTY",
   "serviceType": "10-Packing Charges",
   "rateUnit": 2,
   "totalValue": 640
  },
  {
   "customer": 10001,
   "module": "Packing",
   "orderNo": "TO-081057",
   "sku": "020304539",
   "description": "RAIN COAT 0.02MM CLRCX-1",
   "transactionQty": 300,
   "transactionDate": "21-05-2023",
   "chargeUnit": "QTY",
   "serviceType": "10-Packing Charges",
   "rateUnit": 4,
   "totalValue": 1200
  },
  {
   "totalValue": 27807.4
  }
 ]

@Component({
  selector: 'app-threepl-transfer',
  templateUrl: './threepl-transfer.component.html',
  styleUrls: ['./threepl-transfer.component.scss']
})
export class ThreeplTransferComponent implements OnInit {

  screenid=3208;
  transfer: any[] = [];
  selectedtransfer : any[] = [];
  @ViewChild('transferTag') transferTag: Table | any;
    isShowDiv = false;
    table = false;
    fullscreen = false;
    search = true;
    back = false;
  
  
  
    showFloatingButtons: any;
    toggle = true;
    public icon = 'expand_more';
    showFiller = false;

    displayedColumns: string[] = ['sourceItemCode', 'sourceStockTypeId', 'sourceStorageBin', 'targetItemCode', 'targetStockTypeId', 'targetStorageBin', 'transferConfirmedQty', 'packBarcodes', 'confirmedBy', 'confirmedOn'];
    sub = new Subscription();
    ELEMENT_DATA: any[] = [];

    movementList: any[];
    submovementList: any[];
    multiselectItemCodeList1: any[] = [];
    constructor(public dialog: MatDialog,
      private http: HttpClient,
      // private cas: CommonApiService,
      private fb: FormBuilder,
      public toastr: ToastrService,
      private router: Router,
      private spin: NgxSpinnerService,
      public cs: CommonService,
      private auth: AuthService) {

        this.multiselectItemCodeList1 = [
          {value: '10001', label: 'True Value'},
          {value: '10002', label: 'Almailem'},
          {value: '10003', label: 'Toyota Spares'},
      ];
      }
    routeto(url: any, id: any) {
      sessionStorage.setItem('crrentmenu', id);
      this.router.navigate([url]);
    }
    animal: string | undefined;
   
    ngOnInit(): void {
    }
 


    filtersearch(){
      this.spin.show();
        this.transfer = ELEMENT_DATA1;
     
       
        setTimeout(() => {
          this.table = true;
          this.search = false;
          this.fullscreen = false;
          this.back = true;
          this.spin.hide();
        }, 2000);
    }

   
  
  
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
  
    ngOnDestroy() {
      if (this.sub != null) {
        this.sub.unsubscribe();
      }
    }
  
    downloadexcel() {
      // if (excel)
      var res: any = [];
      this.transfer.forEach(x => {
        res.push({

          "Source Item Code": x.sourceItemCode,
          'Source StockType ID': x.sourceStockTypeId,
          "Source Storage Bin": x.sourceStorageBin,
          "Target Item Code": x.targetItemCode,
          "Target StockType ID": (x.targetStockTypeId == 1 ? 'On Hand' : x.targetStockTypeId == 7 ? 'Hold' : '') ,
          "Target Storage Bin":  x.targetStorageBin,
          "Transfer Confirmed Qty":  x.transferConfirmedQty,
          "Pack Barcodes": x.packBarcodes,
          "Confirmed By": x.confirmedBy,
          "Confirmed On": this.cs.dateapi(x.confirmedOn),
        });
  
      })
      this.cs.exportAsExcel(res, "Transfer");
    }

  
    togglesearch() {
      this.search = false;
      this.table = true;
      this.fullscreen = false;
      this.back = true;
    }
    backsearch() {
      this.table = true;
      this.search = true;
      this.fullscreen = true;
      this.back = false;
    }
  }


