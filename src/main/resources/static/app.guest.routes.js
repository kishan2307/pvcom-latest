'use strict';

angular
    .module('app.routes', ['ngRoute'])
    .config(config);

function config($routeProvider) {
    $routeProvider./* home section */
    when('/', {
        templateUrl: 'sections/home/home.guest.tpl.html',
        controller: 'GuestHomeController as home',
        resolve: {
            show: function (entryservice, $route) {
                return entryservice.getNewEntrysList();
            },
            users: function (userService) {
                return userService.getUserList();
            }
        }
    })
        .when('/home', {
            templateUrl: 'sections/home/home.guest.tpl.html',
            controller: 'GuestHomeController as home',
            resolve: {
                show: function (entryservice, $route) {
                    return entryservice.getNewEntrysList();
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
        .when('/entries/create', {
            templateUrl: 'sections/entries/create.tpl.html',
            controller: 'CreateEntryController as entry'
        })
        .when('/entry/:id', {
            templateUrl: 'sections/entries/create.tpl.html',
            controller: 'UpdateEntryController as entry',
            resolve: {
                entry: function (entryservice, $route) {
                    return entryservice.getEntryById($route.current.params.id);
                }
            }
        })
        /* user section */
        .otherwise({
            templateUrl: 'sections/error/404.tpl.html'
        });
}