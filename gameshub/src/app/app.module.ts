import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HomeComponent} from './components/home/home.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import {NavbarComponent} from './components/navbar/navbar.component';
import {LogoComponent} from './components/logo/logo.component';

import {FlexLayoutModule} from '@angular/flex-layout';

import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule, MatIconRegistry} from '@angular/material/icon';
import {MatTabsModule} from '@angular/material/tabs';
import {MatCardModule} from '@angular/material/card';
import {HttpClientModule} from '@angular/common/http';
import {GamehubService} from './services/gamehub.service';
import {TruncatePipe} from './components/pipes/truncate.pipe';
import {MatButtonModule} from '@angular/material/button';
import {DetailsComponent} from './components/details/details.component';
import {SwiperModule} from 'ngx-swiper-wrapper';
import {OfferComponent} from './components/offer/offer.component';
import {EmptyStateComponent} from './components/empty-state/empty-state.component';
import {FiltersComponent} from './components/filters/filters.component';
import {ReactiveFormsModule} from '@angular/forms';
import {MatPaginatorModule} from '@angular/material/paginator';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    NavbarComponent,
    LogoComponent,
    TruncatePipe,
    DetailsComponent,
    OfferComponent,
    EmptyStateComponent,
    FiltersComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FlexLayoutModule,
    MatFormFieldModule,
    MatIconModule,
    MatTabsModule,
    MatCardModule,
    HttpClientModule,
    MatButtonModule,
    SwiperModule,
    ReactiveFormsModule,
    MatPaginatorModule
  ],
  providers: [
    MatIconRegistry,
    GamehubService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
