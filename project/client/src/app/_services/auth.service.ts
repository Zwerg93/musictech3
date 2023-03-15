import {HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {HttpService} from "./http.service";
import {Observable} from "rxjs";



@Injectable({
  providedIn: 'root'
})


export class AuthService {


  constructor(private http: HttpService) {
  }


}
