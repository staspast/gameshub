import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HomeComponent} from './home/home.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import {NavbarComponent} from './common/navbar/navbar.component';
import {LogoComponent} from './common/logo/logo.component';
import {CardDetailsComponent} from './common/card-details/card-details.component';

import {FlexLayoutModule} from '@angular/flex-layout';

import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule, MatIconRegistry} from '@angular/material/icon';
import {MatTabsModule} from '@angular/material/tabs';
import {MatCardModule} from '@angular/material/card';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    NavbarComponent,
    LogoComponent,
    CardDetailsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FlexLayoutModule,
    MatFormFieldModule,
    MatIconModule,
    MatTabsModule,
    MatCardModule
  ],
  providers: [
    MatIconRegistry
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
