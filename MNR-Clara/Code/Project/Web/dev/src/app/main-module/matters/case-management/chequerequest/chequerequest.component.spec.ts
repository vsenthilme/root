import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChequerequestComponent } from './chequerequest.component';

describe('ChequerequestComponent', () => {
  let component: ChequerequestComponent;
  let fixture: ComponentFixture<ChequerequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChequerequestComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChequerequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
