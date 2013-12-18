define([
'backbone',
'jquery',
'view/CreateGameView',
'view/ActiveGameView',
'view/ListMyGamesView',
'view/CreatePlayerView'],
function(Backbone, JQuery, CreateGameView, ActiveGameView, ListMyGamesView,
CreatePlayerView) {

    var activeView = {};

    var YakutiaRouter = Backbone.Router.extend({

        routes: {
            "listgames" : "listGames",
            "createGames" : "createGames",
            "game/play/:gameId" : "playGame",
            '*path':  'defaultRoute'
        },
        listGames: function() {
            this.fixViews();
            console.log("listing my games"),
            activeView = new ListMyGamesView();
        },
        createGames: function() {
            this.fixViews();
            console.log("create game page");
            activeView = new CreateGameView();
        },
        playGame: function(gameId) {
            this.fixViews();
            console.log("Go to game id: " + gameId);
            activeView = new ActiveGameView();
        },
        defaultRoute: function() {
            this.fixViews();
            console.log("Create player page");
            activeView = new CreatePlayerView();
        },
        fixViews: function() {
            if (!$.isEmptyObject(activeView)) {
                $('#game-container').remove();
                $('#player-container').remove();
                $('#yakutia-main').append('<div id="game-container" class="container"></div>');
                $('#yakutia-main').append('<div id="player-container" class="container"></div>');
            }
        }
    });

    return YakutiaRouter;
});