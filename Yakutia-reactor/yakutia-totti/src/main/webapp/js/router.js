define([
'backbone',
'jquery',
'view/CreateGameView',
'view/ActiveGameView',
'view/ListMyGamesView',
'view/CreatePlayerView',
'view/SearchFriendsView',
'view/MyFriendsView'],
function(Backbone, JQuery, CreateGameView, ActiveGameView, ListMyGamesView,
CreatePlayerView, SearchFriendsView, MyFriendsView) {

    var activeView = {};

    var YakutiaRouter = Backbone.Router.extend({

        routes: {
            "listgames" : "listGames",
            "createGames" : "createGames",
            "game/play/:gameId" : "playGame",
            "friends/search" : "searchFriend",
            "friends/my" : 'listMyFriends',
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
        searchFriend: function() {
            this.fixViews();
            console.log("SearchFriends!!!");
            activeView = new SearchFriendsView();
        },
        listMyFriends: function() {
            this.fixViews();
            console.log("ListMyFriends ey yo");
            activeView = new MyFriendsView();
        },
        fixViews: function() {
            if (!$.isEmptyObject(activeView)) {
                $('#game-container').remove();
                $('#slask-container').remove();
                $('#yakutia-main').append('<div id="game-container" class="container"></div>');
                $('#yakutia-main').append('<div id="slask-container" class="container"></div>');
            }
        }
    });

    return YakutiaRouter;
});