import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { Router } from '@angular/router';
import { Subscription, timer } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { MenuService } from 'src/app/common-service/menu.service';
import { AuthService } from 'src/app/core/core';
import { environment } from 'src/environments/environment';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  animations: [
    // Each unique animation requires its own trigger. The first argument of the trigger function is the name
    trigger('rotatedState', [
      state('default', style({ transform: 'rotate(0)' })),
      state('rotated', style({ transform: 'rotate(-180deg)' })),
      transition('rotated => default', animate('400ms ease-out')),
      transition('default => rotated', animate('400ms ease-in'))
    ])
  ]
})
export class HomeComponent implements OnInit {


  public icon = 'menu';
  showFloatingButtons: any;
  toggle = true;
  state: string = 'default';
  usertype: number;
  toggleFloat() {
    this.toggle = !this.toggle;
    this.state = (this.state === 'default' ? 'rotated' : 'default');
    if (this.icon === 'menu') {
      this.icon = 'arrow_back_ios_new';
    } else {
      this.icon = 'menu'
    }

  }




  // username = this.auth.userID;
  // usertype = this.auth.userTypeId;

  currentdate = new Date();
  // default list of  menu items
  menulist: any;

  sub = new Subscription();

  constructor(private router: Router, public dialog: MatDialog, private ms: MenuService, private auth: AuthService,
    ) { }

  showFiller = false;

  ngOnInit(): void {
    this.usertype = 2;
    // console.log(this.auth.userTypeId)
    // // sidenav.toggle();
    // // Using RxJS Timer
    // var username = this.auth.username

    // console.log(this.username)
    // let menu =
    //   JSON.parse("[" + localStorage.getItem('menu') + "]");
    // this.menulist = this.ms.getMeuList('save').filter(x => menu.includes(x.id));
    // console.log(menu);
    // this.sub = timer(0, 10000)
    //   .pipe(
    //     map(() => new Date()),
    //     share()
    //   )
    //   .subscribe(time => {
    //     this.currentdate = time;
    //   });
  }
  logout() {
    this.auth.logout();
 
  }

  // ngOnDestroy() {
  //   if (this.sub) {
  //     this.sub.unsubscribe();
  //   }
  // }
  // routeto(url: any, id: any) {
  //   localStorage.setItem('crrentmenu', id);
  //   console.log(url);
  //   if (!url) {
  //     let menu =
  //       JSON.parse("[" + localStorage.getItem('menu') + "]");
  //     url = this.ms.getMeuList('save').filter(x => x.id == Number(id))[0].children?.filter(x => menu.includes(x.id))[0].url;
  //   }
  //   this.router.navigate([url]);
  // }
  ///side pannel

  @ViewChild('sidenav') sidenav: MatSidenav | undefined;
  isExpanded = true;
  showSubmenu: boolean = false;
  isShowing = false;
  showSubSubMenu: boolean = false;






}
