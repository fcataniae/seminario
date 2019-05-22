import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AgregarRecursoComponent } from './agregar-recurso.component';

describe('AgregarRecursoComponent', () => {
  let component: AgregarRecursoComponent;
  let fixture: ComponentFixture<AgregarRecursoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AgregarRecursoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AgregarRecursoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
