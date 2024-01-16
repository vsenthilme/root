import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

export interface ClientNotesElement {
  classId: number;
  clientId: string;
  createdBy: string;
  createdOn: Date;
  deletionIndicator: number;
  languageId: string;
  matterNumber: string;
  noteText: string;
  noteTypeId: number;
  notesNumber: string;
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
  statusId: string;
  updatedBy: string;
  updatedOn: Date;
}
@Injectable({
  providedIn: 'root'
})
export class ClientNotesService {


  constructor(private http: HttpClient) { }

  apiName = '/mnr-management-service/';
  method = 'clientnote'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + '/' + code);
  }
  Create(obj: ClientNotesElement) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: ClientNotesElement, code: any) {
    return this.http.patch<any>(this.url + `/` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.url + '/' + code);
  }
  Search(obj: any) {
    return this.http.post<any>(this.url + '/findClientNotes', obj);
  }

}
