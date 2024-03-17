import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { variant } from 'src/app/main-module/cycle-count/variant-analysis/variant-analysis.component';

export interface  variant1 {

 
  servicedetails:  string;
  orderno:string;
  des:  string;
  customer:string;
  qty:  string;
  itemcode:string;
  date:string;
  unit:  string;
  CBMQ:string;
  rate:  string;
  value:string;
  type:string;
  billamount:string;
} 
const ELEMENT_DATA1:  variant1[] = [
  {customer:'10001',servicedetails: 'Inbound',orderno:'ASN000001070',itemcode:'0057510968',des: 'FRESH CONTAINER 1Ltr 3pc RECT055010',qty: '252',unit: 'CBM',date:'08/05/2023',CBMQ:'0.05',value:'12.6',type:'2-Handling Charges',
  rate:'$ 80',billamount:'$ 1008.00'},{customer:'10001',servicedetails: 'Inbound',orderno:'ASN000001070',itemcode:'0057517341',des: 'CONTAINER AROMA VEGETABLES 4Ltr',qty: '540',unit: 'CBM',date:'11/05/2023',CBMQ:'0.07',value:'37.8',type:'2-Handling Charges',
  rate:'$ 60',billamount:'$ 2268.00'},{customer:'10001',servicedetails: 'Inbound',orderno:'ASN000001070',itemcode:'005751262',des: 'ICE LOLLY POP Pk(1)020560/020520/020580',qty: '744',unit: 'CBM',date:'11/05/2023',CBMQ:'0.02',value:'14.88',type:'2-Handling Charges',
  rate:'$ 80',billamount:'$ 1190.40'},{customer:'10001',servicedetails: 'Inbound',orderno:'ASN000001064',itemcode:'0057511926',des: 'BANANA SAVER021269/000180',qty: '800',unit: 'CBM',date:'12/05/2023',CBMQ:'0.01',value:'8',type:'2-Handling Charges',
  rate:'$ 60',billamount:'$ 480'},{customer:'10001',servicedetails: 'Inbound',orderno:'ASN000001064',itemcode:'0057517698',des: 'BANANA SAVER021269/000180',qty: '900',unit: 'CBM',date:'14/05/2023',CBMQ:'0.03',value:'27',type:'2-Handling Charges',
  rate:'$ 80',billamount:'$ 2160'},  {customer:'10001',servicedetails: 'Inbound',orderno:'ASN000001064',itemcode:'0057517055',des: 'LEMON KEEPER',qty: '900',unit: 'CBM',date:'18/05/2023',CBMQ:'0.02',value:'18',type:'2-Handling Charges',
  rate:'$ 80',billamount:'$ 1440.00'},{customer:'10001',servicedetails: 'Inbound',orderno:'ASN000001064',itemcode:'0057517609',des: 'SALAD SPINNER WASH&DRY SALAD',qty: '180',unit: 'CBM',date:'20/05/2023',CBMQ:'0.15',value:'27',type:'2-Handling Charges',
  rate:'$ 60',billamount:'$ 1620.00'},{customer:'10001',servicedetails: 'Storage',orderno:'',itemcode:'0057510968',des: 'FRESH CONTAINER 1Ltr 3pc RECT055010',qty: '252',unit: 'CBM',date:'',CBMQ:'0.05',value:'12.6',type:'4-storage charges',
  rate:'$ 25',billamount:'$ 315.00'},{customer:'10001',servicedetails: 'Storage',orderno:'',itemcode:'0057517341',des: 'CONTAINER AROMA VEGETABLES 4Ltr',qty: '540',unit: 'CBM',date:'',CBMQ:'0.07',value:'37.8',type:'4-storage charges',
  rate:'$ 25',billamount:'$ 945.00'},{customer:'10001',servicedetails: 'Storage',orderno:'',itemcode:'005751262',des: 'ICE LOLLY POP Pk(1)020560/020520/020580',qty: '744',unit: 'CBM',date:'',CBMQ:'0.02',value:'14.88',type:'4-storage charges',
  rate:'$ 25',billamount:'$ 372.00'}, {customer:'10001',servicedetails: 'Storage',orderno:'',itemcode:'0057511926',des: 'BANANA SAVER021269/000180',qty: '804',unit: 'CBM',date:'',CBMQ:'0.01',value:'8.04',type:'4-storage charges',
  rate:'$ 25',billamount:'$ 201.00'},{customer:'10001',servicedetails: 'Storage',orderno:'',itemcode:'0057517698',des: 'SAPONELLO SOAP DISPENSER WHT/GREY',qty: '900',unit: 'CBM',date:'',CBMQ:'0.03',value:'27',type:'4-storage charges',
  rate:'$ 25',billamount:'$ 675.00'},{customer:'10001',servicedetails: 'Storage',orderno:'',itemcode:'0057517055',des: 'LEMON KEEPER',qty: '900',unit: 'CBM',date:'',CBMQ:'0.02',value:'18',type:'4-storage charges',
  rate:'$ 25',billamount:'$ 450.00'},{customer:'10001',servicedetails: 'Storage',orderno:'',itemcode:'0057517609',des: 'SALAD SPINNER WASH&DRY SALAD',qty: '180',unit: 'CBM',date:'',CBMQ:'0.15',value:'27',type:'4-storage charges',
  rate:'$ 25',billamount:'$ 675.00'},{customer:'10001',servicedetails: 'Storage',orderno:'',itemcode:'005759569',des: 'CHESTNUT CUTTER020490/020491/020493',qty: '200',unit: 'CBM',date:'',CBMQ:'0.18',value:'36',type:'4-storage charges',
  rate:'$ 25',billamount:'$ 900.00'}, {customer:'10001',servicedetails: 'Picking',orderno:'TO-080857',itemcode:'020304420',des: 'GARBAGE BAG 45x55CM ASSTD COLOR8009',qty: '200',unit: 'QTY',date:'23-05-2023',CBMQ:'',value:'',type:'9-Picking Charges',
  rate:'$ 2',billamount:'$ 400.00'},{customer:'10001',servicedetails: 'Picking',orderno:'TO-080960',itemcode:'0203013102',des: 'NON-WOVEN BAG SMALL',qty: '200',unit: 'QTY',date:'13-05-2023',CBMQ:'',value:'',type:'9-Picking Charges',
  rate:'$ 5',billamount:'$ 1000.00'},{customer:'10001',servicedetails: 'Picking',orderno:'TO-080653',itemcode:'011671276',des: 'TISSUE HOLDER1141',qty: '216',unit: 'QTY',date:'19-05-2023',CBMQ:'',value:'',type:'9-Picking Charges',
  rate:'$ 2',billamount:'$ 432.00'},{customer:'10001',servicedetails: 'Picking',orderno:'TO-081446',itemcode:'0203013287',des: '5pc KIDS HANGER SET PINK',qty: '240',unit: 'QTY',date:'24-05-2023',CBMQ:'',value:'',type:'9-Picking Charges',
  rate:'$ 3',billamount:'$ 720.00'},{customer:'10001',servicedetails: 'Picking',orderno:'TO-080299',itemcode:'21300073015',des: 'PHILIPS BIT #2 2FRV P2/98660',qty: '240',unit: 'QTY',date:'04-05-2023',CBMQ:'',value:'',type:'9-Picking Charges',
  rate:'$ 5',billamount:'$ 1200.00'},{customer:'10001',servicedetails: 'Picking',orderno:'TO-081273',itemcode:'0203011529',des: '30WET SWEEPER CLOTHES REFILLS',qty: '270',unit: 'QTY',date:'22-05-2023',CBMQ:'',value:'',type:'9-Picking Charges',
  rate:'$ 5',billamount:'$ 1350.00'},{customer:'10001',servicedetails: 'Picking',orderno:'TO-080492',itemcode:'0203012838',des: 'METAL STORAGE BOX  ROUND RED',qty: '300',unit: 'QTY',date:'11-05-2023',CBMQ:'',value:'',type:'9-Picking Charges',
  rate:'$ 2',billamount:'$ 600.00'},{customer:'10001',servicedetails: 'Picking',orderno:'TO-081446',itemcode:'020307402',des: '5pc KIDS HANGER SET-BLUE',qty: '320',unit: 'QTY',date:'24-05-2023',CBMQ:'',value:'',type:'9-Picking Charges',
  rate:'$ 2',billamount:'$ 640.00'},{customer:'10001',servicedetails: 'Picking',orderno:'TO-081057',itemcode:'020304539',des: 'RAIN COAT 0.02MM CLRCX-1',qty: '300',unit: 'QTY',date:'21-05-2023',CBMQ:'',value:'',type:'9-Picking Charges',
  rate:'$ 2',billamount:'$ 600.00'},{customer:'10001',servicedetails: 'Picking',orderno:'TO-080882',itemcode:'0203013314',des: 'FLY FAN',qty: '432',unit: 'QTY',date:'13-05-2023',CBMQ:'',value:'',type:'9-Picking Charges',
  rate:'$ 3',billamount:'$ 1296.00'},{customer:'10001',servicedetails: 'Picking',orderno:'TO-080427',itemcode:'2000394',des: '1/8PLASTIC COATED CABLE (1 Roll=75mtr)7000497',qty: '525',unit: 'QTY',date:'02-05-2023',CBMQ:'',value:'',type:'9-Picking Charges',
  rate:'$ 2',billamount:'$ 1050.00'},{customer:'10001',servicedetails: 'Packing',orderno:'TO-081273',itemcode:'0203011529',des: '30WET SWEEPER CLOTHES REFILLS',qty: '270',unit: 'QTY',date:'22-05-2023',CBMQ:'',value:'',type:'10-Packing Charges',
  rate:'$ 4',billamount:'$ 1080.00'},{customer:'10001',servicedetails: 'Packing',orderno:'TO-080492',itemcode:'0203012838',des: 'METAL STORAGE BOX  ROUND RED',qty: '300',unit: 'QTY',date:'11-05-2023',CBMQ:'',value:'',type:'10-Packing Charges',
  rate:'$ 3',billamount:'$ 900.00'},{customer:'10001',servicedetails: 'Packing',orderno:'TO-081446',itemcode:'020307402',des: '5pc KIDS HANGER SET-BLUE',qty: '320',unit: 'QTY',date:'24-05-2023',CBMQ:'',value:'',type:'10-Packing Charges',
  rate:'$ 2',billamount:'$ 600.00'},{customer:'10001',servicedetails: 'Packing',orderno:'TO-081057',itemcode:'020304539',des: 'RAIN COAT 0.02MM CLRCX-1',qty: '300',unit: 'QTY',date:'21-05-2023',CBMQ:'',value:'',type:'10-Packing Charges',
  rate:'$ 3',billamount:'$ 1296.00'}



];



@Component({
  selector: 'app-invoice-new',
  templateUrl: './invoice-new.component.html',
  styleUrls: ['./invoice-new.component.scss']
})
export class InvoiceNewComponent implements OnInit {

  advanceFilterShow: boolean;
  @ViewChild('Setupinvoice') Setupinvoice: Table | undefined;
  invoice: any;
  selectedinvoice : any;
  form = this.fb.group({
    
  });
  isShowDiv = false;
  table = true;
  fullscreen = false;
  search = true;
  step = 0;
  back = false;
  div1Function() {
    this.table = !this.table;
  }
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';
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
  showFiller = false;
  //displayedColumns: string[] = [ 'yz', 'number','servicedetails','des','qty','unit','rate','billamount'];
  sub = new Subscription();
  ELEMENT_DATA: variant[] = [];
  constructor(
    private router: Router, public toastr: ToastrService, private spin: NgxSpinnerService,private fb: FormBuilder,
    ) { }
  animal: string | undefined;
  
  ngOnInit(): void {
    this.spin.show();
    setTimeout(() => {
      this.invoice = ELEMENT_DATA1;
      this.spin.hide();
    }, 500);
   }

 

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
  @ViewChild(MatSort, { static: true })
  sort!: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator!: MatPaginator; // Pagination
  // Pagination





  filtersearch() {
    //  this.spin.show();
      this.table = true;
      this.search = true;
      this.fullscreen = true;


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

  reset() {
    this.form.reset();
  }
  setStep(index: number) {
    this.step = index;
  }





  onChange() {
    console.log(this.selectedinvoice.length)
    const choosen= this.selectedinvoice[this.selectedinvoice.length - 1];   
    this.selectedinvoice.length = 0;
    this.selectedinvoice.push(choosen);
  } 



}



