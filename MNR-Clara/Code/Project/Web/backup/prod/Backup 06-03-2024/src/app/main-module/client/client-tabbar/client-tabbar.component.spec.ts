import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientTabbarComponent } from './client-tabbar.component';

describe('ClientTabbarComponent', () => {
  let component: ClientTabbarComponent;
  let fixture: ComponentFixture<ClientTabbarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientTabbarComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientTabbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
