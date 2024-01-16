import { HttpErrorResponse, HttpRequest, HttpResponse, HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Router, ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { DocumentTemplateService } from '../../document-template.service';

@Component({
  selector: 'app-document-view',
  templateUrl: './document-view.component.html',
  styleUrls: ['./document-view.component.scss']
})
export class DocumentViewComponent implements OnInit {

  constructor(public HttpClient: HttpClient,
    private location: Location,
    private sanitizer: DomSanitizer,
    private cs: CommonService,
    private router: Router,
    private route: ActivatedRoute,
    private auth: AuthService,
    private service: DocumentTemplateService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService, public toastr: ToastrService,
  ) { }

  sub = new Subscription();
  pageflowupload = false;
  pageflow = '';
  code!: string;
  ngOnInit(): void {
    let code = this.route.snapshot.params.code;
    let js = this.cs.decrypt(code);
    this.pageflow = js.pageflow
    if (js.pageflow == "upload")
      this.pageflowupload = true;
    this.fill(js.code);

    this.code = js.code;

  }
  locationfile = '';
  fileName = '';
  classId = '';
  docurl: any;
  docurlpdf: any;
  files: File[] = []
  postUrl = '/doc-storage/upload'
  myFormData!: FormData//populated by ngfFormData directive
  // httpfileUrldownloadEvent!: HttpEvent<{}> = new HttpEvent<{}>();
  fileUrldownload: any;
  uploadPercent = 0
  issend = false;
  accept = '*'
  documentNo!: string;
  async fill(code: any) {
    // code = 2000006;
    this.spin.show();
    this.sub.add(this.service.GetmatterDocument(code).subscribe(async ress => {

      this.spin.hide();

      // res.agreementUrl = '/A001-Agreement-Document_processed_v5.docx';
      // res.agreementUrl = "001-agreement-document_signed.pdf";
      // this.fileName = res.agreementUrl
      this.documentNo = ress.documentNo;
//      this.locationfile = 'document/' + ress.documentNo + '_' + ress.matterNumber;//+ '_' + code;
this.locationfile = 'document/' + ress.clientId + '/' + ress.matterNumber;



        this.sub.add(this.service.Get_documentTemplate(ress.documentNo).subscribe(async res => {


          this.spin.show();
          this.classId =  res.classId
          this.fileName = !ress.documentUrl ? res.documentUrl : ress.documentUrl;
          const blob = await this.service.getfile1(this.classId,  window.encodeURIComponent(this.fileName), this.locationfile)
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


            // const url = window.URL.createObjectURL(blob); this.docurl = url + '.docx';
          }

        }, err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }));



    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));


  }

}
