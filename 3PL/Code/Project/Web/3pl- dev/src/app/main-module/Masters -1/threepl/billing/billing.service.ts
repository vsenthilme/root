import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class BillingService {

 
  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/wms-masters-service/';
 

  Getall() {
    return this.http.get<any>(this.apiName + `billing`);
  }
  Get(code: string,warehouseId:any,languageId:any,plantId:any,companyCodeId:any,moduleId:any) {
    return this.http.get<any>(this.apiName + `billing/` + code +'?warehouseId='+warehouseId+'&languageId='+languageId+'&plantId='+plantId+'&companyCodeId='+companyCodeId+'&moduleId='+moduleId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `billing`, obj);
  }
  Update(obj: any, code: any,warehouseId:any,languageId:any,plantId:any,companyCodeId:any,moduleId:any) {
    return this.http.patch<any>(this.apiName + `billing/` + code +'?warehouseId='+warehouseId+'&languageId='+languageId+'&plantId='+plantId+'&companyCodeId='+companyCodeId+'&moduleId='+moduleId, obj);
  }
  Delete(storageBin: any,warehouseId:any,languageId:any,plantId:any,companyCodeId:any,moduleId:any) {
    return this.http.delete<any>(this.apiName + `billing/` +  storageBin +'?warehouseId='+warehouseId+'&languageId='+languageId+'&plantId='+plantId+'&companyCodeId='+companyCodeId+'&moduleId='+moduleId );
  }
  search(obj: any) {
    return this.http.post<any>(this.apiName + 'billing/findBilling', obj);
  }

 
}

