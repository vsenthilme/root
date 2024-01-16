import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class BarcodesubtypeidService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  Getall() {
    return this.http.get<any>('/wms-idmaster-service/barcodesubtypeid');
  }
  Get(code: string,barcodeTypeId:any,warehouseId: string,languageId:any,companyCodeId:any,plantId:any) {
    return this.http.get<any>('/wms-idmaster-service/barcodesubtypeid/' + code +'?barcodeTypeId='+barcodeTypeId +'&warehouseId=' + warehouseId+'&languageId='+languageId+'&companyCodeId='+companyCodeId+'&plantId='+plantId);
  }
  Create(obj: any) {
    return this.http.post<any>('/wms-idmaster-service/barcodesubtypeid', obj);
  }
  Update(obj: any, code: any,barcodeTypeId:any,warehouseId: string,languageId:any,companyCodeId:any,plantId:any) {
    return this.http.patch<any>('/wms-idmaster-service/barcodesubtypeid/' + code+'?barcodeTypeId='+barcodeTypeId +'&warehouseId=' + warehouseId+'&languageId='+languageId+'&companyCodeId='+companyCodeId+'&plantId='+plantId, obj);
  }
  Delete(barcodeSubTypeId: any,barcodeTypeId:any,warehouseId: string,languageId:any,companyCodeId:any,plantId:any) {
    return this.http.delete<any>('/wms-idmaster-service/barcodesubtypeid/' + barcodeSubTypeId+'?barcodeTypeId='+barcodeTypeId +'&warehouseId=' + warehouseId+'&languageId='+languageId+'&companyCodeId='+companyCodeId+'&plantId='+plantId);
}
search(obj: any) {
  return this.http.post<any>('/wms-idmaster-service/barcodesubtypeid/findBarcodeSubTypeId', obj);
}
}
