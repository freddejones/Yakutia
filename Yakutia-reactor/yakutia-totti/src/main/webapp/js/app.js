define([
    'view/yakutia-view',
    'lib/bootstrap',
    'view/main-view'],
    function(
    Yakutia, Bootstrap, MenuView) {

    new MenuView();
    var stuff = new Yakutia();

    $('#placeUnitButton').click(function() {
        $('#placeUnitButton')
            .prop('disabled', 'disabled')
            .removeClass('btn-primary');
        $('#attackButton').removeAttr('disabled').addClass('btn-primary');
    });

    $('#attackButton').click(function() {
        $('#attackButton')
            .prop('disabled', 'disabled')
            .removeClass('btn-primary');
        $('#moveUnitButton').removeAttr('disabled').addClass('btn-primary');
    });

    $('#moveUnitButton').click(function() {
        $('#moveUnitButton')
            .prop('disabled', 'disabled')
            .removeClass('btn-primary');
        $('#myModal').modal({
            backdrop: 'static'
        }).show();
    });
});
