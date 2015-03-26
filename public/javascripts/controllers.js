angular.module('vocabularyApp.controllers',[]).controller('VocabularyListController',function($scope,$state,popupService,$window,Vocabulaire){

    $scope.vocabulaires=Vocabulaire.query();
  
    $scope.deleteVocabulary=function(vocabulary){
        if(popupService.showPopup('Really delete this?')){
        	vocabulary.$delete(function(){
                $window.location.href='';
            });
        }
    }

}).controller('VocabularyViewController',function($scope,$stateParams,Vocabulaire){

    $scope.vocabulary=Vocabulaire.get({id:$stateParams.id});

    $scope.audioId=$scope.vocabulary.audioId;
    
    //update when we have the object,as $scope.vocabulary is a promise
    $scope.$watch(function() {
    	  return $scope.vocabulary.audioId;
    	}, function(newValue, oldValue) {
    		console.log("VocabularyViewController new value:"+newValue);
    		 $scope.audioId=newValue;
    	});

}).controller('VocabuleryCreateController',function($scope,$state,$http,$stateParams,$upload,Vocabulaire){

	console.log("create control");
    $scope.vocabulary=new Vocabulaire();

    $scope.addVocabulary=function(){
        $scope.vocabulary.$save(function(){
            $state.go('vocabularies');
        });
    }      
    
    $scope.upload=function(files){
    	
    	if (files && files.length) {

            for (var i = 0; i < files.length; i++) {
                var file = files[i];
                $upload.upload({
                    url: 'upload',
                    fields: {'audioId': $scope.vocabulary.audioId},
                    file: file
                }).progress(function (evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    console.log('progress: ' + progressPercentage + '% ' + evt.config.file.name);
                }).success(function (data, status, headers, config) {
                    console.log('file ' + config.file.name + 'uploaded. Response: ' + data);                   
                    console.log("our url"+data.url);
                    
                    $scope.vocabulary.audioId=data.audioId;
                    $scope.audioId=data.audioId;
                });
            }
        }
    }
    
    
}).controller('VocabularyEditController',function($scope,$state,$stateParams,Vocabulaire,uploadfile,$upload){

	//alreay submitted => error at the first time
	$scope.submitted=true;
	
    $scope.updateVocabulary=function(){
        $scope.vocabulary.$update(function(){
            $state.go('vocabularies');
        });
    };

    $scope.loadVocabulary=function(){
        $scope.vocabulary=Vocabulaire.get({id:$stateParams.id});
    };

    $scope.loadVocabulary();
    
    $scope.upload=function(files){
    	
    	if (files && files.length) {

            for (var i = 0; i < files.length; i++) {
                var file = files[i];
                $upload.upload({
                    url: 'upload',
                    fields: {'audioId': $scope.vocabulary.audioId},
                    file: file
                }).progress(function (evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    console.log('progress: ' + progressPercentage + '% ' + evt.config.file.name);
                }).success(function (data, status, headers, config) {
                    console.log('file ' + config.file.name + 'uploaded. Response: ' + data);                   
                    console.log("our url"+data.url);
                    
                    $scope.vocabulary.audioId=data.audioId;
                    $scope.audioId=data.audioId;
                });
            }
        }
    }
});