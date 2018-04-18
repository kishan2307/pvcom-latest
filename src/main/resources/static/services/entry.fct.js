'use strict';

/*
 * Contains a service to communicate with the TRACK TV API
 */
angular
        .module('app.services')
        .factory('entryservice', entryservice);

function entryservice($http, $log, commonServices) {
    var data = {        
        'getEntryById': getEntryById,
        'createEntry': createEntry,
        'updateEntry': updateEntry,
        'getlocalReferenceNumber': getlocalReferenceNumber,
        'getNewEntrysList': getNewEntrysList,
        'deleteEntry': deleteEntry,
        'getUniqEntrysList': getUniqEntrysList,
        'getDupEntrysList': getDupEntrysList,
        'getEntrysList':getEntrysList
    };   

    function updateEntry(data, id, callback) {
        commonServices.makeRequest("entry/" + id, "POST", data,callback);
    }
    function createEntry(data, callback) {
        commonServices.makeRequest("entry", "PUT", data,callback);
    }
    function deleteEntry(id, callback) {
        commonServices.makeRequest("entry/" + id, "DELETE", null,callback);
    }

    function getlocalReferenceNumber(callback) {
        commonServices.makeRequest("entry/localRefs", "GET", null, callback);
    }

    function getNewEntrysList() {
        return commonServices.makeRequest("entry", "GET");
    }

    function getUniqEntrysList() {
        return commonServices.makeRequest("entry/uniq", "GET");
    }

    function getDupEntrysList() {
        return commonServices.makeRequest("entry/dup", "GET");
    }

    function getEntrysList(type) {
        return commonServices.makeRequest("entry/list/"+type, "GET");
    }

    function getEntryById(id) {
        return commonServices.makeRequest("entry/" + id, "GET");
    }

    return data;
}