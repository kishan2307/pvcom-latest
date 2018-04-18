'use strict';


angular.module('app').controller('EditWorkflowController', function ($scope, users, show, worklowService, $location, PageValues, commonServices) {
    var vm = this;
    vm.title = "Update case";
    vm.showAssignto = PageValues.role === 'admin' ? true : false;
    vm.countrys = PageValues.countries;
    vm.data = {};
    vm.validList = ["YES", "NO", "Potential"];
    vm.users = users && users.code == 100 && users.result ? users.result : [];
    vm.getList = function (name) {
        var list = $.grep(vm.users, function (i) {
            i.name = i.firstName + " " + i.lastName;
            return i.role == name
        });
        return list || [];
    };

    if (show && show.code == 100 && typeof show.result == 'object') {
        var role = $("#profile").data('role');
        vm.data = show.result;
        vm.entryby = commonServices.getUserName(vm.users, vm.data.workflow.createdBy);
        if (vm.data.workflow.de) {
            vm.deby = commonServices.getUserName(vm.users, vm.data.workflow.de.doneBy);
        }
        if (vm.data.workflow.qc) {
            vm.qcby = commonServices.getUserName(vm.users, vm.data.workflow.qc.doneBy);
        }
        if (vm.data.workflow.mr) {
            vm.mrby = commonServices.getUserName(vm.users, vm.data.workflow.mr.doneBy);
        }
        if (vm.data.workflow.fs) {
            vm.fsby = commonServices.getUserName(vm.users, vm.data.workflow.fs.doneBy);
        }
        if (vm.data && vm.data.workflow && !vm.data.workflow[role]) {
            vm.data.workflow[role] = {};
        }
    }

    vm.submit = function (e) {
        if ($(e.target).closest('form')[0].checkValidity()) {
            panel_refresh($(".panel_to_refresh"), "shown");
            if (vm.data.userId == 0) {
                vm.data.userId = vm.data.deUserId;
            }
            worklowService.UpdateWorkflow(vm.data, function (res) {
                if (res && res.code === 100) {
                    PageValues.msg = {msg: 'Case Updated Successfully.', type: 'success'};
                    $location.path('/home').replace();
                } else {
                    $.notify("Unable Update case,Please try again.", "error");
                }
                panel_refresh($(".panel_to_refresh"), "hidden");
            });
        }
    }
});

angular.module('app').controller('InitiateflowController', function ($scope, show, users, PageValues, worklowService, $location, commonServices) {
    var vm = this;
    vm.title = "New case";
    vm.data = {};
    vm.countrys = PageValues.countries;
    console.log(show);
    vm.data = {};
    vm.users = users && users.code == 100 && users.result ? users.result : [];
    vm.validList = ["YES", "NO", "Potential"];
    vm.getList = function (name) {
        var list = $.grep(vm.users, function (i) {
            i.name = i.firstName + " " + i.lastName;
            return i.role == name
        });
        return list || [];
    };

    if (show && show.code == 100 && typeof show.result == 'object') {
        vm.data.workflow = show.result;
        vm.entryby = commonServices.getUserName(vm.users, vm.data.workflow.createdBy);
    }


    vm.submit = function (e) {
        if ($(e.target).closest('form')[0].checkValidity()) {
            panel_refresh($(".panel_to_refresh"), "shown");
            worklowService.addWorkflow(vm.data, function (res) {
                if (res && res.code === 100) {
                    $location.path('/home').replace();
                    PageValues.msg = {msg: 'workflow Created Successfully.', type: 'success'};
                } else {
                    $.notify("Unable Create Workflow,Please try again.", "error");
                }
                panel_refresh($(".panel_to_refresh"), "hidden");
            });
        }
    }
});

angular.module('app').controller('NewCaseController', function ($scope, users, PageValues, worklowService, $location) {
    var vm = this;
    vm.title = "New Case";
    vm.data = {};
    vm.countrys = PageValues.countries;
    vm.data = {};

    vm.users = users && users.code == 100 && users.result ? users.result : [];
    vm.validList = ["YES", "NO", "Potential"];
    vm.getList = function (name) {
        var list = $.grep(vm.users, function (i) {
            i.name = i.firstName + " " + i.lastName;
            return i.role == name
        });
        return list || [];
    };

    vm.data = {};
    vm.data.workflow = {};

    vm.submit = function (e) {
        if ($(e.target).closest('form')[0].checkValidity()) {
            panel_refresh($(".panel_to_refresh"), "shown");
            worklowService.addWorkflow(vm.data, function (res) {
                if (res && res.code === 100) {
                    $location.path('/home').replace();
                    PageValues.msg = {msg: 'Case Created Successfully.', type: 'success'};
                } else {
                    $.notify("Unable Create Case,Please try again.", "error");
                }
                panel_refresh($(".panel_to_refresh"), "hidden");
            });
        }
    }
});

angular.module('app').controller('ListWorkflowController', function ($scope, show, users, PageValues, worklowService, $routeParams, commonServices, $route, $compile) {
    var vm = this;
    vm.list = show;
    var groupName = $routeParams.name;
    vm.title = groupName == 'de' ? 'Data entry cases' : groupName == 'qc' ? 'Quality Check cases' : groupName == 'mr' ? 'Medicl Review cases' : groupName == 'fs' ? 'Submission cases' : groupName == 'done' ? 'Completed cases' : groupName == 'lpc' ? 'Licence partner cases' : 'Active cases';
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
                {"title": "User"},
                {"title": "Action"}];
            var data = [];
            var columnDefs = [];
            var defaultHiddenColumn = [];
            angular.forEach(show.result, function (value, key) {
                if (value && typeof value.workflow == "object") {
                    data.push([
                        value.workflow.date,
                        '<a class="green" href="#/edit/' + value.id + '">' + value.workflow.local_uniq_id + '</a>',
                        '<a class="green" href="#/edit/' + value.id + '">' + value.worldWideUniqId + '</a>',
                        value.workflow.country,
                        value.workflow.source,
                        value.workflow.drugs,
                        value.workflow.valid,
                        value.workflow.seriousness,
                        value.workflow.expedite,
                        value.workflow.lp_name,
                        value.workflow.reason,
                        value.workflow.submission_country,
//                        commonServices.getUserName(ulist, value.deUserId),
//                        commonServices.getUserName(ulist, value.qcUserId),
//                        commonServices.getUserName(ulist, value.mrUserId),
                        commonServices.getUserName(ulist, value.userId),
                        '<a class="cst-btn red" href="#" ng-click="workflow.delete(' + value.id + ')"><i class="fa fa-trash-o"></i></a>'
                    ]);
                }
            });
            switch (groupName) {
                case "de":
                    defaultHiddenColumn = [2, 10, 11,13];
                    break;
                case "qc":
                    defaultHiddenColumn = [10, 11,13];
                    break;
                case "mr":
                    defaultHiddenColumn = [10, 11,13];
                    break;
                case "fs":
                    defaultHiddenColumn = [10, 11,13];
                    break;
                case "done":
                    defaultHiddenColumn = [10, 11,13];
                    break;
                case "lpc":
                    defaultHiddenColumn = [10, 11,13];
                    break;
                default:
                    defaultHiddenColumn = [10, 11,13];
            }

            defaultHiddenColumn.forEach(function (t, number) {
                columnDefs.push({
                    "targets": [t],
                    "visible": false,
                    "searchable": false
                });
            });


            vm.delete = function (id) {
                bootbox.confirm("Are You Sure You want to delete ?", function (result) {
                    if (result) {
                        worklowService.deleteCase(id, function (result) {
                            if (result && result.code === 100) {
                                $route.reload();
                                $.notify("Case Deleted Successfully.", "success");
                            } else {
                                alert("Unable to delete case , Please try again.")
                            }
                        });
                    }
                });
            }

            $('#exportTable').dataTable({
                columns: columns,
                data: data,
                colReorder: true,
                // dom: 'Bfrtip',
                "columnDefs": columnDefs,
                "initComplete": function (settings, json) {
                    $compile(angular.element($('#exportTable').find('tbody')))($scope);
                }
            });
        }
    }
});

angular.module('app').controller('ViewWorkflowController', function ($scope, show, PageValues) {
    var vm = this;
    vm.tabin = 1;
    vm.flow = show;
});