import { Component, OnInit } from '@angular/core';
import { DataService } from "./data.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {

  ngOnInit() {
  	this.data.currentMessage.subscribe(message => {
  		console.log(message);
  		document.getElementById("username").innerHTML = message;
  		if (message != "") {
  			document.getElementById("login").innerHTML = "Logout";
  		}
  	});
  }

  doLogout() {
  	document.getElementById("login").innerHTML = "Login";
  	document.getElementById("username").innerHTML = "";
  }
  
  constructor(private data: DataService) {
  } 
}

