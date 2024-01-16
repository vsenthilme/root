import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';


export interface NoteTypeElement {
  noteTypeText: string;
  noteTypeStatus: string;
  noteTypeId: number;
  classId: number;
  clientId: number;
  createdBy: string;
  createdOn: Date;
  deletionIndicator: number;
  languageId: string;
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
export class NoteTypeService {


  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `noteType`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `noteType/` + code);
  }
  Create(obj: NoteTypeElement) {
    return this.http.post<any>(this.apiName + `noteType`, obj);
  }
  Update(obj: NoteTypeElement, code: any) {
    return this.http.patch<any>(this.apiName + `noteType` + `?noteTypeId=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `noteType/` + code);
  }
}
