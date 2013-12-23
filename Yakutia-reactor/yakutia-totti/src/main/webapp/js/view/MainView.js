define(['backbone', 'underscore', 'text!templates/Menu.html'],
function(Backbone, _, MenuTemplate) {

    var MenuView = Backbone.View.extend({

        el: "#main-menu",

        initialize: function() {
            this.template = _.template(MenuTemplate);
            this.render();
        },
        render: function() {
            this.$el.html(this.template);
            return this;
        },

    });
    return MenuView;
});