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

  number:  string;
  servicedetails:  string;
  des:  string;
  qty:  string;
  unit:  string;
  rate:  string;
  billamount:string;
} 
const ELEMENT_DATA1:  variant1[] = [
  {number:  '1',servicedetails: 'Inbound',des: 'Unloading charges',qty: '100',unit: 'CBM',
  rate:'$ 10',billamount:'$ 1000'},{number:  '2',servicedetails: 'Inbound',des: 'Receiving charges',qty: '100',unit: 'CBM',
  rate:'$ 20',billamount:'$ 2000'},{number:  '3',servicedetails: 'Storage',des: 'Storing charges',qty: '120',unit: 'CBM',
  rate:'$ 5',billamount:'$ 600'},{number:  '4',servicedetails: 'Outbound',des: 'Picking charges',qty: '4500',unit: 'NO',
  rate:'$ 1',billamount:'$ 4500'},{number:  '5',servicedetails: 'Outbound',des: 'Packing charges',qty: '2000',unit: 'NO',
  rate:'$ 0.5',billamount:'$ 1000'}
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



