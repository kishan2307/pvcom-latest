'use strict';
angular
    .module('app')
    .controller('HomeController', function ($scope, data, users, PageValues, $rootScope, commonServices) {
        $rootScope.pageTab = "home";
        var vm = this;
        vm.data = {};
        var ulist = users && users.code === 100 && users.result ? users.result : null;
        if (data && data.code == 100 && data.result) {
            vm.data = data.result;
            vm.rcentFlow = [];
            if (data.result) {
                if (data.result.recentUpdated) {
                    vm.rcentFlow = [];
                    angular.forEach(data.result.recentUpdated, function (val, i) {
                        var status = val.status == 1 ? 'Data entry' : val.status == 2 ? 'Quality check' : val.status == 3 ? 'Medical Review' : val.status == 4 ? 'Submission' : 'CLOSE';
                        var u = val.updatedBy || val.createdBy;
                        var w = val.workflow || {};
                        vm.rcentFlow.push({
                            id: val.id,
                            uniq: val.worldWideUniqId,
                            local:w.local_uniq_id,
                            status: status,
                            company_receive_date:w.company_receive_date,
                            updatedBy: commonServices.getUserName(ulist, u),
                            updatedOn: val.updatedOn || val.createdOn,
                            expedite: w.expedite || 'NA',
                            country:w.country,
                            drugs:w.drugs,
                            source:w.source,
                            lp:w.lp_name,
                            date:w.date,
                            seriousness:w.seriousness
                        });
                    });
                }
            }
        }
    });

angular.module('app').controller('UserHomeController', function ($scope, show, users, PageValues, $rootScope, commonServices) {
    $rootScope.pageTab = "home";
    var vm = this;
    vm.title="Update case";
    var groupName = $("#profile").data('role');
    vm.init = function () {
        var ulist = users && users.code == 100 && users.result ? users.result : null;
        if (show && show.code === 100) {
            var columns = [{"title": "Date"},
                {"title": "Local Reference No"},
                {"title": "World-Wide Uniq Id"},
                {"title": "Country"},
                {"title": "Source"},
                {"title": "Suspect drug"},
                {"title": "Valid"},
                {"title": "Serious/Non-Serious"},
                {"title": "Expedite/Non Expedite"},
                {"title": "LP case"},
                {"title": "Reason"},
                {"title": "Submission Country"},
            ];
            var data = [];
            var columnDefs = [6, 7, 10];
            var defaultHiddenColumn = [];
            angular.forEach(show.result, function (value, key) {
                if (value && typeof value.workflow == "object") {
                    data.push([
                        value.workflow.date,
                        '<a class="green" href="#/edit/' + value.id + '"><strong>' + value.workflow.local_uniq_id + '</strong></a>',
                        '<a class="green" href="#/edit/' + value.id + '"><strong>' + value.worldWideUniqId + '</strong></a>',
                        value.workflow.country,
                        value.workflow.source,
                        value.workflow.drugs,
                        value.workflow.valid,
                        value.workflow.seriousness,
                        value.workflow.expedite,
                        value.workflow.lp_name,
                        value.workflow.reason,
                        value.workflow.submission_country
                    ]);
                }
            });
            switch (groupName) {
                case "de":
                    defaultHiddenColumn = [2, 10, 11];
                    break;
                case "qc":
                    defaultHiddenColumn = [10, 11];
                    break;
                case "mr":
                    defaultHiddenColumn = [10, 11];
                    break;
                case "fs":
                    defaultHiddenColumn = [10, 11];
                    break;
                default:
                    defaultHiddenColumn = [10, 11];
            }

            defaultHiddenColumn.forEach(function (t, number) {
                columnDefs.push({
                    "targets": [t],
                    "visible": false,
                    "searchable": false
                });
            });

            $('#exportTable').dataTable({
                columns: columns,
                data: data,
                colReorder: true,
                //dom: 'Bfrtip',
                "columnDefs": columnDefs
            });
        }
    }
});


angular.module('app').controller('GuestHomeController', function ($scope, show, users, PageValues, $rootScope, commonServices) {
    $rootScope.pageTab = "home";
    var vm = this;
    var groupName = $("#profile").data('role');
    vm.init = function () {
        var ulist = users && users.code == 100 && users.result ? users.result : null;
        if (show && show.code === 100) {
            var columns = [{"title": "Date"},
                {"title": "Local Reference No"},
                {"title": "Country"},
                {"title": "Source"},
                {"title": "Suspect drug"},
                {"title": "Valid"},
                {"title": "Serious/Non-Serious"},
                {"title": "Expedite/Non Expedite"},
                {"title": "LP case"}
                ];
            var data = [];
            var columnDefs = [];
            var defaultHiddenColumn = [];
            angular.forEach(show.result, function (value, key) {
                if (value && typeof value == "object") {
                    data.push([
                        value.date,
                        '<a class="green" href="#/entry/' + value.id + '"><strong>' + value.local_uniq_id + '</strong></a>',
                        value.country,
                        value.source,
                        value.drugs,
                        value.valid,
                        value.seriousness,
                        value.expedite,
                        value.lp_name
                    ]);
                }
            });

            $('#exportTable').dataTable({
                columns: columns,
                data: data,
                columnDefs: columnDefs
            });
        }
    }
});
