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
    data = {
      ...data,
      pageNumber: 0,
      pageSize: 10
    };

    let params = new HttpParams();

    params = params.append('pageNumber', data.pageNumber);
    params = params.append('pageSize', data.pageSize);

    if (data.name) {
      params = params.append('name', data.name);
    }

    if (data.marketplaceName) {
      params = params.append('marketplaceName', data.marketplaceName);
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

  addNotification = () => {
    return this.http.post<any>('http://192.168.0.198:8081/api/notifications/add', {
      params: {
        gameId: 58216,
        email: 'staspast1@gmail.com',
        priceGoal: 100.89
      }
    });
  }
}
