import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {GamehubService} from '../../services/gamehub.service';

@Component({
  selector: 'app-filters',
  templateUrl: './filters.component.html',
  styleUrls: ['./filters.component.scss']
})
export class FiltersComponent implements OnInit {
  categories = ['Indie', 'Action', 'Adventure', 'Casual', 'Simulation', 'Strategy', 'RPG', 'Free to Play', 'Early Access', 'Sports', 'Racing', 'Massively Multiplayer', 'Design & Illustration', 'Web Publishing', 'Violent', 'Utilities', 'Role-playing', 'Fantasy', 'Sci-fi', 'Puzzle']

  searchForm = new FormGroup({
    search: new FormControl()
  });

  filterParameters = {
    name: ''
  };

  @Output() searchEvent = new EventEmitter();

  constructor(
    public gamehubService: GamehubService
  ) {
  }

  ngOnInit(): void {

  }

  search = () => {
    if (this.searchForm.get('search').value) {
      this.filterParameters.name = this.searchForm.get('search').value;
    }
    this.gamehubService.search(this.filterParameters).subscribe(response => {
      this.searchEvent.emit(response);
    });
  }

  clear = () => {
    this.searchForm.reset();
    this.searchEvent.emit([]);
  }
}
