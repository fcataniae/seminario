import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AgregarBienComponent } from './agregar-bien.component';

describe('AgregarBienComponent', () => {
  let component: AgregarBienComponent;
  let fixture: ComponentFixture<AgregarBienComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AgregarBienComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AgregarBienComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
