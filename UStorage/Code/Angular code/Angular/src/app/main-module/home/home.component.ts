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
  toggleFloat() {
    this.toggle = !this.toggle;
    this.state = (this.state === 'default' ? 'rotated' : 'default');
    if (this.icon === 'menu') {
      this.icon = 'arrow_back_ios_new';
    } else {
      this.icon = 'menu'
    }
  }
  validateToggle(){
    if(this.toggle == false){
      this.toggle =  true;
      this.isExpanded =  false;
      this.state = (this.state === 'default' ? 'rotated' : 'default');
      if (this.icon === 'menu') {
        this.icon = 'arrow_back_ios_new';
      } else {
        this.icon = 'menu'
      }
    }
    
  }


  username = this.auth.username;
  role = this.auth.role;
  currentdate = new Date();
  // default list of  menu items
  menulist: any;

  sub = new Subscription();

  constructor(private router: Router, public dialog: MatDialog, private ms: MenuService, private auth: AuthService) { }

  showFiller = false;

  ngOnInit(): void {
    console.log(this.auth.userTypeId)
    // sidenav.toggle();
    // Using RxJS Timer
    var username = this.auth.username

    //this.username = this.auth.username.toUpperCase();
    let menu =
      JSON.parse("[" + localStorage.getItem('menu') + "]");
    this.menulist = this.ms.getMeuList('save').filter(x => menu.includes(x.id));
    console.log(menu);
    this.sub = timer(0, 10000)
      .pipe(
        map(() => new Date()),
        share()
      )
      .subscribe(time => {
        this.currentdate = time;
      });
  }
  logout() {
    this.auth.logout();
    setTimeout(() => {
      window.location.reload();
  }, 200);
  }

  ngOnDestroy() {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }
  routeto(url: any, id: any) {
    localStorage.setItem('crrentmenu', id);
    console.log(url);
    if (!url) {
      let menu =
        JSON.parse("[" + localStorage.getItem('menu') + "]");
      url = this.ms.getMeuList('save').filter(x => x.id == Number(id))[0].children?.filter(x => menu.includes(x.id))[0].url;
    }
    this.router.navigate([url]);
  }
  ///side pannel

  @ViewChild('sidenav') sidenav: MatSidenav | undefined;
  isExpanded = false;
  showSubmenu: boolean = false;
  isShowing = false;
  showSubSubMenu: boolean = false;

  routeto1() {
    localStorage.setItem('crrentmenu', '1001');
    this.router.navigate(["/main/setup/User-management"]);
  }

  routeto2() {
    localStorage.setItem('crrentmenu', '1002');
    this.router.navigate(["/main/setup/nationality"]);
  }

  routetostorage() {
    localStorage.setItem('crrentmenu', '2210');
    this.router.navigate(["/main/materialmasters/storageunit"]);
  }

  routetocrm(){
    localStorage.setItem('crrentmenu', '1011');
    this.router.navigate(["/main/crm/enquiry"]);
  }

  routetooperation(){
    localStorage.setItem('crrentmenu', '2301');
    this.router.navigate(["/main/operation/agreement"]);
  }
}
