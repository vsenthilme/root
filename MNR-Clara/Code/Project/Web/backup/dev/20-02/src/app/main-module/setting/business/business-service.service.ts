import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BusinessServiceService {

  constructor(private http: HttpClient,) { }

  apiName = '/mnr-setup-service/';
  getallactivityCode() {
    return this.http.get<any>(this.apiName + `activityCode`);
  }
  getactivityCode(code: string) {
    return this.http.get<any>(this.apiName + `activityCode/` + code);
  }

}
