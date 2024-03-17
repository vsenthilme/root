import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TranserrorTabComponent } from './transerror-tab.component';

describe('TranserrorTabComponent', () => {
  let component: TranserrorTabComponent;
  let fixture: ComponentFixture<TranserrorTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TranserrorTabComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TranserrorTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
