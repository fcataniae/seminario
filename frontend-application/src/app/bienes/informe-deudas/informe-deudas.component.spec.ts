import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InformeDeudasComponent } from './informe-deudas.component';

describe('InformeDeudasComponent', () => {
  let component: InformeDeudasComponent;
  let fixture: ComponentFixture<InformeDeudasComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InformeDeudasComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InformeDeudasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
