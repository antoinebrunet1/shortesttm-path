// Source - https://stackoverflow.com/a/79182225
// Posted by JSON Derulo, modified by community. See post 'Timeline' for change history
// Retrieved 2026-06-21, License - CC BY-SA 4.0

import { NgModule, provideZonelessChangeDetection } from '@angular/core';
import { getTestBed } from '@angular/core/testing';
import { BrowserTestingModule, platformBrowserTesting } from '@angular/platform-browser/testing';

@NgModule({
  // pre Angular v20, use provideExperimentalZonelessChangeDetection
  providers: [provideZonelessChangeDetection()],
})
class ZonelessModule {}

getTestBed().initTestEnvironment([BrowserTestingModule, ZonelessModule], platformBrowserTesting());
