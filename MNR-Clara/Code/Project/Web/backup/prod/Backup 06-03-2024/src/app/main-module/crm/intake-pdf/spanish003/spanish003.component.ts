import { Component, Injectable, OnInit } from '@angular/core';
import { logo } from "../../../../../assets/font/logo.js";
import pdfFontsNew from "../../../../../assets/font/vfs_fonts.js";
import { defaultStyle } from "../../../../config/customStylesNew";
import { fonts1 } from "../../../../config/pdfFontsNew";
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
  selector: 'app-spanish003',
  templateUrl: './spanish003.component.html',
  styleUrls: ['./spanish003.component.scss']
})

export class Spanish003Component implements OnInit {

  constructor(private datePipe: DatePipe,  private spin: NgxSpinnerService,) { }

  ngOnInit(): void {
  }

  generatePdf(element: any, intakeRes: any, referral: any) {
    var dd: any;
    let headerTable: any[] = [];
    
    headerTable.push([
      { image: logo.headerLogo, fit: [100, 100], bold: true, fontSize: 12, border: [false, false, false, false],margin:[10,0,0] },
     // { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
  //    { text: 'Monty & Ramirez LLP \n 150 W Parker Road, 3rd Floor Houston, TX 77076',  alignment: 'center', fontSize: 12, border: [false, false, false, false] },
       { text:   'PC - Information Sheet - '+ intakeRes.intakeFormNumber, bold: true, alignment: "right", fontSize: 13, margin:[20,0,0], border: [false, false, false, false],color:'#666362' },
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
              widths: ['*','*'],
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
    let headerArray2: any[] = [];
    headerArray2.push([
      { text: 'Detalles Personales ', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:12, color:'#FFFFFF' ,fillColor: '#267FA0'},
      
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*'],
          body: headerArray2
        },
        margin: [0, -20,2, 0]
      }
    )
    let headerArray6: any[] = [];
    headerArray6.push([
      { text: 'Nombre Completo ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:   element.name, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']},
      
     
     
    ]);headerArray6.push([
      { text: 'Calle Dirección  ', bold: true, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[0,5,0,0] },
      { text:   element.address, bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] ,margin:[0,5,0,0]},
     
    
     
    ]);
   
    headerArray6.push([
      { text: 'Ciudad ', bold: true, fontSize: 10, border: [true, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.city, bold: false, fontSize: 10,  border: [false, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray6.push([
      { text: 'Estado ', bold: true, fontSize: 10, border: [true, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.state, bold: false, fontSize: 10,  border: [false, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray6.push([
      { text: 'código postal ', bold: true, fontSize: 10, border: [true, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.zipcode, bold: false, fontSize: 10,  border: [false, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray6.push([
      { text: 'Vive en esta dirección? ', bold: true, fontSize: 10, border: [true, false, true, true]  , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.doYouLiveAtThisAddress == true ?' Si' : 'No', bold: false, fontSize: 10,  border: [false, false, true, true]  , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray6
        },
        margin: [0, 10, 2, 0]
      }
    )
    let headerArray777: any[] = [];
    headerArray777.push([
      { text: 'No. de Teléfono', bold: true, fontSize: 10, border: [true, true, true, true]  ,  borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text: 'Trabajo :'+'\t'+ element.contactNumber.workNo    , bold: false, fontSize: 10,  border: [true, true, true, true]  ,  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: ' Celular:'+ '\t'+element.contactNumber.cellNo , bold: false, fontSize: 10,  border: [true, true, true, true]  ,  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
    
     
    
     
    ]);
    headerArray777.push([
      { text: '  ', bold: false, fontSize: 10, border: [true, false, false, true]  ,  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: ' Directa / Hogar:'+'\t'+ element.contactNumber.homeNo   , bold: false, fontSize: 10,  border: [true, true, true, true]  ,  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.contactNumber.faxNo== null ? 'Fax:':'Fax:'+element.contactNumber.faxNo, bold: false, fontSize: 10,  border: [true, true, true, true]  , borderColor: ['#808080', '#808080', '#808080', '#808080'] }, 
    
     
    ]);
    headerArray777.push([
      { text: '  ', bold: false, fontSize: 10, border: [true, false, false, true]  ,  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.emailAddress== null ? 'Correo Electrónico : ':'Correo Electrónico : '+element.emailAddress , bold: false, fontSize: 10,  border: [true, true, true, true]  , borderColor: ['#808080', '#808080', '#808080', '#808080'] }, 
      { text: ' Contactar a :'+'\t'+element.contactPersonName, bold: false, fontSize: 10,  border: [true, true, true, true]  , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*','*'],
          body: headerArray777
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray778: any[] = [];
    headerArray778.push([
      { text: '¿Le gustaría recibir información sobre asuntos de inmigración? ', bold: true, fontSize: 10, border: [true, false, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:  element.doNeedImmigrationUpdates == true ?' Si' : 'No', bold: false, fontSize: 10,  border: [false, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray778
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray3: any[] = [];
    headerArray3.push([
      { text: 'Detalles del caso ', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:13, color:'#FFFFFF' ,fillColor: '#267FA0'},
      
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*'],
          body: headerArray3
        },
        margin: [0, 15, 2, 0]
      }
    )
    let headerArray7: any[] = [];
    headerArray7.push([
      { text: '¿Cómo se enteró de nuestro buffet de abogados? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:    referral.find(y => y.value == element.referenceMedium.listOfMediumAboutFirm)?.label, bold: false, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080']},
      
     
     
    ]);headerArray7.push([
      { text: 'Esta consulta es para  ', bold: true, fontSize: 10, border: [true, true, true, false], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.thisConsultationIsFor, bold: false, fontSize: 10,  border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    headerArray7.push([
      { text: 'Otra', bold: true, fontSize: 10, border: [true, true, false, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.referenceMedium.referredByOthers, bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    
    headerArray7.push([
      { text: 'Objetivo de la visita. ¿Cual es el propósito de tu visita?', bold: true, fontSize: 10, border: [true, false, true, false] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: element.purposeOfVisit, bold: false, fontSize: 10,  border: [false, false, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
   
    headerArray7.push([
      { text: '  ', bold: false, fontSize: 10, border: [true, false, false, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:   '  ', bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray7.push([
      { text: '¿Tiene actualmente un abogado(a)? ', bold: true, fontSize: 10, border: [true, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: element.doHaveImmigrationAttorney == true ?' Si' : 'No', bold: false, fontSize: 10,  border: [false, false, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    if(element.doHaveImmigrationAttorney == true ){
      headerArray7.push([
        { text: 'En caso afirmativo, complete el nombre del abogado ', bold: true, fontSize: 10, border: [true, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text: element.nameOfAttorney, bold: false, fontSize: 10,  border: [false, false, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
      
       
      ]);
    }
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray7
        },
        margin: [0, 10, 2, 0]
      }
    )
    let headerArray4: any[] = [];
    headerArray4.push([
      { text: 'Detalles generales del cliente', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:13, color:'#FFFFFF' ,fillColor: '#267FA0'},
      
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*'],
          body: headerArray4
        },
        margin: [0, 15, 5, 0]
      }
    )
    let headerArray8: any[] = [];
    headerArray8.push([
      { text: 'Nombre Completo ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:   element.clientGeneralInfo.clientPersonalInfo.fullName, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      
     
     
    ]);headerArray8.push([
      { text: 'Otros nombres o aliases', bold: true, fontSize: 10, border: [true, true, true, false] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.clientPersonalInfo.otherNamesUsed, bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray8.push([
      { text: 'Fecha de Nacimiento', bold: true, fontSize: 10, border: [true, true, false, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: this.datePipe.transform(element.clientGeneralInfo.clientPersonalInfo.dateOfBirth, 'MM-dd-yyyy'), bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    
    headerArray8.push([
      { text: 'Ciudad y País de Nacimiento', bold: true, fontSize: 10, border: [true, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: element.clientGeneralInfo.clientPersonalInfo.cityAndCountryOfBirth, bold: false, fontSize: 10,  border: [false, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
   
    headerArray8.push([
      { text: 'País de su Ciudadanía:  ', bold: true, fontSize: 10, border: [true, false, false, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.clientPersonalInfo.countryOfCitizenship, bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray8.push([
      { text: 'Estado Civil ', bold: true, fontSize: 10, border: [true, false, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.maritalStatus, bold: false, fontSize: 10,  border: [false, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray8.push([
      { text: 'Si esta casado(a) o compromet', bold: true, fontSize: 10, border: [false, false, false, false] ,margin:[0,5,0,5], color:'#267FA0' , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  'ido(a), favor de completar la siguiente sección: ', bold: true, fontSize: 10,  border: [false, true, false, false],margin:[-10,5,0,5], color:'#267FA0',  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray8.push([
      { text: 'Nombre de Cónyuge o prometido(a)', bold: true, fontSize: 10,  border: [true, true, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: element.clientGeneralInfo.spouseOrFiancePersonalInfo.fullName, bold: false, fontSize: 10,  border: [true, true, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
     
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray8
        },
        margin: [0, 10, 2, 0]
      }
    ) 
    let headerArray9999: any[] = [];
    headerArray9999.push([
      { text: 'Fecha de Nacimiento', bold: true, fontSize: 10,  border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: this.datePipe.transform(element.clientGeneralInfo.spouseOrFiancePersonalInfo.dateOfBirth, 'MM-dd-yyyy'), bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray9999
        },
        margin: [0, -1, 2, 0]
      }
    ) 
    let headerArray150: any[] = [];
     headerArray150.push([
      { text: 'Ciudad y País de Nacimiento ', bold: true, fontSize: 10, border: [true, true, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:   element.clientGeneralInfo.spouseOrFiancePersonalInfo.cityAndCountryOfBirth, bold: false, fontSize: 10, border: [true, true, true, true],margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      
     
     
    ]);headerArray150.push([
      { text: '¿Son residentes permanentes legales o USC?', bold: true, fontSize: 10, border: [true, true, true, true],margin:[0,0,0,0] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.permanentResidentOrUSC == true ?' Si' : 'No' , bold: false, fontSize: 10,  border: [true, true, true, true] ,margin:[0,0,0,0] , borderColor: ['#808080', '#808080', '#808080', '#808080']},
     
    
     
    ]);
   
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray150
        },
        margin: [0, -1, 2, 0]
      }
    ) 
    
    let headerArray9998: any[] = [];
    headerArray9998.push([
      { text: 'Fecha de Matrimonio', bold: true, fontSize: 10, border: [true, true, false, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  this.datePipe.transform(element.clientGeneralInfo.dateOfMarriage, 'MM-dd-yyyy') , bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray9998
        },
        margin: [0, -1, 2, 0]
      }
    ) 
    let headerArray10: any[] = [];
    headerArray10.push([
      { text: '¿En qué ciudad y país se casó?', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text: element.clientGeneralInfo.whichCityAndCountryDidYouMarryIn, bold: false, fontSize: 10,  border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
   
    headerArray10.push([
      { text: 'Lugar de Residencia  ', bold: true, fontSize: 10, border: [true, false, false, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.placeOfResidence, bold: false, fontSize: 10,  border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    headerArray10.push([
      { text: '¿Alguna vez su cónyuge ha sido condenado por un delito? ', bold: true, fontSize: 10, border: [true, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: element.clientGeneralInfo.hasSpouseEverbeenConvictedOfACrime == true ?' Si' : 'No' , bold: false, fontSize: 10,  border: [false, false, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    if(element.clientGeneralInfo.spouseCrimeNotes!=''){
      headerArray10.push([
        { text: 'Notes:', bold: true, fontSize: 10, border: [true, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text:element.clientGeneralInfo.spouseCrimeNotes , bold: false, fontSize: 10,  border: [false, false, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
      
       
      ]);
    }
    headerArray10.push([
      { text: 'Si esta divorciado(a) o es viud', bold: true, fontSize: 10, border: [false, false, false, true] ,margin:[0,5,0,5],color:'#267FA0',  borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:  'o(a), favor de completar la siguiente sección: ', bold: true, fontSize: 10,  border: [false, false, false, true],margin:[-14.5,5,0,5],color:'#267FA0' , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray10.push([
      { text: 'Nombre del/la excónyuge', bold: true, fontSize: 10,  border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.divorcedOrWidowedPersonalInfo.fullName, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0] ,  borderColor: ['#808080', '#808080', '#808080', '#808080']},
     
    
     
    ]);
    headerArray10.push([
      { text: 'Ciudad y País de Nacimiento', bold: true, fontSize: 10,  border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:  element.clientGeneralInfo.divorcedOrWidowedPersonalInfo.cityAndCountryOfBirth, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray10.push([
      { text: 'Ciudadanía', bold: true, fontSize: 10,  border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.divorcedOrWidowedPersonalInfo.countryOfCitizenship, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    
    headerArray10.push([
      { text: 'Fecha de Matrimonio', bold: true, fontSize: 10,  border: [true, false, true, true] ,margin:[0,0,0,0] , borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text: this.datePipe.transform(element.clientGeneralInfo.dateOfMarriageOfDivorcedOrWidowed, 'MM-dd-yyyy') , bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    headerArray10.push([
      { text: '¿Es el/ella un residente permanente? ', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:  element.clientGeneralInfo.permanentResident == true ?' Si' : 'No', bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    if(element.clientGeneralInfo.permanentResident == true){
      headerArray10.push([
        { text: 'Si la Respuesta es afrmative ', bold: true, fontSize: 10,   border: [true, false, false, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080']},
        { text: "" , bold: false, fontSize: 10,  border: [false, true, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
      
       
      ]);
      headerArray10.push([
        { text: '¿Qué Año? ', bold: true, fontSize: 10,  border: [true, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080']},
        { text: element.clientGeneralInfo.yearOfResidency==0?'':element.clientGeneralInfo.yearOfResidency , bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
      
       
      ]);
      headerArray10.push([
        { text: '¿Cómo obtuvo la residencia? ', bold: true, fontSize: 10,  border: [true, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080']},
        { text: element.clientGeneralInfo.residencyObtainedThrough , bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
      
       
      ]);
    }
    headerArray10.push([
      { text: '¿Tiene padre o madre con ciu', bold: true, fontSize: 10,  border: [false, false, false, true] ,margin:[0,5,0,5],color:'#267FA0', borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:  'dadanía americana? Si la Respuesta es afirmativa :', bold: true, fontSize: 10,  border: [false, false, false, true] ,margin:[-18,5,0,5], color:'#267FA0',  borderColor: ['#808080', '#808080', '#808080', '#808080']},
     
    
     
    ]);
    headerArray10.push([
      { text: '¿Cómo obtuvo su padre/madre la ciudadanía estadounidense?', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.americanCitizenshipObtainedThrough, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray10.push([
      { text: '¿Qué Año?', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.yearOfCitizenship==0?'':element.clientGeneralInfo.yearOfCitizenship, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray10.push([
      { text: '¿Qué edad tenia usted?', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.ageOfClient==0?'':element.clientGeneralInfo.ageOfClient, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray10.push([
      { text: '¿Alguien ha presentado alguna vez una petición de visa de inmigrante para usted o sus padres?', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.hasAnyoneEverFiledImmigrantVisaPetition== true ?' Si' : 'No', bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    if(element.clientGeneralInfo.visaPetitionNotes!=''){
      headerArray10.push([
        { text: 'Notes:', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text:  element.clientGeneralInfo.visaPetitionNotes, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
      
       
      ]);
    }
    if(element.clientGeneralInfo.hasAnyoneEverFiledImmigrantVisaPetition== true){
      headerArray10.push([
        { text: 'En caso afirmativo, ¿quién presentó la solicitud por su padre?', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text:  element.clientGeneralInfo.relationshipOfFiledPersonForParents+ '    ' +this.datePipe.transform(element.clientGeneralInfo.datePetitionFiled, 'MM-dd-yyyy')==''?'':this.datePipe.transform(element.clientGeneralInfo.datePetitionFiled, 'MM-dd-yyyy'), bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
      
       
      ]);
      headerArray10.push([
        { text: 'Persona que presentó la petición', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text:  element.clientGeneralInfo.nameOfFiledPersonForParents, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
      
       
      ]);
      headerArray10.push([
        { text: 'Resultado', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text:  element.clientGeneralInfo.resultOfFiledPetition, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
      
       
      ]);
    }
    headerArray10.push([
      { text: 'Si tiene hijos(as)/hijastros(as)', bold: true, fontSize: 10,  border: [false, false, false, true] ,margin:[0,5,0,5], color:'#267FA0', borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  ',por favor de completar la siguiente sección:', bold: true, fontSize: 10,  border: [false, false, false, true] ,margin:[-16.75,5,0,5] , color:'#267FA0', borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray10
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray950: any[] = [];
    headerArray950.push([
      { text: '1.Nobre del Hijo(a)/Hijastro(a) ', bold: true, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:   element.clientGeneralInfo.child1.nameOfChild, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: '2.Nobre del Hijo(a)/Hijastro(a)', bold: true, fontSize: 10, border: [true, true, true, false], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:   element.clientGeneralInfo.child2.nameOfChild, bold: false, fontSize: 10,  border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      
     
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray950
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray951: any[] = [];
    headerArray951.push([
      { text: 'Ciudad y País Natal ', bold: true, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:  element.clientGeneralInfo.child1.cityAndCountryOfBirth, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'Ciudad y País Natal', bold: true, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.child2.cityAndCountryOfBirth, bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray951
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray952: any[] = [];
    headerArray952.push([
      { text: 'Fecha de Nacimiento ', bold: true, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:  this.datePipe.transform(element.clientGeneralInfo.child1.dateOfBirth, 'MM-dd-yyyy') , bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text: 'Fecha de Nacimiento', bold: true, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:    this.datePipe.transform(element.clientGeneralInfo.child2.dateOfBirth, 'MM-dd-yyyy'), bold: false, fontSize: 10,  border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray952
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray953: any[] = [];
    headerArray953.push([
      { text: 'años ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.child1.age==0?'':element.clientGeneralInfo.child1.age, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'años', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:   element.clientGeneralInfo.child2.age==0?'':element.clientGeneralInfo.child2.age, bold: false, fontSize: 10,  border: [true, true, true, true] ,  borderColor: ['#808080', '#808080', '#808080', '#808080']},
     
    
     
    ]); dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray953
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray954: any[] = [];
    headerArray954.push([
      { text: 'Vive su hijo(a) con Ud.? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.child1.doesChildLiveWithYou==true?' Si' : 'No', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'Vive su hijo(a) con Ud.?', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:   element.clientGeneralInfo.child2.doesChildLiveWithYou==true?' Si' : 'No', bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray954
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray955: any[] = [];
    headerArray955.push([
      { text: '3.Nobre del Hijo(a)/Hijastro(a) ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.child3.nameOfChild, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: '4.Nobre del Hijo(a)/Hijastro(a)', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:  element.clientGeneralInfo.child4.nameOfChild, bold: false, fontSize: 10,  border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
     
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray955
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray956: any[] = [];
    headerArray956.push([
      { text: 'Ciudad y País Natal ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.child3.cityAndCountryOfBirth, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'Ciudad y País Natal', bold: true, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.child4.cityAndCountryOfBirth, bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray956
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray957: any[] = [];
    headerArray957.push([
      { text: 'Fecha de Nacimiento ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:  this.datePipe.transform(element.clientGeneralInfo.child3.dateOfBirth, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'Fecha de Nacimiento', bold: true, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: this.datePipe.transform(element.clientGeneralInfo.child4.dateOfBirth, 'MM-dd-yyyy'), bold: false, fontSize: 10,  border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray957,
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray998: any[] = [];
    headerArray998.push([
      { text: 'años ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.child3.age==0?'':element.clientGeneralInfo.child3.age, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'años', bold: true, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.child4.age==0?'':element.clientGeneralInfo.child4.age, bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
  
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray998,
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray999: any[] = [];
    headerArray999.push([
      { text: 'Vive su hijo(a) con Ud.? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.child3.doesChildLiveWithYou==true?' Si' : 'No', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'Vive su hijo(a) con Ud.?', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:   element.clientGeneralInfo.child4.doesChildLiveWithYou==true?' Si' : 'No', bold: false, fontSize: 10,  border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray999,
        },
        margin: [0, -1, 2, 0]
      }
    )
   
 
   
    let headerArray966: any[] = [];
    headerArray966.push([
      { text: '5.Nobre del Hijo(a)/Hijastro(a) ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:    element.clientGeneralInfo.child5.nameOfChild, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: '6.Nobre del Hijo(a)/Hijastro(a)', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:    element.clientGeneralInfo.child6.nameOfChild, bold: false, fontSize: 10,  border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      
     
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray966,
        },
        margin: [0,-1, 2, 0]
      }
    )
    let headerArray967: any[] = [];
    headerArray967.push([
      { text: 'Ciudad y País Natal ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:    element.clientGeneralInfo.child5.cityAndCountryOfBirth, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'Ciudad y País Natal', bold: true, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.child6.cityAndCountryOfBirth, bold: false, fontSize: 10,  border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray967,
        },
        margin: [0,-1, 2, 0]
      }
    )
    let headerArray968: any[] = [];
    headerArray968.push([
      { text: 'Fecha de Nacimiento ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   this.datePipe.transform(element.clientGeneralInfo.child5.dateOfBirth, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'Fecha de Nacimiento', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:   this.datePipe.transform(element.clientGeneralInfo.child6.dateOfBirth, 'MM-dd-yyyy'), bold: false, fontSize: 10,  border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray968,
        },
        margin: [0,-1, 2, 0]
      }
    )
    let headerArray969: any[] = [];
    headerArray969.push([
      { text: 'años ', bold: true, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:  element.clientGeneralInfo.child5.age==0?'':element.clientGeneralInfo.child5.age, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'], },
      { text: 'años', bold: true, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.child6.age==0?'':element.clientGeneralInfo.child6.age, bold: false, fontSize: 10,  border: [true, true, true, true] ,  borderColor: ['#808080', '#808080', '#808080', '#808080']},
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray969,
        },
        margin: [0,-1, 2, 0]
      }
    )
    let headerArray970: any[] = [];
    headerArray970.push([
      { text: 'Vive su hijo(a) con Ud.? ', bold: true, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:    element.clientGeneralInfo.child5.doesChildLiveWithYou==true?' Si' : 'No', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'Vive su hijo(a) con Ud.?', bold: true, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:    element.clientGeneralInfo.child6.doesChildLiveWithYou==true?' Si' : 'No', bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray970,
        },
        margin: [0,-1, 2, 0]
      }
    )
    let headerArray971: any[] = [];
    headerArray971.push([
      { text: '7.Nobre del Hijo(a)/Hijastro(a) ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.child7.nameOfChild, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: '8.Nobre del Hijo(a)/Hijastro(a)', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:  element.clientGeneralInfo.child8.nameOfChild, bold: false, fontSize: 10,  border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
     
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray971,
        },
        margin: [0,-1, 2, 0]
      }
    )
    let headerArray972: any[] = [];
    headerArray972.push([
      { text: 'Ciudad y País Natal ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:    element.clientGeneralInfo.child7.cityAndCountryOfBirth, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text: 'Ciudad y País Natal', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:   element.clientGeneralInfo.child8.cityAndCountryOfBirth, bold: false, fontSize: 10,  border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray972,
        },
        margin: [0,-1, 2, 0]
      }
    )
    let headerArray973: any[] = [];
    headerArray973.push([
      { text: 'Fecha de Nacimiento ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:   this.datePipe.transform(element.clientGeneralInfo.child7.dateOfBirth, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text: 'Fecha de Nacimiento', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:  this.datePipe.transform(element.clientGeneralInfo.child8.dateOfBirth, 'MM-dd-yyyy'), bold: false, fontSize: 10,  border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray973,
        },
        margin: [0,-1, 2, 0]
      }
    )
    let headerArray974: any[] = [];
    headerArray974.push([
      { text: 'años ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.child7.age==0?'':element.clientGeneralInfo.child7.age, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text: 'años', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:   element.clientGeneralInfo.child8.age==0?'':element.clientGeneralInfo.child8.age, bold: false, fontSize: 10,  border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray974,
        },
        margin: [0,-1, 2, 0]
      }
    )
    let headerArray976: any[] = [];
    headerArray976.push([
      { text: 'Vive su hijo(a) con Ud.? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:    element.clientGeneralInfo.child7.doesChildLiveWithYou==true?' Si' : 'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text: 'Vive su hijo(a) con Ud.?', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:   element.clientGeneralInfo.child8.doesChildLiveWithYou==true?' Si' : 'No', bold: false, fontSize: 10,  border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: ['*','*','*','*'],
          body: headerArray976,
        },
        margin: [0,-1, 2, 0]
      }
    )
    dd.content.push(
      {
        stack: [{
          text: '',
          alignment: 'center',
          fontSize: 12,
          lineHeight: 1.3,
          bold: true,
         
        }]
      })
      let headerArray975: any[] = [];
      headerArray975.push([
        { text: '', bold: true, fontSize: 10, border: [false, false, false, false],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text:  '', bold: false, fontSize: 10, border: [false, false, false, false],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       
      
       
      ]);
      if(element.clientGeneralInfo.childNotes!=''){
        headerArray975.push([
          { text: 'Notes: ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
          { text:    element.clientGeneralInfo.childNotes, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
         
        
         
        ]);
      }
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray975,
          },
          margin: [0,-6, 2, 0]
        }
      )
      let headerArray5: any[] = [];
      headerArray5.push([
        { text: 'Cuestionario ', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:12, color:'#FFFFFF' ,fillColor: '#267FA0'},
        
       
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: ['*'],
            body: headerArray5
          },
          margin: [0, 10, 10, 0]
        }
      )
      let headerArray12: any[] = [];
      headerArray12.push([
        { text: 'Fechas de entradas y salidas a/de los Estados Unidos: ', bold: true, fontSize: 10, border: [false, false, false, false],color: '#267FA0',margin:[0,0,0,0] },
      
        
       
       
      ]);headerArray12.push([
        { text: 'Si no sabe las fechas exactas, ponga el primer día del mes  ', bold: true, fontSize: 10, border: [false, false, false, true] ,color: '#267FA0',margin:[0,0,0,0] },
       
       
      
       
      ]);
    
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: ['*'],
            body: headerArray12
          },
          margin: [0, 6, 10, 0]
        }
      )
      let headerArray13: any[] = [];
      headerArray13.push([
        { text: 'S.No ', bold: true, fontSize: 10, border: [true, true, true, false],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text: 'Entrado', bold: true, fontSize: 10, border: [true, true, true, false], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text: 'Salido', bold: true, fontSize: 10, border: [true, true, true, false] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] ,},
        { text: '¿Entró con visa? ', bold: true, fontSize: 10, border: [true, true, true, false],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
       
       
      ]);headerArray13.push([
        { text: '1 ', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text:  this.datePipe.transform(element.clientGeneralInfo.enteredDates.enteredDate1, 'MM-dd-yyyy') , bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text: this.datePipe.transform(element.clientGeneralInfo.exitedDates.exitedDate1, 'MM-dd-yyyy') , bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text: element.clientGeneralInfo.visaEntry.didYouComeEnterWithVISA1== true?"Si":"No", bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
       
      
       
      ]);
      headerArray13.push([
        { text: '2 ', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text:  this.datePipe.transform(element.clientGeneralInfo.enteredDates.enteredDate2, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text: this.datePipe.transform(element.clientGeneralInfo.exitedDates.exitedDate2, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text: element.clientGeneralInfo.visaEntry.didYouComeEnterWithVISA2== true?"Si":"No", bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
       
      
       
      ]);
      headerArray13.push([
        { text: '3 ', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text:  this.datePipe.transform(element.clientGeneralInfo.enteredDates.enteredDate3, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text: this.datePipe.transform(element.clientGeneralInfo.exitedDates.exitedDate3, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text: element.clientGeneralInfo.visaEntry.didYouComeEnterWithVISA3== true?"Si":"No", bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
       
      
       
      ]);
      headerArray13.push([
        { text: '4 ', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text: this.datePipe.transform(element.clientGeneralInfo.enteredDates.enteredDate4, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text: this.datePipe.transform(element.clientGeneralInfo.exitedDates.exitedDate4, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text:  element.clientGeneralInfo.visaEntry.didYouComeEnterWithVISA4== true?"Si":"No", bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
       
      
       
      ]);
      headerArray13.push([
        { text: '5 ', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text:  this.datePipe.transform(element.clientGeneralInfo.enteredDates.enteredDate5, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text: this.datePipe.transform(element.clientGeneralInfo.exitedDates.exitedDate5, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text:  element.clientGeneralInfo.visaEntry.didYouComeEnterWithVISA5== true?"Si":"No", bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
       
      
       
      ]);
    
      
    
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: ['*','*','*','*'],
            body: headerArray13
          },
          margin: [0, -1, 10, 0]
        }
      )
     
      let headerArray1007: any[] = [];
      headerArray1007.push([
        { text: '6 ', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text:this.datePipe.transform(element.clientGeneralInfo.enteredDates.lastEenteredDate, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text: this.datePipe.transform(element.clientGeneralInfo.exitedDates.lastExitedDate, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text: element.clientGeneralInfo.visaEntry.didYouComeEnterWithVISA6== true?"Si":"No", bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
      ]);
   
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: ['*','*','*','*'],
            body: headerArray1007
          },
          margin: [0, -1, 10, 0]
        }
      )
      if(element.clientGeneralInfo.entryExitNotes!=''){
      let headerArray979: any[] = [];
      headerArray979.push([
        { text: '', bold: true, fontSize: 10, border: [false, false, false, false],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text:  '', bold: false, fontSize: 10, border: [false, false, false, false],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       
      
       
      ]);
      if(element.clientGeneralInfo.childNotes!=''){
        headerArray979.push([
          { text: 'Notes: ', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
          { text:element.clientGeneralInfo.entryExitNotes, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
         
        
         
        ]);
      }
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray979,
          },
          margin: [0,-6, 10, 0]
        }
      )
    }
    
      let headerArray100: any[] = [];
      headerArray100.push([
        { text: 'A. Primera llegada a EUA ', bold: true, fontSize: 10, border: [false, false, false, false],color: '#267FA0',margin:[0,5,0,5] },
        { text: ' ', bold: false, fontSize: 10, border: [false, false, false, false] },
        
       
       
      ]);headerArray100.push([
        { text: 'Fecha Mes/Año', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        { text: element.clientGeneralInfo.firstDateOfEntry.dateOfEntry, bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       
      ]);
      headerArray100.push([
        { text: 'Lugar de llegada', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        { text: element.clientGeneralInfo.firstDateOfEntry.placeOfArrival, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray100
          },
          margin: [0, -1, 10, 0]
        }
      )
      let headerArray191: any[] = [];
      headerArray191.push([
        { text: 'Fue inspeccionado por un oficial de inmigración? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        { text: element.clientGeneralInfo.firstDateOfEntry.wereYouInspectedByAnImmigrationOfficer==true?"Si":"No", bold: false, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      
       
      ]);

      if(element.clientGeneralInfo.firstDateOfEntry.wereYouInspectedByAnImmigrationOfficer!=true){
        headerArray191.push([
          { text: 'Si no, ¿cómo ingresó a los Estados Unidos? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
          { text: element.clientGeneralInfo.firstDateOfEntry.unitedStatesEnteredThrough==''?'':element.clientGeneralInfo.firstDateOfEntry.unitedStatesEnteredThrough+'     '+element.clientGeneralInfo.firstDateOfEntry.kindOfDocuments==''?'':element.clientGeneralInfo.firstDateOfEntry.kindOfDocuments, bold: false, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        
         
        ]);
  
      }
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray191
          },
          margin: [0, -1, 10, 0]
        }
      )
      let headerArray101: any[] = [];
      headerArray101.push([
        { text: 'B. Última llegada a EUA ', bold: true, fontSize: 10, border: [false, true, false, false],color: '#267FA0',margin:[0,5,0,5] },
        { text: ' ',bold: false, fontSize: 10, border: [false, true, false, false] },
        
       
       
      ]);headerArray101.push([
        { text: 'Fecha Mes/Año', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        { text: element.clientGeneralInfo.lastDateOfEntry.dateOfEntry, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       
      ]);
      headerArray101.push([
        { text: 'Lugar de llegada: ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        { text: element.clientGeneralInfo.lastDateOfEntry.placeOfArrival,bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       
      ]);
      headerArray101.push([
        { text: 'Fue inspeccionado por un oficial de inmigración? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        { text: element.clientGeneralInfo.lastDateOfEntry.wereYouInspectedByAnImmigrationOfficer== "true" ?' Si' :  element.clientGeneralInfo.lastDateOfEntry.wereYouInspectedByAnImmigrationOfficer == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       
      ]);
      if(element.clientGeneralInfo.lastDateOfEntry.wereYouInspectedByAnImmigrationOfficer!= "true"){
        headerArray101.push([
          { text: 'Si no, ¿cómo ingresó a los Estados Unidos? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
          { text: element.clientGeneralInfo.lastDateOfEntry.unitedStatesEnteredThrough==''?'': element.clientGeneralInfo.lastDateOfEntry.unitedStatesEnteredThrough+'     '+element.clientGeneralInfo.lastDateOfEntry.kindOfDocuments==''?'':element.clientGeneralInfo.lastDateOfEntry.kindOfDocuments, bold: false, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        
         
        ]);
      }
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray101
          },
          margin: [0, -1, 10, 0]
        }
      )
      let headerArray102: any[] = [];
      headerArray102.push([
        { text: '¿Vive actualmente en los Estados Unidos?', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        { text: element.clientGeneralInfo.questions.areYouCurrentlyLivingInUSA== "true" ?' Si' :  element.clientGeneralInfo.lastDateOfEntry.wereYouInspectedByAnImmigrationOfficer == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
      
       
      ]);
      if(element.clientGeneralInfo.questions.areYouCurrentlyLivingInUSA== "true" ){
        headerArray102.push([
          { text: 'Vives en los EE. UU. Desde', bold: true, fontSize: 10, border: [true, true, false, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text:'', bold: false, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
        headerArray102.push([
          { text: 'Mes', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text:element.clientGeneralInfo.questions.stayingSinceMonth, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
        headerArray102.push([
          { text: 'Año', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text:element.clientGeneralInfo.questions.stayingSinceYear, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
        headerArray102.push([
          { text: '¿Dónde?', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text:element.clientGeneralInfo.questions.currentLocation, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
      }
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray102
          },
          margin: [0, -1, 10, 0]
        }
      )
      let headerArray103: any[] = [];
      headerArray103.push([
        { text: '¿Alguna vez ha tenido una Visa Americana? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        { text: element.clientGeneralInfo.questions.haveYouEverHadUSVisa== "true" ?' Si' :  element.clientGeneralInfo.lastDateOfEntry.wereYouInspectedByAnImmigrationOfficer == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       
      ]);
      if(element.clientGeneralInfo.questions.haveYouEverHadUSVisa== "true"){
        headerArray103.push([
          { text: 'En caso afirmativo, favor de proporcio', bold: true, fontSize: 10, border: [true, true, false, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text:'nar lo siguiente:', bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,margin:[-10,0,0,0]  },
        
         
        ]);
        headerArray103.push([
          { text: 'Consulado donde le dieron la Visa', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text:element.clientGeneralInfo.questions.consulateThatIssuedVISA, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
        headerArray103.push([
          { text: 'Fecha que se le Entrego', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text:this.datePipe.transform(element.clientGeneralInfo.questions.issueDateOFVISA, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
        headerArray103.push([
          { text: 'Fecha que Expira:', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text:this.datePipe.transform(element.clientGeneralInfo.questions.expirationDateOFVISA, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
      }
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray103
            },
            margin: [0,-1, 10, 0]
          }
        )
        let headerArray104: any[] = [];
        headerArray104.push([
          { text: '¿Alguna vez ha sido negada una Visa Americana? ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          { text: element.clientGeneralInfo.questions.haveYouEverbeenRefusedUSVisa=="true" ?' Si' :  element.clientGeneralInfo.questions.haveYouEverbeenRefusedUSVisa == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         
        ]);
        if(element.clientGeneralInfo.questions.haveYouEverbeenRefusedUSVisa=="true"){
          headerArray104.push([
            { text: 'En caso afirmativo, proporcione lo sig ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: "uiente:", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
          
           
          ]);
          headerArray104.push([
            { text: 'Nombre del consulado que niega la visa emitida', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: element.clientGeneralInfo.questions.consulateDenyingVISAIssued, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           
          ]);
          headerArray104.push([
            { text: 'Negada más de una vez:', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: element.clientGeneralInfo.questions.deniedMoreThanOnce=="true" ?' Si' :  element.clientGeneralInfo.questions.deniedMoreThanOnce == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           
          ]);
        }
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray104
            },
            margin: [0, -1, 10, 0]
          }
        )
        let headerArray105: any[] = [];
        headerArray105.push([
          { text: '¿Alguna vez ha sido detenido o interrogado por un oficial de inmigración?  ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text: element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByAnImmigOfficer=="true" ?' Si' :  element.clientGeneralInfo.questions.deniedMoreThanOnce == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         
        ]);
        if(element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByAnImmigOfficer=="true" ){
          headerArray105.push([
            { text: 'En caso afirmativo, proporcione lo sig ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: "uiente:", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
          
           
          ]);
          headerArray105.push([
            { text: '¿Dónde?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: element.clientGeneralInfo.questions.locationOfStoppedOrQuestioned, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           
          ]);
          headerArray105.push([
            { text: 'Mes/Año', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: this.datePipe.transform(element.clientGeneralInfo.questions.dateOfStoppedOrQuestioned, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           
          ]);
          headerArray105.push([
            { text: '¿Más de una vez?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: element.clientGeneralInfo.questions.stoppedMoreThanOnce=="true" ?' Si' :  element.clientGeneralInfo.questions.stoppedMoreThanOnce == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           
          ]);
        }
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray105
            },
            margin: [0, -1, 10, 0]
          }
        )
        let headerArray106: any[] = [];
        headerArray106.push([
          { text: '¿Alguna vez le han negado la entrada a los EE.UU.', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
          { text: element.clientGeneralInfo.questions.haveYouEverbeenDeniedEntryIntoUS=="true" ?' Si' :  element.clientGeneralInfo.questions.haveYouEverbeenDeniedEntryIntoUS == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         
        ]);
        if(element.clientGeneralInfo.questions.haveYouEverbeenDeniedEntryIntoUS=="true"){
          headerArray106.push([
            { text: 'En caso afirmativo, proporcione lo sig ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: "uiente:", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
          
           
          ]);
          headerArray106.push([
            { text: '¿Dónde?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: element.clientGeneralInfo.questions.locationOfDeniedEntry, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           
          ]);
          headerArray106.push([
            { text: 'Mes/Año', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: this.datePipe.transform(element.clientGeneralInfo.questions.dateOfDeniedEntry, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           
          ]);
          headerArray106.push([
            { text: '¿Más de una vez?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: element.clientGeneralInfo.questions.deniedMoreThanOnce=="true" ?' Si' :  element.clientGeneralInfo.questions.deniedMoreThanOnce == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           
          ]);
    
        }
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray106
            },
            margin: [0, -1, 10, 0]
          }
        )
        let headerArray107: any[] = [];
        headerArray107.push([
         { text: '¿Alguna vez ha estado en proceso de deportación? ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.clientGeneralInfo.questions.haveYouEverbeenInDeportationProceedings=="true" ?' Si' :  element.clientGeneralInfo.questions.haveYouEverbeenInDeportationProceedings == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
       if(element.clientGeneralInfo.questions.haveYouEverbeenInDeportationProceedings=="true"){
         headerArray107.push([
           { text: 'En caso afirmativo, proporcione lo sig ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: "uiente:", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
         
          
         ]);
         headerArray107.push([
           { text: '¿Dónde?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.locationOfDeportation, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         headerArray107.push([
           { text: 'Mes/Año', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: this.datePipe.transform(element.clientGeneralInfo.questions.dateOfDeportationProceedings, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         headerArray107.push([
           { text: 'Resultado final', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.endResultOfDeportationProceedings, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
   
       }
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray107
           },
           margin: [0, -1, 10, 0]
         }
       )
       let headerArray108: any[] = [];
       headerArray108.push([
         { text: '¿Alguna vez ha sido deportado o expulsado de los EE.UU.?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.clientGeneralInfo.questions.haveYouEverbeenDeportedOrRemovedFromUS=="true" ?' Si' :  element.clientGeneralInfo.questions.haveYouEverbeenDeportedOrRemovedFromUS == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
       if(element.clientGeneralInfo.deportedNotes!=''){
        headerArray108.push([
          { text: 'Notes:', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text: element.clientGeneralInfo.deportedNotes, bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         
        ]);
       }
       if(element.clientGeneralInfo.questions.haveYouEverbeenDeportedOrRemovedFromUS=="true"){
         headerArray108.push([
           { text: 'En caso afirmativo, proporcione lo sig ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: "uiente:", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
         
          
         ]);
         headerArray108.push([
           { text: '¿Dónde?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.locationOfDeportedOrRemovedFromUS, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         headerArray108.push([
           { text: 'Mes/Año', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: this.datePipe.transform(element.clientGeneralInfo.questions.dateOfDeportation, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         headerArray108.push([
           { text: 'Motivo de la Deportación', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.reasonForDeportation, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
       }
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray108
           },
           margin: [0, -1, 10, 0]
         }
       )
   
       let headerArray109: any[] = [];
       headerArray109.push([
         { text: '¿Alguna vez ha sido detenido o interrogado por la Policía? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByThePolice=="true" ?' Si' :  element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByThePolice == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
       if(element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByThePolice=="true"){
         headerArray109.push([
           { text: 'En caso afirmativo, proporcione lo sig ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: "uiente:", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
         
          
         ]);
         headerArray109.push([
           { text: '¿Dónde?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.locationOfStoppedByPolice, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         headerArray109.push([
           { text: 'Mes/Año', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: this.datePipe.transform(element.clientGeneralInfo.questions.dateOfStop, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         headerArray109.push([
           { text: 'Motivo de la Deportación', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.reasonForStop, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         headerArray109.push([
           { text: '¿Fue Arrestado?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.wereYouArrested=="true" ?' Si' :  element.clientGeneralInfo.questions.wereYouArrested == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
       }
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray109
           },
           margin: [0, -1, 10, 0]
         }
       )
       let headerArray110: any[] = [];
       headerArray110.push([
         { text: '¿Alguna vez ha sido acusado de cometer un crimen? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.clientGeneralInfo.questions.haveYouEverbeenChargedWWithCommittingACrime=="true" ?' Si' :  element.clientGeneralInfo.questions.haveYouEverbeenChargedWWithCommittingACrime == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
       if(element.clientGeneralInfo.questions.haveYouEverbeenChargedWWithCommittingACrime=="true"){
         headerArray110.push([
           { text: 'En caso afirmativo, proporcione lo sig ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: "uiente:", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
         
          
         ]);
         headerArray110.push([
          { text: '¿Dónde?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          { text: element.clientGeneralInfo.questions.locationOfCrimeCharged, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        
         
        ]);
        headerArray110.push([
          { text: 'Mes/Año', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          { text: this.datePipe.transform(element.clientGeneralInfo.questions.dateOfCrimeCharged, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        
         
        ]);
        headerArray110.push([
          { text: 'Crimen Acusado', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          { text: element.clientGeneralInfo.questions.locationOfCrimeCharged, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        
         
        ]);
        headerArray110.push([
          { text: '¿Fue a Juicio?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          { text: element.clientGeneralInfo.questions.didYouGotoCourt=="true" ?' Si' :  element.clientGeneralInfo.questions.didYouGotoCourt == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        
         
        ]); 
        headerArray110.push([
          { text: '¿Dónde?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          { text: element.clientGeneralInfo.questions.locationOfCrimeCharged, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        
         
        ]);
        headerArray110.push([
          { text: 'Mes/Año', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          { text: this.datePipe.transform(element.clientGeneralInfo.questions.dateOfCrimeCharged, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        
         
        ]);
        headerArray110.push([
          { text: 'Crimen Acusado', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          { text: element.clientGeneralInfo.questions.locationOfCrimeCharged, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        
         
        ]);
        headerArray110.push([
          { text: '¿Fue a Juicio?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          { text: element.clientGeneralInfo.questions.didYouGotoCourt=="true" ?' Si' :  element.clientGeneralInfo.questions.didYouGotoCourt == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        
         
        ]); 
      }
       
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray110
           },
           margin: [0, -1, 10, 0]
         }
       )
       let headerArray111: any[] = [];
       headerArray111.push([
         { text: '¿Alguna vez ha sido declarado CULPABLE de haber cometido un delito? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.clientGeneralInfo.questions.haveYouEverbeenFoundGuiltyOfCommittingACrime=="true" ?' Si' :  element.clientGeneralInfo.questions.haveYouEverbeenFoundGuiltyOfCommittingACrime == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
    
       if(element.clientGeneralInfo.questions.haveYouEverbeenFoundGuiltyOfCommittingACrime=="true"){
         headerArray111.push([
           { text: 'En caso afirmativo, proporcione lo sig ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: "uiente:", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
         
          
         ]);
         headerArray111.push([
           { text: '¿Dónde?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.foundGuiltyLocation, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         headerArray111.push([
           { text: 'Mes/Año', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: this.datePipe.transform(element.clientGeneralInfo.questions.dateOfFoundGuiltyCrime, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         headerArray111.push([
           { text: 'Crimen Acusado', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.foundGuiltyCrimeCharged, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         headerArray111.push([
           { text: 'Multa', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.penalty, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
       
       
       }
      
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray111
           },
           margin: [0, -1, 10, 0]
         }
       )
       let headerArray151: any[] = [];
       if(element.clientGeneralInfo.guiltyNotes!=''){
        headerArray151.push([
          { text: 'Notes: ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text:element.clientGeneralInfo.guiltyNotes, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
       }
       dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray151
          },
          margin: [0, -1, 10, 0]
        }
      )
       let headerArray112: any[] = [];
       headerArray112.push([
         { text: '¿Tiene licencia de conducir?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        
         { text: element.clientGeneralInfo.questions.doYouHaveValidDriverLicense=="true" ?' Si' :  element.clientGeneralInfo.questions.doYouHaveValidDriverLicense == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
        
       ]);
       if(element.clientGeneralInfo.questions.doYouHaveValidDriverLicense=="true"){
         headerArray112.push([
           { text: 'En caso afirmativo, proporcione lo sig ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: "uiente:", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
         
          
         ]);
         headerArray112.push([
           { text: 'Estado', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.stateOfDriverLicense, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         headerArray112.push([
           { text: 'Fecha de caducidad', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: this.datePipe.transform(element.clientGeneralInfo.questions.dateOfDriverLicense, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
       }
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray112
           },
           margin: [0, -1, 10, 0]
         }
       )
       let headerArray113: any[] = [];
       headerArray113.push([
         { text: '¿Paga impuestos en EE. UU?  ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.clientGeneralInfo.questions.doYouPayTaxesInUS=="true" ?' Si' :  element.clientGeneralInfo.questions.doYouPayTaxesInUS == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       if(element.clientGeneralInfo.questions.doYouPayTaxesInUS=="true"){
         headerArray113.push([
           { text: 'En caso afirmativo, proporcione lo sig ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: "uiente:", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
         
          
         ]);
         headerArray113.push([
           { text: 'Año', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.sinceWhen, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
       }
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray113
           },
           margin: [0, -1, 10, 0]
         }
       )
       let headerArray114: any[] = [];
       headerArray114.push([
         { text: '¿Está trabajando en EE. UU empleando un número de Seguro Social? ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.clientGeneralInfo.questions.areYouCurrentlyEmployedUsingUSSSN=="true" ?' Si' :  element.clientGeneralInfo.questions.areYouCurrentlyEmployedUsingUSSSN == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
       if(element.clientGeneralInfo.questions.areYouCurrentlyEmployedUsingUSSSN=="true"){
         headerArray114.push([
           { text: '¿Es válido el seguro? ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.ssnValidStatus, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
       }
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray114
           },
           margin: [0, -1, 10, 0]
         }
       )
       let headerArray115: any[] = [];
       headerArray115.push([
         { text: '¿Tienes autorización de empleo? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.clientGeneralInfo.questions.doYouHaveEmploymentAuthorization=="true" ?' Si' :  element.clientGeneralInfo.questions.doYouHaveEmploymentAuthorization == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       if(element.clientGeneralInfo.questions.doYouHaveEmploymentAuthorization=="true"){
         headerArray115.push([
           { text: 'En caso afirmativo, ¿es válido?: ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.employmentAuthorizationValidStatus, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         headerArray115.push([
           { text: 'Cuál es su oficio: ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.occupation, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         headerArray115.push([
           { text: '¿Está trabajando actualmente? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
          
           { text: element.clientGeneralInfo.questions.areYouCurrentlyWorking == "true" ?' Si' : 'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
          
         ]);
       }
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray115
           },
           margin: [0, -1, 10, 0]
         }
       )
       let headerArray116: any[] = [];
       headerArray116.push([
         { text: 'Cuál es tu ocupación? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.clientGeneralInfo.whatIsYourOccupation, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       headerArray116.push([
         { text: '¿Está trabajando actualmente? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text:  element.clientGeneralInfo.questions.areYouCurrentlyWorking == "true" ?' Si' : 'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       headerArray116.push([
         { text: 'Si no, ¿por qué no y está recibiendo desempleo? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.clientGeneralInfo.questions.areYouReceivingUnemployment, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       headerArray116.push([
         { text: '¿Ha presentado impuestos durante los últimos 3 años? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.clientGeneralInfo.questions.haveYouFiledTaxesDuringLast3Years=="true" ?' Si' :  element.clientGeneralInfo.questions.haveYouFiledTaxesDuringLast3Years == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
   
      if(element.clientGeneralInfo.questions.haveYouFiledTaxesDuringLast3Years!="true"){headerArray116.push([
         { text: 'Si no, ¿por qué no? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.clientGeneralInfo.questions.reasonForNonTaxFiling, bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);}
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray116
           },
           margin: [0, -1, 10, 0]
         }
       )
       let headerArray117: any[] = [];
       headerArray117.push([
         { text: 'Nivel De Educación más alto:  ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.clientGeneralInfo.questions.education, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       headerArray117.push([
         { text: 'otro  ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.clientGeneralInfo.questions.educationOther, bold: false, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
        
       ]);
       headerArray117.push([
         { text: 'Dominio del idioma inglés  ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.clientGeneralInfo.questions.englishLanguageProficiency, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
   
       headerArray117.push([
         { text: '¿Tiene algún certificado de educación continua / Capacitación / Licencias especiales?  ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.clientGeneralInfo.questions.owningAnyCertications=="true" ?' Si' : element.clientGeneralInfo.questions.owningAnyCertications == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       if(element.clientGeneralInfo.questions.owningAnyCertications=="true"){
         headerArray117.push([
           { text: 'En caso afirmativo, ¿qué? ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
          
           { text: element.clientGeneralInfo.questions.certicateDetails, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          
         ]);
       }
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray117
           },
           margin: [0, -1, 10, 0]
         }
       )
       let headerArray118: any[] = [];
       headerArray118.push([
         { text: 'CARGO PÚBLICO / OTRA EVIDENCIA ', bold: true, fontSize: 10, border: [false, true, false, false],color: '#267FA0',margin:[0,5,0,5], },
         { text: ' ', bold: false, fontSize: 10, border: [false, true, false, false] },
         
        
        
       ]);
       headerArray118.push([
         { text: '¿Es dueño de tu casa? ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text:element.clientGeneralInfo.clientReceivedBenefits.doYouOwnYourHome=="true" ?' Si' : element.clientGeneralInfo.clientReceivedBenefits.doYouOwnYourHome == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
       if(element.clientGeneralInfo.clientReceivedBenefits.doYouOwnYourHome=="true"){
         headerArray118.push([
           { text: '¿Está pagando alguna propiedad? ¿Qué tipo? ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
          
           { text: element.clientGeneralInfo.clientReceivedBenefits.whatType, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          
         ]);
       
       }
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray118
           },
           margin: [0, -1, 10, 0]
         }
       )
       let headerArray119: any[] = [];
       headerArray119.push([
         { text: '¿Ha sido usted o cualquier miembro inmediato de la familia víctima de un delito o acto violento en los Estados Unidos?  ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.clientGeneralInfo.clientReceivedBenefits.haveYouOrAnyFamilyMemberEverbeenAVictimOfACrimeOrAViolentAct=="true" ?' Si' : element.clientGeneralInfo.clientReceivedBenefits.haveYouOrAnyFamilyMemberEverbeenAVictimOfACrimeOrAViolentAct == "false" ? 'No'  : 'N/A',bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       if(element.clientGeneralInfo.victimOfACrimeNotes!=''){
        headerArray119.push([
          { text: 'Notes:  ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
          { text: element.clientGeneralInfo.victimOfACrimeNotes,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
       }
       if(element.clientGeneralInfo.clientReceivedBenefits.haveYouOrAnyFamilyMemberEverbeenAVictimOfACrimeOrAViolentAct=="true"){
         headerArray119.push([
           { text: 'Sí, por favor complete abajo.', bold: true, fontSize: 10, border: [false, true, false, false],color: '#267FA0',margin:[0,5,0,5], },
           { text: ' ', bold: false, fontSize: 10, border: [false, true, false, false] },
           
          
          
         ]);
       
       
         headerArray119.push([
           { text: 'Fecha que ocurrió el crimen o acto violento ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
          
           { text:this.datePipe.transform(element.clientGeneralInfo.clientReceivedBenefits.dateOfCrimeOrViolentActCommitted, 'MM-dd-yyyy') , bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          
         ]);
         headerArray119.push([
           { text: '¿Fue reportado?', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
          
           { text:element.clientGeneralInfo.clientReceivedBenefits.wasItReported=="true" ?' Si' : element.clientGeneralInfo.clientReceivedBenefits.wasItReported == "false" ? 'No'  : 'N/A' , bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          
         ]);
         headerArray119.push([
           { text: 'Resumen de lo que sucedió:', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
          
           { text:element.clientGeneralInfo.clientReceivedBenefits.summaryOfWhatHappened , bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          
         ]);
       }
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray119
           },
           margin: [0, -1, 10, 0]
         }
       )
       let headerArray121: any[] = [];
       headerArray121.push([
         { text: '¿Alguna vez había pedido alguna ayuda pública? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.clientGeneralInfo.haveYouEverAskedForAnyPubilcAssistance == "true" ?' Si' :  element.clientGeneralInfo.haveYouEverAskedForAnyPubilcAssistance == "false" ? 'No'  : 'NA', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
   
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray121
           },
           margin: [0, -1, 10, 0]
         }
       )
       dd.content.push(
         {
           stack: [{
             text: '',
             alignment: 'center',
             fontSize: 12,
             lineHeight: 1.3,
             bold: true,
             pageBreak: 'before',
           }]
         })
       let headerArray51: any[] = [];
       headerArray51.push([
         { text: 'Plan de caso ', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:12, color:'#FFFFFF' ,fillColor: '#267FA0'},
         
        
       ]);
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: ['*'],
             body: headerArray51
           },
           margin: [0, 10, 10, 0]
         }
       )
       let headerArray15: any[] = [];
       headerArray15.push([
         { text: 'Fecha límite', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: this.datePipe.transform(element.clientGeneralInfo.deadlineDate, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
       headerArray15.push([
         { text: 'Depósito inicial', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text:element.casePlan.intialDepositAmount, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       headerArray15.push([
         { text: 'método de pago ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.casePlan.checkOrCash=="true"?'Dinero':'Controlar', bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
       headerArray15.push([
         { text: 'Plan de caso  ', bold: true, fontSize: 10, border: [true, true, true, false],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.casePlan.casePlan,bold: false, fontSize: 10, border: [true, false, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       headerArray15.push([
         { text: '  ', bold: true, fontSize: 10, border: [true, false, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: '', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       headerArray15.push([
         { text: 'Attorney ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.casePlan.attorney, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       headerArray15.push([
         { text: 'Paralegal', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.casePlan.paralegal, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
       headerArray15.push([
         { text: 'Legal Assistant ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.casePlan.legalAssistant, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
     
     
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [140,'*'],
             body: headerArray15
           },
           margin: [0, 12, 10, 0]
         }
       )
       let headerArray16: any[] = [];
       headerArray16.push([
        { text: '', bold: true, fontSize: 10, border: [false, false, false, false] ,margin:[0,0,10,0],color:'#267FA0' },
         { text: 'Legal Fees:', bold: true, fontSize: 10, border: [false, false, false, false] ,margin:[0,0,10,0],color:'#267FA0' },
        
         { text: '   ', bold: false, fontSize: 10, border: [false, false, false, false] ,margin:[0,0,10,0] },
         { text: '   ', bold: false, fontSize: 10, border: [false, false, false, false] ,margin:[0,0,10,0] },
       
        
       ]);
       headerArray16.push([
        { text: 'S.No', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         { text: 'Type', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: 'Legal Fee', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         { text: 'Admin Cost', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
       headerArray16.push([
        { text: '1', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         { text: element.casePlan.legalFeeType1, bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.casePlan.legalFee1, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         { text: element.casePlan.adminCost1,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
       ]);
       headerArray16.push([
        { text: '2', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         { text: element.casePlan.legalFeeType2, bold: true, fontSize: 10, border: [true, true, true, false],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.casePlan.legalFee2,bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
         { text: element.casePlan.adminCost2,bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       ]);
       headerArray16.push([
        { text: '3', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         { text: element.casePlan.legalFeeType3, bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.casePlan.legalFee3, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         { text:element.casePlan.adminCost3,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
       ]);
       headerArray16.push([
        { text: '', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         { text: 'Total Cost:', bold: true, fontSize: 10, border: [true, true, false, true],margin:[0,3,0,3],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.casePlan.totalCost, bold: false, fontSize: 10, border: [false, true, false, true] ,margin:[0,3,0,3],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         { text: '   ', bold: false, fontSize: 10,  border: [false, true, true, true] ,margin:[0,3,0,3],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
     
     
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [60,'*','*','*'],
             body: headerArray16
           },
           margin: [0, 5, 10, 0]
         }
       )
    
        let headerArray979: any[] = [];
        headerArray979.push([
          { text: '', bold: true, fontSize: 10, border: [false, false, false, false],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
          { text:  '', bold: false, fontSize: 10, border: [false, false, false, false],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         
        ]);
        if(element.casePlan.notes!=''){
          headerArray979.push([
            { text: 'Notes: ', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
            { text:element.casePlan.notes, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
           
          
           
          ]);
        }
 
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray979
            },
            margin: [0, -6, 10, 0]
          }
        )
       
       let headerArray17: any[] = [];
       headerArray17.push([
        { text: '   ', bold: false, fontSize: 10, border: [false, false, false, false] ,margin:[0,0,10,0] },
         { text: 'Government Filing Fees:', bold: true, fontSize: 10, border: [false, false, false, false],margin:[0,0,10,0],color:'#267FA0' },
        
         { text: '   ', bold: false, fontSize: 10, border: [false, false, false, false] ,margin:[0,0,10,0] },
      
       
        
       ]);
       headerArray17.push([
        { text: ' S.No  ', bold: false, fontSize: 10, border: [true, true, true, true] ,margin:[0,0,10,0],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         { text: 'Type', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: 'Amount', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
       
        
       ]);
     
       headerArray17.push([
        { text: '  1 ', bold: false, fontSize: 10, border: [true, true, true, true] ,margin:[0,0,10,0],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         { text: element.casePlan.governmentFilngType1, bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.casePlan.governmentFilingAmount1,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
       
       ]);
       headerArray17.push([
        { text:  '2', bold: false, fontSize: 10, border: [true, true, true, true] ,margin:[0,0,10,0],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         { text: element.casePlan.governmentFilngType2, bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.casePlan.governmentFilingAmount2,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
       
       ]);
       headerArray17.push([
        { text: ' 3', bold: false, fontSize: 10, border: [true, true, true, true] ,margin:[0,0,10,0], borderColor: ['#808080', '#808080', '#808080', '#808080']},
         { text: element.casePlan.governmentFilngType3, bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.casePlan.governmentFilingAmount3,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
       
       ]);
       headerArray17.push([
        { text: '4', bold: false, fontSize: 10, border: [true, true, true, true] ,margin:[0,0,10,0],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         { text: element.casePlan.governmentFilngType4, bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.casePlan.governmentFilingAmount4,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
       
       ]);
       headerArray17.push([
        { text: '5', bold: false, fontSize: 10, border: [true, true, true, true] ,margin:[0,0,10,0] ,borderColor: ['#808080', '#808080', '#808080', '#808080']},
         { text: element.casePlan.governmentFilngType5, bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.casePlan.governmentFilingAmount5,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
       
       ]);
      
     
     
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [60,'*','*'],
             body: headerArray17
           },
           margin: [0, 5, 10, 0]
         }
       )
       let headerArray52: any[] = [];
       headerArray52.push([
         { text: 'Declaración ', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:12, color:'#FFFFFF' ,fillColor: '#267FA0'},
         
        
       ]);
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: ['*'],
             body: headerArray52
           },
           margin: [0,15, 10, 0]
         }
       )
       let headerArray25: any[] = [];
       headerArray25.push([
         { text: 'Yo certifico que toda la información que he suministrado es verdad y correcta debido a mi conocimiento personal. Entiendo que presentando información falsa puede arriesgar mi futura representación legal. A la misma vez, entiendo que una consulta con el buffet de abogados no constituye representación legal y que Monty & Ramirez, LLP no me representa hasta que reciba una carta de contratación.', bold: false, fontSize: 10, border: [false, false, false, false] },
     
        
       ]);
       headerArray25.push([
         { text: 'Fecha:'+this.datePipe.transform(element.clientGeneralInfo.date, 'MM-dd-yyyy') , bold: true, fontSize: 10, border: [false, false, false, false], color:'#267FA0' },
     
        
       ]);
     
     
     
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: ['*'],
             body: headerArray25
           },
           margin: [0, 5, 10, 0]
         }
       )
       let headerArray53: any[] = [];
       headerArray53.push([
         { text: 'Asignar ', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:12, color:'#FFFFFF' ,fillColor: '#267FA0'},
         
        
       ]);
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: ['*'],
             body: headerArray53
           },
           margin: [0,15, 10, 0]
         }
       )
       let headerArray26: any[] = [];
       headerArray26.push([
         { text: element.confRoomNo==null?'Conf Room No:':'Conf Room No:'+element.confRoomNo  ,bold: false, fontSize: 10, border: [false,false,false,false] },
        
     
        
       ]);
       headerArray26.push([
         { text: 'Bienvenidos a nuestra oficina. Habrá una tarifa de'+''+ '$' +''+( element.feeInDollar != null ? element.feeInDollar: '') +'por consulta. Esta tarifa se cobrará antes de su consulta con el abogado. Por favor complete las preguntas a continuación', bold: false, fontSize: 10, border: [false, false, false, false]},
     
        
       ]);
       headerArray26.push([
        { text:  intakeRes.referenceField1== null ? 'Consultation Date:':'Consultation Date:'+this.datePipe.transform(intakeRes.referenceField1, 'MM-dd-yyyy'), bold: true, fontSize: 10, border: [false, false, false, false], color:'#267FA0' },
     
        
       ]);
       headerArray26.push([
        { text: intakeRes.referenceField2 == '' ? 'Assigned Attorney:':'Assigned Attorney:'+ intakeRes.referenceField2, bold: true, fontSize: 10, border: [false, false, false, false], color:'#267FA0' },
     
        
       ]);
     
     
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: ['*'],
             body: headerArray26
           },
           margin: [0, 5, 10, 0]
         }
       )   
       let headerArray78: any[] = [];
       headerArray78.push([
         { text: 'Uso en oficina ', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:12, color:'#FFFFFF' ,fillColor: '#267FA0'},
         
        
       ]);
       dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: ['*'],
            body: headerArray78
          },
          margin: [0, 50, 10, 0]
        }
      )   
      let headerArray79: any[] = [];
      headerArray79.push([
        { text: 'Abogado', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        { text: element.attorney, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       
      ]);
      headerArray79.push([
        { text: 'Asistente legal', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        { text:element.legalAssistant, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
      
       
      ]);
      headerArray79.push([
        { text: 'Fecha ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        { text:this.datePipe.transform(element.attorneyDate, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       
      ]);
      headerArray79.push([
        { text: 'Attoney Notes ', bold: true, fontSize: 10, border: [true, true, true, false],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        { text: element.attorneyNotes,bold: false, fontSize: 10, border: [true, false, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
      
       
      ]);
      headerArray79.push([
        { text: 'Others ', bold: true, fontSize: 10, border: [true, true, true, false],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        { text: element.attorneyNotesForOthers,bold: false, fontSize: 10, border: [true, false, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
      
       
      ]);
      headerArray79.push([
        { text: 'Removal ', bold: true, fontSize: 10, border: [true, true, true, false],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        { text: element.attorneyNotesForRemoval,bold: false, fontSize: 10, border: [true, false, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
      
       
      ]);
      headerArray79.push([
        { text: 'Notes ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        { text: element.notes,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
      
       
      ]);
    
    
    
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [140,'*'],
            body: headerArray79
          },
          margin: [0, 5, 10, 0]
        }
      )

    pdfMake.createPdf(dd).download('Intake Number : ' +  intakeRes.intakeFormNumber);
    pdfMake.createPdf(dd).open();

this.spin.hide();
}
}
