import { TestBed, inject } from '@angular/core/testing';

import { UserService } from './user.service';
import {expect, describe} from "@angular/core/testing/src/testing_internal";

describe('UserService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserService]
    });
  });

  it('should be created', inject([UserService], (service: UserService) => {
    expect(service).toBeTruthy();
  }));
});
