import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';


export interface customerElement {
  address: string;
  authorizedCivilID: string;
  authorizedPerson: string;
  balanceToBePaid: string;
  billedAmount: string;
  billedAmountTillDate: string;
  civilId: string;
  createdBy: string;
  createdOn: Date;
  currency: string;
  customerCode: string;
  customerGroup: string;
  customerName: string;
  deletionIndicator: number;
  email: string;
  faxNumber: string;
  isActive: boolean;
  mobileNumber: string;
  nationality: string;
  paidAmountTillDate: string;
  paidDate: Date;
  phoneNumber: string;
  preferedPaymentMode: string;
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
  seviceRendered: string;
  status: string;
  type: string;
  updatedBy: string;
  updatedOn: Date;
  voucherNumber: string;
}


@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  constructor(private http: HttpClient, private auth: AuthService,  private httpClient: HttpClient,) { }


  apiName = '/us-trans-service/master/';
  methodName = 'crm/';
  url = this.apiName + this.methodName;
  Getall() {
    return this.http.get<any>(this.apiName + `leadcustomer`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `leadcustomer/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `leadcustomer`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.apiName + `leadcustomer/` + code, obj);
  }
  Delete(leadCustomerId: any,) {
    return this.http.delete<any>(this.apiName + `leadcustomer/` + leadCustomerId);
  }
  search(obj: any) {
    return this.http.post<any>(this.apiName + 'leadcustomer/find', obj);
  }

  getDropdown() {
    return this.http.get<any>(`/us-trans-service/reports/dropdown/customerName`);
  }

  customerDetails(obj: any) {
    return this.http.post<any>(`/us-trans-service/reports/customerDetail`, obj);
  }

  public uploadfile(file: File, location) {
    let formParams = new FormData();
    formParams.append('file', file)
    return this.httpClient.post('/us-master-service/doc-storage/upload?' + `location=${location}`, formParams)
  }

  getfile1(code: any, url: any,): Promise<File> {
    return this.http
      .get<any>('/us-master-service/doc-storage/download?fileName=' + code + '&location=' + url, {
        responseType: 'blob' as 'json',
      })
      .toPromise();
  }

  searchDocumentStorage(obj: any) {
    return this.http.post<any>(`/us-master-service/documentstorage/find`, obj);
  }
  documentStorage(obj: any) {
    return this.http.post<any>(`/us-master-service/documentstorage`, obj);
  }
  DeleteDocumentStorage(documentNumber: any,) {
    return this.http.delete<any>(`/us-master-service/documentstorage/` + documentNumber);
  }
}
