import { DatePipe } from '@angular/common';
import { Component, Injectable, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { logo } from "../../../../../assets/font/logo.js";
import pdfMake from "pdfmake/build/pdfmake";
import { defaultStyle } from "../../../../config/customStylesNew";
@Component({
  selector: 'app-n400intake',
  templateUrl: './n400intake.component.html',
  styleUrls: ['./n400intake.component.scss']
})
@Injectable({
  providedIn: 'root'
})

export class N400intakeComponent implements OnInit {

  constructor(private datePipe: DatePipe,  private spin: NgxSpinnerService,) { }

  ngOnInit(): void {
  }

  generatePdf(element: any, intakeRes: any, referral: any) {

    console.log(element)
    var dd: any;
    let headerTable: any[] = [];
    headerTable.push([
      { image: logo.headerLogo, fit: [100, 100], bold: true, fontSize: 12, border: [false, false, false, false],margin:[10,0,0] },
     // { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
  //    { text: 'Monty & Ramirez LLP \n 150 W Parker Road, 3rd Floor Houston, TX 77076',  alignment: 'center', fontSize: 12, border: [false, false, false, false] },
       { text:  'PC - Information Sheet -' +element.itFormNo, bold: true, alignment: "right", fontSize: 13, margin:[20,0,0], border: [false, false, false, false],color:'#666362' },
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
      { text: 'Personal Details ', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:12, color:'#FFFFFF' ,fillColor: '#267FA0'},
      
     
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
       { text: 'Name ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']},
       { text:   element.name, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']},
      
     
     
     ]);
    headerArray6.push([
      { text: 'Street Address  ', bold: true, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[0,5,0,0] },
      { text:   element.address, bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] ,margin:[0,5,0,0]},
     
    
     
    ]);
   
    headerArray6.push([
      { text: 'City ', bold: true, fontSize: 10, border: [true, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.city, bold: false, fontSize: 10,  border: [false, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray6.push([
      { text: 'State ', bold: true, fontSize: 10, border: [true, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.state, bold: false, fontSize: 10,  border: [false, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray6.push([
      { text: 'Zipcode ', bold: true, fontSize: 10, border: [true, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.zipcode, bold: false, fontSize: 10,  border: [false, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray6.push([
      { text: 'Do you live at this address? ', bold: true, fontSize: 10, border: [true, false, true, true]  , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.doYouLiveAtThisAddress == true ? 'Yes' : 'No', bold: false, fontSize: 10,  border: [false, false, true, true]  , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
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
      { text: 'Telephone', bold: true, fontSize: 10, border: [true, true, true, true]  ,  borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text: 'Office :'+'\t'+ element.contactNumber.workNo    , bold: false, fontSize: 10,  border: [true, true, true, true]  ,  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'Cell:'+ '\t'+element.contactNumber.cellNo , bold: false, fontSize: 10,  border: [true, true, true, true]  ,  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
    
     
    
     
    ]);
    headerArray777.push([
      { text: '  ', bold: false, fontSize: 10, border: [true, false, false, true]  ,  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'Direct/Home:'+'\t'+ element.contactNumber.homeNo   , bold: false, fontSize: 10,  border: [true, true, true, true]  ,  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.contactNumber.faxNo== null ? 'Fax:':'Fax:'+element.contactNumber.faxNo, bold: false, fontSize: 10,  border: [true, true, true, true]  , borderColor: ['#808080', '#808080', '#808080', '#808080'] },   //31/7/23
    
     
    ]);
    headerArray777.push([
      { text: '  ', bold: false, fontSize: 10, border: [true, false, false, true]  ,  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.emailAddress== null ? 'Email Address : ':'Email Address : '+element.emailAddress , bold: false, fontSize: 10,  border: [true, true, true, true]  , borderColor: ['#808080', '#808080', '#808080', '#808080'] },    //31/7/23
      { text: 'Contact Person :'+'\t'+element.contactPersonName, bold: false, fontSize: 10,  border: [true, true, true, true]  , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
    
     
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
      { text: 'Do you want to receive information regarding immigration updates?  ', bold: true, fontSize: 10, border: [true, false, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:  element.doNeedImmigrationUpdates == true ?' Yes' : 'No', bold: false, fontSize: 10,  border: [false, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
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
      { text: 'Case Details', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:13, color:'#FFFFFF' ,fillColor: '#267FA0'},
      
     
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
      { text: 'How did you hear about our firm? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:    referral.find(y => y.value == element.referenceMedium.listOfMediumAboutFirm)?.label, bold: false, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080']},
      
     
     
    ]);headerArray7.push([
      { text: 'This consultation is for', bold: true, fontSize: 10, border: [true, true, true, false], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.thisConsultationIsFor, bold: false, fontSize: 10,  border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    headerArray7.push([
      { text: 'Other', bold: true, fontSize: 10, border: [true, true, false, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.referenceMedium.referredByOthers, bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    
    headerArray7.push([
      { text: 'Objective of Visit. What is the purpose of your visit?', bold: true, fontSize: 10, border: [true, false, true, false] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: element.purposeOfVisit, bold: false, fontSize: 10,  border: [false, false, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
   
    headerArray7.push([
      { text: '  ', bold: false, fontSize: 10, border: [true, false, false, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:   '  ', bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray7.push([
      { text: 'Do you already have an attorney?  ', bold: true, fontSize: 10, border: [true, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: element.doHaveImmigrationAttorney == true ?' Yes' : 'No', bold: false, fontSize: 10,  border: [false, false, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    if(element.doHaveImmigrationAttorney == true ){
      headerArray7.push([
        { text: 'If Yes, Fill the Name of the Attorney ', bold: true, fontSize: 10, border: [true, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
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
      { text: 'Client General Details', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:13, color:'#FFFFFF' ,fillColor: '#267FA0'},
      
     
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
      { text: 'Full Name ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:   element.clientGeneralInfo.clientPersonalInfo.fullName, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      
     
     
    ]);headerArray8.push([
      { text: 'Other Names Used', bold: true, fontSize: 10, border: [true, true, true, false] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.clientPersonalInfo.otherNamesUsed, bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray8.push([
      { text: 'Date of Birth', bold: true, fontSize: 10, border: [true, true, false, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: this.datePipe.transform(element.clientGeneralInfo.clientPersonalInfo.dateOfBirth, 'MM-dd-yyyy'), bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    
    headerArray8.push([
      { text: 'City and Country of Birth', bold: true, fontSize: 10, border: [true, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: element.clientGeneralInfo.clientPersonalInfo.cityAndCountryOfBirth, bold: false, fontSize: 10,  border: [false, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
   
    headerArray8.push([
      { text: 'Country of Citizenship:  ', bold: true, fontSize: 10, border: [true, false, false, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.clientPersonalInfo.countryOfCitizenship, bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray8.push([
      { text: 'Marital Status ', bold: true, fontSize: 10, border: [true, false, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.maritalStatus, bold: false, fontSize: 10,  border: [false, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray8.push([
      { text: 'If married or engaged, Comple', bold: true, fontSize: 10, border: [false, false, false, false] ,margin:[0,5,0,5], color:'#267FA0' , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  'te this section.', bold: true, fontSize: 10,  border: [false, true, false, false],margin:[-13,5,0,5], color:'#267FA0',  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    headerArray8.push([
      { text: 'Name of Spouse or Fiancé(e):', bold: true, fontSize: 10,  border: [true, true, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
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
      { text: 'Date of Birth', bold: true, fontSize: 10,  border: [true, true, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: this.datePipe.transform(element.clientGeneralInfo.spouseOrFiancePersonalInfo.dateOfBirth, 'MM-dd-yyyy'), bold: false, fontSize: 10,  border: [true, true, true, true] ,margin:[0,0,0,0] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
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
      { text: 'City and Country of Birth ', bold: true, fontSize: 10, border: [true, true, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:   element.clientGeneralInfo.spouseOrFiancePersonalInfo.cityAndCountryOfBirth, bold: false, fontSize: 10, border: [true, true, true, true],margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      
     
     
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
    let headerArray892: any[] = [];
    headerArray892.push([
      { text: 'Are they a Legal Permanent Resident or USC?', bold: true, fontSize: 10, border: [true, true, true, true],margin:[0,0,0,0] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.permanentResidentOrUSC == true ?' Yes' : 'No' , bold: false, fontSize: 10,  border: [true, true, true, true] ,margin:[0,0,0,0] , borderColor: ['#808080', '#808080', '#808080', '#808080']},
     
    
     
    ]);
   
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray892
        },
        margin: [0, -1, 2, 0]
      }
    ) 
    
    let headerArray9998: any[] = [];
    headerArray9998.push([
      { text: 'Date of Marriage', bold: true, fontSize: 10, border: [true, true, false, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
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
      { text: 'Which city and country did you get married in?', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text: element.clientGeneralInfo.whichCityAndCountryDidYouMarryIn, bold: false, fontSize: 10,  border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
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
    let headerArray120: any[] = [];
    headerArray120.push([
      { text: 'Place of Residence', bold: true, fontSize: 10, border: [true, false, false, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.placeOfResidence, bold: false, fontSize: 10,  border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray120
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray121: any[] = [];
    headerArray121.push([
      { text: 'Has your spouse ever been convicted of a crime? ', bold: true, fontSize: 10, border: [true, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: element.clientGeneralInfo.hasSpouseEverbeenConvictedOfACrime == true ?' Yes' : 'No' , bold: false, fontSize: 10,  border: [false, false, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray121
        },
        margin: [0, -1, 2, 0]
      }
    )
    if(element.clientGeneralInfo.spouseCrimeNotes!=''){
      let headerArray122: any[] = [];
      headerArray122.push([
        { text: 'Notes:', bold: true, fontSize: 10, border: [true, false, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text:element.clientGeneralInfo.spouseCrimeNotes , bold: false, fontSize: 10,  border: [false, false, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
      
       
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [140,'*'],
            body: headerArray122
          },
          margin: [0, -1, 2, 0]
        }
      )
    }
    let headerArray123: any[] = [];
    headerArray123.push([
      { text: 'If divorced or widowed, Pleas', bold: true, fontSize: 10, border: [false, false, false, true] ,margin:[0,5,0,5],color:'#267FA0',  borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:  'e complete this section', bold: true, fontSize: 10,  border: [false, false, false, true],margin:[-18,5,0,5],color:'#267FA0' , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray123
        },
        margin: [0, 2, 2, 0]
      }
    )
    let headerArray124: any[] = [];
    headerArray124.push([
      { text: 'Name of Former Spouse', bold: true, fontSize: 10,  border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.divorcedOrWidowedPersonalInfo.fullName, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0] ,  borderColor: ['#808080', '#808080', '#808080', '#808080']},
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray124
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray125: any[] = [];
    headerArray125.push([
      { text: 'City and Country of Birth', bold: true, fontSize: 10,  border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:  element.clientGeneralInfo.divorcedOrWidowedPersonalInfo.cityAndCountryOfBirth, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray125
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray126: any[] = [];
    headerArray126.push([
      { text: 'Country of Citizenship', bold: true, fontSize: 10,  border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.divorcedOrWidowedPersonalInfo.countryOfCitizenship, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray126
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray127: any[] = [];
    headerArray127.push([
      { text: 'Date of Marriage', bold: true, fontSize: 10,  border: [true, false, true, true] ,margin:[0,0,0,0] , borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text: this.datePipe.transform(element.clientGeneralInfo.dateOfMarriageOfDivorcedOrWidowed, 'MM-dd-yyyy') , bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray127
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray128: any[] = [];
    headerArray128.push([
      { text: 'Are you a Permanent Resident? ', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:  element.clientGeneralInfo.permanentResident == true ?' Yes' : 'No', bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray128
        },
        margin: [0, -1, 2, 0]
      }
    )
    if(element.clientGeneralInfo.permanentResident == true){
      let headerArray129: any[] = [];
      headerArray129.push([
        { text: 'If the answer is affirmative. ', bold: true, fontSize: 10,   border: [true, false, false, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080']},
        { text: "" , bold: false, fontSize: 10,  border: [false, true, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
      
       
      ]);
      headerArray129.push([
        { text: 'Which Year ', bold: true, fontSize: 10,  border: [true, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080']},
        { text: element.clientGeneralInfo.yearOfResidency==0?'':element.clientGeneralInfo.yearOfResidency , bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
      
       
      ]);
      headerArray129.push([
        { text: 'How did you obtain your residency? ', bold: true, fontSize: 10,  border: [true, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080']},
        { text: element.clientGeneralInfo.residencyObtainedThrough , bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
      
       
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [140,'*'],
            body: headerArray129
          },
          margin: [0, -1, 2, 0]
        }
      )
    } 
    let headerArray130: any[] = [];
    headerArray130.push([
      { text: 'DO YOU HAVE A FATHER OR ', bold: true, fontSize: 10,  border: [false, false, false, true] ,margin:[0,5,0,5],color:'#267FA0', borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:  'MOTHER WITH AMERICAN CITIZENSHIP? If the answer is affirmative ', bold: true, fontSize: 10,  border: [false, false, false, true] ,margin:[-18,5,0,5], color:'#267FA0',  borderColor: ['#808080', '#808080', '#808080', '#808080']},
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray130
        },
        margin: [0, 3, 2, 0]
      }
    )
    let headerArray131: any[] = [];
    headerArray131.push([
      { text: 'How did your Father/Mother obtain American Citizenship?', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.americanCitizenshipObtainedThrough, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray131
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray132: any[] = [];
    headerArray132.push([
      { text: 'What year?', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.yearOfCitizenship==0?'':element.clientGeneralInfo.yearOfCitizenship, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);  dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray132
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray133: any[] = [];
    headerArray133.push([
      { text: 'How old were you?', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.ageOfClient==0?'':element.clientGeneralInfo.ageOfClient, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray133
        },
        margin: [0, -1, 2, 0]
      }
    )
    let headerArray134: any[] = [];
    headerArray134.push([
      { text: 'Has anyone ever filed an immigrant visa petition for you or your parents?', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.hasAnyoneEverFiledImmigrantVisaPetition== true ?' Yes' : 'No', bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray134
        },
        margin: [0, -1, 2, 0]
      }
    )
    
    if(element.clientGeneralInfo.visaPetitionNotes!=''){
      let headerArray135: any[] = [];
      headerArray135.push([
        { text: 'Notes:', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text:  element.clientGeneralInfo.visaPetitionNotes, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
      
       
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [140,'*'],
            body: headerArray135
          },
          margin: [0, -1, 2, 0]
        }
      )
    } 
    if(element.clientGeneralInfo.hasAnyoneEverFiledImmigrantVisaPetition== true){
      let headerArray136: any[] = [];
      headerArray136.push([
        { text: 'If yes, who filed for your parent? ', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text:  element.clientGeneralInfo.relationshipOfFiledPersonForParents, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
      
       
      ]);
      headerArray136.push([
        { text: 'If yes, date Petition Filed Month/Year', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text: this.datePipe.transform(element.clientGeneralInfo.datePetitionFiled, 'MM-dd-yyyy')==''?'':this.datePipe.transform(element.clientGeneralInfo.datePetitionFiled, 'MM-dd-yyyy'), bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
      
       
      ]);
      headerArray136.push([
        { text: 'Person who filed Petition', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text:  element.clientGeneralInfo.nameOfFiledPersonForParents, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
      
       
      ]);
      headerArray136.push([
        { text: 'Result', bold: true, fontSize: 10,   border: [true, false, true, true] ,margin:[0,0,0,0], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text:  element.clientGeneralInfo.resultOfFiledPetition, bold: false, fontSize: 10,  border: [false, false, true, true] ,margin:[0,0,0,0],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
      
       
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [140,'*'],
            body: headerArray136
          },
          margin: [0, -1, 2, 0]
        }
      )
    }
    let headerArray137: any[] = [];
    headerArray137.push([
      { text: 'If you have children or step ch', bold: true, fontSize: 10,  border: [false, false, false, true] ,margin:[0,5,0,5], color:'#267FA0', borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  'ildren, Please complete this section:', bold: true, fontSize: 10,  border: [false, false, false, true] ,margin:[-14.5,5,0,5] , color:'#267FA0', borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [140,'*'],
          body: headerArray137
        },
        margin: [0, 2, 2, 0]
      }
    )
    let headerArray950: any[] = [];
    headerArray950.push([
      { text: '1.Name of Child or Stepchild ', bold: true, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:   element.clientGeneralInfo.child1.nameOfChild, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: '2.Name of Child or Stepchild', bold: true, fontSize: 10, border: [true, true, true, false], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
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
      { text: 'City and Country of Birth', bold: true, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:  element.clientGeneralInfo.child1.cityAndCountryOfBirth, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'City and Country of Birth', bold: true, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
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
      { text: 'Date of Birth', bold: true, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:  this.datePipe.transform(element.clientGeneralInfo.child1.dateOfBirth, 'MM-dd-yyyy') , bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text: 'Date of Birth', bold: true, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
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
      { text: 'Age ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.child1.age==0?'':element.clientGeneralInfo.child1.age, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'Age', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
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
      { text: 'Does child live with you? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.child1.doesChildLiveWithYou== true?' Yes' : 'No', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'Does child live with you?', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:   element.clientGeneralInfo.child2.doesChildLiveWithYou== true?' Yes' : 'No', bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
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
      { text: '3.Name of Child or Stepchild ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.child3.nameOfChild, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: '4.Name of Child or Stepchild', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
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
      { text: 'City and Country of Birth', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.child3.cityAndCountryOfBirth, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'City and Country of Birth', bold: true, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
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
      { text: 'Date of Birth ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:  this.datePipe.transform(element.clientGeneralInfo.child3.dateOfBirth, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'Date of Birth', bold: true, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
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
      { text: 'Age ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   element.clientGeneralInfo.child3.age==0?'':element.clientGeneralInfo.child3.age, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'Age', bold: true, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
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
      { text: 'Does child live with you? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.child3.doesChildLiveWithYou== true?' Yes' : 'No', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'Does child live with you?', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:   element.clientGeneralInfo.child4.doesChildLiveWithYou== true?' Yes' : 'No', bold: false, fontSize: 10,  border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
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
      { text: '5.Name of Child or Stepchild ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:    element.clientGeneralInfo.child5.nameOfChild, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: '6.Name of Child or Stepchild', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
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
      { text: 'City and Country of Birth ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:    element.clientGeneralInfo.child5.cityAndCountryOfBirth, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'City and Country of Birth', bold: true, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
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
      { text: 'Date of Birth ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:   this.datePipe.transform(element.clientGeneralInfo.child5.dateOfBirth, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'Date of Birth ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
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
      { text: 'Age ', bold: true, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:  element.clientGeneralInfo.child5.age==0?'':element.clientGeneralInfo.child5.age, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'], },
      { text: 'Age', bold: true, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
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
      { text: 'Does child live with you? ', bold: true, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080']},
      { text:    element.clientGeneralInfo.child5.doesChildLiveWithYou== true?' Yes' : 'No', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: 'Does child live with you?', bold: true, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:    element.clientGeneralInfo.child6.doesChildLiveWithYou== true?' Yes' : 'No', bold: false, fontSize: 10,  border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
     
    
     
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
      { text: '7.Name of Child or Stepchild ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.child7.nameOfChild, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text: '8.Name of Child or Stepchild', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
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
      { text: 'City and Country of Birth ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:    element.clientGeneralInfo.child7.cityAndCountryOfBirth, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text: 'City and Country of Birth ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
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
      { text: 'Date of Birth ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:   this.datePipe.transform(element.clientGeneralInfo.child7.dateOfBirth, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text: 'Date of Birth', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
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
      { text: 'Age ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      { text:  element.clientGeneralInfo.child7.age==0?'':element.clientGeneralInfo.child7.age, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text: 'Age', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
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
      { text: 'Does child live with you? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:    element.clientGeneralInfo.child7.doesChildLiveWithYou== true?' Yes' : 'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text: 'Does child live with you?', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      { text:   element.clientGeneralInfo.child8.doesChildLiveWithYou== true?' Yes' : 'No', bold: false, fontSize: 10,  border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
     
    
     
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
        { text: 'Questionnaire ', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:12, color:'#FFFFFF' ,fillColor: '#267FA0'},
        
       
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
        { text: 'Dates of entries and exits to the United States', bold: true, fontSize: 10, border: [false, false, false, false],color: '#267FA0',margin:[0,0,0,0] },
      
        
       
       
      ]);headerArray12.push([
        { text: 'If exact dates are unknown, please use the first of the month', bold: true, fontSize: 10, border: [false, false, false, false] ,color: '#267FA0',margin:[0,0,5,0] },
       
       
      
       
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
        { text: 'Entered', bold: true, fontSize: 10, border: [true, true, true, false], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text: 'Exited', bold: true, fontSize: 10, border: [true, true, true, false] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] ,},
        { text: 'Did you come enter with visa? ', bold: true, fontSize: 10, border: [true, true, true, false],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
       
       
      ]);headerArray13.push([
        { text: '1 ', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text:  this.datePipe.transform(element.clientGeneralInfo.enteredDates.enteredDate1, 'MM-dd-yyyy') , bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text: this.datePipe.transform(element.clientGeneralInfo.exitedDates.exitedDate1, 'MM-dd-yyyy') , bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text: element.clientGeneralInfo.visaEntry.didYouComeEnterWithVISA1== true?"Yes":"No", bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
       
      
       
      ]);
      headerArray13.push([
        { text: '2 ', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text:  this.datePipe.transform(element.clientGeneralInfo.enteredDates.enteredDate2, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text: this.datePipe.transform(element.clientGeneralInfo.exitedDates.exitedDate2, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text: element.clientGeneralInfo.visaEntry.didYouComeEnterWithVISA2== true?"Yes":"No", bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
       
      
       
      ]);
      headerArray13.push([
        { text: '3 ', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        { text:  this.datePipe.transform(element.clientGeneralInfo.enteredDates.enteredDate3, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text: this.datePipe.transform(element.clientGeneralInfo.exitedDates.exitedDate3, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text: element.clientGeneralInfo.visaEntry.didYouComeEnterWithVISA3== true?"Yes":"No", bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
       
      
       
      ]);
      headerArray13.push([
        { text: '4 ', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text: this.datePipe.transform(element.clientGeneralInfo.enteredDates.enteredDate4, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text: this.datePipe.transform(element.clientGeneralInfo.exitedDates.exitedDate4, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text:  element.clientGeneralInfo.visaEntry.didYouComeEnterWithVISA4== true?"Yes":"No", bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
       
      
       
      ]);
      headerArray13.push([
        { text: '5 ', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text:  this.datePipe.transform(element.clientGeneralInfo.enteredDates.enteredDate5, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text: this.datePipe.transform(element.clientGeneralInfo.exitedDates.exitedDate5, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        { text:  element.clientGeneralInfo.visaEntry.didYouComeEnterWithVISA5== true?"Yes":"No", bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
       
      
       
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
        { text: element.clientGeneralInfo.visaEntry.didYouComeEnterWithVISA6== true?"Yes":"No", bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
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
        { text: 'A. First Date of Entry: ', bold: true, fontSize: 10, border: [false, false, false, false],color: '#267FA0',margin:[0,8,0,8] },
        { text: ' ', bold: false, fontSize: 10, border: [false, false, false, false] },
        
       
       
      ]); dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray100
          },
          margin: [0, -1, 10, 0]
        }
      )
let headerArray201:any[]=[];
      headerArray201.push([
        { text: 'Month/Year', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        { text: element.clientGeneralInfo.firstDateOfEntry.dateOfEntry, bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray201
          },
          margin: [0, -1, 10, 0]
        }
      )
      let headerArray202:any[]=[]; 
      headerArray202.push([
        { text: 'Place of Arrival', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        { text: element.clientGeneralInfo.firstDateOfEntry.placeOfArrival, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray202
          },
          margin: [0, -1, 10, 0]
        }
      )
      let headerArray191: any[] = [];
      headerArray191.push([
        { text: 'Were you inspected by an Immigration Officer?  ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        { text: element.clientGeneralInfo.firstDateOfEntry.wereYouInspectedByAnImmigrationOfficer== "true" ? "Yes" : element.clientGeneralInfo.firstDateOfEntry.wereYouInspectedByAnImmigrationOfficer ==  "false" ? 'No' : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      
       
      ]);

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
      let headerArray203:any[]=[];
        headerArray203.push([
          { text: 'If not, how did you enter the United States? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
        { text: (element.clientGeneralInfo.firstDateOfEntry.unitedStatesEnteredThrough != null ? element.clientGeneralInfo.firstDateOfEntry.unitedStatesEnteredThrough : '') + '     ' + (element.clientGeneralInfo.firstDateOfEntry.kindOfDocuments != null ? element.clientGeneralInfo.firstDateOfEntry.kindOfDocuments : '') , bold: false, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
    
         
        ]);
  
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray203
          },
          margin: [0, -1, 10, 0]
        }
      )
      let headerArray101: any[] = [];
      headerArray101.push([
        { text: 'B. Last Date of Entry:', bold: true, fontSize: 10, border: [false, false, false, false],color: '#267FA0',margin:[0,8,0,8] },
        { text: ' ',bold: false, fontSize: 10, border: [false, false, false, false] },
        
       
       
      ]); dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray101
          },
          margin: [0, -1, 10, 0]
        }
      )
      let headerArray204:any[]=[]; 
      headerArray204.push([
        { text: 'Month/Year', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        { text: element.clientGeneralInfo.lastDateOfEntry.dateOfEntry, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray204
          },
          margin: [0, -1, 10, 0]
        }
      )
      let headerArray138: any[]=[];
      headerArray138.push([
        { text: 'Place of Arrival', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        { text: element.clientGeneralInfo.lastDateOfEntry.placeOfArrival,bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray138
          },
          margin: [0, -1, 10, 0]
        }
      )
      let headerArray139: any[]=[];
      headerArray139.push([
        { text: 'Were you inspected by an Immigration Officer?  ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        { text: element.clientGeneralInfo.lastDateOfEntry.wereYouInspectedByAnImmigrationOfficer == "true" ?' Yes' :  element.clientGeneralInfo.lastDateOfEntry.wereYouInspectedByAnImmigrationOfficer  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray139
          },
          margin: [0, -1, 10, 0]
        }
      )
      let headerArray140:any[]=[];
        headerArray140.push([
          { text: 'If not, how did you enter the United States?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
          { text: (element.clientGeneralInfo.lastDateOfEntry.unitedStatesEnteredThrough != null ? element.clientGeneralInfo.lastDateOfEntry.unitedStatesEnteredThrough : '') + '     ' + (element.clientGeneralInfo.lastDateOfEntry.kindOfDocuments != null ? element.clientGeneralInfo.lastDateOfEntry.kindOfDocuments : ''), bold: false, fontSize: 10, border: [true, true, true, true],  borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        
         
        ]);
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray140
          },
          margin: [0, -1, 10, 0]
        }
      )
      let headerArray102: any[] = [];
      headerArray102.push([
        { text: 'Are you currently Living in the United States?', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        { text: element.clientGeneralInfo.questions.areYouCurrentlyLivingInUSA  == "true" ?' Yes' :  element.clientGeneralInfo.questions.areYouCurrentlyLivingInUSA  == "false" ? 'No'  :  'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
      
       
      ]);
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
      if(element.clientGeneralInfo.questions.areYouCurrentlyLivingInUSA == "true" ){
        let headerArray141:any[]=[];
        headerArray141.push([
          { text: 'You have been living in the US since', bold: true, fontSize: 10, border: [true, true, false, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text:'', bold: false, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray141
            },
            margin: [0, -1, 10, 0]
          }
        )
      }
        if(element.clientGeneralInfo.questions.areYouCurrentlyLivingInUSA == "true" ){
          let headerArray205:any[]=[];
        headerArray205.push([
          { text: 'Month', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text:element.clientGeneralInfo.questions.stayingSinceMonth, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]); dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray205
            },
            margin: [0, -1, 10, 0]
          }
        )
        }
        if(element.clientGeneralInfo.questions.areYouCurrentlyLivingInUSA == "true" ){
          let headerArray206:any[]=[];
        headerArray206.push([
          { text: 'Year', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text:element.clientGeneralInfo.questions.stayingSinceYear, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray206
            },
            margin: [0, -1, 10, 0]
          }
        )
        }
        if(element.clientGeneralInfo.questions.areYouCurrentlyLivingInUSA == "true" ){
          let headerArray207:any[]=[];
        headerArray207.push([
          { text: 'Where?', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text:element.clientGeneralInfo.questions.currentLocation, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]); 
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray207
            },
            margin: [0, -1, 10, 0]
          }
        )
        }
    
      let headerArray103: any[] = [];
      headerArray103.push([
        { text: 'Have you ever had a U.S. Visa?', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        { text: element.clientGeneralInfo.questions.haveYouEverHadUSVisa  == "true" ?' Yes' :  element.clientGeneralInfo.questions.haveYouEverHadUSVisa  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       
      ]); dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray103
          },
          margin: [0,-1, 10, 0]
        }
      )

      if(element.clientGeneralInfo.questions.haveYouEverHadUSVisa == "true"){
        let headerArray142: any[]=[];
        headerArray142.push([
          { text: 'If Yes, please provide the following:', bold: true, fontSize: 10, border: [true, true, false, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text:'', bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] ,margin:[-10,0,0,0]  },
        
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray142
            },
            margin: [0,-1, 10, 0]
          }
        )
        }
        if(element.clientGeneralInfo.questions.haveYouEverHadUSVisa == "true"){   
        let headerArray143: any[]=[];
        headerArray143.push([
          { text: 'Consulate that issued visa', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text:element.clientGeneralInfo.questions.consulateThatIssuedVISA, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray143
            },
            margin: [0,-1, 10, 0]
          }
        )
      }
      if(element.clientGeneralInfo.questions.haveYouEverHadUSVisa == "true"){  
        let headerArray144: any[]=[];
        headerArray144.push([
          { text: 'Date issued', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text:this.datePipe.transform(element.clientGeneralInfo.questions.issueDateOFVISA, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray144
            },
            margin: [0,-1, 10, 0]
          }
        )
      }
      if(element.clientGeneralInfo.questions.haveYouEverHadUSVisa == "true"){  
        let headerArray145: any[]=[];
        headerArray145.push([
          { text: 'Date of Expirations', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text:this.datePipe.transform(element.clientGeneralInfo.questions.expirationDateOFVISA, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray145
          },
          margin: [0,-1, 10, 0]
        }
      )
      }
        let headerArray104: any[] = [];
        headerArray104.push([
          { text: 'Have you ever been refused a U.S. visa? ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          { text: element.clientGeneralInfo.questions.haveYouEverbeenRefusedUSVisa== "true" ?' Yes' :  element.clientGeneralInfo.questions.haveYouEverbeenRefusedUSVisa  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         
        ]);
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
        if(element.clientGeneralInfo.questions.haveYouEverbeenRefusedUSVisa== "true"){
          let headerArray146: any[]=[];
          headerArray146.push([
            { text: 'If Yes, please provide the following: ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: "", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
          
           
          ]);
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: [170,'*'],
                body: headerArray146
              },
              margin: [0, -1, 10, 0]
            }
          )
        }
        if(element.clientGeneralInfo.questions.haveYouEverbeenRefusedUSVisa== "true"){
          let headerArray147: any[]=[];
          headerArray147.push([
            { text: 'Name of Consulate denying visa issued', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: element.clientGeneralInfo.questions.consulateDenyingVISAIssued, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           
          ]);
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: [170,'*'],
                body: headerArray147
              },
              margin: [0, -1, 10, 0]
            }
          )
        }
        if(element.clientGeneralInfo.questions.haveYouEverbeenRefusedUSVisa== "true"){
          let headerArray148: any[]=[];
          headerArray148.push([
            { text: 'Denied more than Once: ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: element.clientGeneralInfo.questions.deniedMoreThanOnce== "true" ?' Yes' :  element.clientGeneralInfo.questions.deniedMoreThanOnce  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           
          ]);
        
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray148
            },
            margin: [0, -1, 10, 0]
          }
        )
        }
        let headerArray105: any[] = [];
        headerArray105.push([
          { text: 'Have you ever been stopped or questioned by an Immigration Officer: ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text: element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByAnImmigOfficer== "true" ?' Yes' :  element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByAnImmigOfficer  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         
        ]);
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
        if(element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByAnImmigOfficer== "true" ){
          let headerArray149:any[]=[];
          headerArray149.push([
            { text: 'If Yes, please provide the following: ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: "", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
          
           
          ]);
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: [170,'*'],
                body: headerArray149
              },
              margin: [0, -1, 10, 0]
            }
          )
        }
        if(element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByAnImmigOfficer== "true" ){
          let headerArray150:any[]=[];
          headerArray150.push([
            { text: 'Where?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: element.clientGeneralInfo.questions.locationOfStoppedOrQuestioned, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           
          ]);
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: [170,'*'],
                body: headerArray150
              },
              margin: [0, -1, 10, 0]
            }
          )
        }
        if(element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByAnImmigOfficer== "true" ){
          let headerArray151:any[]=[];
          headerArray151.push([
            { text: 'Month/Year', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: this.datePipe.transform(element.clientGeneralInfo.questions.dateOfStoppedOrQuestioned, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           
          ]);
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
        }
        console.log( element.clientGeneralInfo.questions.stoppedMoreThanOnce);
        if(element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByAnImmigOfficer== "true" ){
          let headerArray152:any[]=[];
          headerArray152.push([
            { text: 'More than Once', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: (element.clientGeneralInfo.questions.stoppedMoreThanOnce  == "true"  ?' Yes' :  element.clientGeneralInfo.questions.stoppedMoreThanOnce  ==  "false"  ?  'No'   : 'N/A'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           
          ]);
        
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray152
            },
            margin: [0, -1, 10, 0]
          }
        )
        }
        let headerArray106: any[] = [];
        headerArray106.push([
          { text: 'Have you ever been denied entry into the U.S.?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
          { text: element.clientGeneralInfo.questions.haveYouEverbeenDeniedEntryIntoUS== "true" ?' Yes' :  element.clientGeneralInfo.questions.haveYouEverbeenDeniedEntryIntoUS  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         
        ]);
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
        if(element.clientGeneralInfo.questions.haveYouEverbeenDeniedEntryIntoUS== "true"){
          let headerArray153:any[]=[];
          headerArray153.push([
            { text: 'If Yes, please provide the following: ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: "", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
          
           
          ]);
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: [170,'*'],
                body: headerArray153
              },
              margin: [0, -1, 10, 0]
            }
          )
        }
        if(element.clientGeneralInfo.questions.haveYouEverbeenDeniedEntryIntoUS== "true"){
          let headerArray154:any[]=[];
          headerArray154.push([
            { text: 'Where?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: element.clientGeneralInfo.questions.locationOfDeniedEntry, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           
          ]);
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: [170,'*'],
                body: headerArray154
              },
              margin: [0, -1, 10, 0]
            }
          )
        }
        if(element.clientGeneralInfo.questions.haveYouEverbeenDeniedEntryIntoUS== "true"){
          let headerArray155:any[]=[];
          headerArray155.push([
            { text: 'Month/Year', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: this.datePipe.transform(element.clientGeneralInfo.questions.dateOfDeniedEntry, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           
          ]);
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: [170,'*'],
                body: headerArray155
              },
              margin: [0, -1, 10, 0]
            }
          )
        }
        if(element.clientGeneralInfo.questions.haveYouEverbeenDeniedEntryIntoUS== "true"){
          let headerArray156:any[]=[];
          headerArray156.push([
            { text: 'More than Once?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
           
            { text: element.clientGeneralInfo.questions.deniedMoreThanOnce == "true"  ? 'Yes' :  element.clientGeneralInfo.questions.deniedMoreThanOnce  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           
          ]);
  
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray156
            },
            margin: [0, -1, 10, 0]
          }
        )
        }
        let headerArray107: any[] = [];
        headerArray107.push([
         { text: 'Have you ever been in deportation proceedings?', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.clientGeneralInfo.questions.haveYouEverbeenInDeportationProceedings== "true" ?' Yes' :  element.clientGeneralInfo.questions.haveYouEverbeenInDeportationProceedings  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
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
       if(element.clientGeneralInfo.questions.haveYouEverbeenInDeportationProceedings== "true"){
        let headerArray157:any[]=[];
         headerArray157.push([
           { text: 'If Yes, please provide the following: ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: "", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray157
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.haveYouEverbeenInDeportationProceedings== "true"){
        let headerArray158:any[]=[];
         headerArray158.push([
           { text: 'Where', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.locationOfDeportation, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);  dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray158
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.haveYouEverbeenInDeportationProceedings== "true"){
        let headerArray159:any[]=[];
         headerArray159.push([
           { text: 'Month/Year', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: this.datePipe.transform(element.clientGeneralInfo.questions.dateOfDeportationProceedings, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray159
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.haveYouEverbeenInDeportationProceedings== "true"){
        let headerArray160:any[]=[];
         headerArray160.push([
           { text: 'End Result', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.endResultOfDeportationProceedings, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray160
           },
           margin: [0, -1, 10, 0]
         }
       )
        }
       let headerArray108: any[] = [];
       headerArray108.push([
         { text: 'Have you ever been deported or removed from the U.S.?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.clientGeneralInfo.questions.haveYouEverbeenDeportedOrRemovedFromUS== "true" ?' Yes' :  element.clientGeneralInfo.questions.haveYouEverbeenDeportedOrRemovedFromUS  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
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
       if(element.clientGeneralInfo.deportedNotes!=''){
       let headerArray161:any[]=[];
        headerArray161.push([
          { text: 'Notes:', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          { text: element.clientGeneralInfo.deportedNotes, bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray161
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.haveYouEverbeenDeportedOrRemovedFromUS== "true"){
        let headerArray162:any[]=[];
         headerArray162.push([
           { text: 'If Yes, please provide the following: ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: "", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray162
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.haveYouEverbeenDeportedOrRemovedFromUS== "true"){
        let headerArray163:any[]=[];
         headerArray163.push([
           { text: 'Where', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.locationOfDeportedOrRemovedFromUS, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray163
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.haveYouEverbeenDeportedOrRemovedFromUS== "true"){
        let headerArray164:any[]=[];
         headerArray164.push([
           { text: 'Month/Year', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: this.datePipe.transform(element.clientGeneralInfo.questions.dateOfDeportation, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray164
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.haveYouEverbeenDeportedOrRemovedFromUS== "true"){
        let headerArray164:any[]=[];
         headerArray164.push([
           { text: 'Reason for Deportation', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.reasonForDeportation, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
      
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray164
           },
           margin: [0, -1, 10, 0]
         }
       )
        }
       let headerArray109: any[] = [];
       headerArray109.push([
         { text: 'Have you ever been stopped or questioned by the Police? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByThePolice== "true" ?' Yes' :  element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByThePolice  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
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
       if(element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByThePolice== "true"){
        let headerArray165: any[]=[];
         headerArray165.push([
           { text: 'If Yes, please provide the following:', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: "", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray165
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByThePolice== "true"){
        let headerArray166: any[]=[];
         headerArray166.push([
           { text: 'Where', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.locationOfStoppedByPolice, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray166
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByThePolice== "true"){
        let headerArray167: any[]=[];
         headerArray167.push([
           { text: 'Month/Year', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: this.datePipe.transform(element.clientGeneralInfo.questions.dateOfStop, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray167
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByThePolice== "true"){
        let headerArray168: any[]=[];
         headerArray168.push([
           { text: 'Reason for Stop', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.reasonForStop, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray168
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.haveYouEverbeenStoppedOrQuestionedByThePolice== "true"){
        let headerArray169: any[]=[];
         headerArray169.push([
           { text: 'Were you arrested? ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.wereYouArrested== "true" ?' Yes' :  element.clientGeneralInfo.questions.wereYouArrested  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
       
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray169
           },
           margin: [0, -1, 10, 0]
         }
       )
        }
       let headerArray110: any[] = [];
       headerArray110.push([
         { text: 'Have you ever been charged with committing a crime? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.clientGeneralInfo.questions.haveYouEverbeenChargedWWithCommittingACrime== "true" ?' Yes' :  element.clientGeneralInfo.questions.haveYouEverbeenChargedWWithCommittingACrime  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
      
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
      if(element.clientGeneralInfo.questions.haveYouEverbeenChargedWWithCommittingACrime== "true"){
         let headerArray167: any[] = [];
         headerArray167.push([
          { text: 'If Yes, please provide the following:', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          { text: "", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
        
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray167
            },
            margin: [0, -1, 10, 0]
          }
          
        )}
         if(element.clientGeneralInfo.questions.haveYouEverbeenChargedWWithCommittingACrime== "true"){
          let headerArray168:any[]=[];
         headerArray168.push([
          { text: 'Where', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          { text: element.clientGeneralInfo.questions.locationOfCrimeCharged, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray168
            },
            margin: [0, -1, 10, 0]
          }
        )
      }
      if(element.clientGeneralInfo.questions.haveYouEverbeenChargedWWithCommittingACrime== "true"){
      let headerArray169:any[]=[];
        headerArray169.push([
          { text: 'Month/Year', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          { text: this.datePipe.transform(element.clientGeneralInfo.questions.dateOfCrimeCharged, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray169
            },
            margin: [0, -1, 10, 0]
          }
        )
      }
      if(element.clientGeneralInfo.questions.haveYouEverbeenChargedWWithCommittingACrime== "true"){
        let headerArray170:any[]=[];
        headerArray170.push([
          { text: 'Crime Charged', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          { text: element.clientGeneralInfo.questions.crimeCharged, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray170
            },
            margin: [0, -1, 10, 0]
          }
        )
      }
      if(element.clientGeneralInfo.questions.haveYouEverbeenChargedWWithCommittingACrime== "true"){
        let headerArray171:any[]=[];
        headerArray171.push([
          { text: 'Did you go to court?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          { text: element.clientGeneralInfo.questions.didYouGotoCourt== "true" ?' Yes' :  element.clientGeneralInfo.questions.didYouGotoCourt  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        
         
        ]); 
      

       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray171
           },
           margin: [0, -1, 10, 0]
         }
       )
        }
       let headerArray111: any[] = [];
       headerArray111.push([
         { text: 'Have you ever been FOUND GUILTY of committing a crime? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.clientGeneralInfo.questions.haveYouEverbeenFoundGuiltyOfCommittingACrime== "true" ?' Yes' :  element.clientGeneralInfo.questions.haveYouEverbeenFoundGuiltyOfCommittingACrime  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
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
      let headerArray645: any[] = [];
      if(element.clientGeneralInfo.guiltyNotes!=''){
       headerArray645.push([
         { text: 'Notes: ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text:element.clientGeneralInfo.guiltyNotes, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
      }
      dd.content.push(
       {
         table: {
           headerRows: 1,
           widths: [170,'*'],
           body: headerArray645
         },
         margin: [0, -1, 10, 0]
       }
     )
       if(element.clientGeneralInfo.questions.haveYouEverbeenFoundGuiltyOfCommittingACrime== "true"){
        let headerArray172:any[]=[];
         headerArray172.push([
           { text: 'If Yes, please provide the following:', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: "", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray172
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.haveYouEverbeenFoundGuiltyOfCommittingACrime== "true"){
        let headerArray173:any[]=[];
         headerArray173.push([
           { text: 'Where', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.foundGuiltyLocation, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray173
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.haveYouEverbeenFoundGuiltyOfCommittingACrime== "true"){
        let headerArray174:any[]=[];
         headerArray174.push([
           { text: 'Month/Year', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: this.datePipe.transform(element.clientGeneralInfo.questions.dateOfFoundGuiltyCrime, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray174
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.haveYouEverbeenFoundGuiltyOfCommittingACrime== "true"){
        let headerArray175:any[]=[];
         headerArray175.push([
           { text: 'Crime Charged', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.foundGuiltyCrimeCharged, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]); dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray175
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.haveYouEverbeenFoundGuiltyOfCommittingACrime== "true"){
        let headerArray176:any[]=[];
         headerArray176.push([
           { text: 'Penalty', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.penalty, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray176
           },
           margin: [0, -1, 10, 0]
         }
       )
        }
     
       let headerArray112: any[] = [];
       headerArray112.push([
         { text: 'Do you have a valid driver license?', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
        
         { text: element.clientGeneralInfo.questions.doYouHaveValidDriverLicense== "true" ?' Yes' :  element.clientGeneralInfo.questions.doYouHaveValidDriverLicense  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
        
       ]);
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
       if(element.clientGeneralInfo.questions.doYouHaveValidDriverLicense== "true"){
        let headerArray177:any[]=[];
         headerArray177.push([
           { text: 'If Yes, please provide the following: ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: "", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray177
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.doYouHaveValidDriverLicense== "true"){
        let headerArray178:any[]=[];
         headerArray178.push([
           { text: 'State', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.stateOfDriverLicense, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray178
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.doYouHaveValidDriverLicense== "true"){
        let headerArray179:any[]=[];
         headerArray179.push([
           { text: 'Expiration Date', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: this.datePipe.transform(element.clientGeneralInfo.questions.dateOfDriverLicense, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
    
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray179
           },
           margin: [0, -1, 10, 0]
         }
       )
        }
       let headerArray113: any[] = [];
       headerArray113.push([
         { text: 'Do you pay taxes in the U.S.? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.clientGeneralInfo.questions.doYouPayTaxesInUS== "true" ?' Yes' :  element.clientGeneralInfo.questions.doYouPayTaxesInUS  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
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
       if(element.clientGeneralInfo.questions.doYouPayTaxesInUS== "true"){
        let headerArray180:any[]=[];
         headerArray180.push([
           { text: 'If Yes, please provide the following: ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: "", bold: true, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'],margin:[-10,0,0,0] },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray180
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.questions.doYouPayTaxesInUS== "true"){
        let headerArray181:any[]=[];
         headerArray181.push([
           { text: 'Year', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.sinceWhen, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
       
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray181
           },
           margin: [0, -1, 10, 0]
         }
       )
        }
       let headerArray114: any[] = [];
       headerArray114.push([
         { text: 'Are you currently employed using a U.S. Social Security Number? ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.clientGeneralInfo.questions.areYouCurrentlyEmployedUsingUSSSN== "true" ?' Yes' :  element.clientGeneralInfo.questions.areYouCurrentlyEmployedUsingUSSSN  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
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
       if(element.clientGeneralInfo.questions.areYouCurrentlyEmployedUsingUSSSN== "true"){
        let headerArray182:any[]=[];
         headerArray182.push([
           { text: 'If Yes, is it valid ?: ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.ssnValidStatus, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray182
           },
           margin: [0, -1, 10, 0]
         }
       )
        }
       let headerArray115: any[] = [];
       headerArray115.push([
         { text: 'Do you have employment authorization? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.clientGeneralInfo.questions.doYouHaveEmploymentAuthorization== "true" ?' Yes' :  element.clientGeneralInfo.questions.doYouHaveEmploymentAuthorization  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
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
       if(element.clientGeneralInfo.questions.doYouHaveEmploymentAuthorization  == "true"){
        let headerArray183:any[]=[];
         headerArray183.push([
           { text: 'If Yes, is it valid ?: ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
          
           { text: element.clientGeneralInfo.questions.employmentAuthorizationValidStatus, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray183
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
      
       let headerArray116: any[] = [];
       headerArray116.push([
        { text: 'What is your occupation: ', bold: true, fontSize: 10, border: [true, true, false, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
        { text: element.clientGeneralInfo.questions.occupation, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080'] },
      
       
      ]);
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
      let headerArray184:any[]=[];
      headerArray184.push([
        { text: 'Are you currently working? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        { text: element.clientGeneralInfo.questions.areYouCurrentlyWorking == true ? 'Yes' : 'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
      
      
      ]);
       dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray184
          },
          margin: [0, -1, 10, 0]
        }
      )
      if(element.clientGeneralInfo.questions.areYouCurrentlyWorking == false ){
      let headerArray185:any[]=[];
      headerArray185.push([
       { text: 'If not, why not and are you receiving unemployment?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
      
       { text: element.clientGeneralInfo.questions.areYouReceivingUnemployment  == "true" ? 'Yes':element.clientGeneralInfo.questions.areYouReceivingUnemployment =="false" ?'No':'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      
     ]);
     dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [170,'*'],
          body: headerArray185
        },
        margin: [0, -1, 10, 0]
      }
    )
   }
   let headerArray186:any[]=[];
       headerArray186.push([
         { text: 'Have you filed taxes during the last 3 years? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.clientGeneralInfo.questions.haveYouFiledTaxesDuringLast3Years == "true" ?' Yes' :  element.clientGeneralInfo.questions.haveYouFiledTaxesDuringLast3Years  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray186
          },
          margin: [0, -1, 10, 0]
        }
      )
      if(element.clientGeneralInfo.questions.haveYouFiledTaxesDuringLast3Years!="true"){
        let headerArray187:any[]=[];
        headerArray187.push([
         { text: 'If not, why? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.clientGeneralInfo.questions.reasonForNonTaxFiling, bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
       dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray187
          },
          margin: [0, -1, 10, 0]
        }
      )
      }
      let headerArray188:any[]=[];
       headerArray188.push([
        { text: 'Highest level of Education:  ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        { text: element.clientGeneralInfo.questions.education, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
      
       
      ]);
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray188
           },
           margin: [0, -1, 10, 0]
         }
       )
       let headerArray117: any[] = [];
       headerArray117.push([
         { text: 'other  ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.clientGeneralInfo.questions.educationOther, bold: false, fontSize: 10, border: [true, true, true, true] , borderColor: ['#808080', '#808080', '#808080', '#808080'] },
       
        
       ]);
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
      let headerArray189:any[]=[];
       headerArray189.push([
         { text: 'English language proficiency  ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.clientGeneralInfo.questions.englishLanguageProficiency, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       ]);
       dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray189
          },
          margin: [0, -1, 10, 0]
        }
      )
   let headerArray190:any[]=[];
       headerArray190.push([
         { text: 'Do you have any certificate of continuing education /Training /Special licenses? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.clientGeneralInfo.questions.owningAnyCertications == "true" ?' Yes' : element.clientGeneralInfo.questions.owningAnyCertications  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray190
          },
          margin: [0, -1, 10, 0]
        }
      )
       if(element.clientGeneralInfo.questions.owningAnyCertications== "true"){
        let headerArray191:any[]=[];
         headerArray191.push([
           { text: 'If yes, what? ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
          
           { text: element.clientGeneralInfo.questions.certicateDetails, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          
         ]);
       
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
        }
       let headerArray118: any[] = [];
       headerArray118.push([
         { text: 'Other Evidence ', bold: true, fontSize: 10, border: [false, false, false, false],color: '#267FA0',margin:[0,5,0,5], },
         { text: ' ', bold: false, fontSize: 10, border: [false, false, false, false] },
         
        
        
       ]);
       dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray118
          },
          margin: [5, -1, 10, 0]
        }
      )
      let headerArray1999: any[] = [];
       headerArray1999.push([
         { text: 'Do you own your home? ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text:element.clientGeneralInfo.clientReceivedBenefits.doYouOwnYourHome == "true" ?' Yes' : element.clientGeneralInfo.clientReceivedBenefits.doYouOwnYourHome  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
       dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [170,'*'],
            body: headerArray1999
          },
          margin: [0, -1, 10, 0]
        }
      )
       if(element.clientGeneralInfo.clientReceivedBenefits.doYouOwnYourHome== "true"){
        let headerArray192:any[]=[];
         headerArray192.push([
           { text: 'Are you paying any property? What type? ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
          
           { text: element.clientGeneralInfo.clientReceivedBenefits.whatType, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          
         ]);
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray192
           },
           margin: [0, -1, 10, 0]
         }
       )
        }
       let headerArray119: any[] = [];
       headerArray119.push([
         { text: 'Have you or any immediate family member ever been a victim of acrime or a violent act in the United States?  ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.clientGeneralInfo.clientReceivedBenefits.haveYouOrAnyFamilyMemberEverbeenAVictimOfACrimeOrAViolentAct == "true" ?' Yes' : element.clientGeneralInfo.clientReceivedBenefits.haveYouOrAnyFamilyMemberEverbeenAVictimOfACrimeOrAViolentAct  == "false" ? 'No'  : 'N/A',bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
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
       if(element.clientGeneralInfo.victimOfACrimeNotes!=''){
        let headerArray193:any[]=[];
        headerArray193.push([
          { text: 'Notes:  ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
          { text: element.clientGeneralInfo.victimOfACrimeNotes,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray193
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.clientReceivedBenefits.haveYouOrAnyFamilyMemberEverbeenAVictimOfACrimeOrAViolentAct== "true"){
        let headerArray194:any[]=[];
         headerArray194.push([
           { text: 'If Yes, please complete below..', bold: true, fontSize: 10, border: [false, true, false, false],color: '#267FA0',margin:[0,5,0,5], },
           { text: ' ', bold: false, fontSize: 10, border: [false, true, false, false] },
 
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray194
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.clientReceivedBenefits.haveYouOrAnyFamilyMemberEverbeenAVictimOfACrimeOrAViolentAct== "true"){
      let headerArray195:any[]=[];
         headerArray195.push([
           { text: 'Date of crime or violent act committed ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
          
           { text:this.datePipe.transform(element.clientGeneralInfo.clientReceivedBenefits.dateOfCrimeOrViolentActCommitted, 'MM-dd-yyyy') , bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray195
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.clientReceivedBenefits.haveYouOrAnyFamilyMemberEverbeenAVictimOfACrimeOrAViolentAct== "true"){
        let headerArray196:any[]=[];
         headerArray196.push([
           { text: 'Was it reported?', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
          
           { text:element.clientGeneralInfo.clientReceivedBenefits.wasItReported== "true" ?' Yes' : element.clientGeneralInfo.clientReceivedBenefits.wasItReported  == "false" ? 'No'  : 'N/A' , bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray196
            },
            margin: [0, -1, 10, 0]
          }
        )
       }
       if(element.clientGeneralInfo.clientReceivedBenefits.haveYouOrAnyFamilyMemberEverbeenAVictimOfACrimeOrAViolentAct== "true"){
        let headerArray197:any[]=[];
         headerArray197.push([
           { text: 'Summary of what happened:', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
          
           { text:element.clientGeneralInfo.clientReceivedBenefits.summaryOfWhatHappened , bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [170,'*'],
              body: headerArray197
            },
            margin: [0, -1, 10, 0]
          }
        )
        }
      let headerArray198:any[]=[];
       headerArray198.push([
         { text: ' Have you ever asked for any public assistance? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.clientGeneralInfo.haveYouEverAskedForAnyPubilcAssistance  == "true" ?' Yes' :  element.clientGeneralInfo.haveYouEverAskedForAnyPubilcAssistance  == "false" ? 'No'  : 'N/A', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
   
       dd.content.push(
         {
           table: {
             headerRows: 1,
             widths: [170,'*'],
             body: headerArray198
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
         let headerArray523: any[] = [];
         headerArray523.push([
           { text: 'Naturalization Eligibility Test ', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:12, color:'#FFFFFF' ,fillColor: '#267FA0'},
           
          
         ]);
         dd.content.push(
           {
             table: {
               headerRows: 1,
               widths: ['*'],
               body: headerArray523
             },
             margin: [0, 10, 10, 0]
           }
         )
         let headerArray600: any[] = [];
         headerArray600.push([
           { text: 'Basic Requirements', bold: true, fontSize: 10, border: [true, true, false, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
          
           { text: '', bold: false, fontSize: 10, border: [false, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          
         ]);  dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [250,'*'],
              body: headerArray600
            },
            margin: [0, -1, 10, 0]
          }
        )
        let headerArray601: any[] = [];
         headerArray601.push([
           { text: 'Have you been a permanent resident of the US for at least 5 years OR at least 3 years and been married to and living with the same US citizen for the last 3 years?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
          
           { text:element.basicRequirementsQuestions.haveYouBeenAPermanentResidentFor5OR3Years == true ?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [250,'*'],
              body: headerArray601
            },
            margin: [0, -1, 10, 0]
          }
        )
        let headerArray602: any[] = [];
         headerArray602.push([
           { text: 'Have you lived at the same address the past 3 months? ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
          
           { text: element.basicRequirementsQuestions.areYouLivingAtTheSameAddressForPast3Months == true ?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [250,'*'],
              body: headerArray602
            },
            margin: [0, -1, 10, 0]
          }
        )
        let headerArray603: any[] = [];
         headerArray603.push([
           { text: 'Were any of your parents/grandparents US citizens before your 18th birthday?  ', bold: true, fontSize: 10, border: [true, true, true, false],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
          
           { text: element.basicRequirementsQuestions.wereAnyOfYourParentsORGrandparentsUSCitizensBefore18thBirthday == true?'Yes':'No',bold: false, fontSize: 10, border: [true, false, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [250,'*'],
              body: headerArray603
            },
            margin: [0, -1, 10, 0]
          }
        )
        let headerArray604: any[] = [];
         headerArray604.push([
           { text: 'MALES: have you registered w/ Selective Service?  ', bold: true, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
          
           { text: element.basicRequirementsQuestions.haveYouRegisteredWithSelectiveService == true ? 'Yes' : 'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [250,'*'],
              body: headerArray604
            },
            margin: [0, -1, 10, 0]
          }
        )
         if(element.basicRequirementsQuestions.haveYouRegisteredWithSelectiveService== true){
          let headerArray605: any[] = [];
         headerArray605.push([
           { text: 'Selective Service System ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
          
           { text: element.basicRequirementsQuestions.selectiveServiceSystem, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [250,'*'],
              body: headerArray605
            },
            margin: [0, -1, 10, 0]
          }
        )
         }
         if(element.basicRequirementsQuestions.haveYouRegisteredWithSelectiveService== true){
          let headerArray606: any[] = [];
         headerArray606.push([
           { text: 'Date Registered', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
          
           { text: this.datePipe.transform(element.basicRequirementsQuestions.registeredDate, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [250,'*'],
              body: headerArray606
            },
            margin: [0, -1, 10, 0]
          }
        )
        }
        let headerArray607: any[] = [];
         headerArray607.push([
           { text: 'Absences(have you…) ', bold: true, fontSize: 10, border: [true, true, false, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
          
           { text: '', bold: false, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
          
         ]);
         dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [250,'*'],
              body: headerArray607
            },
            margin: [0, -1, 10, 0]
          }
        )
        let headerArray608: any[] = [];
         headerArray608.push([
          { text: 'Taken any trips outside the US for more than 1 year? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
          { text: element.absencesQuestions.haveYouTakenAnyTripsOutsideUSForMoreThanOneYear == true ?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [250,'*'],
              body: headerArray608
            },
            margin: [0, -1, 10, 0]
          }
        )
        let headerArray609: any[] = [];
        headerArray609.push([
          { text: 'Taken any trips outside the US for more than 6 months/less than a year?  ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
          { text: element.absencesQuestions.haveYouTakenAnyTripsOutsideUSForMoreThanSixMonthsORLessThanAYear == true ?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [250,'*'],
              body: headerArray609
            },
            margin: [0, -1, 10, 0]
          }
        )
        let headerArray610: any[] = [];
        headerArray610.push([
          { text: 'Worked outside of US?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
          { text: element.absencesQuestions.didYouWorkOutsideOfUS == true?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [250,'*'],
              body: headerArray610
            },
            margin: [0, -1, 10, 0]
          }
        )
        let headerArray611: any[] = [];
        headerArray611.push([
          { text: 'Detentions(have you…) ', bold: true, fontSize: 10, border: [true, true, false, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
         
          { text: '', bold: false, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [250,'*'],
              body: headerArray611
            },
            margin: [0, -1, 10, 0]
          }
        )
        let headerArray612: any[] = [];
        headerArray612.push([
         { text: 'Ever been detained by Border Patrol/Immigration and Customs Enforcement (ICE)? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.detentionsQuestions.haveYouEverbeenDetainedByBorderPatrolORICE == true ?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [250,'*'],
            body: headerArray612
          },
          margin: [0, -1, 10, 0]
        }
      )
      let headerArray613: any[] = [];
       headerArray613.push([
         { text: 'Ever been detained briefly at border or airport?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.detentionsQuestions.haveYouEverbeenDetainedBrieflyAtBorderOrAirport == true ?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [250,'*'],
            body: headerArray613
          },
          margin: [0, -1, 10, 0]
        }
      )
      let headerArray614: any[] = [];
       headerArray614.push([
         { text: 'Ever been fingerprinted or have signed anything when detained?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.detentionsQuestions.haveYouEverbeenFingerprintedOrSignedWhenDetained == true?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [250,'*'],
            body: headerArray614
          },
          margin: [0, -1, 10, 0]
        }
      )
      let headerArray615: any[] = [];
       headerArray615.push([
        { text: 'Deportation/Removal(have you…) ', bold: true, fontSize: 10, border: [true, true, false, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        { text: '', bold: false, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
      
       
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [250,'*'],
            body: headerArray615
          },
          margin: [0, -1, 10, 0]
        }
      )
      let headerArray616: any[] = [];
      headerArray616.push([
       { text: 'Ever been or are you now in immigration proceedings/before an Immigration Judge ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
      
       { text: element.deporationQuestions.haveYouEverbeenOrAreYouNowInImmigrationProceedings == true ?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      
     ]);
     dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray616
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray617: any[] = [];
     headerArray617.push([
       { text: 'Ever been detained and did not go to court?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
      
       { text: element.deporationQuestions.haveYouEverbeenDetainedAndDidNotGoToCourt == true ?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      
     ]);
     dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray617
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray618: any[] = [];
     headerArray618.push([
       { text: 'Ever been deported and returned unlawfully (Entry Without Inspection)?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
      
       { text: element.deporationQuestions.haveYouEverbeenDeportedAndReturnedUnlawfully == true?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      
     ]);
     dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray618
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray619: any[] = [];
     headerArray619.push([
      { text: 'Approximate Date', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text: this.datePipe.transform(element.deporationQuestions.approximateDate, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray619
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray620: any[] = [];
    headerArray620.push([
      { text: 'Ever been ordered deported and never left?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text: element.deporationQuestions.haveYouEverbeenOrderedDeportedAndNeverLeft== true ? 'Yes' : 'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray620
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray621: any[] = [];
    headerArray621.push([
      { text: 'Ever been granted Voluntary Deportation and never left?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text: element.deporationQuestions.haveYouEverbeenGrantedVoluntaryDeportationAndNeverLeft== true?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray621
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray622: any[] = [];
    headerArray622.push([
      { text: 'Ever applied for any kind of relief from deportation (asylum,etc.)?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text: element.deporationQuestions.haveYouEverAppliedForAnyKindOfReliefFromDeportation== true?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray622
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray623: any[] = [];
    headerArray623.push([
      { text: 'Crimes', bold: true, fontSize: 10, border: [true, true, false, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text: "", bold: false, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray623
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray624: any[] = [];
    headerArray624.push([
      { text: 'Have you ever been cited/received traffic tickets?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text: element.crimeQuestions.haveYouEverbeenCitedTrafficTickets == true ?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray624
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray625: any[] = [];
    headerArray625.push([
      { text: 'Have you ever been arrested by the police?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text: element.crimeQuestions.haveYouEverbeenArrestedByPolice== true?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray625
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray626: any[] = [];
    headerArray626.push([
      { text: 'Have you ever been charged with violating any law or convicted of a crime or offense?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text: element.crimeQuestions.haveYouEverbeenChargedWithViolatingAnyLawOrCrimeOrOffense== true?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray626
        },
        margin: [0, -1, 10, 0]
      }
    )
  
    let headerArray628: any[] = [];
    headerArray628.push([
      { text: 'Have you ever been in jail?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text: element.crimeQuestions.haveYouEverbeenInJail== true ?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray628
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray629: any[] = [];
    headerArray629.push([
      { text: 'Are you currently on probation/parole?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text: element.crimeQuestions.areYouCurrentlyOnProbationOrParole== true?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
   
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray629
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray526: any[] = [];
    headerArray526.push([
      { text: 'Crimes', bold: true, fontSize: 10, border: [true, true, false, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text:'', bold: false, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray526
        },
        margin: [0, -1, 10, 0]
      }
    )
  
    let headerArray630: any[] = [];
    headerArray630.push([
      { text: 'Failed to reveal applicable arrests/charges/detentions/deportations when applying for residency?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text:element.crimeQuestions.haveYouFailedToRevealApplicableArrestsWhenApplyingForResidency== true?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray630
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray631: any[] = [];
    headerArray631.push([
      { text: 'Lied/committed fraud to receive public benefits?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text:element.crimeQuestions.haveYouLiedOrCommittedFraudToReceivePublicBenefits == true?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray631
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray632: any[] = [];
    headerArray632.push([
      { text: 'Married someone in order to obtain an immigration benefit?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text:element.crimeQuestions.haveYouMarriedSomeoneInOrderToObtainAnImmigrationBenefit== true?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray632
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray633: any[] = [];
    headerArray633.push([
      { text: 'Good Moral Character', bold: true, fontSize: 10, border: [true, true, false, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text:'', bold: false, fontSize: 10, border: [false, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray633
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray634: any[] = [];
    headerArray634.push([
      { text: 'Have you ever claimed to be a US citizen? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text:element.goodMoralCharacterQuestions.haveYouEverClaimedToBeAUSCitizen== true?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray634
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray635: any[] = [];
    headerArray635.push([
      { text: 'Have you ever registered to vote in the US or ever voted in a US election? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text:element.goodMoralCharacterQuestions.haveYouEverRegisteredToVoteInUSOrEverVotedInUSElection== true?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray635
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray636: any[] = [];
    headerArray636.push([
      { text: 'Do you owe overdue taxes? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text:element.goodMoralCharacterQuestions.doYouOweOverdueTaxes== true ?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray636
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray637: any[] = [];
    headerArray637.push([
      { text: 'Have you ever failed to file income taxes? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text:element.goodMoralCharacterQuestions.haveYouOverfailedToFileRequiredIncomeTaxes== true ?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray637
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray638: any[] = [];
    headerArray638.push([
      { text: 'Do you have any children under the age of 18 that do not reside with you? ', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text:element.goodMoralCharacterQuestions.doYouHaveAnyChildrenUnderTheAgeOf18ThatDoNotResideWithYou== true ?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray638
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray639: any[] = [];
    headerArray639.push([
      { text: 'Do you have proof of support for these children not living with you?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text:element.goodMoralCharacterQuestions.doYouHaveProofOfSupportForTheseChildrenNotLivingWithYou== true?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]); 
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray639
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray640: any[] = [];
    headerArray640.push([
      { text: 'Do you owe child support?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text:element.goodMoralCharacterQuestions.doYouOweChildSupport== true ?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]); 
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray640
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray641: any[] = [];
    headerArray641.push([
      { text: 'Have you ever been married to more than one person at the same time?(even if marriage was in another country)', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text:element.goodMoralCharacterQuestions.haveYouEverbeenMarriedToMorethanOnePersonAtTheSametime== true ?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray641
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray642: any[] = [];
    headerArray642.push([
      { text: 'Have you ever helped or tried to help anyone enter the US illegally?', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text:element.goodMoralCharacterQuestions.haveYouEverHelpedOrTriedToHelpAnyoneEnterUSIllegally== true ?'Yes':'No', bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
    dd.content.push(
      {
        table: {
          headerRows: 1,
          widths: [250,'*'],
          body: headerArray642
        },
        margin: [0, -1, 10, 0]
      }
    )
    let headerArray643: any[] = [];
    headerArray643.push([
      { text: 'Notes:', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
     
      { text:element.basicRequirementsQuestions.notes==''?'':element.basicRequirementsQuestions.notes, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
    
     
    ]);
       
       
         dd.content.push(
           {
             table: {
               headerRows: 1,
               widths: [250,'*'],
               body: headerArray643
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
              pageBreak: 'after',
            }]
          })
       let headerArray51: any[] = [];
       headerArray51.push([
         { text: 'Case Plan ', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:12, color:'#FFFFFF' ,fillColor: '#267FA0'},
         
        
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
         { text: 'Deadline', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: this.datePipe.transform(element.clientGeneralInfo.deadlineDate, 'MM-dd-yyyy'), bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
       headerArray15.push([
         { text: 'Initial Deposit', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text:element.casePlan.intialDepositAmount, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        
       ]);
       headerArray15.push([
         { text: 'Method ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
        
         { text: element.casePlan.checkOrCash ==  true ? 'Cash' : 'Cheque', bold: false, fontSize: 10, border: [true, true, true, true] ,borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        
       ]);
       headerArray15.push([
         { text: 'Case Plan  ', bold: true, fontSize: 10, border: [true, true, true, false],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
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
         { text: 'Legal Fees:', bold: true, fontSize: 10, border: [false, false, false, false] ,margin:[-75,0,10,0],color:'#267FA0' },
        
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
         { text: 'Government Filing Fees:', bold: true, fontSize: 10, border: [false, false, false, false],margin:[-75,0,10,0],color:'#267FA0' },
        
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
       headerArray17.push([
        { text: '6', bold: false, fontSize: 10, border: [true, true, true, true] ,margin:[0,0,10,0] ,borderColor: ['#808080', '#808080', '#808080', '#808080']},
         { text: element.casePlan.governmentFilngType6, bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
        
         { text: element.casePlan.governmentFilingAmount6,bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
       
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
         { text: 'Declaration ', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:12, color:'#FFFFFF' ,fillColor: '#267FA0'},
         
        
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
         { text: 'I hereby certify that all the information which I have provided is true and correct based on my personal knowledge. I understand that my failure to give accurate and true information could compromise my representation in the future. I further understand, that a consultation with the firm does not constitute legal representation and that the firm, Monty & Ramirez LLP, does not represent me unless I have been presented with an engagement letter.', bold: false, fontSize: 10, border: [false, false, false, false],alignment:'justify' },
     
        
       ]);
       headerArray25.push([
         { text: 'Date:'+this.datePipe.transform(element.clientGeneralInfo.date, 'MM-dd-yyyy') , bold: true, fontSize: 10, border: [false, false, false, false], color:'#267FA0' },
     
        
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
         { text: 'Assign ', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:12, color:'#FFFFFF' ,fillColor: '#267FA0'},
         
        
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
         { text: 'There will be a fee of  '+''+ '$' +''+( element.feeInDollar != null ? element.feeInDollar: '') +'per consultation. This fee will be charged before your consultation with the attorney. Please complete the questions below.', bold: false, fontSize: 10, border: [false, false, false, false]},
     
        
       ]);
       headerArray26.push([
         { text:  intakeRes.referenceField1== null ? 'Consultation Date:':'Consultation Date:'+this.datePipe.transform(intakeRes.referenceField1, 'MM-dd-yyyy'), bold: true, fontSize: 10, border: [false, false, false, false], color:'#267FA0' },  //31/7/23
     
        
       ]);
       headerArray26.push([
         { text: intakeRes.referenceField2 == null ? 'Assigned Attorney:':'Assigned Attorney:'+ intakeRes.referenceField2, bold: true, fontSize: 10, border: [false, false, false, false], color:'#267FA0' },  //31/7/23
     
        
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
         { text: 'Office Use ', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:12, color:'#FFFFFF' ,fillColor: '#267FA0'},
         
        
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
        { text: 'Attorney', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
        { text: element.attorney, bold: false, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
      
       
      ]);
      headerArray79.push([
        { text: 'Legal Assistant', bold: true, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
       
        { text:element.legalAssistant, bold: false, fontSize: 10, border: [true, true, true, true],borderColor: ['#808080', '#808080', '#808080', '#808080']   },
      
       
      ]);
      headerArray79.push([
        { text: 'Date ', bold: true, fontSize: 10, border: [true, true, true, true], borderColor: ['#808080', '#808080', '#808080', '#808080']  },
       
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
