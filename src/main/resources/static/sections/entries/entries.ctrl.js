'use strict';


angular.module('app').controller('NewEntryController', function ($scope, list, users, PageValues, $compile, entryservice, $route, commonServices, $routeParams) {
    var vm = this;
    vm.heading = $routeParams.type == 'unique' ? "New entries/Triage" : $routeParams.type == 'duplicate' ? "Duplicate entries/Triage" : $routeParams.type == 'invalid' ? "Invalid entries/Triage" : $routeParams.type == 'potential' ? "Potential entries/Triage" : "";
    vm.init = function () {
        vm.list = list && list.code === 100 ? list.result : [];
        var ulist = users && users.code == 100 && users.result ? users.result : null;
        angular.forEach(vm.list, function (value, key) {
            if (value && value.id) {
                value.createbtn = value.valid == "YES" ? '<a href="#/flow/create/' + value.id + '" class="btn btn-success active">Assign</a>' : '';
                value.deletebtn = '<button type="button" ng-click="entry.deleteClick(' + value.id + ',$event)" class="btn btn-danger data-id-' + value.id + '">Delete</button>';
                value.createdBy = commonServices.getUserName(ulist, value.createdBy);
                value.local_uniq_id = '<a href="#/entry/' + value.id + '" class="active">' + value.local_uniq_id + '</a>';
            }
        });

        vm.datatable = $('#entry-list').find('.datatable').dataTable({
            "aaData": vm.list,
            "aoColumns": [
                {"mDataProp": "created"},
                {"mDataProp": "local_uniq_id"},
                {"mDataProp": "country"},
                {"mDataProp": "source"},
                {"mDataProp": "drugs"},
                {"mDataProp": "valid"},
                {"mDataProp": "seriousness"},
                {"mDataProp": "expedite"},
                {"mDataProp": "lp_name"},
                {"mDataProp": "createdBy"},
                {"mDataProp": "createbtn"}
                // {"mDataProp": "deletebtn"}
            ],
            "initComplete": function (settings, json) {
                $compile(angular.element($('#entry-list tbody')))($scope);
            }
        });
    };

    vm.deleteClick = function (id, event) {
        panel_refresh($(".panel_to_refresh"), "shown");
        bootbox.confirm("Are You Sure You want to delete ?", function (flag) {
            if (flag) {
                entryservice.deleteEntry(id, function (result) {
                    if (result && result.code == 100) {
                        PageValues.msg = {msg: 'Entry Deleted Successfully.', type: 'success'};
                        $route.reload();
                    } else {
                        $.notify("Unable Delete Entry,Please try again.", "error");
                    }
                });
            }
            panel_refresh($(".panel_to_refresh"), "hidden");
        });
    };

    vm.downloadClick = function () {

    }
});


angular.module('app').controller('DuplicateEntryController', function ($scope) {
    var vm = this;
});
angular.module('app').controller('CreateEntryController', function ($scope, PageValues, entryservice, $location, commonServices) {
    var vm = this;
    vm.title = 'Create entry';
    vm.heading = 'Create entry';
    vm.isValid = true;
    vm.countrys = PageValues.countries;
    vm.isReferenceAvail = false;
    vm.submit = function (e) {
        if ($("#create-entry-form")[0].checkValidity()) {
            if (vm.isValid) {
                panel_refresh($(".panel_to_refresh"), "shown");
                console.log(vm.data);
                entryservice.createEntry(vm.data, function (res) {
                    if (res && res.code === 100) {
                        PageValues.msg = {msg: 'New Entry Created Successfully.', type: 'success'};
                        $location.path('/home').replace();
                    } else {
                        $.notify("Unable ceate entry,Please try again.", "error");
                    }
                    panel_refresh($(".panel_to_refresh"), "hidden");
                });
            } else {
                bootbox.alert("Local reference number already exist please try another one.");
            }
        }
    };
    vm.validList = ["YES", "NO", "Potential"];
    vm.init = function () {
        vm.data = {"country": "India", "submission_country": "India"};
    };

    vm.uploadFile = function () {
        var ele = $('#create-entry-form').find('#pdfupload');
        commonServices.fileUpload(ele, function (result) {
            console.log(result);
        });
    }
});
angular.module('app').controller('UpdateEntryController', function ($scope, entry, PageValues, entryservice, $location) {
    var vm = this;
    vm.title = 'Update entry';
    vm.heading = 'Update entry';
    vm.countrys = PageValues.countries;
    vm.isReferenceAvail = false;
    vm.submit = function (e) {
        if ($("#create-entry-form")[0].checkValidity()) {
            panel_refresh($(".panel_to_refresh"), "shown");
            console.log(vm.data);
            entryservice.updateEntry(vm.data, vm.data.id, function (res) {
                if (res && res.code === 100) {
                    PageValues.msg = {msg: 'Entry updated successfully.', type: 'success'};
                    $location.path('/home').replace();
                } else {
                    $.notify("Unable update Entry,Please try again.", "error");
                }
                panel_refresh($(".panel_to_refresh"), "hidden");
            });
        }
    };
    vm.validList = ["YES", "NO", "Potential"];
    vm.init = function () {
        if (entry && entry.code === 100 && entry.result) {
            vm.data = entry.result;
        }
    };


});
