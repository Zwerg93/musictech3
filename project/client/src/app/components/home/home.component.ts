import { Component, OnInit } from '@angular/core';
import {StorageService} from "../../_services/storage.service";
import {HttpService} from "../../_services/http.service";
import {Router} from "@angular/router";
import {PlaylistModel} from "../../model/playlist.model";
const USER_KEY = 'auth-user';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  playlists? : PlaylistModel[];

  constructor(private storageService: StorageService, private http: HttpService, private _router: Router) { }

  ngOnInit(): void {
    if (!this.storageService.isLoggedIn()) {
      this._router.navigate(['login'])
    }
    this.http.getAllPlaylists(JSON.parse(window.sessionStorage.getItem(USER_KEY)!)).subscribe((c=>{
     this.playlists = c;
      console.log(c)
    }))
  }

  getPlaylist(i: number) {

  }
}
