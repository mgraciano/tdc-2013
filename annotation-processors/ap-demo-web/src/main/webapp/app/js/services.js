'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('myApp.services', ['ngResource']).
    factory('Script',['$resource', function($resource){
        return $resource('/annotation-processors-demo-web/ScriptServlet');
    }]);
