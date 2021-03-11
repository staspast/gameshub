import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {GamehubService} from '../../services/gamehub.service';
import {SwiperConfigInterface, SwiperPaginationInterface} from 'ngx-swiper-wrapper';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss']
})
export class DetailsComponent implements OnInit {
  offer;
  offerFromOtherStore;

  pagination: SwiperPaginationInterface = {
    el: '.swiper-pagination',
    clickable: true,
    hideOnClick: false
  };
  
  configSwiper: SwiperConfigInterface = {
    pagination: this.pagination,
    slidesPerView: 1,
    navigation: true,
    loop: true,
    speed: 1000
  };

  constructor(
    private route: ActivatedRoute,
    public gamehubService: GamehubService,
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.gamehubService.getOfferDetails(params.id).subscribe(response => {
        this.offer = response;

        this.compareWithOtherStore({
          name: this.offer.name,
          marketplaceName: this.offer.marketplaceName,
        });
      });
    });
  }

  compareWithOtherStore = (parameters) => {
    this.gamehubService.getCompareWithOtherStore(parameters).subscribe(response => {
      this.offerFromOtherStore = response.body;
    });
  }
}
