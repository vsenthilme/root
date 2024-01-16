import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { NavItem } from 'src/app/common-service/menu.service';
import { AuthService } from 'src/app/core/core';

@Component({
  selector: 'app-menu-item',
  templateUrl: './menu-item.component.html',
  styleUrls: ['./menu-item.component.scss']
})
export class MenuItemComponent implements OnInit {
  @Input() items: NavItem[] = [];
  // @ViewChild('childMenu') public childMenu: any;
  @ViewChild('childMenu', { static: true }) public childMenu: any;
  @Output() menuname: EventEmitter<any> = new EventEmitter();
  constructor(private auth: AuthService) {
  }

  ngOnInit() {
    this.auth.isMenudata();


  }
  isdiabled(id: any) {
    this.auth.isMenudata();

    if (id == 6)
      return false;
    let fileterdata = this.auth.MenuData.filter((x: any) => x.subScreenId == id && (x.view || x.delete || x.createUpdate));

    if (fileterdata.length > 0) {
      return false;
    }


    return true;
  }

}