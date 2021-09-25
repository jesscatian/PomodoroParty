/** @brief Timer to get current pomodoro timer, has the minutes left, seconds left, and whether it is breaktime relative to clock. Use prettyPrint() to return a mm:ss format string 
	@member secondsLeft Seconds remaining in the current minute
	@member minutesLeft Minutes remaining in the current pomotimer (25 minute worktime, 5 minute break time)
	@member break Boolean that represents if currently on break
*/
class PomoTime {
	constructor() {
		let date = new Date();
		let minutes = date.getMinutes();
		let seconds = date.getSeconds();
		if (minutes % 30 < 25) {
		    
			this.secondsLeft = 59 - seconds;
			this.minutesLeft = 24 - (minutes % 30);
			this.break = false;
		}
		else {
			this.secondsLeft = 59 - seconds;
			this.minutesLeft = 4 - (minutes % 5);
			this.break = true;
		}
		/*console.log(this.secondsLeft);
		console.log(this.minutesLeft);
		console.log(this.break);*/
	}
	/** @brief Returns a string current time left of format mm:ss /*/
	prettyPrint() {
	    let timerString = "";
	    if (this.minutesLeft < 10) timerString +=  `0${String(this.minutesLeft)}`;
	    else timerString += this.minutesLeft;
	    timerString += ':';
        if (this.secondsLeft < 10) timerString += `0${String(this.secondsLeft)}`;
		else timerString += this.secondsLeft;
        return String(timerString);
    }
}

const audio = document.querySelector("audio");
const path = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/PomodoroParty/";
// Examples of how to use timer, refreshing every second
function displayTimeUsers() {
    let date = new PomoTime();

	if(date.break){
		document.getElementById("timer-p").innerHTML = date.prettyPrint();
		document.getElementById("timer-col").style.backgroundColor = "#dcedb9";
	}
	else{
		document.getElementById("timer-p").innerHTML = date.prettyPrint();
		document.getElementById("timer-col").style.backgroundColor = "#db7f67";
	}
	
	$.ajax ({
		url: "StudyRoom",
		type: "GET",
		success: function(data)
		{
			document.getElementById("currOnlineNum").innerHTML = data.numOnline;
			document.getElementById("1rankname").innerHTML = data.username;
			document.getElementById("1rankstreak").innerHTML = data.streak;
		},
	})
	
    console.log(date.prettyPrint());

	if(audio.currentSrc == path + "SSG_BlendItUpSmooth_Loop.wav"){
		document.getElementById("song-title").innerHTML = "Blend It Up";
		document.getElementById("artist").innerHTML = "James Gillen";
	}
	if(audio.currentSrc == path + "BEAT_Smoothieenlaplaya.wav"){
		document.getElementById("song-title").innerHTML = "Smoothie en la Playa";
		document.getElementById("artist").innerHTML = "Markel Badallo Detorre";
	}
	if(audio.currentSrc == path + "Smoothie_Song_Chandler.wav"){
		document.getElementById("song-title").innerHTML = "8-bit Fruits";
		document.getElementById("artist").innerHTML = "Remi Chandler";
	}
}

var update_loop = setInterval(displayTimeUsers, 1000);

displayTimeUsers();

const button = document.getElementById("muteButton");

button.addEventListener("click", () => {
  if (audio.paused) {
    audio.volume = 0.2;
    audio.play();
    button.src = "Speaker.png";
  } else {
    audio.pause();
	button.src = "Muted.png";
  }
});

audio.addEventListener('ended', () => {
	var nextSong;
	if(audio.currentSrc == path + "SSG_BlendItUpSmooth_Loop.wav"){
		nextSong = path + "BEAT_Smoothieenlaplaya.wav";
	}
	if(audio.currentSrc == path + "BEAT_Smoothieenlaplaya.wav"){
		nextSong = path + "Smoothie_Song_Chandler.wav";
	}
	if(audio.currentSrc == path + "Smoothie_Song_Chandler.wav"){
		nextSong = path + "SSG_BlendItUpSmooth_Loop.wav";
	}
	audio.src = nextSong;
	audio.play;
});