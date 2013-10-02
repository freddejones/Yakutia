$('.yakimg').click(function (e) {
    var offset = $(this).offset();
    var left = e.pageX;
    var top = e.pageY;
    var theHeight = $('.luls').height();
    $('.luls').show();
    $('.luls').css('left', (left+10) + 'px');
    $('.luls').css('top', (top-(theHeight/2)-10) + 'px');
});