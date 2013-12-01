define(['backbone', 'underscore', 'text!templates/menu.html'], function(Backbone, _, MenuTemplate) {

    var MenuModel = Backbone.Model.extend({
        defaults: {
            test: "bepan"
        }
    });

    var MenuView = Backbone.View.extend({

        el: "#main-menu",

        initialize: function() {
            this.template = _.template(MenuTemplate);
            this.model = new MenuModel();
            this.render();
        },
        render: function() {
            this.$el.html(this.template(this.model.attributes));
            return this;
        },

    });
    return MenuView;
});