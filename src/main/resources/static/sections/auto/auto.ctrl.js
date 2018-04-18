'use strict';
angular.module('app').controller('AutoController', function ($scope, $rootScope, PageValues, data,autoService,$location,$route) {
    var vm = this;
    $rootScope.pageTab = 'search';
    vm.title = 'Create';
    vm.heading = 'Create user';
    vm.list = [];

    if (data && data.code === 100 && data.result) {
        vm.list = data.result;
    }

    vm.deleteClick = function (id) {
        panel_refresh($(".panel_to_refresh"), "shown");
        bootbox.confirm("Are You Sure You want to delete ?", function (flag) {
            if (flag) {
                autoService.deleteEntry(id, function (result) {
                    if (result && result.code == 100) {
                        PageValues.msg = {msg: 'Auto entry Deleted Successfully.', type: 'success'};
                        $route.reload();
                    } else {
                        $.notify("Unable Delete Auto,Please try again.", "error");
                    }
                });
            }
            panel_refresh($(".panel_to_refresh"), "hidden");
        });
    };
});

angular.module('app').controller('AutoAddController', function ($scope, $rootScope, PageValues,autoService,$location) {
    var vm = this;
    $rootScope.pageTab = 'auto';
    vm.title = 'Create';
    vm.heading = 'Create Auto entry';
    vm.data={};
    vm.submit = function (e) {
        if ($("#autocreateform")[0].checkValidity()) {
                panel_refresh($(".panel_to_refresh"), "shown");
                console.log(vm.data);
                autoService.createEntry(vm.data, function (res) {
                    if (res && res.code === 100) {
                        PageValues.msg = {msg: 'New Entry Created Successfully.', type: 'success'};
                        $location.path('/auto').replace();
                    } else {
                        $.notify("Unable create recourd,Please try again.", "error");
                    }
                    panel_refresh($(".panel_to_refresh"), "hidden");
                });
        }
    };

});

angular.module('app').controller('AutoEditController', function ($scope, $rootScope, PageValues,autoService,$location,data) {
    var vm = this;
    $rootScope.pageTab = 'auto';
    vm.title = 'Update';
    vm.heading = 'Update Auto entry';
    vm.data={};
    if (data && data.code === 100 && data.result) {
        vm.data = data.result;
    }

    vm.submit = function (e) {
        if ($("#autocreateform")[0].checkValidity()) {
            panel_refresh($(".panel_to_refresh"), "shown");
            autoService.updateEntry(vm.data,vm.data.id, function (res) {
                if (res && res.code === 100) {
                    PageValues.msg = {msg: ' Entry Updated Successfully.', type: 'success'};
                    $location.path('/auto').replace();
                } else {
                    $.notify("Unable update recourd,Please try again.", "error");
                }
                panel_refresh($(".panel_to_refresh"), "hidden");
            });
        }
    };

});

