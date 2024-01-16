import { Component, OnInit, HostListener } from "@angular/core";


@Component({
  selector: 'app-scroll-button',
  templateUrl: './scroll-button.component.html',
  styleUrls: ['./scroll-button.component.scss']
})
export class ScrollButtonComponent implements OnInit {
  ngOnInit(): void {
  }

  public lastScrolledHeight: number = 0;
  public showAddButton: boolean = false;

  @HostListener('window:scroll', ['$event']) onScroll(event: { path: any[]; }) {
    const window = event.path[1];
    const currentScrollHeight = window.scrollY;


    if (currentScrollHeight > this.lastScrolledHeight) {
      this.showAddButton = true;

    } else {
      this.showAddButton = false;

    }
    this.lastScrolledHeight = currentScrollHeight;
  }

  scrollToTop(el: { scrollTop: number; }) {
    el.scrollTop = 0;
  }
}


