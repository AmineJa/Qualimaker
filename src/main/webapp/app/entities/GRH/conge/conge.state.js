(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('conge', {
            parent: 'entity',
            url: '/conge?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.conge.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/GRH/conge/conges.html',
                    controller: 'CongeController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('conge');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('conge-detail', {
            parent: 'conge',
            url: '/conge/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.conge.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/GRH/conge/conge-detail.html',
                    controller: 'CongeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('conge');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Conge', function($stateParams, Conge) {
                    return Conge.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'conge',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('conge-detail.edit', {
            parent: 'conge-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/conge/conge-dialog.html',
                    controller: 'CongeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Conge', function(Conge) {
                            return Conge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('conge.new', {
            parent: 'conge',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/conge/conge-dialog.html',
                    controller: 'CongeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                statut: null,
                                dateD: null,
                                dateF: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('conge', null, { reload: 'conge' });
                }, function() {
                    $state.go('conge');
                });
            }]
        })
        .state('conge.edit', {
            parent: 'conge',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/conge/conge-dialog.html',
                    controller: 'CongeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Conge', function(Conge) {
                            return Conge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('conge', null, { reload: 'conge' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('conge.delete', {
            parent: 'conge',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/conge/conge-delete-dialog.html',
                    controller: 'CongeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Conge', function(Conge) {
                            return Conge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('conge', null, { reload: 'conge' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
