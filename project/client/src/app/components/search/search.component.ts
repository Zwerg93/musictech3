import {Component, OnInit} from '@angular/core';
import {HttpService} from "../../_services/http.service";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

  searchString: String = "";
  result?: any[]

  constructor(private http: HttpService) {
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
    this.http.download(videoId,title).subscribe((c=>{
      console.log(c)
    }))
  }
}
