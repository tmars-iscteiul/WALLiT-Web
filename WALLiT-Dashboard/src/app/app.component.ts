import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { DataService } from "./data.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {

  ngOnInit() {
  	this.data.currentMessage.subscribe(message => {
  		document.getElementById("username").innerHTML = message;
  		if (message != "") {
  			document.getElementById("login").innerHTML = "Logout";
  		}
  	});
  }

  doLogout() {
  	document.getElementById("login").innerHTML = "Login";
  	document.getElementById("username").innerHTML = "";
    this.data.changeMessage("");
  }

  goTo(page) {
    document.getElementById("home").classList.remove("active");
    document.getElementById("dashboard").classList.remove("active");
    document.getElementById("personal").classList.remove("active");
    document.getElementById("about").classList.remove("active");
    document.getElementById("personal").classList.remove("active");
    document.getElementById(page).classList.add("active");

    if (page == "personal" && document.getElementById("login").innerHTML == "Login") {
      this.router.navigateByUrl("/login");
    }
  }

  constructor(private router: Router, private data: DataService) {
  } 
}

