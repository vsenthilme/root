import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

export interface ImmigirationElement {
  adressLine1: string;
  adressLine2: string;
  alienNumber: string;
  alternateEmailId: string;
  alternateTelephone1: string;
  caseCategoryId: number;
  caseSubCategoryId: number;
  city: string;
  classId: number;
  clientId: string;
  companyName: string;
  contactNumber: string;
  country: string;
  countryOfBirth: string;
  createdBy: string;
  createdOn: Date;
  dateOfBirth: string;
  deletionIndicator: number;
  emailId: string;
  fax: string;
  homeNo: string;
  id: string;
  inquiryNumber: string;
  intakeFormId: number;
  intakeFormNumber: string;
  languageId: string;
  locationOfFile: string;
  matterDescription: string;
  matterNumber: string;
  nameOfEr: string;
  potentialClientId: string;
  referenceField1: string;
  referenceField10: string;
  referenceField11: string;
  referenceField12: string;
  referenceField13: string;
  referenceField14: string;
  referenceField15: string;
  referenceField16: string;
  referenceField17: string;
  referenceField18: string;
  referenceField19: string;
  referenceField2: string;
  referenceField20: string;
  referenceField3: string;
  referenceField4: string;
  referenceField5: string;
  referenceField6: string;
  referenceField7: string;
  referenceField8: string;
  referenceField9: string;
  state: string;
  statusId: number;
  title: string;
  transactionId: number;
  typeOfMatter: string;
  updatedBy: string;
  updatedOn: Date;
  workNo: string;
  zipCode: string;
  statusIddes: string;
  caseInformationID: string;
}
@Injectable({
  providedIn: 'root'
})
export class ImmigirationService {


  constructor(private http: HttpClient) { }

  apiName = '/mnr-management-service/';
  method = 'immCaseInfoSheet'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + '/' + code);
  }
  Create(obj: ImmigirationElement) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: ImmigirationElement, code: any) {
    return this.http.patch<any>(this.url + `/` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.url + '/' + code);
  }
  Search(obj: any) {
    return this.http.post<any>(this.url + '/search', obj);
  }
  CreateMatter(code: any) {
    return this.http.get<any>(this.url + '/' + code + '/matter');
  }

  GetClient(code: string) {
    return this.http.get<any>(this.apiName + 'clientgeneral/' + code);
  }

}
