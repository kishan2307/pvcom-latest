'use strict';

/*
 * Contains a service to communicate with the TRACK TV API
 */
angular
    .module('app.services')
    .factory('userService', userservice);

function userservice($http, $log, commonServices) {
    var data = {
        'getUsers': getUsers,
        'getUser': getUser,
        'deleteUser': deleteUser,
        'updateUser': updateUser,
        'createUser': createUser,
        'getUserList': getUserList
    };

    var list = null;

    var USER_RESOUREC_URL = "user/resources/";

    function getUsers() {
        return commonServices.makeRequest(USER_RESOUREC_URL, 'GET');
    }

    function updateUser(data, callback) {
        if (data && data.id) {
            commonServices.makeRequest(USER_RESOUREC_URL + data.id, 'POST', data, callback);
        } else {
            callback(null);
        }
    }

    function createUser(data, callback) {
        if (data && data.email) {
            commonServices.makeRequest(USER_RESOUREC_URL, 'PUT', data, callback);
        } else {
            callback(null);
        }
    }

    function getUser(id) {
        return commonServices.makeRequest(USER_RESOUREC_URL + id, 'GET');
    }

    function deleteUser(id, callback) {
        return commonServices.makeRequest(USER_RESOUREC_URL + id, 'DELETE', null, callback);
    }

    function getUserList() {
        return commonServices.makeRequest(USER_RESOUREC_URL + 'dropdownList', 'GET');
    }

    return data;
}