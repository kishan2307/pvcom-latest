'use strict';

/*
 * Contains a service to communicate with the TRACK TV API
 */
angular
    .module('app.services')
    .factory('worklowService', worklowServices);

function worklowServices($http, API_KEY, BASE_URL, $log, commonServices) {
    var data = {
        'getList': getList,
        'getDetails': getDetails,
        'getWorkflowById': getWorkflowById,
        'getWorkflowDashBoardData': getWorkflowDashBoardData,
        'addWorkflow': addWorkflow,
        'UpdateWorkflow': UpdateWorkflow,
        'getUserHomeData': getUserHomeData,
        'searchByName': searchcasesAndEntries,
        'deleteCase': deleteCase,
        'search': search,
        'assign':assign,
        'report':report
    };

    var FLOW_URL = "flow";

    function report(data,callback) {
        $.fileDownload("export/report", {
            httpMethod: 'POST',
            data: {data:JSON.stringify(data)},
            successCallback: function (url) {
                alert("success");
            },
            failCallback: function (html, url) {
                alert("fail");
            }
        });
    }

    function searchcasesAndEntries(name) {
        return commonServices.makeRequest('flow/search/' + name, 'GET');
    }

    function search(data, calback) {
        commonServices.makeRequest('flow/filter', 'POST', data, calback);
    }

    function assign(data, calback) {
        commonServices.makeRequest('flow/assign', 'POST', data, calback);
    }

    function getList(listName) {
        return commonServices.makeRequest('flow/list/' + listName, 'GET');
    }

    function getDetails(list) {
        console.log("listname", list);
        return commonServices.makeRequest('flow.json', []);
    }

    function getWorkflowById(id) {
        return commonServices.makeRequest(FLOW_URL + '/' + id, 'GET');
    }

    function deleteCase(id, callback) {
        commonServices.makeRequest(FLOW_URL + '/' + id, 'DELETE',null, callback);
    }

    function getWorkflowDashBoardData() {
        return commonServices.makeRequest(FLOW_URL + "/home", "GET");
    }

    function addWorkflow(data, callback) {
        commonServices.makeRequest(FLOW_URL, "PUT", data, callback);
    }

    function UpdateWorkflow(data, callback) {
        commonServices.makeRequest(FLOW_URL + "/" + data.id, "POST", data, callback);
    }

    function getUserHomeData() {
        return commonServices.makeRequest("resource/home/user", "GET");
    }

    return data;
}