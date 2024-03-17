import { Component, OnInit, ViewChild } from '@angular/core';
import { SettingsService } from '../settings.service';
import { MatDialog } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { UserprofileNewComponent } from '../../admin/user-profile/userprofile-new/userprofile-new.component';
import { UserProfileNewComponent } from './user-profile-new/user-profile-new.component';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  @ViewChild('userProfile') userProfile: Table | undefined;
  userProfileTable: any[] = [];
  selecteduserProfile : any[] = [];
  advanceFilterShow: boolean;

  constructor(public dialog: MatDialog, private service: SettingsService,  private spin: NgxSpinnerService, private cs: CommonService,
    private messageService: MessageService,) { 
    
  }
  sub = new Subscription();
  ngOnInit(): void {
    this.getAll();
  }


  getAll(){
    this.spin.show();
    this.sub.add(this.service.getAll().subscribe((res: any[]) => {
      this.userProfileTable = res;
      this.selecteduserProfile = [];
      this.spin.hide();
    }, err => {
      this.spin.hide();      
this.cs.commonerror(err);
    }));

  }

  



  advanceFilter(){
    this.advanceFilterShow = !this.advanceFilterShow;
  }
  

  onChange() {
    const choosen= this.selecteduserProfile[this.selecteduserProfile.length - 1];   
    this.selecteduserProfile.length = 0;
    this.selecteduserProfile.push(choosen);
  } 


  applyFilterGlobal($event: any, stringVal: any) {
    this.userProfile!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }

  
  downloadExcel() {
    var res: any = [];
    this.userProfileTable.forEach(x => {
      res.push({
        "Language":x.languageId,
        "Company Code ":x.companyCode,
        "First Name": x.firstName,
        "User Name ":x.userName,
        "Created On": this.cs.dateapi(x.createdOn),
        "Crreated By":x.createdBy,
      });
  
    })
    this.cs.exportAsExcel(res, "User Profile");
  }


  docurl: any;
  fileUrldownload: any;



  openDialog(data: any = 'new'): void {
    if (data != 'New')
      if (this.selecteduserProfile.length === 0) {
      this.messageService.add({key: 'br', severity:'info', summary:'Warning', detail:  "Kindly Select any row"});
      return;
    }
    const dialogRef = this.dialog.open(UserProfileNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, code: data != 'New' ? this.selecteduserProfile[0].userId : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      //this.getAllListData();
      window.location.reload();
    });
  }


  deleteDialog() {
    if (this.selecteduserProfile.length === 0) {
      this.messageService.add({key: 'br',
      severity:'warning', 
      summary: 'Warning', detail: 'Kindly select any row", "Notification'});
      return;
    }
    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '80%',
      position: { top: '9%', },
      data: this.selecteduserProfile[0].userId,
    });
  
    dialogRef.afterClosed().subscribe(result => {
  
      if (result) {
        this.deleterecord(this.selecteduserProfile[0].userId);
  
      }
    });
  }
  
  
  deleterecord(id: any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id).subscribe((res) => {
      this.messageService.add({key: 'br',
      severity:'success', 
      summary: 'Success', detail: 'Deleted successfully'});

      this.spin.hide();
      this.getAll();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  
}

