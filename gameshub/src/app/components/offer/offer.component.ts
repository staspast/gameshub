import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-offer',
  templateUrl: './offer.component.html',
  styleUrls: ['./offer.component.scss']
})
export class OfferComponent implements OnInit {

  constructor(private route: ActivatedRoute, private router: Router) {
  }

  @Input() offers;

  ngOnInit(): void {
  }

  goToDetails = (offer) => {
    this.router.navigate(['details', offer.id]);
  }
}
