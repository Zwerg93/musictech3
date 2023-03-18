import {Component, OnInit} from '@angular/core';
import {CloudService} from "../../_services/cloud.service";
import {PlayerService} from "../../_services/player.service";

@Component({
  selector: 'app-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit {

  constructor(public cloudService: CloudService, public playerService: PlayerService) {
  }

  ngOnInit(): void {
  }


  play() {
    this.playerService.play();
  }

  pause() {
    this.playerService.pause();
  }

  currentSongClickedon(i: number) {
    this.playerService.currentSongName = this.cloudService.songlist[i].title;
    this.playerService.currentArtist = this.cloudService.songlist[i].title;
  }

  openOptions() {
    console.log("Test")
  }
}
