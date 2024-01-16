import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { AssignuserPopupComponent } from "../../../prepetual-count/prepetual-confirm/assignuser-popup/assignuser-popup.component";
import { PhysicalInventoryService } from "../../physical-inventory.service";
@Component({
  selector: 'app-variantedit-annual',
  templateUrl: './variantedit-annual.component.html',
  styleUrls: ['./variantedit-annual.component.scss']
})
export class VarianteditAnnualComponent implements OnInit {

  title1 = "Cycle count";
  title2 = "Prepetual Confirm";
  isShown: boolean = false; // hidden by default
  toggleShow() { this.isShown = !this.isShown; }
  animal: string | undefined;
  name: string | undefined;

  data: any = {};

  isShowDiv = false;
  public icon = 'expand_more';
  showFloatingButtons: any;
  toggle = true;

  disabled = false;
  step = 0;
  panelOpenState = false;

  dataSource = new MatTableDataSource<any>();
  displayedColumns: string[] = ['actions', 'no', 'itemCode', 'referenceField8', 'referenceField9', 'referenceField10', 'storageBin', 'pallet', 'packBarcodes', 'stockTypeId', 'spl', 'inventoryQty', 'countedQty', 'varianceQty', 'by', 'countedOn', 'status',];

  
  cycleCountActions: any[] = [{ view: 'Recount', value: 'RECOUNT' }, { view: 'Skip', value: 'SKIP' }, { view: 'Write Off', value: 'WRITEOFF' }]

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(
    public dialog: MatDialog,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    public cs: CommonService,
    public auth: AuthService,
    public router: Router,
    private route: ActivatedRoute,
    public periodicService: PhysicalInventoryService
  ) { }


  @ViewChild(MatSort) sort: MatSort;
  
  filteredVariance: any[] = [];
  ngOnInit(): void {
    this.data = this.cs.decrypt(this.route.snapshot.params.data);
    console.log( this.data)
    this.periodicService.findPeriodicDLine({cycleCountNo: this.data.periodicHeaderData.cycleCountNo}).subscribe(res => {
    if (this.data.type == "Edit" && res) {
      res.forEach(element => {
        if((element.countedQty - element.inventoryQuantity) != 0  && element.varianceQty != null ){
        if(element.statusId == 74 || element.statusId == 75){
          console.log(element.varianceQty)
         if(element.cycleCountAction == null){
          element.cycleCountAction = "RECOUNT";
         }
          this.filteredVariance.push(element)
        }
      }
      });
      this.dataSource = new MatTableDataSource(this.filteredVariance);
      this.displayedColumns = ['actions', 'no', 'itemCode', 'referenceField8', 'referenceField9', 'referenceField10', 'storageBin', 'packBarcodes', 'stockTypeId', 'inventoryQty', 'countedQty', 'varianceQty', 'cycleCounterId', 'status'];
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }
    console.log(res)
    if (this.data.type =='Display' && res) {
      console.log(res)
      res.forEach(element => {
        if((element.countedQty - element.inventoryQuantity) != 0  && element.varianceQty != null ){
          this.filteredVariance.push(element);
      }
      });
      this.dataSource = new MatTableDataSource(this.filteredVariance);
      this.displayedColumns = ['actions', 'no', 'itemCode', 'referenceField8', 'referenceField9', 'referenceField10', 'storageBin', 'packBarcodes', 'stockTypeId', 'inventoryQty', 'countedQty', 'varianceQty', 'cycleCounterId', 'status'];
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }
  })

  }

  confirm() {
    this.spin.show();
    this.periodicService.updatePeriodicLine(this.dataSource.data).subscribe(
      result => {
        this.spin.hide();
        this.toastr.success(
          "Stock count confirmed Successfully",
          "Notification"
        );
        this.router.navigate(['/main/cycle-count/variant-analysis-annual'])
      },
      error => {
        this.spin.hide();
        this.toastr.error(
          "Error",
          "Notification"
        );
      }
    );
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
    console.log('show:' + this.showFloatingButtons);
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  setStep(index: number) {
    this.step = index;
  }
  nextStep() {
    this.step++;
  }

  prevStep() {
    this.step--;
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }


  calculateVariance(element){
    element.varianceQty = element.countedQty - element.inventoryQuantity;
  }
}

