import { SelectionModel } from '@angular/cdk/collections';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { ActivityService } from 'src/app/main-module/setting/business/activity-code/activity.service';

@Component({
  selector: 'app-fees-sharing',
  templateUrl: './fees-sharing.component.html',
  styleUrls: ['./fees-sharing.component.scss']
})
export class FeesSharingComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<FeesSharingComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: ActivityService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService,
  ) { }

  ngOnInit(): void {
    console.log(this.data)
    this.dataSource.data = this.data.feeShareAttorneyReports
  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
  @ViewChild(MatPaginator) paginator: MatPaginator;
 @ViewChild(MatSort) sort: MatSort;

 applyFilter(event: Event) {
  const filterValue = (event.target as HTMLInputElement).value;
  this.dataSource.filter = filterValue.trim().toLowerCase();
}

 displayedColumns: string[] = [
  'matterNumber', 
  'timeKeeperCode',
  'feeSharingAmount',
  'feeSharingPercentage',
];

 dataSource = new MatTableDataSource<any>();
 selection = new SelectionModel<any>(true, []);
}
