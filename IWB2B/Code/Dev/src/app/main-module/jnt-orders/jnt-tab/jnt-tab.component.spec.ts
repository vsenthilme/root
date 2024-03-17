import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JntTabComponent } from './jnt-tab.component';

describe('JntTabComponent', () => {
  let component: JntTabComponent;
  let fixture: ComponentFixture<JntTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JntTabComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(JntTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
