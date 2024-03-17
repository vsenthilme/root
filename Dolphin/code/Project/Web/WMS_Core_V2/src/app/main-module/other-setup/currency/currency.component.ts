import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { CurrencyNewComponent } from './currency-new/currency-new.component';
import { CurrencyService } from './currency.service';
import { CommonApiService } from 'src/app/common-service/common-api.service';

@Component({
  selector: 'app-currency',
  templateUrl: './currency.component.html',
  styleUrls: ['./currency.component.scss']
})
export class CurrencyComponent implements OnInit {
  screenid=3104;
  advanceFilterShow: boolean;
  @ViewChild('Setupcurrency') Setupcurrency: Table | undefined;
  currrencys: any;
  selectedcurrency : any;

  displayedColumns: string[] = ['select', 'currencyDescription', 'currencyId','languageId',  ];
  sub = new Subscription();
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';

  ELEMENT_DATA: any[] = [];
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);
  
  constructor(public dialog: MatDialog,
    private cas: CommonApiService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    public cs: CommonService,
   // private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService,
    private service:CurrencyService ) { }
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
    animal: string | undefined;
    applyFilter(event: Event) {
      const filterValue = (event.target as HTMLInputElement).value;
  
      this.dataSource.filter = filterValue.trim().toLowerCase();
  
      if (this.dataSource.paginator) {
        this.dataSource.paginator.firstPage();
      }
    }
    applyFilterGlobal($event: any, stringVal: any) {
      this.Setupcurrency!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
    } 
    RA: any = {};
    ngOnInit(): void {
      this.RA = this.auth.getRoleAccess(this.screenid);
      this.getAll();
     // this.getAll();

     // this.getallfilter();
    }
    multilanguageList: any[] = [];
    multiyseridList: any[] = [];
    multicountryList:any[]=[];
        multiselectyseridList: any[] = [];
        searhform = this.fb.group({
        currencyId:[],
  languageId: [[this.auth.languageId]],
  stateId:[],
  createdBy:[],
        });
        search() {
          this.spin.show();
          this.sub.add(this.service.search(this.searhform.getRawValue()).subscribe((res: any[]) => {
            console.log(res);
            this.currrencys = res;
           
            
            this.spin.hide();
          }, err => {
            this.cs.commonerrorNew(err);
            this.spin.hide();
          }));
        }
      
        multistateList:any[]=[];
    getallfilter() {
      this.multilanguageList = [];
      this.multicountryList = [];
      this.multistateList=[];
      let obj: any = {};
      obj.languageId=[this.auth.languageId];
      this.spin.show();
      this.sub.add(this.service.search(obj).subscribe((res: any[]) => {
           this.dataSource = new MatTableDataSource < any >(res);
          this.spin.hide();
          res.forEach((x: { languageId: string }) => {
              this.multilanguageList.push({
                value: x.languageId,
                label: x.languageId,
              });
          });
          this.multilanguageList = this.cas.removeDuplicatesFromArrayNew(this.multilanguageList);
          res.forEach((x: {
            createdBy: string;
          }) => this.multiyseridList.push({
            value: x.createdBy,
            label: x.createdBy
          }))
          res.forEach((x: { countryId: string; countryIdAndDescription: string, }) => {
            this.multicountryList.push({
              value: x.countryId,
              label: x.countryId + '-' + x.countryIdAndDescription,
            });
        });
        res.forEach((x: { currencyId: string; currencyDescription: string, }) => {
          this.multistateList.push({
            value: x.currencyId,
            label: x.currencyId + '-' + x.currencyDescription,
          });
      });
          this.multiselectyseridList = this.multiyseridList;
          this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(this.multiyseridList);
          this.multicountryList = this.cas.removeDuplicatesFromArrayNew(this.multicountryList);
         this.currrencys=res;
        }
        , err => {
          this.cs.commonerrorNew(err);
          this.spin.hide();
        }));
    } 
  
    reset() {
      this.searhform.reset();
    }
  
  openDialog(data: any = 'New'): void {
  console.log(this.selectedcurrency)
    if (data != 'New')
    if (this.selectedcurrency.length === 0) {
      this.toastr.warning("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    const dialogRef = this.dialog.open(CurrencyNewComponent, {
      disableClose: true,
      width: '55%',
      maxWidth: '80%',
      data: { pageflow: data, code: data != 'New' ? this.selectedcurrency[0].currencyId : null,languageId: data != 'New' ? this.selectedcurrency[0].languageId : null}
    });
  
    dialogRef.afterClosed().subscribe(result => {
      this.getAll();
    });
  }
  getAll() {
    if(this.auth.userTypeId == 1){
      this.superAdmin()
    }else{
      this.adminUser()
    }
  }
  
  adminUser(){
    let obj: any = {};
    obj.languageId = [this.auth.languageId];
    this.spin.show();
    this.sub.add(this.service.search(obj).subscribe((res: any[]) => {
      console.log(res)
if(res){
  this.currrencys = res;
}
      this.spin.hide();
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
  }
  superAdmin(){
    this.spin.show();
    this.sub.add(this.service.Getall().subscribe((res: any[]) => {
      if(res){
        this.currrencys=res;
       } this.spin.hide();
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
  }
  
  deleteDialog() {
    if (this.selectedcurrency.length === 0) {
      this.toastr.error("Kindly select any row", "Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '80%',
      position: { top: '9%', },
      data: this.selectedcurrency[0].code,
    });
  
    dialogRef.afterClosed().subscribe(result => {
  
      if (result) {
        this.deleterecord(this.selectedcurrency[0].currencyId,this.selectedcurrency[0].languageId);
  
      }
    });
  }
  
  
  deleterecord(id: any,languageId:any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id,languageId).subscribe((res) => {
      this.toastr.success(id + " Deleted successfully.","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();
      this.getAll();
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
  }
  downloadexcel() {
    var res: any = [];
    this.currrencys.forEach(x => {
      res.push({
        "Currency Id":x.currencyId,
        "Description":x.currencyDescription,
        "Language Id":x.languageId,
        "Created By":x.createdBy,
        "Created On":x.createdOn,
      
      
      });
  
    })
    this.cs.exportAsExcel(res, "Currency");
  }
  onChange() {
    console.log(this.selectedcurrency.length)
    const choosen= this.selectedcurrency[this.selectedcurrency.length - 1];   
    this.selectedcurrency.length = 0;
    this.selectedcurrency.push(choosen);
  } 
  }
   
  
  
  


