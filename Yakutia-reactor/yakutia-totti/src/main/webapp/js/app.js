define([
    'router',
    'lib/bootstrap',
    'view/main-view'],
    function(YakutiaRouter, Bootstrap, MenuView) {

    window.playerId = 1;
    var route = new YakutiaRouter();
    Backbone.history.start();
    new MenuView();
});
