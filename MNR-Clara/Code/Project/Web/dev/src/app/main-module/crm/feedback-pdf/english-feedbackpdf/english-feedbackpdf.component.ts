import { Component,Injectable, OnInit } from '@angular/core';
import { DatePipe } from "@angular/common";
import { logo } from "../../../../../assets/font/logo.js";
import pdfFontsNew from "../../../../../assets/font/vfs_fonts.js";
import { defaultStyle } from "../../../../config/customStylesNew";
import { fonts1 } from "../../../..//config/pdfFontsNew";
import pdfMake from "pdfmake/build/pdfmake";
import { NgxSpinnerService } from 'ngx-spinner';
pdfMake.fonts = fonts1;
pdfMake.vfs = pdfFontsNew.pdfMake.vfs;


@Injectable({
  providedIn: 'root'
})


@Component({
  selector: 'app-english-feedbackpdf',
  templateUrl: './english-feedbackpdf.component.html',
  styleUrls: ['./english-feedbackpdf.component.scss']
})
export class EnglishFeedbackpdfComponent implements OnInit {

 
  constructor(private datePipe: DatePipe,  private spin: NgxSpinnerService,) { }

  ngOnInit(): void {
  }

  generatePdf(element: any) {

    console.log(element)
    var dd: any;
    let headerTable: any[] = [];
    
    headerTable.push([
      { image: logo.headerLogo, fit: [100, 100], bold: true, fontSize: 12, border: [false, false, false, false],margin:[10,0,0] },
     // { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
  //    { text: 'Monty & Ramirez LLP \n 150 W Parker Road, 3rd Floor Houston, TX 77076',  alignment: 'center', fontSize: 12, border: [false, false, false, false] },
       { text:   'Feedback Form', bold: true, alignment: "center", fontSize: 13, margin:[10,20,10,0], border: [false, false, false, false],color:'#666362' },
       { text:   '', bold: true, alignment: "center", fontSize: 13, margin:[10,20,10,0], border: [false, false, false, false],color:'#666362' },
     ]);
   
     dd = {
      pageSize: "A4",
      pageOrientation: "portrait",
      pageMargins: [40, 95, 40, 60],
      defaultStyle,
      header(currentPage: number, pageCount: number, pageSize: any): any {
        return [
          {
            table: {
              // layout: 'noBorders', // optional
              // heights: [,60,], // height for each row
              headerRows: 1,
              widths: [20,'*', 10],
              body: headerTable
            },
            margin: [20, 20, 20, 40]
          }
        ]
      },
      styles: {
        anotherStyle: {
          bordercolor: '#6102D3'
        }
      },
      footer(currentPage: number, pageCount: number, pageSize: any): any {
        return [{
          text: 'Page ' + currentPage + ' of ' + pageCount,
          style: 'header',
          alignment: 'center',
          bold: true,
          fontSize: 6
        }]
      },
      content: ['\n'],
     
    };
    // let headerArray4: any[] = [];
    // headerArray4.push([
    //   { text: 'Inquiry No- '+element.inquiryNumber, bold: true, border: [false, false, false, false], alignment: 'left', fontSize:13, color:'#267FA0'},
      
     
    // ]);
    // dd.content.push(
    //   {
    //     table: {
    //       headerRows: 1,
    //       widths: ['*'],
    //       body: headerArray4
    //     },
    //     margin: [-8, -10, 2, 0]
    //   }
    // )
   
      let headerArray79: any[] = [];
      headerArray79.push([
        { text: 'Overall, how was your experience with Monty & Ramirez?', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[2,2,2,2]  },
       
        { text: element.overallExperience, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[2,2,2,2] , alignment: "center" },
      
       
      ]);
      headerArray79.push([
        { text: 'How was your experience inquiring about our services?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'], margin:[2,2,2,2]  },
       
        { text:element.inquiryExperience, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,alignment:"center"  },
      
       
      ]);
      headerArray79.push([
        { text: 'How would you rate your interaction with the first person you spoke with? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'], margin:[2,2,2,2] },
       
        { text:element.firstPersonInteraction, bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] ,alignment:"center" },
      
       
      ]);
      headerArray79.push([
        { text: 'Was it easy to schedule your consultation?', bold: true, fontSize: 10, border: [true, true, true, false],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,margin:[2,2,2,2] },
       
        { text: element.consultationSchedule,bold: false, fontSize: 10, border: [true, false, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,alignment:"center"  },
      
       
      ]);
      headerArray79.push([
        { text: 'Did our staff do a good job following up with you and giving you reminders about your consultation? ', bold: true, fontSize: 10, border: [true, true, true, false],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,margin:[2,2,2,2]  },
       
        { text: element.staffFollowUp,bold: false, fontSize: 10, border: [true, false, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,alignment:"center"  },
      
       
      ]);
      headerArray79.push([
        { text: 'How satisfied were you with your consultation with your attorney? ', bold: true, fontSize: 10, border: [true, true, true, false],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,margin:[2,2,2,2]  },
       
        { text: element.attorneyConsultation,bold: false, fontSize: 10, border: [true, false, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,alignment:"center"  },
      
       
      ]);
      headerArray79.push([
        { text: 'How informative was your consultation? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] , margin:[2,2,2,2] },
       
        { text: element.consultationInformative,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,alignment:"center"  },
      
       
      ]);
      headerArray79.push([
        { text: 'How comfortable did you feel discussing your case with your attorney? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[2,2,2,2]   },
       
        { text: element.attorneyDiscussionComfortable,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,alignment:"center"  },
      
       
      ]);
      headerArray79.push([
        { text: 'Would you refer our firm to a friend or family member? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  ,margin:[2,2,2,2] },
       
        { text: element.referFriendOrFamily,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],alignment:"center"   },
      
       
      ]);
      headerArray79.push([
        { text: 'How can we improve? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,margin:[2,2,2,2]  },
       
        { text: element.improveRemark,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],alignment:"center"   },
      
       
      ]);
    
    
    
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [400,'*'],
            body: headerArray79
          },
          margin: [-3, 15, 10, 0]
        }
      )

    pdfMake.createPdf(dd).download('Intake Number : ' +  element.intakeFormNumber);
    pdfMake.createPdf(dd).open();

this.spin.hide();
}

}

