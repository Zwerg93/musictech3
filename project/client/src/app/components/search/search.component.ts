import {Component, OnInit} from '@angular/core';
import {HttpService} from "../../_services/http.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {PostSongModel} from "../../model/postSong.model";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

  searchString: String = "";
  result?: any[]

  constructor(private http: HttpService, private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
  }

  search() {
    this.http.searchOnYoutube(this.searchString).subscribe((c => {
      this.result = c[0].items
      console.log(this.result)
    }))
  }

  download(videoId: string, title: string, thumbnailurl: string, channelTitle: string) {
    console.log(channelTitle + " titel")
    const song: PostSongModel = {
      title : title,
      id: videoId,
      thumbnailUrl: thumbnailurl,
      artist: channelTitle
    }
    this.http.download(song).subscribe((c => {
          this.snackBar.open("Succes", undefined, {
              duration: 1000
            }
          )
        }

      ), (error => {
        this.snackBar.open("Error", undefined, {
            duration: 1000
          }
        )
      })
    )
  }
}
