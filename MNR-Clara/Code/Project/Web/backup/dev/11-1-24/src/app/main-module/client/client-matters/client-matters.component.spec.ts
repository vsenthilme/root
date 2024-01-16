import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientMattersComponent } from './client-matters.component';

describe('ClientMattersComponent', () => {
  let component: ClientMattersComponent;
  let fixture: ComponentFixture<ClientMattersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientMattersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientMattersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
