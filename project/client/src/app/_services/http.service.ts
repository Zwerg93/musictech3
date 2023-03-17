import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {UserModel} from "../model/user.model";
import {TokenModel} from "../model/token.model";

const USER_KEY = 'auth-user';
const API_URL = environment.API_URL;
//const token: TokenModel = JSON.parse(window.sessionStorage.getItem(USER_KEY)!)

const httpOptionsPost = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  })
};

@Injectable({
  providedIn: 'root'
})


export class HttpService {
  //console.log)

  constructor(private http: HttpClient) {
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(API_URL + "/auth", {username, password}, httpOptionsPost)
  }

  register(username: string, firstname: string, lastname: string, password: string, mail: string): Observable<any> {
    return this.http.post<any>(API_URL + "/user", {username, firstname, lastname, password, mail}, httpOptionsPost);
  }


  getAllUser( token: TokenModel): Observable<UserModel[]> {


    return this.http.get<UserModel[]>(API_URL + "/user/all", {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token.token}`
      })
    })
  }


}
