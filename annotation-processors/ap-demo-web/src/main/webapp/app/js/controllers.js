'use strict';

/* Controllers */
  
function codeCtrl($scope, $http) {
    
    $http({
        method: 'GET', 
        url: '/annotation-processors-demo-web/ScriptServlet?script=groovy'
    }).
    success(function(data) {
        $scope.codeGroovy = data;
    });
    
    $http({
        method: 'GET', 
        url: '/annotation-processors-demo-web/ScriptServlet?script=javaScript'
    }).
    success(function(data) {
        $scope.codeJavaScript = data;
    });
    
    $http({
        method: 'GET', 
        url: '/annotation-processors-demo-web/ScriptServlet?script=python'
    }).
    success(function(data) {
        $scope.codePython = data;
    });
    
    $scope.save = function(data, script){
        $http({
            method: 'POST', 
            url: '/annotation-processors-demo-web/ScriptServlet?script=' + script,
            data: data
        });
    }
}