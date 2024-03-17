import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/Auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class PricelistService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/wms-masters-service/';
 

  Getall() {
    return this.http.get<any>(this.apiName + `pricelist`);
  }
  Get(code: string,warehouseId:any,languageId:any,plantId:any,companyCodeId:any,moduleId:any,chargeRangeId:any,serviceTypeId:any) {
    return this.http.get<any>(this.apiName + `pricelist/` + code +'?warehouseId='+warehouseId+'&languageId='+languageId+'&plantId='+plantId+'&companyCodeId='+companyCodeId+'&moduleId='+moduleId+'&chargeRangeId='+chargeRangeId+'&serviceTypeId='+serviceTypeId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `pricelist`, obj);
  }
  Update(obj: any, code: any,warehouseId:any,languageId:any,plantId:any,companyCodeId:any,moduleId:any,chargeRangeId:any,serviceTypeId:any) {
    return this.http.patch<any>(this.apiName + `pricelist/` + code +'?warehouseId='+warehouseId+'&languageId='+languageId+'&plantId='+plantId+'&companyCodeId='+companyCodeId+'&moduleId='+moduleId+'&chargeRangeId='+chargeRangeId+'&serviceTypeId='+serviceTypeId, obj);
  }
  Delete(priceListId: any,warehouseId:any,languageId:any,plantId:any,companyCodeId:any,moduleId:any,chargeRangeId:any,serviceTypeId:any) {
    return this.http.delete<any>(this.apiName + `pricelist/` +  priceListId +'?warehouseId='+warehouseId+'&languageId='+languageId+'&plantId='+plantId+'&companyCodeId='+companyCodeId+'&moduleId='+moduleId+'&chargeRangeId='+chargeRangeId+'&serviceTypeId='+serviceTypeId );
  }
  search(obj: any) {
    return this.http.post<any>(this.apiName + 'pricelist/findPriceList', obj);
  }

 
}

