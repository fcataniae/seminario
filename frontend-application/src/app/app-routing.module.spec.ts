import { AppRoutingModule } from './app-routing.module';
import {expect, describe} from "@angular/core/testing/src/testing_internal";

describe('AppRoutingModule', () => {
  let appRoutingModule: AppRoutingModule;

  beforeEach(() => {
    appRoutingModule = new AppRoutingModule();
  });

  it('should create an instance', () => {
    expect(appRoutingModule).toBeTruthy();
  });
});
