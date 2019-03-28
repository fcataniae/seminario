import { TestBed, inject } from '@angular/core/testing';

import { LoginService } from './login.service';
import {expect, describe} from "@angular/core/testing/src/testing_internal";

describe('LoginService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LoginService]
    });
  });

  it('should be created', inject([LoginService], (service: LoginService) => {
    expect(service).toBeTruthy();
  }));
});
