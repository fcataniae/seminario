import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionMovimientosComponent } from './gestion-movimientos.component';

describe('GestionMovimientosComponent', () => {
  let component: GestionMovimientosComponent;
  let fixture: ComponentFixture<GestionMovimientosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GestionMovimientosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GestionMovimientosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
