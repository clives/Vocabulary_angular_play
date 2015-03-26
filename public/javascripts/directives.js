// widget sound player using howl.
// use parameters audiofileurl to display or not the botton 
// and to create the player
// how to pass parameters: http://jsfiddle.net/joshdmiller/FHVD9/ 
angular.module('vocabularyApp').directive('audioplayer', function () {
	return {
	    restrict: 'E',
	    scope: { idaudiofile: '=idaudiofile' },
	    templateUrl: '/partials/vocFRBR_audioComponent.html',
	    controller: function ($scope, $http, $attrs) {
	    	$scope.$watch(function() {
	      	  return $scope.idaudiofile;
	      	}, function(newValue, oldValue) {
	      		console.log("update id audio file: new value:"+newValue);
	      	  if( newValue )
	      		  $scope.player=new Howl({ urls: [window.urlaudioFile(newValue)],loop: false});
	      	});
	    	
	        $scope.playVocabulary=function(vocabulary){
	        	$scope.player.play();        
	        }
	        
	        $scope.stopVocabulary=function(vocabulary){
	            $scope.player.stop();        
	        }
	        
	        $scope.audiofiledefined=function(){return angular.isDefined($scope.idaudiofile)};
	        
	    }
	}
	});