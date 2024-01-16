import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NationalityNewComponent } from './nationality-new.component';

describe('NationalityNewComponent', () => {
  let component: NationalityNewComponent;
  let fixture: ComponentFixture<NationalityNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NationalityNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NationalityNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
