angular.module('vocabularyApp',['ui.router','angularFileUpload','ngResource','vocabularyApp.controllers','vocabulaireApp.services']);



angular.module('vocabularyApp').config(function($stateProvider,$httpProvider){
    $stateProvider.state('vocabularies',{
        url:'/vocabularies',
        templateUrl:'partials/vocFRBR.html',
        controller:'VocabularyListController'
    }).state('viewVocabulary',{
        url:'/vocabularies/:id/view',
        templateUrl:'partials/vocFRBR-view.html',
        controller:'VocabularyViewController'
     }).state('newVocabulary',{
        url:'/vocabuleries/new',
        templateUrl:'partials/vocFRBR-add.html',
        controller:'VocabuleryCreateController'
    }).state('editVocabulary',{
        url:'/vocabularies/:id/edit',
        templateUrl:'partials/vocFRBR-edit.html',
        controller:'VocabularyEditController'
    });
}).run(function($state){
   $state.go('vocabularies');
});