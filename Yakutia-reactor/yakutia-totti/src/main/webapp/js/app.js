define([
    'router',
    'view/yakutia-view',
    'lib/bootstrap',
    'view/main-view'],
    function(YakutiaRouter, Yakutia, Bootstrap, MenuView) {

    window.playerId = 66;
    var route = new YakutiaRouter();
    Backbone.history.start();
    new MenuView();


//    var stuff = new Yakutia();
//
//    $('#placeUnitButton').click(function() {
//        $('#placeUnitButton')
//            .prop('disabled', 'disabled')
//            .removeClass('btn-primary');
//        $('#attackButton').removeAttr('disabled').addClass('btn-primary');
//    });
//
//    $('#attackButton').click(function() {
//        $('#attackButton')
//            .prop('disabled', 'disabled')
//            .removeClass('btn-primary');
//        $('#moveUnitButton').removeAttr('disabled').addClass('btn-primary');
//    });
//
//    $('#moveUnitButton').click(function() {
//        $('#moveUnitButton')
//            .prop('disabled', 'disabled')
//            .removeClass('btn-primary');
//        $('#myModal').modal({
//            backdrop: 'static'
//        }).show();
//    });
//
//    var TestModel = Backbone.Model.extend({
//        url: '/player/create',
//
//        defaults: {
//            playerName: "soffan",
//            email: "fidde@freddejones.se"
//        },
//    });
//
//    $('#testingstuff').click(function() {
//        var testmodel = new TestModel();
//        testmodel.save();
//    });
//
//
});
