

  import { HttpClient } from '@angular/common/http';
  import { Injectable } from '@angular/core';
  import { AuthService } from 'src/app/core/core';
  
  
  export interface flrentElement {
    createdBy: string;
    createdOn: Date;
    deletionIndicator: number;
    description: string;
    itemCode: string;
    itemGroup: string;
    itemType: string;
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
    saleUnitOfMeasure: string;
    status: string;
    unitPrice: string;
    updatedBy: string;
    updatedOn: Date;
  }
  
  @Injectable({
    providedIn: 'root'
  })
  export class FlRentService {
  
    constructor(private http: HttpClient, private auth: AuthService) { }
  
  
    apiName = '/us-trans-service/master/';
    Getall() {
      return this.http.get<any>(this.apiName + `flrent`);
    }
    Get(code: string) {
      return this.http.get<any>(this.apiName + `flrent/` + code);
    }
    Create(obj: any) {
      return this.http.post<any>(this.apiName + `flrent`, obj);
    }
    Update(obj: any, code: any) {
      return this.http.patch<any>(this.apiName + `flrent/` + code, obj);
    }
    Delete(trip: any,) {
      return this.http.delete<any>(this.apiName + `flrent/` + trip);
    }
    search(obj: any) {
      return this.http.post<any>(this.apiName + 'flrent/find', obj);
    }
  }
  