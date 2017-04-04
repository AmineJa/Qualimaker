(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('carriere', {
            parent: 'entity',
            url: '/carriere?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.carriere.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/carriere/carrieres.html',
                    controller: 'CarriereController',
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
                    $translatePartialLoader.addPart('carriere');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('carriere-detail', {
            parent: 'carriere',
            url: '/carriere/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.carriere.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/carriere/carriere-detail.html',
                    controller: 'CarriereDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('carriere');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Carriere', function($stateParams, Carriere) {
                    return Carriere.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'carriere',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('carriere-detail.edit', {
            parent: 'carriere-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/carriere/carriere-dialog.html',
                    controller: 'CarriereDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Carriere', function(Carriere) {
                            return Carriere.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('carriere.new', {
            parent: 'carriere',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/carriere/carriere-dialog.html',
                    controller: 'CarriereDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                etat: null,
                                integre: null,
                                debutINt: null,
                                finINT: null,
                                dateRec: null,
                                fichCont: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('carriere', null, { reload: 'carriere' });
                }, function() {
                    $state.go('carriere');
                });
            }]
        })
        .state('carriere.edit', {
            parent: 'carriere',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/carriere/carriere-dialog.html',
                    controller: 'CarriereDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Carriere', function(Carriere) {
                            return Carriere.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('carriere', null, { reload: 'carriere' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('carriere.delete', {
            parent: 'carriere',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/carriere/carriere-delete-dialog.html',
                    controller: 'CarriereDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Carriere', function(Carriere) {
                            return Carriere.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('carriere', null, { reload: 'carriere' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
