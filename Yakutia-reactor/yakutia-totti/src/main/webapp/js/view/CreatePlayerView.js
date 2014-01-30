define([
'backbone', 'underscore', 'text!templates/CreatePlayerTemplate.html', 'kinetic'],
function(Backbone, _, CreatePlayerTemplate, Kinetic) {

    var CreatePlayerModel = Backbone.Model.extend({
        url: '/player/create',

        defaults: {
            playerName: null,
            email: null
        },
    });

    var CreatePlayerView = Backbone.View.extend({

        el: "#slask-container",

        events: {
            "keyup #gameName" : "handleKeyUp",
            "click #createPlayerButton" : "createNewPlayer",
            "click #createAScenario" : "createScenario"
        },

        initialize: function() {
            this.template = _.template(CreatePlayerTemplate);
            this.model = new CreatePlayerModel();
            this.render();
        },
        render: function() {
            this.$el.html(this.template(this.model.attributes));
            var stage = new Kinetic.Stage({
                container: 'gamemap',
                width: 800,
                height: 600
            });

            var layer = new Kinetic.Layer();

            var pathExample = new Kinetic.Path({
                x: -150,
                y: -50,
                data: 'd="M100,100 L150,100 L150,150 Z"',
                fill: 'green',
                scale: {x:2, y:2}
            });

            var tooltip = new Kinetic.Label({
                x: 20, //pathExample.getX()+pathExample.getWidth()/2,
                y: 50, //pathExample.getY()+pathExample.getHeight()/2,
                opacity: 0.75
            });

            tooltip.add(new Kinetic.Tag({
                fill: 'black',
                pointerDirection: 'down',
                pointerWidth: 10,
                pointerHeight: 10,
                lineJoin: 'round',
                shadowColor: 'black',
                shadowBlur: 10,
                shadowOffset: {x:10,y:20},
                shadowOpacity: 0.5
            }));

            tooltip.add(new Kinetic.Text({
                name: 'tomte',
                text: '12 units',
                fontFamily: 'Calibri',
                fontSize: 12,
                padding: 3,
                fill: 'white'
            }));

//            tooltip.draw();
            layer.add(pathExample);
            stage.add(layer);

            pathExample.on('mouseover', function() {
                this.setFill('#111');
                this.draw();
                layer.add(tooltip);
            });

            pathExample.on('mouseout', function() {
                this.setFill('green');
                this.draw();
            });

            pathExample.on('click', function() {
                console.log('adding unit');
            });

            return this;
        },
        createNewPlayer: function() {
            this.model.set('playerName', $('#playerName').val());
            this.model.set('email', $('#email').val());
            this.model.save(null, {
                success: function (model, response) {
                    console.log(response);
                    window.playerId=response;
                }
            });
        }
    });
    return CreatePlayerView;
});