import {Component, OnInit} from '@angular/core';
import {HttpService} from "../../_services/http.service";
import {MatSnackBar} from "@angular/material/snack-bar";

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

  download(videoId: string, title: string) {
    console.log(title + " titel")
    this.http.download(videoId, title).subscribe((c => {
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
