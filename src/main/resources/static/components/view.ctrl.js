'use strict';
angular
    .module('app')
    .controller('ViewController', function ($scope, PageValues) {
        //Setup the view model object
        var vm = this;
        vm.data = PageValues;
        PageValues.userid = $("#profile").data('id');
        PageValues.email = $("#profile").data('email');
        PageValues.role = $("#profile").data('role');

        vm.getCommentAccess=function (role) {
            if(PageValues.role===role){
                    return false;
            }
            return true;
        };
    });