import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {FormControl, FormGroup} from '@angular/forms';
import {GamehubService} from "../../services/gamehub.service";

@Component({
  selector: 'app-offer-dialog',
  templateUrl: './offer-dialog.component.html',
  styleUrls: ['./offer-dialog.component.scss']
})
export class OfferDialogComponent {
  offerDialogForm = new FormGroup({
    email: new FormControl(),
    priceGoal: new FormControl()
  });

  parameters = {
    gameId: this.data.id,
    email: '',
    priceGoal: '',
  };

  constructor(
    public dialogRef: MatDialogRef<OfferDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data,
    public gamehubService: GamehubService
  ) {
  }

  saveOffer = () => {
    if (this.offerDialogForm.get('email').value) {
      this.parameters.email = this.offerDialogForm.get('email').value;
    }

    if (this.offerDialogForm.get('priceGoal').value) {
      this.parameters.priceGoal = this.offerDialogForm.get('priceGoal').value;
    }

    this.gamehubService.addNotification(this.parameters).subscribe(response => {
      if (response.message === 'OK') {
        this.dialogRef.close();
      }
    });
  }
}
