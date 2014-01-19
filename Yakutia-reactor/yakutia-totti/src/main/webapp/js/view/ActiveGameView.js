define(['backbone', 'underscore', 'text!templates/gameMap.html','view/YakutiaMapUberView'],
function(Backbone, _, GameMapTemplate, YakutiaMapUberView) {

    var ActiveGameView = Backbone.View.extend({

        el: "#game-container",

        initialize: function() {
            this.template = _.template(GameMapTemplate);
            this.model = Backbone.Model.extend({});
            this.render();
            new YakutiaMapUberView();                   // TODO where to attach the view?
        },
        render: function() {
            this.$el.html(this.template(this.model.attributes));
            return this;
        }

    });
    return ActiveGameView;
});