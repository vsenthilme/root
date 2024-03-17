import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ThreeplTransferComponent } from './threepl-transfer.component';

describe('ThreeplTransferComponent', () => {
  let component: ThreeplTransferComponent;
  let fixture: ComponentFixture<ThreeplTransferComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ThreeplTransferComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ThreeplTransferComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
