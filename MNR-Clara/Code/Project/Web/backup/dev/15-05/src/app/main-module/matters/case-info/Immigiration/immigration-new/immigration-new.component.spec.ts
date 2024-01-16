import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImmigrationNewComponent } from './immigration-new.component';

describe('ImmigrationNewComponent', () => {
  let component: ImmigrationNewComponent;
  let fixture: ComponentFixture<ImmigrationNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImmigrationNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImmigrationNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
