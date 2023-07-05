import { Component, OnInit } from '@angular/core';
import {SongService} from "../../_services/song.service";
import {PlayerService} from "../../_services/player.service";
import {CloudService} from "../../_services/cloud.service";

@Component({
  selector: 'app-over-fiew-song',
  templateUrl: './over-fiew-song.component.html',
  styleUrls: ['./over-fiew-song.component.scss']
})
export class OverFiewSongComponent implements OnInit {
  mainColor: string = "";
  constructor(public cloudService : CloudService, public songservice: SongService, public playerService: PlayerService) { }
  ngOnInit(): void {

  }


  close() {
      this.songservice.toggleDetailView()
  }
  isPlaying():boolean{
    return this.playerService.isplaying
  }

  protected readonly PlayerService = PlayerService;
}
