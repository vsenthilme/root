import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute } from "@angular/router";
import { Subscription } from "rxjs";
import { CommonService } from "src/app/common-service/common-service.service";
import { PaymentplanService } from "../paymentplan.service";



export interface ordermanagement {
  lineno: string;
  partner: string;
  product: string;
  status: string; 
  date:string;
  
  }
  
  const ELEMENT_DATA: ordermanagement[] = [
  { lineno: '300001 -01',status: '1500', partner: '3223',date: '2300', product: 'Legal fees for filing I130',},
  { lineno: '300001 -02',status: '1300', partner: '3455',date: '4500', product: 'Legal fees for H1B ',},
  { lineno: '300001 -03',status: '1200', partner: '5543',date: '2400', product: 'Legal fees for H1B ',},
  { lineno: '300001 -04',status: '500', partner: '2345',date: '1300', product: 'Legal fess for N400 ',},
  
  ];
@Component({
  selector: 'app-paymentplan-details',
  templateUrl: './paymentplan-details.component.html',
  styleUrls: ['./paymentplan-details.component.scss']
})
export class PaymentplanDetailsComponent implements OnInit {
  sub = new Subscription();
  matterdesc: any;
  paymentPlanNumber: any;
  paymentPlanTotalAmount: any;
  noofinstallment: any;
  Installmentamt: any;
  constructor(private route: ActivatedRoute,   private cs: CommonService, private service: PaymentplanService) { }

  ngOnInit(): void {
    let code = this.route.snapshot.params.code;
    let js = this.cs.decrypt(code);
    this.fill(js)
  }
  @ViewChild(MatPaginator, { static: true })
  paginator!: MatPaginator; // Pagination
  
  fill(data: any){
    console.log(data.code.paymentPlanNumber)
    this.sub.add(this.service.Get(data.code.paymentPlanNumber, data.code.paymentPlanRevisionNo).subscribe(res => {
console.log(res)
this.matterdesc= res.matterNumber
this.paymentPlanNumber= res.paymentPlanNumber
this.paymentPlanTotalAmount= res.paymentPlanTotalAmount
this.noofinstallment= res.noOfInstallment
this.Installmentamt= res.dueAmount

      this.dataSource = new MatTableDataSource<any>(res.paymentPlanLines);
      this.dataSource.paginator = this.paginator;
    }, err => {
      this.cs.commonerror(err);
      // this.spin.hide();
    }));

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


  displayedColumns: string[] = ['lineno','partner','date','status',];
  dataSource = new MatTableDataSource< ordermanagement>(ELEMENT_DATA);
  selection = new SelectionModel< ordermanagement>(true, []);

  /** Whether the number of selected elements matches the total number of rows. */
  

}
