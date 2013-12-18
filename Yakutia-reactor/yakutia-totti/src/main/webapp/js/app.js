define([
    'router',
    'bootstrap',
    'view/main-view'],
    function(YakutiaRouter,
    Bootstrap,
    MenuView) {

    window.playerId = 1;
    var route = new YakutiaRouter();
    window.router = route;
    Backbone.history.start();
    new MenuView();
});
