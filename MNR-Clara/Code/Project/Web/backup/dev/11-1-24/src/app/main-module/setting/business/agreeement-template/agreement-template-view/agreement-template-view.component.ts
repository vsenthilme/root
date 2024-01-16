import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonService } from 'src/app/common-service/common-service.service';
import { PotentialService } from 'src/app/main-module/crm/potential/potential.service';
import { Location } from "@angular/common";
@Component({
  selector: 'app-agreement-template-view',
  templateUrl: './agreement-template-view.component.html',
  styleUrls: ['./agreement-template-view.component.scss']
})
export class AgreementTemplateViewComponent implements OnInit {
  docurl: string;
  fileUrldownload: any;
  public icon = 'expand_more';
  toggle = true;
  isShowDiv = false;
  showFloatingButtons: any;
  fileName: any;
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
  flow = 'agreement';
  constructor(private spin: NgxSpinnerService, public toastr: ToastrService,
    private service: PotentialService, private cs: CommonService, private route: ActivatedRoute, private location: Location,
    private sanitizer: DomSanitizer,) { }
    docurlpdf: any;
    
  ngOnInit(): void {
    let code = this.route.snapshot.params.code;
    let js = this.cs.decrypt(code);
    if (js.documentUrl) {
      this.flow = 'document';
      this.fill(js.documentUrl);
    }
    else
      this.fill(js.agreementUrl);

  }
  async fill(data: any) {
    this.spin.show();
    this.fileName = data;
    const blob = await this.service.getfile(this.flow, window.encodeURIComponent(data))
  
      .catch((err: HttpErrorResponse) => {

        this.cs.commonerror(err);
      });
    this.spin.hide();
 
    if (blob) {
    
      const blobOb = new Blob([blob], {
        type: "application/pdf",


      });
      this.fileUrldownload = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blobOb));
      http://remote.url.tld/path/to/document.doc&embedded=true
      this.docurl = window.URL.createObjectURL(blob);
       this.docurlpdf = window.URL.createObjectURL(blob);
      // const url = window.URL.createObjectURL(blob); this.docurl = url + '.docx';
    }
  }
  back() {
    this.location.back();
  }
}
