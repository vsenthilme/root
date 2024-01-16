import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private http: HttpClient) { }

  apiName = '/mnr-setup-service/';
  method = 'clientUser'
  url = this.apiName + this.method;

  Getall() {
    return this.http.get<any>(this.url);
  }
  
  Get(code: string) {
    return this.http.get<any>(this.url + '/' + code);
  }

  findClientUser(obj: any) {
    return this.http.post<any>('/mnr-setup-service/clientUser/findClientUserNew', obj);
  }
 
}
