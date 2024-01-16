import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

export interface numberElement {
  createdBy: string;
  createdOn: Date;
  deletionIndicator: number;
  description: string;
  documentCode: string;
  documentName: string;
  numberRangeCode: number;
  numberRangeCurrent: string;
  numberRangeFrom: number;
  numberRangeTo: number;
  updatedBy: string;
  updatedOn: Date;
}

@Injectable({
  providedIn: 'root'
})
export class NumeringseriesService {

  constructor(private http: HttpClient, private auth: AuthService) { }
  
  apiName = '/us-master-service/';
  methodName = 'crm/';
  url = this.apiName + this.methodName;
  Getall() {
    return this.http.get<any>(this.apiName + `numberRange`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `numberRange/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `numberRange`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.apiName + `numberRange?numberRangeCode=` + code, obj);
  }
  Delete(numberRangeCode: any,) {
    return this.http.delete<any>(this.apiName + `numberRange/` + numberRangeCode  );
  }
 
}

