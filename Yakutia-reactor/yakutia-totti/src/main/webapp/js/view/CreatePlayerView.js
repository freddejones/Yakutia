define([
'backbone', 'underscore', 'text!templates/CreatePlayerTemplate.html'],
function(Backbone, _, CreatePlayerTemplate) {

    var CreatePlayerModel = Backbone.Model.extend({
        url: '/player/create',

        defaults: {
            playerName: null,
            email: null
        },
    });

    var CreatePlayerView = Backbone.View.extend({

        el: "#player-container",

        events: {
            "keyup #gameName" : "handleKeyUp",
            "click #createPlayerButton" : "createNewPlayer"
        },

        initialize: function() {
            this.template = _.template(CreatePlayerTemplate);
            this.model = new CreatePlayerModel();
            this.render();
        },
        render: function() {
            this.$el.html(this.template(this.model.attributes));
            return this;
        },
//        handleKeyUp: function() {
////            var textInput = $('#gameName').val();
////            if(_.isNull(textInput) || _.isEmpty(textInput)) {
////                $('#createNewGame').attr('disabled', 'disabled');
////            } else {
////                $('#createNewGame').removeAttr("disabled");
////            }
//        },
        createNewPlayer: function() {
            this.model.set('playerName', $('#playerName').val());
            this.model.set('email', $('#email').val());
            this.model.save();
        }

    });
    return CreatePlayerView;
});