import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModificarBienComponent } from './modificar-bien.component';

describe('ModificarBienComponent', () => {
  let component: ModificarBienComponent;
  let fixture: ComponentFixture<ModificarBienComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModificarBienComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModificarBienComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
