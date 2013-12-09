define([
'backbone', 'underscore', 'text!templates/createGame.html'],
function(Backbone, _, CreateGameTemplate) {

    var GameTestModel = Backbone.Model.extend({
        url: '/game/create',

        defaults: {
            createdByPlayerId: null,
            gameName: ''
        },
    });

    var CreateGameView = Backbone.View.extend({

        el: "#game-container",

        events: {
            "keyup #gameName" : "handleKeyUp",
            "click #createNewGame" : "createNewGame"
        },

        initialize: function() {
            this.template = _.template(CreateGameTemplate);
            this.model = new GameTestModel();
            this.render();
        },
        render: function() {
            this.$el.html(this.template(this.model.attributes));
            return this;
        },
        handleKeyUp: function() {
            var textInput = $('#gameName').val();
            if(_.isNull(textInput) || _.isEmpty(textInput)) {
                $('#createNewGame').attr('disabled', 'disabled');
            } else {
                $('#createNewGame').removeAttr("disabled");
            }
        },
        createNewGame: function() {
            this.model.set('gameName', $('#gameName').val());
            this.model.set('createdByPlayerId', window.playerId);   // TODO remove this playerId dependency
            this.model.save();
        }

    });
    return CreateGameView;
});