var loggedInPlayerId = sessionStorage.getItem('loggedInUser');
var loggedInPlayerName;
var rootRef = firebase.database().ref();
var teamsRef = rootRef.child("Teams");
var playersRef = rootRef.child("Players");
var playerNames = [];
var teamNames = [];
playersRef.once("value", function (snapshot) {
    snapshot.forEach(function (childSnapshot) {
        loggedInPlayerName = childSnapshot.child(loggedInPlayerId).child("name").val();
        console.log(loggedInPlayerId);
        console.log(loggedInPlayerName);
    });
});
teamsRef.on("value", function (snapshot) {
    snapshot.forEach(function (childSnapshot) {

        if (childSnapshot.child("Players").child(loggedInPlayerName).exists()) {
            var teamName = childSnapshot.child("name").val()
            teamNames.push(teamName);

        }
    });
    for (var i = 0; i < playerNames.length; i++)
        console.log(playerNames[i]);

});
teamsRef.on("value", function (snapshot) {
    snapshot.forEach(function (childSnapshot) {
        var playerName = childSnapshot.child("Players").key;
        playerNames.push(playerName);
    });

    for (var i = 0; i < teamNames.length; i++)
        console.log(teamNames[i]);
});