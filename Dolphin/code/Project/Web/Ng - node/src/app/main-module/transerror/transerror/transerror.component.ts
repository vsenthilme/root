import {
  HttpClient
} from '@angular/common/http';
import {
  Component,
  OnInit,
  ViewChild
} from '@angular/core';
import {
  MatPaginator
} from '@angular/material/paginator';
import {
  MatSort
} from '@angular/material/sort';
import {
  MatTableDataSource
} from '@angular/material/table';
import {
  NgxSpinnerService
} from 'ngx-spinner';
import {
  Subscription
} from 'rxjs';
import {
  CommonService
} from 'src/app/common-service/common-service.service';
import {
  TranserrorService
} from './transerror.service';

@Component({
  selector: 'app-transerror',
  templateUrl: './transerror.component.html',
  styleUrls: ['./transerror.component.scss']
})
export class TranserrorComponent implements OnInit {
  ELEMENT_DATA: any[] = [];
  sub = new Subscription();

  constructor(private http: HttpClient, private service: TranserrorService,
    private spin: NgxSpinnerService, private cs: CommonService) {}
  isShowDiv = false;
  table = false;
  fullscreen = false;
  search = true;
  back = false;
  public icon = 'expand_more';
  showFloatingButtons: any;
  toggle = true;
  displayedColumns: string[] = ['createdOn', 'errorId', 'errorType', 'tableName', 'transaction', 'objectData', 'createdBy'];
  dataSource = new MatTableDataSource < any > (this.ELEMENT_DATA);
  @ViewChild(MatSort, {
    static: true
  })
  sort!: MatSort;
  @ViewChild(MatPaginator, {
    static: true
  })
  paginator!: MatPaginator; // Pagination
  // Pagination
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
  ngOnInit(): void {
    this.getDropdown();
  }
  multiOrderNo: any[] = [];
  getDropdown() {
    this.sub.add(this.service.search({}).subscribe(res => {
      res.forEach((x: any) => this.multiOrderNo.push({
        value: x.tableName,
        label: x.tableName
      }));
    }))
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
  startCreatedOn: any;
  endCreatedOn: any;
  tableName: any
  filtersearch() {
    this.spin.show();
    let obj: any = {};
    obj.startCreatedOn = this.cs.day_callapiSearch(this.startCreatedOn);
    obj.endCreatedOn = this.cs.day_callapiSearch(this.endCreatedOn);
    if (this.tableName == null) {
      obj.tableName = this.tableName;
    } else {
      obj.tableName = [this.tableName];
    }
    this.sub.add(this.service.search(obj).subscribe(result => {
        this.dataSource = new MatTableDataSource < any > (result);
        this.table = true;
        this.search = false;
        this.fullscreen = false;
        this.back = true;
        this.spin.hide();
      },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
  }

  downloadexcel() {
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Date": this.cs.dateapi(x.createdOn),
        "Error Id": x.errorId,
        "Error Type": x.errorType,
        "Table Name ": x.tableName,
        "Transaction": x.transaction,
        "Object Data": x.objectData,
        "Created By": x.createdBy,


      });

    })
    this.cs.exportAsExcel(res, "Transaction Log");
  }
  reset() {
    this.startCreatedOn = null;
    this.endCreatedOn = null;
    this.tableName = null;
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
