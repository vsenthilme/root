import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class ConsignmentHistoryService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  Getall() {
    return this.http.get<any>('/mv-master-service/orderdetailsheader');
  }
  Get(code: string) {
    return this.http.get<any>('/mv-master-service/orderdetailsheader/' + code);
  }
  Create(obj: any) {
    return this.http.post<any>('/mv-master-service/orderdetailsheader', obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>('/mv-master-service/orderdetailsheader/' + code, obj);
  }
  Delete(orderId: any) {
    return this.http.delete<any>('/mv-master-service/orderdetailsheader/' + orderId);
}

}


