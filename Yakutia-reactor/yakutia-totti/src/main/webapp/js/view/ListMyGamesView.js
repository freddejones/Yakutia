define([
'backbone', 'underscore', 'text!templates/ListMyGames.html'],
function(Backbone, _, ListMyGamesTmpl, options) {

    var Game = Backbone.Model.extend({
        defaults: {
            name: "",
            status: "",
            date: ""
        }
    });

    var GamesCollection = Backbone.Collection.extend({
        model: Game,
        parse: function(response){

            for (var i=0; i<response.length; i++) {
                this.push(new  Game({
                    name: response[i].name,
                    status: response[i].status,
                    date: response[i].date
                }));
            }
            return this.models;
        }
    });

    var gamesCollection = new GamesCollection();

    var ListMyGamesView = Backbone.View.extend({

        el: "#game-container",

        initialize: function() {
            this.template = _.template(ListMyGamesTmpl);
            this.model = Backbone.Model.extend({});
            var self = this;
            gamesCollection = new GamesCollection();
            gamesCollection.fetch({
                url: '/game/get/'+window.playerId
            }).complete(function() {
                self.render();
            });

        },
        render: function() {
            this.$el.html(this.template(this.model.attributes));
            gamesCollection.each( function(gameObject) {
                if (gameObject.get('status') === 'ONGOING') {
                    $("#activeGameTable").append('<tr><td>'+gameObject.get('name')+'</td>'
                        +'<td>'+gameObject.get('status')+'</td>'
                        +'<td>'+gameObject.get('date')+'</td>'
                        +'<td>'+'A button here'+'</td>'
                        +'</tr>');
                } else {
                    $("#nonActiveGameTable").append('<tr><td>'+gameObject.get('name')+'</td>'
                        +'<td>'+gameObject.get('status')+'</td>'
                        +'<td>'+gameObject.get('date')+'</td>'
                        +'<td>NO BUTTON HERE</td>'
                        +'</tr>');
                }
            });
            return this;
        }
    });
    return ListMyGamesView;
});