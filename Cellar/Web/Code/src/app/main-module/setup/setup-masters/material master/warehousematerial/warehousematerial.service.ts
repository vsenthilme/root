




  import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

export interface warehouseElement{
  code: string;
  createdBy: string;
  createdOn: Date;
  deletionIndicator: number;
  description: string;
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
  updatedBy: string;
  updatedOn: Date;
}
@Injectable({
  providedIn: 'root'
})
export class WarehousematerialService {

  constructor(private http: HttpClient, private auth: AuthService) { }
  
  apiName = '/us-master-service/';
  methodName = 'crm/';
  url = this.apiName + this.methodName;
  Getall() {
    return this.http.get<any>(this.apiName + `warehouse`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `warehouse/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `warehouse`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.apiName + `warehouse/` + code, obj);
  }
  Delete(warehouseId: any,) {
    return this.http.delete<any>(this.apiName + `warehouse/` + warehouseId  );
  }
 
}

