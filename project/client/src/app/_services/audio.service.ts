import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, Subject, takeUntil} from "rxjs";
import {StreamState} from "../model/streamstate.model";
import * as moment from 'moment';
@Injectable({
  providedIn: 'root'
})
export class AudioService {
  private stop$ = new Subject();
  audioObj = new Audio();
  audioEvents = [
    'ended', 'error', 'play', 'playing', 'pause', 'timeupdate', 'canplay', 'loadedmetadata', 'loadstart', 'autoplay'
  ];


  private state: StreamState = {
    playing: false,
    readableCurrentTime: '',
    readableDuration: '',
    duration: undefined,
    currentTime: undefined,
    canplay: false,
    error: false,
    autoplay: false,
    ended: false,

  };

  private streamObservable(url: string) {
    return new Observable(observer => {
      // Play audio
      this.audioObj.src = url;
      this.audioObj.load();
      this.audioObj.play();

      const handler = (event: Event) => {
        this.updateStateEvents(event);
        observer.next(event);
      };

      this.addEvents(this.audioObj, this.audioEvents, handler);
      return () => {
        // Stop Playing
        this.audioObj.pause();
        this.audioObj.currentTime = 0;
        // remove event listeners
        this.removeEvents(this.audioObj, this.audioEvents, handler);
        // reset state
        this.resetState();
      };
    });
  }

  private addEvents(obj:any, events:any, handler:any) {
    // @ts-ignore
    events.forEach(event => {
      obj.addEventListener(event, handler);
    });
  }

  private removeEvents(obj:any, events:any, handler:any) {
    // @ts-ignore
    events.forEach(event => {
      obj.removeEventListener(event, handler);
    });
  }

  playStream(url:string) {
    return this.streamObservable(url).pipe(takeUntil(this.stop$));
  }



  play() {
    this.audioObj.play();
  }

  pause() {
    this.audioObj.pause();
  }

  stop() {
    this.stop$.next(undefined);
  }

  seekTo(seconds: number) {
    this.audioObj.currentTime = seconds;

  }

  autoplayfunc(autoplay: boolean) {
    this.audioObj.autoplay = autoplay
  }


  formatTime(time: number, format: string = 'HH:mm:ss') {
    const momentTime = time * 1000;
    return moment.utc(momentTime).format(format);
  }

  private stateChange: BehaviorSubject<StreamState> = new BehaviorSubject(this.state);

  private updateStateEvents(event: Event): void {
    switch (event.type) {
      case 'canplay':
        this.state.duration = this.audioObj.duration;
        this.state.readableDuration = this.formatTime(this.state.duration);
        this.state.canplay = true;
        break;
      case 'playing':
        this.state.playing = true;
        break;
      case 'pause':
        this.state.playing = false;
        break;
      case 'timeupdate':
        this.state.currentTime = this.audioObj.currentTime;
        this.state.readableCurrentTime = this.formatTime(this.state.currentTime);
        break;
      case 'error':
        this.resetState();
        this.state.error = true;
        break;
      case 'ended':
        this.resetState();
        this.state.playing = true;
        break;
    }
    this.stateChange.next(this.state);
  }

  private resetState() {
    this.state = {
      playing: false,
      readableCurrentTime: '',
      readableDuration: '',
      duration: undefined,
      currentTime: undefined,
      canplay: false,
      error: false,
      ended: false,
      autoplay: false
    };
  }



  getState(): Observable<StreamState> {
    return this.stateChange.asObservable();
  }
}
