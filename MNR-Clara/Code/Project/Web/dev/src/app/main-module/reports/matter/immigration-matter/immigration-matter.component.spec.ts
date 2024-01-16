import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImmigrationMatterComponent } from './immigration-matter.component';

describe('ImmigrationMatterComponent', () => {
  let component: ImmigrationMatterComponent;
  let fixture: ComponentFixture<ImmigrationMatterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImmigrationMatterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImmigrationMatterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
