import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NumberrangecodeService {
  constructor(private http: HttpClient) { }

  apiName = '/mnr-cg-setup-service/';

  Getall() {
    return this.http.get<any>(this.apiName + `NumberRange`);
  }
  Get(code: string,languageId:any,companyId:any,numberRangeObject:any) {
    return this.http.get<any>(this.apiName + `NumberRange/` + code+'?languageId='+languageId+'&companyId='+companyId+'&numberRangeObject='+numberRangeObject);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `NumberRange`, obj);
  }
  Update(obj: any, code: any,languageId:any,companyId:any,numberRangeObject:any) {
    return this.http.patch<any>(this.apiName + `NumberRange/`  + code +'?languageId='+languageId+'&companyId='+companyId+'&numberRangeObject='+numberRangeObject, obj);
  }
  Delete(code: any,languageId:any,companyId:any,numberRangeObject:any) {
    return this.http.delete<any>(this.apiName + `NumberRange/` + code+'?languageId='+languageId+'&companyId='+companyId+'&numberRangeObject='+numberRangeObject);
  }
  search(obj:any){
    return this.http.post<any>('/mnr-cg-setup-service/numberRange/findNumberRange', obj);
  }
  searchlanguage(obj:any){
    return this.http.post<any>('/mnr-cg-setup-service/languageid/findlanguageid', obj);
  }
}

