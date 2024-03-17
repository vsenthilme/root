import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class CbminboundService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/wms-masters-service/';
 

  Getall() {
    return this.http.get<any>(this.apiName + `cbminbound`);
  }
  Get(code: string,warehouseId:any,languageId:any,plantId:any,companyCodeId:any) {
    return this.http.get<any>(this.apiName + `cbminbound/` + code +'?warehouseId='+warehouseId+'&languageId='+languageId+'&plantId='+plantId+'&companyCodeId='+companyCodeId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `cbminbound`, obj);
  }
  Update(obj: any, code: any,warehouseId:any,languageId:any,plantId:any,companyCodeId:any) {
    return this.http.patch<any>(this.apiName + `cbminbound/` + code +'?warehouseId='+warehouseId+'&languageId='+languageId+'&plantId='+plantId+'&companyCodeId='+companyCodeId, obj);
  }
  Delete(itemCode: any,warehouseId:any,languageId:any,plantId:any,companyCodeId:any) {
    return this.http.delete<any>(this.apiName + `cbminbound/` +  itemCode +'?warehouseId='+warehouseId+'&languageId='+languageId+'&plantId='+plantId+'&companyCodeId='+companyCodeId);
  }
  search(obj: any) {
    return this.http.post<any>(this.apiName + 'cbminbound/findCbmInbound', obj);
  }

 
}


