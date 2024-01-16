import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientCatgComponent } from './client-catg.component';

describe('ClientCatgComponent', () => {
  let component: ClientCatgComponent;
  let fixture: ComponentFixture<ClientCatgComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientCatgComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientCatgComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
