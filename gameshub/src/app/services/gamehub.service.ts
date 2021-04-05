import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GamehubService {

  constructor(private http: HttpClient) {
  }

  getOffers = (data) => {
    let params = new HttpParams();

    params = params.append('marketplaceName', data.marketplaceName);
    return this.http.get<any>('http://192.168.0.198:8081/api/games/offers', {
      observe: 'response',
      params
    });
  }

  getOfferDetails = (id) => {
    return this.http.get<any>('http://192.168.0.198:8081/api/games/get/' + id);
  }

  search = (data) => {
    let params = new HttpParams();

    params = params.append('pageNumber', data.pageNumber);
    params = params.append('pageSize', data.pageSize);

    if (data.name) {
      params = params.append('name', data.name);
    }

    if (data.categoryName) {
      params = params.append('categoryName', data.categoryName);
    }

    if (data.priceFrom) {
      params = params.append('priceFrom', data.priceFrom);
    }

    if (data.priceTo) {
      params = params.append('priceTo', data.priceTo);
    }

    if (data.marketplaceName) {
      params = params.append('marketplaceName', data.marketplaceName);
    }

    if (data.sort) {
      params = params.append('sort', data.sort);
    }

    if (data.sortOrder) {
      params = params.append('sortOrder', data.sortOrder);
    }

    return this.http.get<any>('http://192.168.0.198:8081/api/games/lucene_search', {
      observe: 'response',
      params
    });
  }

  getCompareWithOtherStore(data) {
    let params = new HttpParams();

    params = params.append('name', data.name);
    params = params.append('marketplaceName', data.marketplaceName);

    return this.http.get<any>('http://192.168.0.198:8081/api/games/compare_with_other_stores', {
      observe: 'response',
      params
    });
  }

  addNotification = (parameters) => {
    return this.http.post<any>('http://192.168.0.198:8081/api/notifications/add', parameters);
  }
}
