var firebaseConfig = {
    apiKey: "AIzaSyD4fHPPi-1jpURi2p_W-56sNrT2SsYjVx8",
    authDomain: "orwmaprojekt-c9cbb.firebaseapp.com",
    databaseURL: "https://orwmaprojekt-c9cbb-default-rtdb.firebaseio.com",
    projectId: "orwmaprojekt-c9cbb",
    storageBucket: "orwmaprojekt-c9cbb.appspot.com",
    messagingSenderId: "972506753102",
    appId: "1:972506753102:web:cfdc1c43572e615e0a3b1f"
};
firebase.initializeApp(firebaseConfig);
firebase.auth().onAuthStateChanged(function (user) {
    if (user) {
        var user = firebase.auth().currentUser;
    }
    if (user == null) {
        window.location.href = '../index.html';
    }

});
var loggedInPlayerId = sessionStorage.getItem('loggedInUser');
var loggedInPlayerName;
var rootRef = firebase.database().ref();
var teamsRef = rootRef.child("Teams");
var playersRef = rootRef.child("Players");
var playerNames = [[,,,]]; //TODO
var teamNames = [];
playersRef.once("value", function (snapshot) {
    loggedInPlayerName = snapshot.child(loggedInPlayerId).child("fullName").val();
});
teamsRef.on("value", function (snapshot) {
    var i=0;
    snapshot.forEach(function (childSnapshot) {
        if (childSnapshot.child("Players").child(loggedInPlayerName).exists()) {
        i++;
            var teamName = childSnapshot.child("name").val();
            teamNames.push(teamName);
            childSnapshot.forEach(function (grandChildSnapshot) {
                grandChildSnapshot.forEach(function (playersSnapshot) {
                var playerName=playersSnapshot.key;
                var playerWins=playersSnapshot.child("Wins").val();
                var playerGames=playersSnapshot.child("Played").val();
                playerNames.push([i,playerName,playerGames,playerWins]);
            });
        });
        }
    });
    //for(var i=0;i<playerNames.length;i++)
    console.log(playerNames[1][0]);
});