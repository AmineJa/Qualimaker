(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('NotificationController', NotificationController)
        .service('sharedProperties', function () {
            var property ="";

            return {
                getProperty: function () {
                    return property;
                },
                setProperty: function(value) {
                    property = value;
                }
            };
        });

    NotificationController.$inject = ['$state','sharedProperties','Principal', 'Notification','TypeDocumentation','Employe' ,'DocumentInterne', 'NotificationSearch', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function NotificationController($state,sharedProperties,Principal, Notification, TypeDocumentation,DocumentInterne,Employe,NotificationSearch, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;
        vm.hidePrefs=hidePrefs;
        vm.setProperty=Property;
        vm.settingsAccount = null;
        vm.success = null;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;
        vm.typedocumentations = TypeDocumentation.query();
        vm.documentInternes=DocumentInterne.query();
        vm.employes=Employe.query();



        loadAll();


        function Property(value) {
           sharedProperties.setProperty(value);
            $state.go('document-interne');
        }
        function hidePrefs() {
             setProperty(1);

         }
        function loadAll () {
            if (pagingParams.search) {
                NotificationSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Notification.query({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.notifications = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function search(searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.transition();
        }

        function clear() {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }


        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login,
                authorities:account.authorities
            };
        };

        Principal.identity().then(function(account) {
            vm.settingsAccount = copyAccount(account);
        });

        function save () {
            Auth.updateAccount(vm.settingsAccount).then(function() {
                vm.error = null;
                vm.success = 'OK';
                Principal.identity(true).then(function(account) {
                    vm.settingsAccount = copyAccount(account);
                });
                JhiLanguageService.getCurrent().then(function(current) {
                    if (vm.settingsAccount.langKey !== current) {
                        $translate.use(vm.settingsAccount.langKey);
                    }
                });
            }).catch(function() {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }

    }
})();
