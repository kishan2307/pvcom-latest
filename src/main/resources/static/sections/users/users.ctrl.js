'use strict';


angular.module('app').controller('UserController', function ($scope, $rootScope, users, PageValues, userService, $compile, $route) {
    $rootScope.pageTab = 'user';
    var vm = this;
    vm.title='Users';
    vm.heading='User List';
    // setTimeout(function () {
    //     $compile(angular.element($('#user-list-tbl tbody')))($scope);
    // }, 2000)

    vm.init = function () {
        if (users && users.result && users.code == 100) {
            angular.forEach(users.result, function (value, key) {
                if (value && value.id) {
                    value.editbtn = '<a href="#/user/edit/' + value.id + '" class="btn btn-default active">Edit</a>';
                    value.deletebtn = '<button type="button" ng-click="user.deleteUser(' + value.id + ')" class="btn btn-danger active">Delete</button>';
                    value.role = (value.role == 'guest' ? 'Triage/Entry User' : value.role + " User").toUpperCase();
                }
            });
            $('#user-list-tbl').dataTable({
                "aaData": users.result,
                "aoColumns": [
                    {"mDataProp": "firstName"},
                    {"mDataProp": "lastName"},
                    {"mDataProp": "dateOfBirth"},
                    {"mDataProp": "country"},
                    {"mDataProp": "mobile"},
                    {"mDataProp": "email"},
                    {"mDataProp": "role"},
                    {"mDataProp": "editbtn"},
                    {"mDataProp": "deletebtn"}
                ],
                "initComplete": function(settings, json) {
                    $compile(angular.element($('#user-list-tbl tbody')))($scope);
                }
            });
        }
    }

    vm.deleteUser = function (id) {
        bootbox.confirm("Are You Sure You want to delete ?", function (result) {
            if (result) {
                userService.deleteUser(id, function (result) {
                    if (result && result.code === 100) {
                        $route.reload();
                        $.notify("User Deleted Successfully.", "success");
                    } else {
                        alert("Unable to delete User , Please try again.")
                    }
                });
            }
        });
    }
});

angular.module('app').controller('createUserController', function ($scope, $rootScope, PageValues, $route, userService, $location) {
    var vm = this;
    $rootScope.pageTab = 'user';
    vm.title='Create';
    vm.heading='Create user';
    vm.data = {};
    vm.countrys = PageValues.countries;
    PageValues.pageTab = 'user';
    vm.submit = function (e) {
        if ($(e.target).closest('form')[0].checkValidity() && vm.data.password == vm.data.cpassword) {
            panel_refresh($(".panel_to_refresh"), "shown");
            userService.createUser(vm.data, function (res) {
                if (res && res.code === 100) {
                    $location.path('/users').replace();
                    PageValues.msg = {msg: 'User Created Successfully.', type: 'success'};
                } else {
                    $.notify("Unable to create User Please try again.", "error");
                }
                panel_refresh($(".panel_to_refresh"), "hidden");
            });
        }
    }
});

angular.module('app').controller('updateUserController', function ($scope, $rootScope, user, PageValues, userService, $location) {
    var vm = this;
    $rootScope.pageTab = 'user';
    vm.title='Edit';
    vm.heading='Edit user';
    vm.data = {};
    vm.countrys = PageValues.countries;
    if (user && user.code === 100 && user.result) {
        vm.data = user.result;
    }

    vm.submit = function (e) {
        if ($(e.target).closest('form')[0].checkValidity() && vm.data.password == vm.data.cpassword) {
            panel_refresh($(".panel_to_refresh"), "shown");
            userService.updateUser(vm.data, function (res) {
                if (res && res.code === 100) {
                    $location.path('/users').replace();
                    PageValues.msg = {msg: 'User Details Updated Successfully.', type: 'success'};
                } else {
                    $.notify("Unable to update User details,Please try again.", "error");
                }
                panel_refresh($(".panel_to_refresh"), "hidden");
            });
        }
    }
});