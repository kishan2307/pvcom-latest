'use strict';

/*
 * Contains a service to communicate with the TRACK TV API
 */
angular
    .module('app.services')
    .factory('autoService', autoService);

function autoService($http, $log, commonServices) {
    var data = {
        'getEntryById': getEntryById,
        'createEntry': createEntry,
        'updateEntry': updateEntry,
        'deleteEntry': deleteEntry,
        'getEntrysList': getEntrysList
    };

    function updateEntry(data, id, callback) {
        commonServices.makeRequest("api/auto/" + id, "POST", data, callback);
    }

    function createEntry(data, callback) {
        commonServices.makeRequest("api/auto", "PUT", data, callback);
    }

    function deleteEntry(id, callback) {
        commonServices.makeRequest("api/auto/" + id, "DELETE", null, callback);
    }

    function getEntrysList() {
        return commonServices.makeRequest("api/auto", "GET");
    }

    function getEntryById(id) {
        return commonServices.makeRequest("api/auto/" + id, "GET");
    }

    return data;
}