import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiService } from 'src/app/config/api.service';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

 
  constructor(private http: HttpClient, private auth: AuthService) { }
  
  apiName = '/us-trans-service/';
  methodName = 'reports/dashboard/';
  url = this.apiName + this.methodName;
  billedPaid(year: any) {
    return this.http.get<any>(this.url + `billedAndPaidAmount/` + year);
  }
  leadCustomer(year: any) {
    return this.http.get<any>(this.url + `leadAndCustomer/` + year);
  }
}
