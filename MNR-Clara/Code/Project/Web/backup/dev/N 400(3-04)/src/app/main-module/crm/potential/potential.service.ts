import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
export interface prospectiveClienlistelement {
  status: string;
  date: Date;
}
export interface AgreementElement {
  agreementCode: string;
  caseCategoryId: number;
  emailId: string;
  firstNameLastName: string;
  inquiryNumber: string;
  intakeFormId: number;
  statusId: number;
}
export interface PotentialElement {
  addressLine1: string;
  addressLine2: string;
  addressLine3: string;
  agreementCode: string;
  alternateTelephone1: string;
  alternateTelephone2: string;
  caseCategoryId: number;
  city: string;
  classId: number;
  consultationDate: Date;
  contactNumber: string;
  country: string;
  createdBy: string;
  createdOn: Date;
  deletionIndicator: number;
  emailId: string;
  firstName: string;
  firstNameLastName: string;
  inquiryNumber: string;
  intakeFormId: number;
  intakeFormNumber: string;
  languageId: string;
  lastName: string;
  lastNameFirstName: string;
  mailingAddress: string;
  occupation: string;
  pcNotesNumber: string;
  potentialClientId: string;
  referenceField1: string;
  referenceField10: string;
  referenceField2: string;
  referenceField3: string;
  referenceField4: string;
  referenceField5: string;
  referenceField6: string;
  referenceField7: string;
  referenceField8: string;
  referenceField9: string;
  referralId: number;
  socialSecurityNo: string;
  state: string;
  statusId: number;
  transactionId: number;
  updatedBy: string;
  updatedOn: Date;
  zipCode: string;
  statusId_des: string;
  clientCategoryId: string;
}

@Injectable({
  providedIn: 'root'
})
export class PotentialService {
  constructor(private http: HttpClient) { }
  apiNamesetup = '/mnr-setup-service/';
  // apiName = '/wms-crm-service/';
  apiName = '/mnr-crm-service/';
  methodeName = 'potentialClient';
  url = this.apiName + this.methodeName;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + `/` + code);
  }

  Update(obj: any, code: any) {
    return this.http.patch<any>(this.url + `/` + code, obj);
  }

  Agreement(obj: any, code: any) {
    return this.http.patch<any>(this.url + `/` + code + '/agreement', obj);
  }
  ClientGeneral(code: any) {
    return this.http.get<any>(this.url + `/` + code + '/clientGeneral');
  }
  Delete(code: any) {
    return this.http.delete(this.url + `/` + code, { responseType: 'text' });
  }

  Get_agreement(code: string) {
    return this.http.get<any>(this.apiName + 'agreement' + `/` + code);
  }
  Get_agreementTemplate(code: string) {
    return this.http.get<any>(this.apiNamesetup + `agreementTemplate/` + code);
  }
  getfile(code: any, url: any): Promise<File> {
    return this.http
      .get<any>('/doc-storage/download?fileName=' + url + '&location=' + code, {
        responseType: 'blob' as 'json',
      })
      .toPromise();
  }
  getfile1(code: any, url: any, classid: any,): Promise<File> {
    return this.http
      .get<any>('/doc-storage/download?fileName=' + url + '&location=' + classid  + '&classId=' + code, {
        responseType: 'blob' as 'json',
      })
      .toPromise();
  }



  post_agreement(code: string, obj: any) {
    return this.http.post<any>(this.apiName + 'agreement?potentialClientId=' + code, obj);
  }


  Search(obj: any) {
    return this.http.post<any>(this.url + '/findPotentialClient', obj);

  }


  docusignstatus(code: string) {
    return this.http.get<any>(this.apiName + 'agreement/docusign/envelope/status?potentialClientId=' + code);
  }
  docuSignDownload(code: string) {

    return this.http.get(this.apiName + 'agreement/docusign/envelope/download?potentialClientId=' + code, { responseType: 'text' });


  }

}
