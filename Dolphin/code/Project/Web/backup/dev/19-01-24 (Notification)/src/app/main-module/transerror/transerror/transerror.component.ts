import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { NgxSpinnerService } from 'ngx-spinner';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { TranserrorService } from './transerror.service';

@Component({
  selector: 'app-transerror',
  templateUrl: './transerror.component.html',
  styleUrls: ['./transerror.component.scss']
})
export class TranserrorComponent implements OnInit {
  ELEMENT_DATA: any[] = [];
  sub = new Subscription();

  constructor(private http: HttpClient,private service: TranserrorService,
    private spin: NgxSpinnerService, private cs : CommonService) { }
  isShowDiv = false;
  public icon = 'expand_more';
  showFloatingButtons: any;
  toggle = true;
  displayedColumns: string[] = ['createdOn','errorId', 'errorType', 'tableName','transaction','objectData', 'createdBy'];
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
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
this.getAll();
}


applyFilter(event: Event) {
  const filterValue = (event.target as HTMLInputElement).value;
  this.dataSource.filter = filterValue.trim().toLowerCase();
}

getAll() {
  this.spin.show();
  this.sub.add(this.service.Getall().subscribe(res => {
    console.log(res)

   this.dataSource = new MatTableDataSource<any>(res);
   this.dataSource.sort = this.sort;
   this.dataSource.paginator = this.paginator;

    this.spin.hide();
  }, err => {
    this.cs.commonerror(err);
    this.spin.hide();
  }));
}
downloadexcel() {
  var res: any = [];
  this.dataSource.data.forEach(x => {
    res.push({
      "Date":this.cs.dateapi(x.createdOn),
      "Error Id":x.errorId,
     "Error Type":x.errorType,
    "Table Name ":x.tableName,
     "Transaction":x.transaction,
     "Object Data":x.objectData,
     "Created By":x.createdBy,
    
    
    });

  })
  this.cs.exportAsExcel(res, "Transaction Log");
}

   
}


