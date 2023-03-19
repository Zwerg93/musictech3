import {Injectable} from '@angular/core';
import {PlayerService} from "./player.service";

@Injectable({
  providedIn: 'root'
})
export class SongService {
  showDetail: boolean = false;


  constructor(public playerservice: PlayerService) {
  }

  toggleDetailView() {

    if (this.showDetail) {
      this.showDetail = false
    } else {
      this.showDetail = true;
    }
  }

  changeButton() {
    if (this.playerservice.isplaying) {

      this.playerservice.pause()
    } else {

      this.playerservice.play()
    }
  }
}
