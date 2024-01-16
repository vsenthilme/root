import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProspectiveOutputComponent } from './prospective-output.component';

describe('ProspectiveOutputComponent', () => {
  let component: ProspectiveOutputComponent;
  let fixture: ComponentFixture<ProspectiveOutputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProspectiveOutputComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProspectiveOutputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
