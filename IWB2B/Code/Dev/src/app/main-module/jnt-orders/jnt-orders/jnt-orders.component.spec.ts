import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JntOrdersComponent } from './jnt-orders.component';

describe('JntOrdersComponent', () => {
  let component: JntOrdersComponent;
  let fixture: ComponentFixture<JntOrdersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JntOrdersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(JntOrdersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
