import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigurationNewComponent } from './configuration-new.component';

describe('ConfigurationNewComponent', () => {
  let component: ConfigurationNewComponent;
  let fixture: ComponentFixture<ConfigurationNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfigurationNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigurationNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
