




  

  import { SelectionModel } from "@angular/cdk/collections";
  import { HttpClient } from "@angular/common/http";
    import { Component, OnInit, ViewChild } from "@angular/core";
    import { FormBuilder } from "@angular/forms";
    import { MatDialog } from "@angular/material/dialog";
    import { MatPaginator, PageEvent } from "@angular/material/paginator";
    import { MatSort } from "@angular/material/sort";
    import { MatTableDataSource } from "@angular/material/table";
    import { Router } from "@angular/router";
    import { IDropdownSettings } from "ng-multiselect-dropdown";
    import { NgxSpinnerService } from "ngx-spinner";
    import { ToastrService } from "ngx-toastr";
    import { forkJoin, of, Subscription } from "rxjs";
    import { catchError } from "rxjs/operators";
    import { CommonService } from "src/app/common-service/common-service.service";
    import { AuthService } from "src/app/core/core";
    import { MasterService } from "src/app/shared/master.service";
  import { ContainerReceiptService } from "../../Inbound/Container-receipt/container-receipt.service";
  import { PickingService } from "../../Outbound/picking/picking.service";
    import { ReportsService } from "../reports.service";
  import  { stockElement, StocksampleService }  from "../stocksamplereport/stocksample.service";
  import { Table } from "primeng/table";
  
  @Component({
    selector: 'app-binner',
    templateUrl: './binner.component.html',
    styleUrls: ['./binner.component.scss']
  })
  export class BinnerComponent implements OnInit {
    screenid=3166;
    binner: any[] = [];
    selectedbinner : any[] = [];
    @ViewChild('binnerTag') binnerTag: Table | any;
      isShowDiv = false;
      table = false;
      fullscreen = false;
      search = true;
      back = false;
    
    
    
      showFloatingButtons: any;
      toggle = true;
      public icon = 'expand_more';
      showFiller = false;
  
     // displayedColumns: string[] = [ 'statusId','itemCode','proposedStorageBin', 'confirmedStorageBin', 'packBarcodes', 'putAwayQuantity', 'putawayConfirmedQty', 'confirmedBy','confirmedOn','createdOn', 'leadTime'];
          //wms demo
              displayedColumns: string[] = [ 'statusId','itemCode','proposedStorageBin', 'confirmedStorageBin', 'packBarcodes', 'putAwayQuantity', 'putawayConfirmedQty', 'confirmedBy','confirmedOn',];
      sub = new Subscription();
      ELEMENT_DATA: any[] = [];
  
    
      constructor(public dialog: MatDialog,
        private service: StocksampleService,
        private http: HttpClient,
        // private cas: CommonApiService,
        private fb: FormBuilder,
        public toastr: ToastrService,
        private router: Router,
        private spin: NgxSpinnerService,
        public cs: CommonService, private reportService: ReportsService,
        private pickingService: PickingService,
        private auth: AuthService,
        private containerservice: ContainerReceiptService,
        private masterService: MasterService,) { }
      routeto(url: any, id: any) {
        sessionStorage.setItem('crrentmenu', id);
        this.router.navigate([url]);
      }
      animal: string | undefined;
      
      ngOnInit(): void {
        this.getDropdown();
        this.dropdownfill();
      }
      
  multiOrderNo: any[] = [];
      getDropdown(){
        this.sub.add(this.reportService.getInboundStatusReport({warehouseId : [this.auth.warehouseId]}).subscribe(res => {
          res.forEach((x: any) => this.multiOrderNo.push({ value: x.refDocNumber, label: x.refDocNumber }));
        }))
      }
  
  
      warehouseList: any[] = [];
      selectedWarehouseList: any[] = [];
      selectedItems: any[] = [];
      multiselectWarehouseList: any[] = [];
      multiWarehouseList: any[] = [];
      CustomeridList : any[]=[];
      dropdownfill() {
       this.spin.show();
       forkJoin({
         warehouse: this.masterService.getWarehouseMasterDetails().pipe(catchError(err => of(err))),
   
       })
       .subscribe(({ warehouse }) => {
         if(this.auth.userTypeId != 3){
           this.warehouseList = warehouse.filter((x: any) => x.warehouseId == this.auth.warehouseId);
         }else{
           this.warehouseList = warehouse
         }
           this.warehouseList.forEach(x => this.multiWarehouseList.push({ value: x.warehouseId, label: x.warehouseId + (x.description == null ? '' : '- ' + x.description) }));
           this.multiselectWarehouseList = this.multiWarehouseList;
           this.multiselectWarehouseList.forEach((warehouse: any) => {
            console.log(this.multiselectWarehouseList)
             if (warehouse.value == this.auth.warehouseId)
               this.selectedItems = [warehouse.value];
           })
         }, (err) => {
           this.toastr.error(
             err,
             ""
           );
         });
       this.spin.hide();
   
     }
  
     multiselectItemCodeList: any[] = [];
     itemCodeList: any[] = [];
     onItemType(searchKey) {
       let searchVal = searchKey?.filter;
       if (searchVal !== '' && searchVal !== null) {
         forkJoin({
           itemList: this.reportService.getItemCodeDropDown(searchVal.trim()).pipe(catchError(err => of(err))),
         })
           .subscribe(({ itemList }) => {
             if (itemList != null && itemList.length > 0) {
               this.multiselectItemCodeList = [];
               this.itemCodeList = itemList;
               this.itemCodeList.forEach(x => this.multiselectItemCodeList.push({ value: x.itemCode, label: x.itemCode + ' - ' + x.description }))
             }
           });
       }
     }
  
     multiselectStorageList: any[] = [];
     storageBinList1: any[] = [];
     selectedStorageBin: any[] = [];
     onStorageType(searchKey) {
       let searchVal = searchKey?.filter;
       if (searchVal !== '' && searchVal !== null) {
         forkJoin({
           storageList: this.reportService.getStorageDropDown(searchVal.trim()).pipe(catchError(err => of(err))),
         })
           .subscribe(({ storageList }) => {
             if (storageList != null && storageList.length > 0) {
               console.log(3)
               this.multiselectStorageList = [];
               this.storageBinList1 = storageList;
               this.storageBinList1.forEach(x => this.multiselectStorageList.push({ value: x.storageBin, label: x.storageBin}))
             }
           });
       }
     }
     
      itemCode: any;
      confirmedStorageBin: any;
      refDocNumber = '';
      warehouseId = '';
  
      filtersearch(){
  this.spin.show();
        let obj: any = {};
  
        obj.itemCode = [];
        if (this.itemCode != null && this.itemCode.trim() != "") {
          obj.itemCode.push(this.itemCode)
        }
  
        obj.confirmedStorageBin = [];
        if (this.confirmedStorageBin != null) {
          obj.confirmedStorageBin.push(this.confirmedStorageBin)
        }
        //obj.refDocNumber = [];
       if (this.refDocNumber) {
          obj.refDocNumber = this.refDocNumber;
       }
        
        // obj.toPickConfirmedOn = this.toPickConfirmedOn
        // obj.fromPickConfirmedOn = this.fromPickConfirmedOn
        obj.warehouseId = [this.auth.warehouseId]
  
        this.sub.add(this.reportService.getBinnerReport(obj).subscribe(res => {
          console.log(res)
          this.binner = res;
       
          this.table = true;
          this.search = false;
          this.fullscreen = false;
          this.back = true;
          this.spin.hide();
          //this.totalRecords = this.dataSource.data.length
        },
          err => {
            this.cs.commonerror(err);
            this.spin.hide();
          }));
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
    
      @ViewChild(MatSort, { static: true })
      sort!: MatSort;
      @ViewChild(MatPaginator, { static: true })
      paginator: MatPaginator; // Pagination
      // Pagination
    
  
    
  
      totalRecords = 0;
      downloadexcel() {
        // if (excel)
        var res: any = [];
        this.binner.forEach(x => {
          res.push({
            "Status": x.statusId == 20 ? 'Binned' : 'Reversed',
            "Product Code":  x.itemCode,
            "Proposed StorageBin ": x.proposedStorageBin,
            'Confirmed Storage Bin': x.confirmedStorageBin,
            'Pallet ID': x.packBarcodes,
            'TO Qty': x.putAwayQuantity,
            'Binned Qty': x.putawayConfirmedQty,
            'Binner': x.confirmedBy,
            "Confirmed On": this.cs.dateExcel(x.confirmedOn),
    
            // 'Created By': x.createdBy,
            // 'Date': this.cs.dateapi(x.createdOn),
          });
    
        })
        this.cs.exportAsExcel(res, "Binning");
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
  
    
      onItemSelect(item: any) {
      }
    
      onSelectAll(items: any) {
      }
    
    
      
    
     
    
    
     
      // getBillableAmount() {
      //   let total = 0;
      //   this.dataSource.data.forEach(element => {
      //     total = total + (element.s != null ? element.s : 0);
      //   })
      //   return (Math.round(total * 100) / 100);
      // }
  
      getToQtyQty(){
        let total = 0;
        this.binner.forEach(x =>{
          total = total + (x.putAwayQuantity != null ? x.putAwayQuantity : 0)
        })
        return Math.round(total *100 / 100)
      }
      getBinnerQty(){
        let total = 0;
        this.binner.forEach(x =>{
          total = total + (x.putawayConfirmedQty != null ? x.putawayConfirmedQty : 0)
        })
        return Math.round(total *100 / 100)
      }
  
      reset(){
        this.itemCode = '';
        this.confirmedStorageBin = '';
        this.refDocNumber = '';
        this.warehouseId = '';
      }
    }
  