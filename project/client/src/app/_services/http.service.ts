import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";

const API_URL = environment.API_URL;

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})


export class HttpService {

  constructor(private http: HttpClient) {
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(API_URL + "/auth", {username, password}, httpOptions)
  }

  register(username: string, firstname: string, lastname: string, password: string, mail: string): Observable<any> {
    return this.http.post<any>(API_URL + "/user", {username, firstname, lastname, password, mail}, httpOptions);
  }


}
