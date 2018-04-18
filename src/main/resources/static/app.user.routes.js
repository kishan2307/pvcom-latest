'use strict';

angular
    .module('app.routes', ['ngRoute'])
    .config(config);

function config($routeProvider) {
    $routeProvider./* home section */
    when('/', {
        templateUrl: 'sections/home/home.user.tpl.html',
        controller: 'UserHomeController as home',
        resolve: {
            show: function (worklowService) {
                return worklowService.getUserHomeData();
            },
            users: function (userService) {
                return userService.getUserList();
            }
        }
    })
        .when('/home', {
            templateUrl: 'sections/home/home.user.tpl.html',
            controller: 'UserHomeController as home',
            resolve: {
                show: function (worklowService) {
                    return worklowService.getUserHomeData();
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
        .when('/search/:id', {
            templateUrl: 'sections/search/search.tpl.html',
            controller: 'SearchController as view',
            resolve: {
                data: function (worklowService, $route) {
                    return worklowService.searchByName($route.current.params.id);
                }
            }
        })
        /* user section */
        .otherwise({
            templateUrl: 'sections/error/404.tpl.html'
            //redirectTo: '/'
        });
}