define(['backbone', 'underscore', 'text!templates/gameMap.html','view/yakutia-view'],
function(Backbone, _, GameMapTemplate, YakutiaView) {

    var ActiveGameView = Backbone.View.extend({

        el: "#game-container",

        events: {
            'click #placeUnitButton': 'openYakutiaStuff'
        },

        initialize: function() {
            this.template = _.template(GameMapTemplate);
            this.model = Backbone.Model.extend({});
            this.render();
        },
        render: function() {
            this.$el.html(this.template(this.model.attributes));
            return this;
        },
        openYakutiaStuff: function() {
            console.log("Clicking");
            new YakutiaView();
        }

    });
    return ActiveGameView;
});