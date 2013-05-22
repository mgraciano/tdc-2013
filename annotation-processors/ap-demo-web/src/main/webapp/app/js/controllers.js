/*
 * Copyright (c) 2013, Klaus Boeing & Michel Graciano.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the project's authors nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND/OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
'use strict';

/* Controllers */

function repositoryExampleCtrl($scope, $http) {

    get($http, 'js', function(data) {
        $scope.code = data;
    });

    $scope.save = function(data) {
        save($http, data, 'js', function() {
            run($http, 'js', function(data) {
                $scope.result = data;
            });
        });
    };
}

function codeEditorExampleCtrl($scope, $http) {
    get($http, 'groovy', function(data) {
        $scope.codeGroovy = data;
    });

    get($http, 'javaScript', function(data) {
        $scope.codeJavaScript = data;
    });

    get($http, 'python', function(data) {
        $scope.codePython = data;
    });

    $scope.save = function(data, script) {
        save($http, data, script);
    };
    
    $scope.run = function(type) {
        run($http, type, function (data){
            $scope[type + 'Result'] = data;
        });
    };
}

function run(http, scriptType, successCallback) {
    http({
        method: 'PUT',
        url: '/annotation-processors-demo-web/ScriptServlet?script=' + scriptType
    }).success(successCallback);
}

function get(http, scriptType, successCallback) {
    http({
        method: 'GET',
        url: '/annotation-processors-demo-web/ScriptServlet?script=' + scriptType
    }).success(successCallback);
}

function save(http, data, script, successCallback) {
    http({
        method: 'POST',
        url: '/annotation-processors-demo-web/ScriptServlet?script=' + script,
        data: data
    }).success(successCallback);
}