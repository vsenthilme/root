import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { jsPDF } from "jspdf";
import html2canvas from 'html2canvas';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { QuotationService } from '../quotation.service';

@Component({
  selector: 'app-quotation-pdf',
  templateUrl: './quotation-pdf.component.html',
  styleUrls: ['./quotation-pdf.component.scss']
})
export class QuotationPdfComponent implements OnInit {

  date = new Date();
  constructor(
    private auth: AuthService,
    private service: QuotationService,
    private servicecom : QuotationService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,

    private cs: CommonService,) { }

  ngOnInit(): void {
    let code = this.route.snapshot.params.code;
    if (code != 'new') {
      let js = this.cs.decrypt(code);
      this.fill(js);
    }
  }
  element: any;
  elementc: any;

  fill(data: any): void {
    this.spin.show();
    this.sub.add(this.service.Getquotation(data.code.quotationNo, data.code.quotationRevisionNo).subscribe(res => {

      this.element = res;
      this.spin.hide();
      this.spin.show();
      this.sub.add(this.servicecom.Getclient(res.clientId).subscribe(res => {
  
        this.elementc = res;
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));

  
  }

  title = 'html-to-pdf-angular-application';
  public convertToPDF()
  {
  html2canvas(document.getElementById("pdf")!).then(canvas => {
  // Few necessary setting options
   
  const contentDataURL = canvas.toDataURL('image/png')
  var imgWidth = 210; 
  var pageHeight = 295;  
  var imgHeight = canvas.height * imgWidth / canvas.width;
  var heightLeft = imgHeight;
  var pdf = new jsPDF('p', 'mm');
  var position = 0;
  pdf.addImage(contentDataURL, 'PNG', 0, position, imgWidth, imgHeight);
  heightLeft -= pageHeight;
  while (heightLeft >= 0) {
    position = heightLeft - imgHeight;
    pdf.addPage();
    pdf.addImage(contentDataURL, 'PNG', 0, position, imgWidth, imgHeight);
    heightLeft -= pageHeight;
  }
  
  pdf.save('Quotation.pdf');
  });
  }

  sub = new Subscription();
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  toggleFloat() {

    this.isShowDiv = !this.isShowDiv;
    this.toggle = !this.toggle;

    if (this.icon === 'expand_more') {
      this.icon = 'chevron_left';
    } else {
      this.icon = 'expand_more'
    }
    this.showFloatingButtons = !this.showFloatingButtons;
    console.log('show:' + this.showFloatingButtons);
  }
}