import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsertypeIdComponent } from './usertype-id.component';

describe('UsertypeIdComponent', () => {
  let component: UsertypeIdComponent;
  let fixture: ComponentFixture<UsertypeIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UsertypeIdComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UsertypeIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
