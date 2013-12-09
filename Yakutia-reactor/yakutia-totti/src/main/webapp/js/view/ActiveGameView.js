define(['backbone', 'underscore', 'text!templates/gameMap.html'],
function(Backbone, _, GameMapTemplate) {

    var ActiveGameView = Backbone.View.extend({

        el: "#game-container",

        initialize: function() {
            this.template = _.template(GameMapTemplate);
            this.model = Backbone.Model.extend({});
            this.render();
        },
        render: function() {
            this.$el.html(this.template(this.model.attributes));
            return this;
        },

    });
    return ActiveGameView;
});