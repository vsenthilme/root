import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class PricelistassignmentService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/wms-masters-service/';
 

  Getall() {
    return this.http.get<any>(this.apiName + `pricelistassignment`);
  }
  Get(code: string,warehouseId:any,languageId:any,plantId:any,companyCodeId:any,partnerCode:any,) {
    return this.http.get<any>(this.apiName + `pricelistassignment/` + code +'?warehouseId='+warehouseId+'&languageId='+languageId+'&plantId='+plantId+'&companyCodeId='+companyCodeId+'&partnerCode='+partnerCode);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `pricelistassignment`, obj);
  }
  Update(obj: any, code: any,warehouseId:any,languageId:any,plantId:any,companyCodeId:any,partnerCode:any) {
    return this.http.patch<any>(this.apiName + `pricelistassignment/` + code +'?warehouseId='+warehouseId+'&languageId='+languageId+'&plantId='+plantId+'&companyCodeId='+companyCodeId+'&partnerCode='+partnerCode, obj);
  }
  Delete(priceListId: any,warehouseId:any,languageId:any,plantId:any,companyCodeId:any,partnerCode:any) {
    return this.http.delete<any>(this.apiName + `pricelistassignment/` +  priceListId +'?warehouseId='+warehouseId+'&languageId='+languageId+'&plantId='+plantId+'&companyCodeId='+companyCodeId+'&partnerCode='+partnerCode );
  }
  search(obj: any) {
    return this.http.post<any>(this.apiName + 'pricelistassignment/findPriceListAssignment', obj);
  }

 
}


