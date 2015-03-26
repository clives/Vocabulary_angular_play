/*
 * new vocabulaireApp.services, named: Vocabulaire
 * injected in app.js via: angular.module('movieApp',['ui.router',....,'vocabulaireApp.services']);
 * added to our controller (controllers.js) via: function($scope,$state,popupService,$window,..,Vocabulaire)
 */
angular.module('vocabulaireApp.services',[]).factory('Vocabulaire',function($resource){
    return $resource(window.serviceVocabulariesUrl+'/:id',{id:'@id'},{
        update: {
            method: 'PUT'
        }
    });
}).service('popupService',function($window){
    this.showPopup=function(message){
        return $window.confirm(message);
    }
}).service('uploadfile',function($upload,$window){
    this.upload=function(idaudiofile){
        return $window.confirm(idaudiofile);
    }
});

