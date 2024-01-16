import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/core';


  // import the pdfmake library
  import pdfMake from "pdfmake/build/pdfmake";
  // importing the fonts and icons needed
  import pdfFonts from "../../../../assets/font/vfs_fonts";
  import { defaultStyle } from "../../..//config/customStyles";
  import { fonts } from "../../../config/pdfFonts";
  import { HttpRequest, HttpResponse } from "@angular/common/http";
  import { workOrderLogo1 } from "../../../../assets/font/workorder-logo1.js";
  // PDFMAKE fonts
  pdfMake.vfs = pdfFonts.pdfMake.vfs;
  pdfMake.fonts = fonts;

@Component({
  selector: 'app-reports-list',
  templateUrl: './reports-list.component.html',
  styleUrls: ['./reports-list.component.scss']
})
export class ReportsListComponent implements OnInit {
  isChecked = true;
  isShowDiv = false;
  table = true;
  userType = this.auth.userTypeId;
  div1Function() {
    this.table = !this.table;
  }
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';
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
  showFiller = false;
  constructor(public auth: AuthService) { }

  ngOnInit(): void {
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

//   generatePdfBankDetails(){
  
//         //Receipt List
//           var dd: any;
//           let headerTable: any[] = [];
          
//           headerTable.push([
//             { image: workOrderLogo1.headerLogo1, fit: [180, 180], alignment: 'left',  bold: false, fontSize: 12, border: [false, false, false, false] },
//             { text: '', bold: false, fontSize: 13, border: [false, false, false, false] },
//             { text: '', bold: false, fontSize: 13, border: [false, false, false, false] },
//           ]);
         
//           dd = {
//             pageSize: "A4",
//             pageOrientation: "portrait",
//             pageMargins: [40, 95, 40, 60],
//             header(currentPage: number, pageCount: number, pageSize: any): any {
//               return [
//                 {
//                   table: {
//                     // layout: 'noBorders', // optional
//                     // heights: [,60,], // height for each row
//                     headerRows: 1,
//                     widths: ['*', 200, '*'],
//                     body: headerTable
//                   },
//                   margin: [20, 0, 20, 20]
//                 }
//               ]
//             },
//             styles: {
//               anotherStyle: {
//                 bordercolor: '#6102D3'
//               }
//             },
            
//             footer(currentPage: number, pageCount: number, pageSize: any): any {
//               return [
//               //   {
//               //   text: 'Page ' + currentPage + ' of ' + pageCount,
//               //   style: 'header',
//               //   alignment: 'center',
//               //   bold: false,
//               //   fontSize: 6
//               // }
//               { image: workOrderLogo1.headerLogo1, width: 550,  bold: false, margin: [20, -30, 0, 20], fontSize: 13, border: [false, false, false, false] },
//             ]
//             },
//             content: ['\n'],
//             defaultStyle
//           };
  
  
//           let termsArray: any[] = [];
//           termsArray.push([
           
//           ]);
          
//           dd.content.push(
//             {
//               table: {
//                 headerRows: 1,
//                 widths: [70, 70, 250, 100],
//                 body: termsArray
//               },
//               margin: [0, 70, 0, 20]
//             }
//           )
  
//           let header1Array: any[] = [];
//           header1Array.push([
//             { text: '', bold: false, fontSize: 16, border: [false, false, false, false],alignment:'left' },
//             { text: 'KUWAIT INTERNATIONAL BANK DETAILS:' , bold: true, alignment: 'center', fontSize: 20, border: [false, false, false, false]},
//             { text: ' ', bold: false, fontSize: 16, border: [false, false, false, false],alignment:'right' },
//           ]);
          
        
        
//         dd.content.push(
//           {
//             table: {
//               headerRows: 1,
//               widths: [ 70, 300, 100],
//               body: header1Array
//             },
//             margin: [0, -120, 0, 0]
//           }
//         )
//         let bodyArray: any[] = [];
//         bodyArray.push([
//           { text: 'Account Name :', bold: true, fontSize: 16, border: [false, false, false, false],alignment:'left' },
//           { text: 'UNILINK GENERAL TRADING AND CONTRACTING COMPANY' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, false]},
         
//         ]);
//         bodyArray.push([
//           { text: 'Account Number :', bold: true, fontSize: 16, border: [false, false, false, false],alignment:'left' },
//           { text: '011010161741' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, false]},
         
//         ]);
//         bodyArray.push([
//           { text: 'Bank :', bold: true, fontSize: 16, border: [false, false, false, false],alignment:'left' },
//           { text: 'Kuwait International Bank' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, false]},
         
//         ]);
//         bodyArray.push([
//           { text: 'Branch', bold: true, fontSize: 16, border: [false, false, false, false],alignment:'left' },
//           { text: 'Kuwait City-Head Office' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, false]},
         
//         ]);
//         bodyArray.push([
//           { text: 'Account Type :', bold: true, fontSize: 16, border: [false, false, false, false],alignment:'left' },
//           { text: 'Kuwait Dinar Current Account' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, false]},
         
//         ]);
//         bodyArray.push([
//           { text: 'IBAN No:', bold: true, fontSize: 16, border: [false, false, false, false],alignment:'left' },
//           { text: 'KW04KWIB0000000000011010161741' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, false]},
         
//         ]);
//         bodyArray.push([
//           { text: 'Swift Code', bold: true, fontSize: 16, border: [false, false, false, false],alignment:'left' },
//           { text: 'KWIBKWKW' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, false]},
         
//         ]);
//         dd.content.push(
//           {
//             table: {
//               headerRows: 1,
//               widths: [ 120,450],
//               body: bodyArray
//             },
//             margin: [0, 20, 0, 0]
//           }
//         )
  
  
//          // pdfMake.createPdf(dd).download('Payment Receipt');
//          pdfMake.createPdf(dd).open();
     
      
   
//   }
//  generatePdfJobCard(){

//     //Receipt List
//     var dd: any;
//     let headerTable: any[] = [];
    
//     headerTable.push([
//       { image: workOrderLogo1.headerLogo1, fit: [180, 180], alignment: 'left',  bold: false, fontSize: 12, border: [false, false, false, false] },
//       { text: '', bold: false, fontSize: 13, border: [false, false, false, false] },
//       { text: '', bold: false, fontSize: 13, border: [false, false, false, false] },
//     ]);
   
//     dd = {
//       pageSize: "A4",
//       pageOrientation: "portrait",
//       pageMargins: [40, 95, 40, 60],
//       header(currentPage: number, pageCount: number, pageSize: any): any {
//         return [
//           {
//             table: {
//               // layout: 'noBorders', // optional
//               // heights: [,60,], // height for each row
//               headerRows: 1,
//               widths: ['*', 200, '*'],
//               body: headerTable
//             },
//             margin: [20, 0, 20, 20]
//           }
//         ]
//       },
//       styles: {
//         anotherStyle: {
//           bordercolor: '#6102D3'
//         }
//       },
      
//       footer(currentPage: number, pageCount: number, pageSize: any): any {
//         return [
//         //   {
//         //   text: 'Page ' + currentPage + ' of ' + pageCount,
//         //   style: 'header',
//         //   alignment: 'center',
//         //   bold: false,
//         //   fontSize: 6
//         // }
//         { image: workOrderLogo1.headerLogo1, width: 550,  bold: false, margin: [20, -30, 0, 20], fontSize: 13, border: [false, false, false, false] },
//       ]
//       },
//       content: ['\n'],
//       defaultStyle
//     };


//     let header1Array: any[] = [];
//       header1Array.push([
//         { text: 'Team :'+' '+'234', bold: false, fontSize: 16, border: [false, false, false, false],alignment:'left' },
//         { text: '' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, false]},
//         { text: ' Date:'+'  '+'18-11-2022', bold: false, fontSize: 16, border: [false, false, false, false],alignment:'right' },
//       ]);
      
    
    
//     dd.content.push(
//       {
//         table: {
//           headerRows: 1,
//           widths: [ 70, 300, 100],
//           body: header1Array
//         },
//         margin: [0, -30, 0, 0]
//       }
//     )

//     let header2Array: any[] = [];
//     header2Array.push([
//       { text: '', bold: false, fontSize: 16, border: [false, false, false, false],alignment:'left' },
//       { text: '' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, false]},
//       { text: 'Work Time:'+'  '+'34 hrs', bold: false, fontSize: 16, border: [false, false, false, false],alignment:'right' },
//     ]);
//     dd.content.push(
//       {
//         table: {
//           headerRows: 1,
//           widths: [ 70, 305, 100],
//           body: header2Array
//         },
//         margin: [0, -10, 0, 0]
//       }
//     )
//     let body1Array: any[] = [];
//     body1Array.push([
//       { text: 'Customer Name', bold: false, fontSize: 16, border: [false, false, false, false],alignment:'left' },
//       { text: '' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, true],borderColor:['','','','#ddd']},
//       { text: '' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, false]},
//       { text: 'Number ', bold: false, fontSize: 16, border: [false, false, false, false] ,alignment:'left' },
//       { text: '' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, true],borderColor:['','','','#ddd']},

//     ]);
//     body1Array.push([
//       { text: 'From', bold: false, fontSize: 16, border: [false, false, false, false] ,alignment:'left' },
//       { text: '' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, true],borderColor:['','','','#ddd']},
//       { text: '' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, false]},
//       { text: 'Add . Num ', bold: false, fontSize: 16, border: [false, false, false, false] ,alignment:'left' },
//       { text: '' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false,true],borderColor:['','','','#ddd']},

//     ]);
//     body1Array.push([
//       { text:'To', bold: false, fontSize: 16,border: [false, false, false, false] ,alignment:'left' },
//       { text: '' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, true],borderColor:['','','','#ddd']},
//       { text: '' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, false]},
//       { text: ' S.Team ', bold: false, fontSize: 16, border: [false, false, false, false],alignment:'left' },
//       { text: '' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, true],borderColor:['','','','#ddd']},

//     ]);
//     dd.content.push(
//       {
//         table: {
//           headerRows: 1,
//           widths: [ 100,150,20,70,150],
//           body: body1Array
//         },
//         margin: [-20, 20, 0, 0]
//       }
//     )
//     let body2Array: any[] = [];
//     body2Array.push([
//       { text: 'Notes', bold: false, fontSize: 16, border: [false, false, false, false],alignment:'left' },
//       { text: '' , bold: false, alignment: 'left', fontSize: 16, border: [false, false, false, true],borderColor:['','','','#ddd']},
    

//     ]);
//     dd.content.push(
//       {
//         table: {
//           headerRows: 1,
//           widths: [ 100,'*'],
//           body: body2Array
//         },
//         margin: [-20, 0, 0, 0]
//       }
//     )
//     let body3Array: any[] = [];
//     body3Array.push([
//       { text: 'The Prices Attached Below Not Considered Final Prices, Prices Can Be Change As Per Use.', bold: false, fontSize: 16, border: [false, false, false, false],alignment:'center' },
      
//     ]);
//     dd.content.push(
//       {
//         table: {
//           headerRows: 1,
//           widths: ['*'],
//           body: body3Array
//         },
//         margin: [-20, 20, 0, 0]
//       }
//     )
//     let body4Array: any[] = [];
//     body4Array.push([
//       { text: 'Description', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: 'QTY ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: 'Item Price', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: 'Total Price' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: 'Notes  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
      
//     ]);
//     body4Array.push([
//       { text: 'Trips', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: 'MANUALLY', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
      
//     ]);
//     body4Array.push([
//       { text: 'Carton 65*45*65 Logo', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: 'DEPENDS ON IF ITS WITH SERVICE OR WITHOUT ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
      
//     ]);
//     body4Array.push([
//       { text: 'Carton 45*45*45 Logo', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: 'DEPENDS ON IF ITS WITH SERVICE OR WITHOUT ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: '120' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
      
//     ]);
//     body4Array.push([
//       { text: 'Carton 60*60*60 Logo', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: 'DEPENDS ON IF ITS WITH SERVICE OR WITHOUT ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: '120' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
      
//     ]);
//     body4Array.push([
//       { text: 'Carton File', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: 'DEPENDS ON IF ITS WITH SERVICE OR WITHOUT ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: '120' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//     ]);
//     body4Array.push([
//       { text: 'Hanger Box', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: 'DEPENDS ON IF ITS WITH SERVICE OR WITHOUT ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: '120' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//     ]);
//     body4Array.push([
//       { text: 'Bubble Wrap', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: 'DEPENDS ON IF ITS WITH SERVICE OR WITHOUT ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: '120' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//     ]);
//     body4Array.push([
//       { text: 'Nylon Roll', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: 'DEPENDS ON IF ITS WITH SERVICE OR WITHOUT ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: '120' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//     ]);
//     body4Array.push([
//       { text: 'Stretch Film', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: 'DEPENDS ON IF ITS WITH SERVICE OR WITHOUT ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: '120' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//     ]);
//     body4Array.push([
//       { text: 'Carton Roll', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: 'DEPENDS ON IF ITS WITH SERVICE OR WITHOUT ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: '120' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//     ]);
//     body4Array.push([
//       { text: 'Tape', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: 'DEPENDS ON IF ITS WITH SERVICE OR WITHOUT ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//       { text: '120' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//     ]);
//     body4Array.push([
//       { text: 'Total Amount', bold: false, fontSize: 14, border: [true,false,false,true],alignment:'center' },
//       { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [false, false,false,true] },
//       { text: ' ', bold: false, fontSize: 14, border: [false,false,true,true],alignment:'center' },
//       { text: '1080' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//       { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//     ]);
//     dd.content.push(
//       {
//         table: {
//           headerRows: 1,
//           widths: [100,30,250,60,70],
//           body: body4Array
//         },
//         margin: [-20, 30, 0, 0]
//       }
//     )
    
//    // pdfMake.createPdf(dd).download('Payment Receipt');
//    pdfMake.createPdf(dd).open();



    
//   }
//   generatePdfWorkSheet(){
    

//       //Receipt List
//       var dd: any;
//       let headerTable: any[] = [];
      
//       headerTable.push([
//         { image: workOrderLogo1.headerLogo1, fit: [180, 180], alignment: 'left',  bold: false, fontSize: 12, border: [false, false, false, false] },
//         { text: '', bold: false, fontSize: 13, border: [false, false, false, false] },
//         { text: '', bold: false, fontSize: 13, border: [false, false, false, false] },
//       ]);
     
//       dd = {
//         pageSize: "A4",
//         pageOrientation: "landscape",
//         pageMargins: [40, 95, 40, 60],
//         header(currentPage: number, pageCount: number, pageSize: any): any {
//           return [
//             {
//               table: {
//                 // layout: 'noBorders', // optional
//                 // heights: [,60,], // height for each row
//                 headerRows: 1,
//                 widths: ['*', 200, '*'],
//                 body: headerTable
//               },
//               margin: [20, 0, 20, 20]
//             }
//           ]
//         },
//         styles: {
//           anotherStyle: {
//             bordercolor: '#6102D3'
//           }
//         },
        
//         footer(currentPage: number, pageCount: number, pageSize: any): any {
//           return [
//           //   {
//           //   text: 'Page ' + currentPage + ' of ' + pageCount,
//           //   style: 'header',
//           //   alignment: 'center',
//           //   bold: false,
//           //   fontSize: 6
//           // }
//           { image: workOrderLogo1.headerLogo1, width: 550,  bold: false, margin: [20, -30, 0, 20], fontSize: 13, border: [false, false, false, false] },
//         ]
//         },
//         content: ['\n'],
//         defaultStyle
//       };


//       let body5Array: any[] = [];
//       body5Array.push([
//         { text: 'SN ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: 'Order Date ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: 'Services Agreement No.', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: 'Team Assigned' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: 'Job done on:  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: 'Location' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: ' Customer Contact Details ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: 'Mode of payment' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: 'Total Amount ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: 'Customer follow up' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: 'Remarks  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },

//       ]);
//       body5Array.push([
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: ' ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },

//       ]); 
//       body5Array.push([
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: ' ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },

//       ]); 
//       body5Array.push([
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: ' ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },

//       ]); 
//       body5Array.push([
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: ' ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },

//       ]); 
//       body5Array.push([
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: ' ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },

//       ]); 
//       body5Array.push([
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: ' ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },

//       ]); 
//       body5Array.push([
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: ' ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },

//       ]); 
//       body5Array.push([
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: ' ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },

//       ]); 
//       body5Array.push([
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: ' ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },

//       ]); 
//       body5Array.push([
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: ' ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },

//       ]);
//       body5Array.push([
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: ' ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },

//       ]); 
//       body5Array.push([
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: ' ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },

//       ]);
//       body5Array.push([
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: ' ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },

//       ]); 
//       body5Array.push([
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: ' ' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: ' ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },
//         { text: '' , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, true] },
//         { text: '  ', bold: false, fontSize: 14, border: [true, true, true, true],alignment:'center' },

//       ]); 
       
      
      
//       dd.content.push(
//         {
//           table: {
//             headerRows: 1,
//             widths: [20,35,80,'*','*','*',120,'*','*','*','*','*'],
//             body: body5Array
//           },
//           margin: [-20, -20, -90, 0]
//         }
//       )

     
    



//      // pdfMake.createPdf(dd).download('Payment Receipt');
//      pdfMake.createPdf(dd).open();
 
  
     
//   }
}
