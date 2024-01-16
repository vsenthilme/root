import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OwnershippopComponent } from './ownershippop.component';

describe('OwnershippopComponent', () => {
  let component: OwnershippopComponent;
  let fixture: ComponentFixture<OwnershippopComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OwnershippopComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OwnershippopComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
