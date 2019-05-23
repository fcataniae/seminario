import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmarDevolucionComponent } from './confirmar-devolucion.component';

describe('ConfirmarDevolucionComponent', () => {
  let component: ConfirmarDevolucionComponent;
  let fixture: ComponentFixture<ConfirmarDevolucionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfirmarDevolucionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmarDevolucionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
