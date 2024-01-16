import { SelectionModel } from '@angular/cdk/collections';
import { HttpClient, HttpErrorResponse, HttpRequest, HttpResponse } from '@angular/common/http';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormControl, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { FormService } from 'src/app/customerform/form.service';
import { OwnershipService } from '../../ownership.service';
import { BehaviorSubject } from 'rxjs';
import { ConfirmComponent } from 'src/app/common-field/dialog_modules/confirm/confirm.component';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-upload-files',
  templateUrl: './upload-files.component.html',
  styleUrls: ['./upload-files.component.scss']
})
export class UploadFilesComponent implements OnInit {


  ELEMENT_DATA: any[] = [];
  constructor(public dialogRef: MatDialogRef<any>, public dialog: MatDialog, private cas: CommonApiService, private fservice: FormService, private fb: FormBuilder, private auth: AuthService, private cs: CommonService,
    private router: Router, private service: OwnershipService, private sanitizer: DomSanitizer,
    private spin: NgxSpinnerService, public toastr: ToastrService, @Inject(MAT_DIALOG_DATA) public data: any,
    private http: HttpClient,) { }


  FormLine = this.fb.group({
    requestId: [],
    file: [],
    id: [],
    fileName: [],
    statusId: [],
    uploadedBy: [],
    uploadedOn: [],
  });

  rows: FormArray = this.fb.array([this.FormLine]);

  form = this.fb.group({
    formLine: this.rows,
  });




  ngOnInit(): void {
console.log(this.data)
    if (this.data.pageflow == "summaryView") {
      console.log(this.data)
      this.displayedColumns = ['fileName', 'status'];
    }

    if (this.data.code) {
      this.service.Get1(this.data.code).subscribe(res => {
        if (res.length > 0) {
          this.rows.clear();
          res.forEach((d: any) => this.addRow(d, false));
        }
        else {
          this.updateView();
        }
      });
    } else {
      this.updateView();
    }
  }
  @ViewChild(MatSort)
  sort: MatSort;
  @ViewChild(MatPaginator)
  paginator: MatPaginator; // Pagination



  displayedColumns = ['chooseFile', 'fileName', 'status'];
  dataSource = new BehaviorSubject<AbstractControl[]>([]);



  addRow(d?: any, noUpdate?: boolean) {
    const row = this.fb.group({
      requestId: [],
      file: [],
      id: [],
      fileName: [],
      statusId: [],
      uploadedBy: [],
      download: [],
      uploadedOn: [],
    });
    if (d)
      row.patchValue(d);

    this.rows.push(row);
    if (!noUpdate) { this.updateView(); }
  }

  updateView() {
    this.dataSource.next(this.rows.controls);
  }

  remove(element: any) {
  }

  save() {
    let obj: any = {};
    obj.lines = this.form.getRawValue().formLine;
    obj.pageFlow = '2';
    this.dialogRef.close(obj);
  }


  file: File;
  onFilechange(event: any, element: any) {
    console.log(this.file);
    const dialogRef = this.dialog.open(ConfirmComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '80%',
      position: {
        top: '6.5%'
      },
      data: { title: "Confirm", message: "Are you sure you want to upload this " + event.target.files[0].name + " ?" }
    });
    dialogRef.afterClosed().subscribe(result => {
      this.file = event.target.files[0];

      this.spin.show();
      this.service.uploadfile(this.file, 'sharepoint/qb').subscribe((resp: any) => {
        this.toastr.success("File uploaded successfully.", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        element.controls.fileName.patchValue(resp.file)
        element.controls.statusId.patchValue(1)
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      })
    })
  }

back(){
  let obj: any = {};
    obj.lines = this.form.getRawValue().formLine;
}
  fileUrldownload: any;
  docurl: any;
  async download(element) {
    this.spin.show()
    const blob = await this.service.download(element.controls.fileName.value)
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
      const a = document.createElement('a')
      a.href = this.docurl
      a.download = element.controls.fileName.value;
      a.click();
      URL.revokeObjectURL(this.docurl);

    }
    this.spin.hide();
  }

  delete(element, index) {
    this.service.Delete1(element.controls.requestId.value, element.controls.id.value).subscribe(res => {
      this.toastr.success("File deleted successfully.", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.removerow(index)
    })
  }


  removerow(x: any) {
    this.rows.removeAt(x)
    this.reset();
    this.dataSource.next(this.rows.controls);
  }

  reset() {
    let i = 0;

    this.rows.value.forEach((ro: any) => { ro.id = ++i });
    this.rows.patchValue(this.rows.value);
  }
}
