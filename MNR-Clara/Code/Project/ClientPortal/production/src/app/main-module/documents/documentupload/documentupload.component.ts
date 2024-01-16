import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit } from "@angular/core";
import { MatTableDataSource } from "@angular/material/table";

export interface ordermanagement {
  lineno: string;
  partner: string;
  date:string;
  }
  
  const ELEMENT_DATA: ordermanagement[] = [
  { lineno: '10029-01', partner: '04-02-2022',date: 'signature.pdf',},
  { lineno: '10029-02', partner: '04-02-2022',date: 'passport.jpeg',},
  { lineno: '10029-03', partner: '04-02-2022',date: 'visadoc.pdf',},
  
  ];
@Component({
  selector: 'app-documentupload',
  templateUrl: './documentupload.component.html',
  styleUrls: ['./documentupload.component.scss']
})
export class DocumentuploadComponent implements OnInit {

  
  ngOnInit(): void {
  }



  displayedColumns: string[] = ['lineno','partner','date','no'];
  dataSource = new MatTableDataSource< ordermanagement>(ELEMENT_DATA);
  selection = new SelectionModel< ordermanagement>(true, []);

 
}

