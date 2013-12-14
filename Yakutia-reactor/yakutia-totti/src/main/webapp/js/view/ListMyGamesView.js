define([
'backbone', 'underscore', 'text!templates/ListMyGames.html'],
function(Backbone, _, ListMyGamesTmpl, options) {

    var Game = Backbone.Model.extend({
        defaults: {
            id: 12,
            name: "",
            status: "",
            canStartGame: false,
            date: ""
        }
    });

    var GamesCollection = Backbone.Collection.extend({
        model: Game,
        parse: function(response){

            for (var i=0; i<response.length; i++) {
                this.push(new  Game({
                    id: response[i].id,
                    name: response[i].name,
                    status: response[i].status,
                    date: response[i].date,
                    canStartGame: response[i].canStartGames
                }));
            }
            return this.models;
        }
    });

    var gamesCollection = new GamesCollection();

    var ListMyGamesView = Backbone.View.extend({

        el: "#game-container",

        events: {
            'click .StartGame' : 'startGame'
        },

        initialize: function() {
            _.bindAll(this, 'render', 'startGame');
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
        startGame: function(e) {
            var clickedEl = $(e.currentTarget);
            var id = clickedEl.attr("value");
            console.log(JSON.stringify(gamesCollection.get(id)));
            console.log("starting game fo id: "+id);
            this.render();
        },
        render: function() {
            this.$el.html(this.template(this.model.attributes));
            console.log("render");
            gamesCollection.each( function(gameObject) {
                if (gameObject.get('status') === 'ONGOING') {
                    $("#activeGameTable").append('<tr><td>'+gameObject.get('name')+'</td>'
                        +'<td>'+gameObject.get('status')+'</td>'
                        +'<td>'+gameObject.get('date')+'</td>'
                        +'<td>'+'A button here'+'</td>'
                        +'</tr>');
                } else if (gameObject.get('canStartGame') === true) {
                    $("#nonActiveGameTable").append('<tr><td>'+gameObject.get('name')+'</td>'
                                            +'<td>'+gameObject.get('status')+'</td>'
                                            +'<td>'+gameObject.get('date')+'</td>'
                                            +'<td>'
                                            +'<button value="'+gameObject.get('id')+'" type="button" class="btn btn-primary StartGame">Start game</button>'
                                            +'</td>'
                                            +'</tr>');
                }
                else {
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