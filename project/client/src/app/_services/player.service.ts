import {Injectable} from '@angular/core';
import {AudioService} from "./audio.service";
import {CloudService} from "./cloud.service";
import {SongModel} from "../model/song.model";

@Injectable({
  providedIn: 'root'
})
export class PlayerService {
  currentFile: any = {};
  files: Array<any> = [];
  tmpFiles: Array<any> = [];
  currentSongName: String = "";
  currentArtist: String = "";
  currentThumbnail : String = "";

  //audioService : AudioService;
  state: any;
  isplaying = false;
  private errors: any;
  searchstring: String = "";
  toggle = true;
  status = 'Enable';
  private tmp: boolean = true;

  constructor(private audioService: AudioService, public cloudService: CloudService) {

    this.audioService.getState()
      .subscribe(state => {
        this.state = state;
      });
  }

  playStream(url: string) {
    this.audioService.playStream(url)
      .subscribe(events => {
      });
    this.isplaying = true;
  }

  openFile(file: SongModel, index: any) {
    this.currentFile = {index, file};
    this.audioService.stop();
    this.currentThumbnail = this.currentFile.file.thumbnailUrl;
    this.playStream(file.streamUrl);
  }

  pause() {
    this.audioService.pause();
    this.isplaying = false;
  }

  play() {
    this.audioService.play();
    this.isplaying = true;
  }

  shuffle(array: any) {
    if (this.tmp) {
      this.tmp = false
      let currentIndex = array.length, randomIndex;
      while (currentIndex != 0) {
        randomIndex = Math.floor(Math.random() * currentIndex);
        currentIndex--;
        [array[currentIndex], array[randomIndex]] = [
          array[randomIndex], array[currentIndex]];
      }
    } else {
      this.cloudService.songlist = this.tmpFiles;
      this.tmp = true;
      //console.log("test")
    }
    this.toggle = !this.toggle;
    this.status = this.toggle ? 'Enable' : 'Disable';
  }

  random() {
    const index = this.randomIntFromInterval(0, this.cloudService.songlist.length - 1);
    const file = this.cloudService.songlist[index];
    this.openFile(file, index);
    //console.log(index);
  }

  stop() {
    this.audioService.stop();
  }

  next() {
    const index = this.currentFile.index + 1;
    const file = this.cloudService.songlist[index];
    this.openFile(file, index);

    this.currentSongName = this.cloudService.songlist[index].title;
    this.currentThumbnail = this.cloudService.songlist[index].thumbnailUrl;
    this.currentArtist = this.cloudService.songlist[index].artist;
    this.audioService.audioObj.currentTime;
  }

  previous() {
    const index = this.currentFile.index - 1;
    const file = this.cloudService.songlist[index];

    this.openFile(file, index);
    this.currentSongName = this.cloudService.songlist[index].title;
    this.currentThumbnail = this.cloudService.songlist[index].thumbnailUrl;
    this.currentArtist = this.cloudService.songlist[index].artist;
  }

  isFirstPlaying() {
    return this.currentFile.index === 0;
  }

  getNameOfCurrentSOng() {
    //console.log();
    return this.audioService.getState();
  }

  isLastPlaying() {
    return this.currentFile.index === this.cloudService.songlist.length - 1;
  }

  onSliderChangeEnd(change: any) {
    this.audioService.seekTo(change.value);
  }

  autoplayfunc(test: any) {
    this.audioService.autoplayfunc(test);
  };

  randomIntFromInterval(min: number, max: number) {
    return Math.floor(Math.random() * (max - min + 1) + min)
  }

  searchForString(event: any) {
    this.cloudService.songlist = this.tmpFiles.filter((file: { name: string }) => {
      return file.name.toLowerCase().includes(this.searchstring.toLowerCase());
    })
  }

  cancelSearch() {
    this.cloudService.songlist = this.tmpFiles;
    (<HTMLInputElement>document.getElementById("searchinput")).value = '';
  }


  currentSongClickedon(i: number) {
    this.currentSongName = this.cloudService.songlist[i].title;
    this.currentArtist = this.cloudService.songlist[i].artist;
  }


  allSongs() {
    this.cloudService.songlist = this.tmpFiles;
  }
}
