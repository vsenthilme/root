import { SelectionModel } from '@angular/cdk/collections';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { DialogExampleComponent } from 'src/app/common-field/innerheader/dialog-example/dialog-example.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { InboundConfirmationService } from 'src/app/main-module/Inbound/inbound-confirmation/inbound-confirmation.service';
import { HandlingEquipmentService } from 'src/app/main-module/Masters -1/other-masters/handling-equipment/handling-equipment.service';

@Component({
  selector: 'app-inbound-lines',
  templateUrl: './inbound-lines.component.html',
  styleUrls: ['./inbound-lines.component.scss']
})
export class InboundLinesComponent implements OnInit {
  inboundline: any[] = [];
  selectedinboundline : any[] = [];
  @ViewChild('inboundlineTag') inboundlineTag: Table | any;
  constructor(
    public dialogRef: MatDialogRef<DialogExampleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: InboundConfirmationService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    private fb: FormBuilder,
    public cs: CommonService,
  ) { }
  sub = new Subscription();
  ngOnInit(): void {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.warehouseId, this.data.preInboundNo, this.data.refDocNumber).subscribe(res => {

      res.inboundLine.forEach((x: any) => {
        if (!x.damageQty)
          x.damageQty = 0;
        if (!x.acceptedQty)
          x.acceptedQty = 0;
        x.varianceQty = x.orderQty as number - (x.acceptedQty as number
          + x.damageQty as number)

         // x.qtyreceived =     element.approvedAmount = Math.round(((element.approvedHours / element.timeTicketHours) * element.timeTicketAmount) * 100) / 100;
      });
      this.spin.hide();
      this.inboundline =res.inboundLine;

   
    }))
  }


  
  
  ELEMENT_DATA: any[] = [];
  displayedColumns: string[] = [  'lineNo', 'itemCode', 'description', 'manufacturerPartNo',
  'orderQty', 'acceptedQty', 'damageQty', 'varianceQty'];




 


 


  
  submit(){
    if (this.selectedinboundline.length === 0) {
      this.toastr.warning("Kindly select any Row", "Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    this.dialogRef.close(this.selectedinboundline[0].customerCode);
  }
  
}

