import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProspectiveClientComponent } from './prospective-client.component';

describe('ProspectiveClientComponent', () => {
  let component: ProspectiveClientComponent;
  let fixture: ComponentFixture<ProspectiveClientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProspectiveClientComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProspectiveClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
