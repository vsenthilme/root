import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

export interface ClientGeneralElement {
  addressLine1: string;
  addressLine2: string;
  addressLine3: string;
  alternateEmailId: string;
  city: string;
  classId: number;
  leadership:string;//04/10/23
  hrPartners:string;//04/10/23
  position:string;//04/10/23
  dateOfHire:string;//04/10/23
  clientCategoryId: number;
  clientId: string;
  consultationDate: string;
  contactNumber: string;
  corporationClientId: string;
  country: string;
  createdBy: string;
  createdOn: Date;
  createdOnString: string;
  deletionIndicator: number;
  emailId: string;
  fax: string;
  firstName: string;
  firstNameLastName: string;
  homeNo: string;
  inquiryNumber: string;
  intakeFormId: number;
  intakeFormNumber: string;
  isMailingAddressSame: boolean;
  languageId: string;
  lastName: string;
  lastNameFirstName: string;
  mailingAddress: string;
  occupation: string;
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
  suiteDoorNo: string;
  transactionId: number;
  updatedBy: string;
  updatedOn: Date;
  workNo: string;
  zipCode: string;
}
@Injectable({
  providedIn: 'root'
})
export class ClientGeneralService {


  constructor(private http: HttpClient) { }

  apiName = '/mnr-management-service/';
  method = 'clientgeneral'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + '/' + code);
  }
  Create(obj: ClientGeneralElement) {
    return this.http.post<any>(this.url + '?isFromPotentialEndpoint=false', obj);
  }
  Update(obj: ClientGeneralElement, code: any) {
    return this.http.patch<any>(this.url + `/` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.url + '/' + code);
  }

  Search(obj: any) {
    return this.http.post<any>(this.url + '/findClientGeneral', obj);
  }

  GetClientdetails() {
    return this.http.get<any>('/mnr-management-service/clientgeneral/');
  }

  getAllByPagination(pageNo, pageSize, classObj) {
    return this.http.get<any>(this.url + `/pagination?pageNo=${pageNo}&pageSize=${pageSize}&classId=${classObj}&sortBy=${'createdOn'}`);
  }

  getAllSearchDropDown() {
    return this.http.get<any>(this.url + `/dropdown/client`);
  }


  SearchNew(obj: any) {
    return this.http.post<any>('/mnr-management-service/clientgeneral/findClientGeneralNew', obj);
  }
}
