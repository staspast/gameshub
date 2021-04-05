import {Component, Input} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {OfferDialogComponent} from '../offer-dialog/offer-dialog.component';

@Component({
  selector: 'app-offer',
  templateUrl: './offer.component.html',
  styleUrls: ['./offer.component.scss']
})
export class OfferComponent {

  constructor(
    public route: ActivatedRoute,
    public router: Router,
    public dialog: MatDialog
  ) {
  }

  @Input() offers;

  goToDetails = (offer) => {
    this.router.navigate(['details', offer.id]);
  };

  openDialog = (item) => {
    const dialogRef = this.dialog.open(OfferDialogComponent, {
      width: '560px',
      data: {
        name: item.name,
        id: item.id,
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }
}
