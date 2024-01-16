import { Component, Injectable, OnInit } from '@angular/core';
import { logo } from "../../../../../assets/font/logo.js";
import pdfFontsNew from "../../../../../assets/font/vfs_fonts.js";
import { defaultStyle } from "../../../../config/customStylesNew";
import { fonts1 } from "../../../..//config/pdfFontsNew";
import { SpanishService } from "src/app/customerform/spanish/spanish.service";
import { DatePipe } from "@angular/common";
import pdfMake from "pdfmake/build/pdfmake";
import { NgxSpinnerService } from 'ngx-spinner';
pdfMake.fonts = fonts1;
pdfMake.vfs = pdfFontsNew.pdfMake.vfs;

@Injectable({
  providedIn: 'root'
})

@Component({
  selector: 'app-spanish-feedbackpdf',
  templateUrl: './spanish-feedbackpdf.component.html',
  styleUrls: ['./spanish-feedbackpdf.component.scss']
})
export class SpanishFeedbackpdfComponent implements OnInit {


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
        { text: 'En general, ¿cómo fue su experiencia con Monty & Ramirez', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[2,2,2,2]  },
       
        { text: element.overallExperience, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[2,2,2,2] , alignment: "center" },
      
       
      ]);
      headerArray79.push([
        { text: '¿Cómo fue su experiencia al solicitar nuestros servicios?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'], margin:[2,2,2,2]  },
       
        { text:element.inquiryExperience, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,alignment:"center"  },
      
       
      ]);
      headerArray79.push([
        { text: '¿Cómo calificaría su interacción con la primera persona con la que habló? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'], margin:[2,2,2,2] },
       
        { text:element.firstPersonInteraction, bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] ,alignment:"center" },
      
       
      ]);
      headerArray79.push([
        { text: '¿Fue fácil programar su consulta?', bold: true, fontSize: 10, border: [true, true, true, false],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,margin:[2,2,2,2] },
       
        { text: element.consultationSchedule,bold: false, fontSize: 10, border: [true, false, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,alignment:"center"  },
      
       
      ]);
      headerArray79.push([
        { text: '¿Nuestro personal hizo un buen trabajo de seguimiento y recordatorio de su consulta? ', bold: true, fontSize: 10, border: [true, true, true, false],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,margin:[2,2,2,2]  },
       
        { text: element.staffFollowUp,bold: false, fontSize: 10, border: [true, false, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,alignment:"center"  },
      
       
      ]);
      headerArray79.push([
        { text: '¿Cuán satisfecho estuvo con la consulta con su abogado? ', bold: true, fontSize: 10, border: [true, true, true, false],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,margin:[2,2,2,2]  },
       
        { text: element.attorneyConsultation,bold: false, fontSize: 10, border: [true, false, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,alignment:"center"  },
      
       
      ]);
      headerArray79.push([
        { text: '¿Fue su consulta informativa? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] , margin:[2,2,2,2] },
       
        { text: element.consultationInformative,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,alignment:"center"  },
      
       
      ]);
      headerArray79.push([
        { text: '¿Qué tan cómodo se sintió discutiendo su caso con su abogado? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[2,2,2,2]   },
       
        { text: element.attorneyDiscussionComfortable,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,alignment:"center"  },
      
       
      ]);
      headerArray79.push([
        { text: '¿Recomendaría nuestro bufete a un amigo o familiar? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  ,margin:[2,2,2,2] },
       
        { text: element.referFriendOrFamily,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],alignment:"center"   },
      
       
      ]);
      headerArray79.push([
        { text: '¿Cómo podemos mejorar? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,margin:[2,2,2,2]  },
       
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
