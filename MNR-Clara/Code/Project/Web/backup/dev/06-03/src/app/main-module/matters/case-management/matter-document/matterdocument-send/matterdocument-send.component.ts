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
import { ClientDocumentService } from 'src/app/main-module/client/client-document/client-document.service';
import { Location } from "@angular/common";
import { MatterDocumetService } from '../matter-documet.service';
@Component({
  selector: 'app-matterdocument-send',
  templateUrl: './matterdocument-send.component.html',
  styleUrls: ['./matterdocument-send.component.scss']
})
export class MatterdocumentSendComponent implements OnInit {

  screenid: 1093 | undefined;

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
  postUrl = '/doc-storage/upload'
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
  code!: string;
  pageType = '';

  documentNo!: string;
  constructor(public HttpClient: HttpClient,
    private location: Location,
    private sanitizer: DomSanitizer,
    private cs: CommonService,
    private router: Router,
    private route: ActivatedRoute,
    private auth: AuthService,
    private service: MatterDocumetService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService, public toastr: ToastrService,
  ) { }
  ngOnInit(): void {
    let code = this.route.snapshot.params.code;
    this.pageType = this.route.snapshot.params.pageType;
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

  async fill(code: any) {
    console.log(this.pageType)
    // code = 2000006;
    this.spin.show();
    this.sub.add(this.service.Get(code).subscribe(async ress => {

      this.spin.hide();

      // res.agreementUrl = '/A001-Agreement-Document_processed_v5.docx';
      // res.agreementUrl = "001-agreement-document_signed.pdf";
      // this.fileName = res.agreementUrl
      this.documentNo = ress.documentNo;
      //      this.locationfile = 'document/' + ress.documentNo + '_' + ress.matterNumber;//+ '_' + code;
    if (this.pageType == 'DOC_SIGN') {
      this.locationfile = 'document/' + ress.clientId + '/' + ress.matterNumber;
    }
    else{
      this.locationfile = 'clientportal/' + ress.clientId + '/' + ress.matterNumber;
    }
      if (!this.pageflowupload) {
console.log(ress.statusId)
        if(ress.statusId != 23 && ress.statusId != 22){
          this.sub.add(this.service.Get_documentTemplate(ress.documentNo).subscribe(async res => {

            if (res.mailMerge) {
  
              this.issend = true;
            } else {
              if (ress.statusId == 20) {
               // this.locationfile = 'document/' + ress.clientId + '/' + ress.matterNumber;
               this.locationfile = "document";
              }
  
              if (ress.statusId == 22) {
                this.locationfile = 'clientportal/' + ress.clientId + '/' + ress.matterNumber;
              }
  
  
            }
  
            this.spin.show();
            this.classId = res.classId
            this.fileName = !ress.documentUrl ? res.documentUrl : ress.documentUrl;
            const blob = await this.service.getfile1(this.classId, window.encodeURIComponent(this.fileName), this.locationfile)
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
        }
        else{
       //   this.sub.add(this.service.Get_documentTemplate(ress.documentNo).subscribe(async res => {

     
       
  
                this.locationfile = 'clientportal/' + ress.clientId + '/' + ress.matterNumber;
              
  
  
          
  
            this.spin.show();
            this.classId = ress.classId
            this.fileName = ress.documentUrl;
            const blob = await this.service.getfile1(this.classId, window.encodeURIComponent(this.fileName), this.locationfile)
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
  
          // }, err => {
          //   this.cs.commonerror(err);
          //   this.spin.hide();
          // }));
        }

    
      }


    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));


  }

  uploadFiles(files: File[]) {

    this.spin.show();
    this.sub.add(this.service.Get(this.code).subscribe(async ress => {

 //     this.sub.add(this.service.Get_documentTemplate(ress.documentNo).subscribe(async res => {

        if (this.pageType == 'DOC_SIGN') {
        //  alert(1)
          this.locationfile = 'document/' + ress.clientId + '/' + ress.matterNumber;
        }else{
        //  alert(2)
          this.locationfile = 'clientportal/' + ress.clientId + '/' + ress.matterNumber;
        }

        let url = '/mnr-management-service/matterdocument/' + ress.matterDocumentId + '/mailmerge/manual?' +
          'location=' + this.locationfile +
        //  '&documentNumber=' + ress.documentNo +
          '&classId=' + ress.classId;
        // if (ress.mailMerge)
        //   url = '/mnr-management-service/matterdocument/' + ress.matterNumber + '/nonMailmerge?' +
        //     'location=' + this.locationfile +
        //     '&documentNumber=' + ress.documentNo +
        //     '&classId=' + ress.classId;
        const config = new HttpRequest('PATCH',
          url
          , this.myFormData, {
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
              this.spin.hide();
            }
          },
            error => {

              this.cs.commonerror(error);
              this.spin.hide();

            });
      // },
      //   error => {

      //     this.cs.commonerror(error);
      //     this.spin.hide();

      //   }));
    },
      error => {

        this.cs.commonerror(error);
        this.spin.hide();

      }));
  }
  display(files: File[]): void {
    this.docurl = window.URL.createObjectURL(files[0]);

  }

  getDate() {
    return new Date()
  }

  post_agreement() {
    this.spin.show();
    if (this.pageType == 'DOC_SIGN') {
      this.sub.add(this.service.Get(this.code).subscribe(res => {
        this.sub.add(this.service.send_document(res.classId, res.documentNo, res.matterNumber,).subscribe(res => {
          let response = res;
          response.statusId = 12;
          this.sub.add(this.service.Update(response, this.code,).subscribe(res => {
            this.toastr.success("Document Sent Successfully to DocuSign.", "Notification", {
              timeOut: 2000,
              progressBar: false,
            });
            this.spin.hide();
            this.location.back();
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
    } else {
      this.sub.add(this.service.Get(this.code).subscribe(res => {
        res.statusId = 22;
        this.sub.add(this.service.Update(res, this.code).subscribe(res => {
          this.toastr.success("Document Sent Successfully to client portal.", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });
          this.spin.hide();
          this.location.back();
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
  cancel() {
    this.location.back();
  }
  print() {
    window.print();
  }
}

