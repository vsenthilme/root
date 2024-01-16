
import {
  HttpClient, HttpRequest,
  HttpResponse, HttpEvent, HttpErrorResponse
} from '@angular/common/http'
import { Component, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { DomSanitizer } from '@angular/platform-browser';
import { Router, ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from "rxjs";
import { NotesComponent } from "src/app/common-field/notes/notes.component";
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { PotentialService } from '../potential.service';
interface MyObj {
  potentialClientId: string;
  agreementCode: string;
  pageflow: string;
}

@Component({
  selector: 'app-agreement-document',
  templateUrl: './agreement-document.component.html',
  styleUrls: ['./agreement-document.component.scss']
})
export class AgreementDocumentComponent implements OnInit {
  screenid: 1078 | undefined;

  public icon = 'expand_more';
  toggle = true;

  isShowDiv = false;
  showFloatingButtons: any;
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
  docurl: any;
  docurlpdf: any;
  files: File[] = []
  //postUrl = '/doc-storage/upload'
  postUrl = '/doc-storage/nonMailMergeUpload'
  myFormData!: FormData//populated by ngfFormData directive
  // httpfileUrldownloadEvent!: HttpEvent<{}> = new HttpEvent<{}>();
  fileUrldownload: any;
  uploadPercent = 0
  issend = false;
  accept = '*'
  isupload = true;

  progress: number | undefined;
  //url = 'https://evening-anchorage-3159.herokuapp.com/api/'

  //url = 'https://evening-anchorage-3159.herokuapp.com/api/'
  url = '/upload'
  hasBaseDropZoneOver: boolean = false
  httpEmitter: Subscription = new Subscription();
  sendableFormData!: FormData; //populated via ngfFormData directive
  //populated via ngfFormData directive
  sub = new Subscription();
  dragFiles: any
  validComboDrag: any
  lastInvalids: any
  fileDropDisabled: any
  maxSize: any
  baseDropValid: any
  pageflowupload = false;
  pageflow = '';
  potentialClientId!: string;
  agreementCode!: string;
  constructor(public HttpClient: HttpClient,

    private sanitizer: DomSanitizer,
    private cs: CommonService,
    private router: Router,
    private route: ActivatedRoute,
    private auth: AuthService,
    private service: PotentialService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService, public toastr: ToastrService,
  ) { }
  ngOnInit(): void {
    let code = this.route.snapshot.params.code;
    let js: MyObj = this.cs.decrypt(code);
    this.pageflow = js.pageflow
    if (js.pageflow == "upload")
      this.pageflowupload = true;
    this.fill(js.potentialClientId);

    this.potentialClientId = js.potentialClientId;
    this.agreementCode = js.agreementCode;

  }
  locationfile = '';
  fileName = '';
  classId='';
  async fill(code: any) {
    // code = 2000006;
    this.spin.show();
    this.sub.add(this.service.Get(code).subscribe(async ress => {

      this.spin.hide();

      // res.agreementUrl = '/A001-Agreement-Document_processed_v5.docx';
      // res.agreementUrl = "001-agreement-document_signed.pdf";
      // this.fileName = res.agreementUrl
      this.locationfile = 'agreement/' + ress.potentialClientId;
      this.classId =  ress.classId
      if (!this.pageflowupload) {


        this.sub.add(this.service.Get_agreementTemplate(ress.agreementCode).subscribe(async res => {

          if (res.mailMerge) {

            this.issend = true;
          } else {
            if (ress.statusId == 19 || ress.statusId == 20)
              this.locationfile = 'agreement';
          }

          this.spin.show();

          this.fileName = !ress.agreementUrl ? res.agreementUrl : ress.agreementUrl;
          const blob = await this.service.getfile1(this.classId,   window.encodeURIComponent(this.fileName), this.locationfile,)
            .catch((err: HttpErrorResponse) => {

              this.cs.commonerror(err);
              this.fileName = "";
            });
          this.spin.hide();
          if (blob) {
            const blobOb = new Blob([blob], {
              type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",


            });
            this.fileUrldownload = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blobOb));
            // http://remote.url.tld/path/to/document.doc&embedded=true
            this.docurl = window.URL.createObjectURL(blob);
            this.docurlpdf = window.URL.createObjectURL(blob);


            // const url = window.URL.createObjectURL(blob); this.docurl = url + '.docx';
          }

        }, err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }));
      }


    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));


  }

  uploadFiles(files: File[]): Subscription {
    console.log(files)

    const config = new HttpRequest('POST', this.postUrl + `?location=${this.locationfile}&classId=${this.classId}&loginUserID=${this.auth.userID}`,  this.myFormData, {
      reportProgress: true
    })
    return this.HttpClient.request(config)
      .subscribe(event => {
        // this.httpEvent = event


        if (event instanceof HttpResponse) {
          this.issend = true;
          this.toastr.success("Document uploaded successfully.", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
          this.isupload = false;
        }
      },
        error => {

          this.cs.commonerror(error);

        })
  }
  display(files: File[]): void {
    this.docurl = window.URL.createObjectURL(files[0]);

  }

  getDate() {
    return new Date()
  }

  post_agreement() {
    this.spin.show();

    this.sub.add(this.service.post_agreement(this.potentialClientId, { agreementCode: this.agreementCode }).subscribe(res => {
      this.toastr.success("Document Sent Successfully to DocuSign.", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });

      this.spin.hide();
      this.sub.add(this.service.Get(this.potentialClientId).subscribe(res => {

        this.spin.hide();
        let response = res;
        response.statusId = 12;
        this.sub.add(this.service.Update(response, this.potentialClientId).subscribe(res => {

          this.spin.hide();

          this.router.navigate(['main/crm/potential']);

        }, err => {

          this.cs.commonerror(err);
          this.spin.hide();

        }));


      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));

    }, err => {

      this.cs.commonerror(err);
      this.spin.hide();

    }));

  }
  cancel() {
    this.router.navigate(['main/crm/potential']);
  }
  print() {
    window.print();
  }
}

