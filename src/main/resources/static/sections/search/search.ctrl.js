'use strict';
angular.module('app').controller('SearchController', function ($scope, $rootScope, PageValues, data) {
    var vm = this;
    $rootScope.pageTab = 'search';
    vm.title = 'Create';
    vm.heading = 'Create user';
    vm.cases = [];
    vm.entries = [];

    if (data && data.code === 100 && data.result) {
        vm.cases = data.result.cases;
        vm.entries = data.result.entries;
    }

});

angular.module('app').controller('FullSearchController', function ($scope, users, $rootScope, PageValues, worklowService) {
    var vm = this;
    $rootScope.pageTab = 'filter';

    vm.cases = null;
    vm.entries = null;

    vm.result = [];
    vm.submit = function () {
        panel_refresh($(".panel_to_refresh"), "shown");
        vm.result = [];
        worklowService.search(vm.data, function (res) {
            if (res && res.code === 100 && res.result) {
                vm.entries = res.result.entries && res.result.entries.length > 0 ? res.result.entries : null;
                vm.cases = res.result.cases && res.result.cases.length > 0 ? res.result.cases : null;
                $.notify("success, search completed.", "success");
            } else {
                $.notify("Unable to search,please provide valid input", "error");
            }
            panel_refresh($(".panel_to_refresh"), "hidden");
        });
    }

    vm.assign = function (type) {
        vm.assigntype=type;
        $('#mymodel').modal();
    }

    vm.exportcases = function () {
        var exData={cases:[],entries:[]};
        angular.forEach(vm.cases,function (v,i) {
            exData.cases.push(v.id);
        });
        worklowService.report(exData);
    }

    vm.exportEntries = function () {
        var exData={cases:[],entries:[]};
        angular.forEach(vm.entries,function (v,i) {
            exData.entries.push(v.id);
        });
        worklowService.report(exData);
    }

    vm.assignd = {}
    vm.assignSubmit = function () {
        if (vm.assignd!=null) {
            vm.assignd.type=vm.assigntype;
            vm.assignd.ids = [];
            vm.user=PageValues.userid;
            if(vm.assignd.type==='entry') {
                angular.forEach(vm.entries, function (v, i) {
                    if (v.select == true) {
                        vm.assignd.ids.push(v.id);
                    }
                });
            }else if(vm.assignd.type==='cases'){
                angular.forEach(vm.cases, function (v, i) {
                    if (v.select == true) {
                        vm.assignd.ids.push(v.id);
                    }
                });
            }
            worklowService.assign(vm.assignd, function (res) {
                console.log('assign res',res);
                if(res && res.code==100){
                    vm.submit();
                    $('#mymodel').modal('hide')
                    $.notify("Success, assigned successfully.", "success");
                }else{
                    $('#mymodel').modal('hide')
                    $.notify("Error, Fail to assign Plesae Try again.", "error");
                }
            });
        }
    }

    vm.users = users && users.code == 100 && users.result ? users.result : [];
    vm.getList = function (name) {
        var list = $.grep(vm.users, function (i) {
            i.name = i.firstName + " " + i.lastName;
            return i.role == name
        });
        return list || [];
    };
});