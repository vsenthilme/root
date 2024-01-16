import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

export interface LAndECaseElement {
  adressLine1: string;
  adressLine2: string;
  advParty1CellPhone: string;
  advParty1City: string;
  advParty1CompanyName: string;
  advParty1DirectTelephone: string;
  advParty1Email: string;
  advParty1FaxNumber: string;
  advParty1Name: string;
  advParty1OfficeTelephone: string;
  advParty1PostalZipCode: string;
  advParty1State: string;
  advParty1StreetAddress: string;
  advParty2CellPhone: string;
  advParty2City: string;
  advParty2CompanyName: string;
  advParty2DirectTelephone: string;
  advParty2Email: string;
  advParty2FaxNumber: string;
  advParty2Name: string;
  advParty2OfficeTelephone: string;
  advParty2PostalZipCode: string;
  advParty2State: string;
  advParty2StreetAddress: string;
  agencyName: string;
  agentEmail: string;
  agentFaxNumber: string;
  agentName: string;
  agentOfficeTelephone: string;
  agentPostalZipCode: string;
  agentState: string;
  agrentCity: string;
  agrentStreetAddress: string;
  assistantClerkEmail: string;
  assistantClerkName: string;
  assistantClerkTelephone: string;
  baliffCourtReporterEmail: string;
  baliffCourtReporterName: string;
  baliffCourtReporterTelephone: string;
  caseCategoryId: number;
  caseSubCategoryId: number;
  causeNo: string;
  city: string;
  classId: number;
  clerkEmail: string;
  clerkName: string;
  clerkTelephone: string;
  clientId: string;
  companyName: string;
  contactNumber: string;
  country: string;
  courtCity: string;
  courtNo: string;
  courtState: string;
  createdBy: string;
  createdOn: Date;
  defendants: string;
  deletionIndicator: number;
  directTelephone: string;
  email: string;
  emailId: string;
  fax: string;
  faxNumber: string;
  firstNameLastName: string;
  homeNo: string;
  id: string;
  inquiryNumber: string;
  intakeFormId: number;
  intakeFormNumber: string;
  judgeName: string;
  languageId: string;
  locationOfFile: string;
  matterDescription: string;
  matterNumber: string;
  notes: string;
  notesDate: Date;
  officeTelephone: string;
  plaintiffs: string;
  postalZipCode: string;
  potentialClientId: string;
  reference: string;
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
  schedule: string;
  scheduleDate: Date;
  sectionCoorinatorEmail: string;
  sectionCoorinatorName: string;
  sectionCoorinatorTelephone: string;
  state: string;
  statusId: number;
  streetAddress: string;
  taskDate: Date;
  tasksToDo: string;
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
export class LAndECaseService {


  constructor(private http: HttpClient) { }

  apiName = '/mnr-management-service/';
  method = 'leCaseInfoSheet'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + '/' + code);
  }
  Create(obj: LAndECaseElement) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: LAndECaseElement, code: any) {
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

  GetClientdetails() {
    return this.http.get<any>('/mnr-management-service/clientgeneral/');
  }

}
