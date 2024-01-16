import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';



  export interface BusinesspartnerElement {
      address1: string;
      address2: string;
      businessPartnerType: number;
      companyCodeId: string;
      country: string;
      createdby: string;
      createdon: Date;
      deletionIndicator: number;
      emailId: string;
      faxNumber: string;
      languageId: string;
      lattitude: number;
      location: string;
      longitude: number;
      partnerCode: string;
      partnerName: string;
      phoneNumber: string;
      plantId: string;
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
      referenceText: string;
      state: string;
      statusId: number;
      storageBin: string;
      storageTypeId: number;
      updatedby: string;
      updatedon: Date;
      warehouseId: string;
      zone: string;
  }





@Injectable({
  providedIn: 'root'
})
export class BusinessPartnerService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/wms-masters-service/';
  methodName = 'businesspartner';
  url = this.apiName + this.methodName;

  Getall() {
    return this.http.get<any>(this.apiName + `businesspartner`);
  }
  Get(code: string,companyCodeId:any,languageId:any,plantId:any,warehouseId:any,businessPartnerType:any) {
    return this.http.get<any>(this.apiName + `businesspartner/` + code+'?companyCodeId='+companyCodeId+'&languageId='+languageId+'&plantId='+plantId+'&warehouseId='+warehouseId+'&businessPartnerType='+businessPartnerType);
  }
  Create(obj: BusinesspartnerElement) {
    return this.http.post<any>(this.apiName + `businesspartner`, obj);
  }
  Update(obj: BusinesspartnerElement, code: any,companyCodeId:any,languageId:any,plantId:any,warehouseId:any,businessPartnerType:any) {
    return this.http.patch<any>(this.apiName + `businesspartner/` + code+'?companyCodeId='+companyCodeId+'&languageId='+languageId+'&plantId='+plantId+'&warehouseId='+warehouseId+'&businessPartnerType='+businessPartnerType, obj);
  }
  Delete(partnerCode: any,companyCodeId:any,languageId:any,plantId:any,warehouseId:any,businessPartnerType:any) {
    return this.http.delete<any>(this.apiName + `businesspartner/` + '?partnerCode=' + partnerCode +'&companyCodeId='+companyCodeId+'&languageId='+languageId+'&plantId='+plantId+'&warehouseId='+warehouseId+'&businessPartnerType='+businessPartnerType );
  }
  search(obj: any) {
    return this.http.post<any>('/wms-masters-service/businesspartner/findBusinessPartner', obj);
  }
 
}


