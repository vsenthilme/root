import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ClientDocumentService } from '../../../client-document.service';

@Component({
  selector: 'app-clientportal-display',
  templateUrl: './clientportal-display.component.html',
  styleUrls: ['./clientportal-display.component.scss']
})
export class ClientportalDisplayComponent implements OnInit {
  public mask = [/\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]

  sub = new Subscription();

  constructor(
    public dialogRef: MatDialogRef<ClientportalDisplayComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: ClientDocumentService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
  ) { }

  ngOnInit(): void {
  }

  updateClient() {
    this.spin.show();
    this.sub.add(this.service.patchClientUser(this.data.clientDetails, this.data.clientDetails.clientUserId).subscribe((res: any[]) => {
      this.spin.hide();
      this.toastr.success(
        "Client User updated successfully",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );
      this.dialogRef.close();
    }, err => {
      this.spin.hide();
      this.cs.commonerror(err);
    }));
  }

}
