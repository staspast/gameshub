import {Component, OnInit} from '@angular/core';
import {GamehubService} from '../../services/gamehub.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  topOffers: any = [];
  searchOffers: any = [];
  selectedTabIndex = 0;
  markerplace = 'steam';

  constructor(public gamehubService: GamehubService) {
  }

  ngOnInit(): void {
    this.getOffers(this.markerplace);
  }

  getOffers = (marketplaceName) => {
    this.gamehubService.getOffers({marketplaceName}).subscribe(response => {
      this.topOffers = response.body;
    });
  }

  changeStore = ($event) => {
    this.selectedTabIndex = $event.index;
    this.topOffers = [];

    switch (this.selectedTabIndex) {
      case 0: {
        this.markerplace = 'steam';
        this.getOffers(this.markerplace);
        break;
      }
      case 1: {
        this.markerplace = 'gog';
        this.getOffers(this.markerplace);
        break;
      }
      case 2: {
        this.markerplace = 'origin';
        this.getOffers(this.markerplace);
        break;
      }
      case 3: {
        this.markerplace = 'epicgames';
        this.getOffers(this.markerplace);
        break;
      }
      case 4: {
        this.markerplace = 'humblebundle';
        this.getOffers(this.markerplace);
        break;
      }
    }
  }

  searchResult = (result) => {
    if (result && result.body && result.body.content) {
      this.searchOffers = result.body.content;
    } else {
      this.searchOffers = [];
    }
  }
}
