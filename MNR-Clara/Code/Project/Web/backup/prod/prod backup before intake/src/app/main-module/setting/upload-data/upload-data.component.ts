import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Component({
  selector: 'app-upload-data',
  templateUrl: './upload-data.component.html',
  styleUrls: ['./upload-data.component.scss']
})
export class UploadDataComponent implements OnInit {
  screenid = 1127;

  public icon = 'expand_more';

  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  toggleFloat() {

    this.isShowDiv = !this.isShowDiv;
    this.toggle = !this.toggle;

    if (this.icon === 'expand_more') {
      this.icon = 'chevron_left';
    } else {
      this.icon = 'expand_more'
    }
    this.showFloatingButtons = !this.showFloatingButtons;

  }
  constructor(private auth: AuthService) { }

  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
  }

}
