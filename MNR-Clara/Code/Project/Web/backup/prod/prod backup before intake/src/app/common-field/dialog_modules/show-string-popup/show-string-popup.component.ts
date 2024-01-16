import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-show-string-popup',
  templateUrl: './show-string-popup.component.html',
  styleUrls: ['./show-string-popup.component.scss']
})
export class ShowStringPopupComponent implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<any>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService
  ) { }

  private fakeElem: HTMLTextAreaElement | null;

  ngOnInit(): void {
    if (this.data.type == 'CLIENT_DOCUMENT') {
      if (this.data.link) {
        this.data.link = this.data.link.replaceAll('/', '\\');
      }
    }
  }

  submit() {
    this.dialogRef.close();
  }

  copyText() {
    try {
      this.createFake(this.data.link);
      this.toastr.info("Location Copied", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return document.execCommand('copy');
    } catch (err) {
      return false;
    } finally {
      this.removeFake();
    }
  }

  createFake(text: string) {
    const docElem = document.documentElement!;
    const isRTL = docElem.getAttribute('dir') === 'rtl';

    // Create a fake element to hold the contents to copy
    this.fakeElem = document.createElement('textarea');

    // Prevent zooming on iOS
    this.fakeElem.style.fontSize = '12pt';

    // Reset box model
    this.fakeElem.style.border = '0';
    this.fakeElem.style.padding = '0';
    this.fakeElem.style.margin = '0';

    // Move element out of screen horizontally
    this.fakeElem.style.position = 'absolute';
    this.fakeElem.style[isRTL ? 'right' : 'left'] = '-9999px';

    // Move element to the same position vertically
    const yPosition = window.pageYOffset || docElem.scrollTop;
    this.fakeElem.style.top = yPosition + 'px';

    this.fakeElem.setAttribute('readonly', '');
    this.fakeElem.value = text;

    document.body.appendChild(this.fakeElem);

    this.fakeElem.select();
    this.fakeElem.setSelectionRange(0, this.fakeElem.value.length);
  }

  removeFake() {
    if (this.fakeElem) {
      document.body.removeChild(this.fakeElem);
      this.fakeElem = null;
    }
  }

  saveDescription() {
    this.dialogRef.close(this.data.link);
  }
}
