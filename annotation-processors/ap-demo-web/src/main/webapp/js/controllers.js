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

    $scope.salarioMensal=1200.00;
    $scope.diasTrabalhados=28;
    $scope.horasExtras=4;
    
    get($http, 'javascript', 'folhaPagamento', function(data) {
        $scope.code = data;
    });

    $scope.runClient = function(data) {
        $scope.resultClient = eval($scope.clientCode);
    };

    $scope.save = function(data) {
        save($http, data, 'javascript', 'folhaPagamento', function() {
            run($http, 'javascript', 'folhaPagamento', $scope.salarioMensal, $scope.diasTrabalhados, $scope.horasExtras, function(data) {
                $scope.result = data;
            });
        });
    };
}

function codeEditorExampleCtrl($scope, $http) {
    
    $scope.salarioMensal=1200.00;
    $scope.diasTrabalhados=28;
    $scope.horasExtras=4;
    
    get($http, 'groovy', 'folhaPagamento', function(data) {
        $scope.codeGroovy = data;
    });

    get($http, 'javascript', 'folhaPagamento', function(data) {
        $scope.codeJavaScript = data;
    });

    get($http, 'python', 'folhaPagamento', function(data) {
        $scope.codePython = data;
    });

    $scope.save = function(data, engine) {
        save($http, data, engine, 'folhaPagamento');
    };
    
    $scope.run = function(engine) {
        run($http, engine, 'folhaPagamento', $scope.salarioMensal, $scope.diasTrabalhados, $scope.horasExtras, function (data){
            $scope[engine + 'Result'] = data;
        });
    };
}

function run(http, engine, name, salarioMensal, diasTrabalhados, horasExtras, successCallback) {
    http({
        method: 'PUT',
        url: '/annotation-processors-demo-web/ScriptServlet?engine=' + engine +'&name=' + name + '&salarioMensal=' + salarioMensal + '&diasTrabalhados=' + diasTrabalhados + '&horasExtras=' + horasExtras
    }).success(successCallback);
}

function get(http, engine, name, successCallback) {
    http({
        method: 'GET',
        url: '/annotation-processors-demo-web/ScriptServlet?engine=' + engine +'&name=' + name
    }).success(successCallback);
}

function save(http, data, engine, name, successCallback) {
    http({
        method: 'POST',
        url: '/annotation-processors-demo-web/ScriptServlet?engine=' + engine +'&name=' + name,
        data: data
    }).success(successCallback);
}