import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VistaMovimientoComponent } from './vista-movimiento.component';

describe('VistaMovimientoComponent', () => {
  let component: VistaMovimientoComponent;
  let fixture: ComponentFixture<VistaMovimientoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VistaMovimientoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VistaMovimientoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
