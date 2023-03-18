import {Component, OnInit} from '@angular/core';
import {HttpService} from "./_services/http.service";
import {StorageService} from "./_services/storage.service";
import {Router} from "@angular/router";
import {PlayerService} from "./_services/player.service";
const USER_KEY = 'auth-user';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  private roles: string[] = [];
  isLoggedIn = false;
  showAdminBoard = false;
  showModeratorBoard = false;
  username?: string;
  title = 'client';

  constructor(private storageService: StorageService, private http: HttpService, private _router: Router, public palyerSerice: PlayerService) {
    if (JSON.parse(window.sessionStorage.getItem(USER_KEY)!) != null) {
      this.http.getAllUser(JSON.parse(window.sessionStorage.getItem(USER_KEY)!)).subscribe((c => {
        console.log(c[0].firstname + " allUser")
      }))
    }

  }

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();
    //console.log(JSON.parse(window.sessionStorage.getItem(USER_KEY)!))
    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      console.log(user)
      this.roles = user.roles;
      //this.showAdminBoard = this.roles.includes('ROLE_ADMIN');
      //this.showModeratorBoard = this.roles.includes('ROLE_MODERATOR');
      this.username = user.username;

    }else{
      this._router.navigate(['login'])
    }


  }


  logOut() {
    this.storageService.clean();
    //window.sessionStorage.setItem(USER_KEY, JSON.stringify(""));
    window.location.reload();
  }
}
