import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemCreateNewComponent } from './item-create-new.component';

describe('ItemCreateNewComponent', () => {
  let component: ItemCreateNewComponent;
  let fixture: ComponentFixture<ItemCreateNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ItemCreateNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ItemCreateNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
