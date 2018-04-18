'use strict';

angular
    .module('app.routes', ['ngRoute'])
    .config(config);

function config($routeProvider,$locationProvider) {
    $routeProvider./* home section */
    when('/', {
        templateUrl: 'sections/home/home.tpl.html',
        controller: 'HomeController as home',
        resolve: {
            data: function (worklowService) {
                return worklowService.getWorkflowDashBoardData();
            },
            users: function (userService) {
                return userService.getUserList();
            }
        }
    })
        .when('/home', {
            templateUrl: 'sections/home/home.tpl.html',
            controller: 'HomeController as home',
            resolve: {
                data: function (worklowService) {
                    return worklowService.getWorkflowDashBoardData();
                },
                users: function (userService) {
                    return userService.getUserList();
                }
            }
        })

        /* workflow section */
        .when('/entry/:id', {
            templateUrl: 'sections/entries/create.tpl.html',
            controller: 'UpdateEntryController as entry',
            resolve: {
                entry: function (entryservice, $route) {
                    return entryservice.getEntryById($route.current.params.id);
                }
            }
        })
        .when('/entries/create', {
            templateUrl: 'sections/entries/create.tpl.html',
            controller: 'CreateEntryController as entry'
        })
        .when('/entries/:type', {
            templateUrl: 'sections/entries/new.tpl.html',
            controller: 'NewEntryController as entry',
            resolve: {
                list: function (entryservice, $route) {
                    return entryservice.getEntrysList($route.current.params.type);
                },
                users: function (userService) {
                    return userService.getUserList();
                }
            }
        })
        .when('/edit/:id', {
            templateUrl: 'sections/workflow/workflow.form.tpl.html',
            controller: 'EditWorkflowController as workflow',
            resolve: {
                show: function (worklowService, $route) {
                    return worklowService.getWorkflowById($route.current.params.id);
                },
                users: function (userService) {
                    return userService.getUserList();
                }
            }
        })
        .when('/case/new', {
            templateUrl: 'sections/workflow/workflow.form.tpl.html',
            controller: 'NewCaseController as workflow',
            resolve: {                
                users: function (userService) {
                    return userService.getUserList();
                }
            }
        })
        .when('/flow/create/:id', {
            templateUrl: 'sections/workflow/workflow.form.tpl.html',
            controller: 'InitiateflowController as workflow',
            resolve: {
                show: function (entryservice, $route) {
                    return entryservice.getEntryById($route.current.params.id);
                },
                users: function (userService) {
                    return userService.getUserList();
                }
            }
        })
        .when('/download', {
            templateUrl: 'sections/workflow/export.tpl.html',
            controller: 'ExportWorkflowController as workflow'
        })
        .when('/flow/list/:name', {
            templateUrl: 'sections/workflow/export.tpl.html',
            controller: 'ListWorkflowController as workflow',
            resolve: {
                show: function (worklowService, $route) {
                    return worklowService.getList($route.current.params.name);
                },
                users: function (userService) {
                    return userService.getUserList();
                }
            }
        })
        /* user section */
        .when('/users', {
            templateUrl: 'sections/users/list.tpl.html',
            controller: 'UserController as user',
            resolve: {
                users: function (userService) {
                    return userService.getUsers();
                }
            }
        })
        .when('/user/create', {
            templateUrl: 'sections/users/new.tpl.html',
            controller: 'createUserController as user'
        })
        .when('/user/edit/:id', {
            templateUrl: 'sections/users/new.tpl.html',
            controller: 'updateUserController as user',
            resolve: {
                user: function (userService, $route) {
                    return userService.getUser($route.current.params.id);
                }
            }
        })
        /* other section */
        .when('/search/:id', {
            templateUrl: 'sections/search/search.tpl.html',
            controller: 'SearchController as view',
            resolve: {
                data: function (worklowService, $route) {
                    return worklowService.searchByName($route.current.params.id);
                }
            }
        })
        .when('/filter', {
            templateUrl: 'sections/search/full.search.tpl.html',
            controller: 'FullSearchController as search',
            resolve: {
                users: function (userService) {
                    return userService.getUserList();
                }
            }
        })
        .when('/auto', {
            templateUrl: 'sections/auto/auto.tpl.html',
            controller: 'AutoController as auto',
            resolve: {
                data: function (autoService) {
                    return autoService.getEntrysList();
                }
            }
        })
        .when('/auto/add', {
            templateUrl: 'sections/auto/add.tpl.html',
            controller: 'AutoAddController as auto'
        })
        .when('/auto/:id', {
            templateUrl: 'sections/auto/add.tpl.html',
            controller: 'AutoEditController as auto',
            resolve: {
                data: function (autoService,$route) {
                    return autoService.getEntryById($route.current.params.id);
                }
            }
        })
        .otherwise({
            templateUrl: 'sections/error/404.tpl.html'
            //redirectTo: '/'
        });
}