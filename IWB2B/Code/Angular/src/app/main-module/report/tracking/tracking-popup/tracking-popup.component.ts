import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { UserprofileNewComponent } from 'src/app/main-module/admin/user-profile/userprofile-new/userprofile-new.component';
import { JntOrdersService } from 'src/app/main-module/jnt-orders/jnt-orders.service';

@Component({
  selector: 'app-tracking-popup',
  templateUrl: './tracking-popup.component.html',
  styleUrls: ['./tracking-popup.component.scss']
})
export class TrackingPopupComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<TrackingPopupComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private auth: AuthService, 
    private cs: CommonService, 
    private service: JntOrdersService
  ) { }


  result: any[] = [];
  ngOnInit(): void {
    this.spin.show();
    this.service.getConsignmentTracking(this.data.code.reference_number).subscribe(res => {
      res.events.forEach((element, index) => {
       if(index == 0){
        element['color'] = '#05769f';
       }
      });
     this.result = res.events;
     this.spin.hide();
    }, err => {
      this.spin.hide();      
this.cs.commonerror(err);
    })
  }

 
}
