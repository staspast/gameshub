import {Component, EventEmitter, Output} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {GamehubService} from '../../services/gamehub.service';

@Component({
  selector: 'app-filters',
  templateUrl: './filters.component.html',
  styleUrls: ['./filters.component.scss']
})
export class FiltersComponent {
  categories = ['Indie', 'Action', 'Adventure', 'Casual', 'Simulation', 'Strategy', 'RPG', 'Free to Play', 'Early Access', 'Sports', 'Racing', 'Massively Multiplayer', 'Design & Illustration', 'Web Publishing', 'Violent', 'Utilities', 'Role-playing', 'Fantasy', 'Sci-fi', 'Puzzle'];
  marketplaces = ['Steam', 'GOG', 'Origin', 'Epic Games', 'Humble Bundle'];
  sortByList = ['price_asc', 'price_desc'];

  searchForm = new FormGroup({
    search: new FormControl()
  });

  sortForm = new FormGroup({
    sortBy: new FormControl(),
  });

  filterForm = new FormGroup({
    categoryName: new FormControl(),
    priceFrom: new FormControl(),
    priceTo: new FormControl(),
    marketplaceName: new FormControl()
  });

  filterParameters = {
    name: '',
    categoryName: '',
    priceFrom: '',
    priceTo: '',
    marketplaceName: '',
    pageSize: 10,
    pageNumber: 1,
    sort: '',
    sortOrder: ''
  };

  expand = false;

  searchT = false;
  sortT = false;
  filtersT = false;

  @Output() searchEvent = new EventEmitter();

  constructor(
    public gamehubService: GamehubService
  ) {
  }

  search = (parameters?) => {
    if (parameters) {
      this.filterParameters = {
        ...this.filterParameters, ...parameters
      };
    }

    if (this.searchForm.get('search').value) {
      this.filterParameters.name = this.searchForm.get('search').value;
    }

    if (this.filterForm.get('categoryName').value) {
      this.filterParameters.categoryName = this.filterForm.get('categoryName').value;
    }

    if (this.filterForm.get('priceFrom').value) {
      this.filterParameters.priceFrom = this.filterForm.get('priceFrom').value;
    }

    if (this.filterForm.get('priceTo').value) {
      this.filterParameters.priceTo = this.filterForm.get('priceTo').value;
    }

    if (this.filterForm.get('marketplaceName').value) {
      this.filterParameters.marketplaceName = this.filterForm.get('marketplaceName').value;
    }

    if (this.sortForm.get('sortBy').value) {
      this.filterParameters.sort = 'price';

      if (this.sortForm.get('sortBy').value === 'price_asc') {
        this.filterParameters.sortOrder = 'asc';
      } else {
        this.filterParameters.sortOrder = 'desc';
      }
    }

    this.gamehubService.search(this.filterParameters).subscribe(response => {
      this.searchEvent.emit(response);
    });
  };

  clear = (form) => {
    switch (form) {
      case 'searchForm':
        this.filterParameters = {
          ...this.filterParameters,
          name: '',
        };
        break;
      case 'filterForm':
        this.filterParameters = {
          ...this.filterParameters,
          categoryName: '',
          priceFrom: '',
          priceTo: '',
          marketplaceName: '',
        };
        break;
      case 'sortForm':
        this.filterParameters = {
          ...this.filterParameters,
          sort: '',
          sortOrder: '',
        };
        break;
    }
    this[form].reset();
    this.searchEvent.emit([]);
  };

  collapse = ($event, type) => {
    this.expand = !this.expand;

    if (this.expand) {
      document.getElementById(type).classList.add('filters__open');
      document.getElementById(type).classList.remove('filters__close');
    } else {
      document.getElementById(type).classList.add('filters__close');
      document.getElementById(type).classList.remove('filters__open');
    }
  }
}
