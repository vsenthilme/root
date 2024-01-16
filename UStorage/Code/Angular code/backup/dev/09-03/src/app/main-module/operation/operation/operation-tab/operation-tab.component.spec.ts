import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OperationTabComponent } from './operation-tab.component';

describe('OperationTabComponent', () => {
  let component: OperationTabComponent;
  let fixture: ComponentFixture<OperationTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OperationTabComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OperationTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
