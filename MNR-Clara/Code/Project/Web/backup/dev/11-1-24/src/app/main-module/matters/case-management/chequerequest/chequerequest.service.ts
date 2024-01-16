import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ChequerequestService {

  constructor(private http: HttpClient) {
    
   }
   Get(clientId:any) {
    return this.http.get<any>('/mnr-management-service/clientgeneral/'+clientId);
  }
  Update(matterExpenseId:any) {
    return this.http.get<any>('/mnr-management-service/matterexpense/'+matterExpenseId);
  }
}
