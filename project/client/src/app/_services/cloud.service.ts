import {Injectable, OnInit} from '@angular/core';
import {HttpService} from "./http.service";
import {SongModel} from "../model/song.model";

const USER_KEY = 'auth-user';
@Injectable({
  providedIn: 'root'
})
export class CloudService implements OnInit{
  constructor(private http: HttpService) {
    if(JSON.parse(window.sessionStorage.getItem(USER_KEY)!) != null){
      this.http.getAllSongs(JSON.parse(window.sessionStorage.getItem(USER_KEY)!)).subscribe((c => {
        this.songlist = c;
        this.songlist_TMP = c;
        console.log(c + " Songlist")
      }))
    }

  }

  public songlist!: SongModel[]
  public songlist_TMP!: SongModel[]
  public playlists: any;


  ngOnInit(): void {

  }

}
