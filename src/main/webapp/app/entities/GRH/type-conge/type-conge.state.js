(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-conge', {
            parent: 'entity',
            url: '/type-conge?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.typeConge.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/GRH/type-conge/type-conges.html',
                    controller: 'TypeCongeController',
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
                    $translatePartialLoader.addPart('typeConge');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-conge-detail', {
            parent: 'type-conge',
            url: '/type-conge/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.typeConge.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/GRH/type-conge/type-conge-detail.html',
                    controller: 'TypeCongeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeConge');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypeConge', function($stateParams, TypeConge) {
                    return TypeConge.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'type-conge',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('type-conge-detail.edit', {
            parent: 'type-conge-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/type-conge/type-conge-dialog.html',
                    controller: 'TypeCongeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeConge', function(TypeConge) {
                            return TypeConge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-conge.new', {
            parent: 'type-conge',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/type-conge/type-conge-dialog.html',
                    controller: 'TypeCongeDialogController',
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
                    $state.go('type-conge', null, { reload: 'type-conge' });
                }, function() {
                    $state.go('type-conge');
                });
            }]
        })
        .state('type-conge.edit', {
            parent: 'type-conge',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/type-conge/type-conge-dialog.html',
                    controller: 'TypeCongeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeConge', function(TypeConge) {
                            return TypeConge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-conge', null, { reload: 'type-conge' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-conge.delete', {
            parent: 'type-conge',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/type-conge/type-conge-delete-dialog.html',
                    controller: 'TypeCongeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypeConge', function(TypeConge) {
                            return TypeConge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-conge', null, { reload: 'type-conge' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
