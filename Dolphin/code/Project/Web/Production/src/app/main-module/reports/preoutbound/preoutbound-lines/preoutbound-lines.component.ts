import { SelectionModel } from '@angular/cdk/collections';
import { DatePipe } from '@angular/common';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { DeliveryService } from 'src/app/main-module/Outbound/delivery/delivery.service';
import { PreoutboundService } from 'src/app/main-module/Outbound/preoutbound/preoutbound.service';

@Component({
  selector: 'app-preoutbound-lines',
  templateUrl: './preoutbound-lines.component.html',
  styleUrls: ['./preoutbound-lines.component.scss']
})
export class PreoutboundLinesComponent implements OnInit {


  constructor(private fb: FormBuilder,
    public dialogRef: MatDialogRef<any>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private auth: AuthService,
    private service: PreoutboundService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    public cs: CommonService, public dialog: MatDialog,
    public datePipe: DatePipe) { }



    @ViewChild(MatSort, { static: true })
    sort!: MatSort;
    @ViewChild(MatPaginator, { static: true })
    paginator: MatPaginator; // Pagination
    // Pagination
    
  ngOnInit(): void {
    this.spin.show();
    this.service.searchLine({ refDocNumber: [this.data.refDocNumber], preOutboundNo: [this.data.preOutboundNo], warehouseId: [this.data.warehouseId] }).subscribe(res => {

      res.forEach((x: any) => {
        if (!x.deliveryQty)
          x.deliveryQty = 0;
        if (!x.referenceField10)
          x.referenceField10 = 0;
        if (!x.referenceField9)
          x.referenceField9 = 0;

      });

      this.dataSource = new MatTableDataSource<any>(res);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      res.forEach((x: any) => {
        x.deliveryQty == null ? x.deliveryQty = 0 : x.deliveryQty;
      });

      this.spin.hide();
      // this.getclient_class(this.form.controls.classId.value);
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    });
  }

  displayedColumns: string[] = [ 'status','lineNumber','ordercategory','storecode', 'product', 'description',  'order', 'uom', 'req',];
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    if (this.isAllSelected()) {
      this.selection.clear();
      return;
    }

    this.selection.select(...this.dataSource.data);
  }

  /** The label for the checkbox on the passed row */
  checkboxLabel(row?:  any): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.no + 1}`;
  }
   clearselection(row: any) {
    this.selection.clear();
    this.selection.toggle(row);
  }

}

