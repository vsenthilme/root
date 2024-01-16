import { SelectionModel } from '@angular/cdk/collections';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { DomSanitizer } from '@angular/platform-browser';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { DownloadComponent } from 'src/app/common-field/dialog_modules/download/download.component';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { documentTemplateService } from 'src/app/main-module/setting/business/document-template/document-template.service';
import { GeneralMatterService } from '../../../General/general-matter.service';
import { MatterDocumetService } from '../../../matter-document/matter-documet.service';

@Component({
  selector: 'app-document-checklist',
  templateUrl: './document-checklist.component.html',
  styleUrls: ['./document-checklist.component.scss']
})
export class DocumentChecklistComponent implements OnInit {

  sub = new Subscription();



  constructor(
    public dialogRef: MatDialogRef<DocumentChecklistComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private cs: CommonService,
  //private service: GeneralMatterService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    public fb: FormBuilder,
  private service: documentTemplateService,
    private cas: CommonApiService,
    private auth: AuthService,
    private sanitizer: DomSanitizer,
    public dialog: MatDialog,
    public downloadService: MatterDocumetService
  ) { }

  matter: string;
  ngOnInit(): void {
    

    console.log(this.data.document)
    if (this.data.matterNumber) {
      // this.matter = ' Matter - (' + this.data.matter + ') - ';
      this.matter = '' + this.data.matterNumber + ' / ' + this.data.matterdesc + ' - ';
      // this.form.controls.matterNumber.disable();

    }
    this.dataSource.data = this.data.document
  }
  disabled = false;
  step = 0;

  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
  }

  prevStep() {
    this.step--;
  }

  panelOpenState = false;
  displayedColumns: string[] = ['updatedOn', 'partner', 'documentUrl', 'statusId', 'download'];
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);


  locationfile: any;
  fileUrldownload: any;
  docurl: any;
  async download(element: any) {
    console.log(element)
   // this.sub.add(this.service.searchMatterDocListHeader({ checkListNo: [this.data.checklistNo] }).subscribe(async ress => {
      this.spin.hide();
     // this.documentNo = ress.documentNo;
      //this.locationfile = `document/checklist`;

      if(element.statusId == 60){
        this.locationfile = 'clientportal/temp/' + element.clientId + '/' + this.data.matterNumber + '/' + this.data.checklistNo;
      }else{
        this.locationfile = 'clientportal/' + element.clientId + '/' + this.data.matterNumber + '/' + this.data.checklistNo;
      }
      this.spin.show();
      const blob = await this.downloadService.getfile(this.locationfile, element.documentUrl)
        .catch((err: HttpErrorResponse) => {
          this.cs.commonerror(err);
        });
      this.spin.hide();
      if (blob) {
        const blobOb = new Blob([blob], {
          type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        });
        this.fileUrldownload = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blobOb));
        this.docurl = window.URL.createObjectURL(blob);
      }
      this.downloadConfirm(element)
    // }, err => {
    //   this.cs.commonerror(err);
    //   this.spin.hide();
    // }));
  }



  downloadConfirm(element): void {
    // if (this.selection.selected.length === 0) {
    //   this.toastr.warning("Kindly select any Row", "Notification", {
    //     timeOut: 2000,
    //     progressBar: false,
    //   });
    //   return;
    // }
    const dialogRef = this.dialog.open(DownloadComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '80%',
      position: { top: '9%', },
      data: { url: this.fileUrldownload, fileName: element.documentUrl}
    });

    dialogRef.afterClosed().subscribe(result => {
     // this.search();
    });
  }

}
