import {Component} from '@angular/core';
import {MatIconRegistry} from '@angular/material/icon';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  constructor(
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer,
  ) {
    this.matIconRegistry
      .addSvgIcon('account', this.domSanitizer.bypassSecurityTrustResourceUrl('./assets/account.svg'))
      .addSvgIcon('arrow', this.domSanitizer.bypassSecurityTrustResourceUrl('./assets/arrow.svg'))
      .addSvgIcon('grade', this.domSanitizer.bypassSecurityTrustResourceUrl('./assets/grade.svg'));
  }
}
