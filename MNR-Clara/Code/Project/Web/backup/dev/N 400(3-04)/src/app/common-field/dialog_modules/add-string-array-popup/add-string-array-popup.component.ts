import { Component, OnInit, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";

@Component({
  selector: 'app-add-string-array-popup',
  templateUrl: './add-string-array-popup.component.html',
  styleUrls: ['./add-string-array-popup.component.scss']
})
export class AddStringArrayPopupComponent implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<AddStringArrayPopupComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

    displayedColumns: string[] = ['sino', 'email'];;
    dataSource = new MatTableDataSource<any>([]);

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<any>(this.data);
  }

  onNoClick(): void {
    this.dialogRef.close(this.dataSource.data);
  }

  addEmail() {
    this.dataSource.data.push({email:''});
    this.dataSource._updateChangeSubscription();
  }
  removeEmail(index:any,element:any) {
    console.log(element)
    this.dataSource.data.splice(0, -1);
  }

}
