(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('remplacer', {
            parent: 'entity',
            url: '/remplacer?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.remplacer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/remplacer/remplacers.html',
                    controller: 'RemplacerController',
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
                    $translatePartialLoader.addPart('remplacer');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('remplacer-detail', {
            parent: 'remplacer',
            url: '/remplacer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.remplacer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/remplacer/remplacer-detail.html',
                    controller: 'RemplacerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('remplacer');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Remplacer', function($stateParams, Remplacer) {
                    return Remplacer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'remplacer',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('remplacer-detail.edit', {
            parent: 'remplacer-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/remplacer/remplacer-dialog.html',
                    controller: 'RemplacerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Remplacer', function(Remplacer) {
                            return Remplacer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('remplacer.new', {
            parent: 'remplacer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/remplacer/remplacer-dialog.html',
                    controller: 'RemplacerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                debut: null,
                                fin: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('remplacer', null, { reload: 'remplacer' });
                }, function() {
                    $state.go('remplacer');
                });
            }]
        })
        .state('remplacer.edit', {
            parent: 'remplacer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/remplacer/remplacer-dialog.html',
                    controller: 'RemplacerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Remplacer', function(Remplacer) {
                            return Remplacer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('remplacer', null, { reload: 'remplacer' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('remplacer.delete', {
            parent: 'remplacer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/remplacer/remplacer-delete-dialog.html',
                    controller: 'RemplacerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Remplacer', function(Remplacer) {
                            return Remplacer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('remplacer', null, { reload: 'remplacer' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
