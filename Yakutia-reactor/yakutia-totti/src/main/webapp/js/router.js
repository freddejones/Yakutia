define([
'backbone',
'view/CreateGameView',
'view/ActiveGameView',
'view/ListMyGamesView',
'view/CreatePlayerView'],
function(Backbone, CreateGameView, ActiveGameView, ListMyGamesView,
CreatePlayerView) {

    var activeView = {};

    var YakutiaRouter = Backbone.Router.extend({

        routes: {
            "listgames" : "listGames",
            "createGames" : "createGames",
            "game/play/:gameId" : "playGame",
            "player/create" : "createPlayer"
        },
        listGames: function() {
            this.bind("reset", this.activeView);
            console.log("listing my games"),
            activeView = new ListMyGamesView();
        },
        createGames: function() {
            this.bind("reset", this.activeView);
            console.log("create game page");
            activeView = new CreateGameView();
        },
        playGame: function(gameId) {
            console.log("Go to game id: " + gameId);
            activeView = new ActiveGameView();
        },
        createPlayer: function() {
            console.log("Create player page");
            activeView = new CreatePlayerView();
        },
        clearPlayerView: function() {
            $('#player-container').remove();
        },
        clearGameView: function() {
            $('#game.container').remove();
        }

    });

    return YakutiaRouter;
});