import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigTabbarComponent } from './config-tabbar.component';

describe('ConfigTabbarComponent', () => {
  let component: ConfigTabbarComponent;
  let fixture: ComponentFixture<ConfigTabbarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfigTabbarComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigTabbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
