import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TranserrorComponent } from './transerror.component';

describe('TranserrorComponent', () => {
  let component: TranserrorComponent;
  let fixture: ComponentFixture<TranserrorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TranserrorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TranserrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
