(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-contrat', {
            parent: 'entity',
            url: '/type-contrat?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.typeContrat.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-contrat/type-contrats.html',
                    controller: 'TypeContratController',
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
                    $translatePartialLoader.addPart('typeContrat');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-contrat-detail', {
            parent: 'type-contrat',
            url: '/type-contrat/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.typeContrat.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-contrat/type-contrat-detail.html',
                    controller: 'TypeContratDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeContrat');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypeContrat', function($stateParams, TypeContrat) {
                    return TypeContrat.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'type-contrat',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('type-contrat-detail.edit', {
            parent: 'type-contrat-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-contrat/type-contrat-dialog.html',
                    controller: 'TypeContratDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeContrat', function(TypeContrat) {
                            return TypeContrat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-contrat.new', {
            parent: 'type-contrat',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-contrat/type-contrat-dialog.html',
                    controller: 'TypeContratDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('type-contrat', null, { reload: 'type-contrat' });
                }, function() {
                    $state.go('type-contrat');
                });
            }]
        })
        .state('type-contrat.edit', {
            parent: 'type-contrat',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-contrat/type-contrat-dialog.html',
                    controller: 'TypeContratDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeContrat', function(TypeContrat) {
                            return TypeContrat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-contrat', null, { reload: 'type-contrat' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-contrat.delete', {
            parent: 'type-contrat',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-contrat/type-contrat-delete-dialog.html',
                    controller: 'TypeContratDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypeContrat', function(TypeContrat) {
                            return TypeContrat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-contrat', null, { reload: 'type-contrat' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
