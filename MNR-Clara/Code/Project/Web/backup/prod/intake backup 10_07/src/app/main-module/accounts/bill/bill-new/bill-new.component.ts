import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-bill-new',
  templateUrl: './bill-new.component.html',
  styleUrls: ['./bill-new.component.scss']
})
export class BillNewComponent implements OnInit {

  constructor(private router: Router) { }

  sub = new Subscription();
  form: any;
  ngOnInit(): void {
  }

  run() {

  }



  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
}
