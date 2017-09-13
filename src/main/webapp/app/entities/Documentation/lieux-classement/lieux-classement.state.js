(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lieux-classement', {
            parent: 'entity',
            url: '/lieux-classement?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.lieuxClassement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/Documentation/lieux-classement/lieux-classements.html',
                    controller: 'LieuxClassementController',
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
                    $translatePartialLoader.addPart('lieuxClassement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lieux-classement-detail', {
            parent: 'lieux-classement',
            url: '/lieux-classement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.lieuxClassement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/Documentation/lieux-classement/lieux-classement-detail.html',
                    controller: 'LieuxClassementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lieuxClassement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LieuxClassement', function($stateParams, LieuxClassement) {
                    return LieuxClassement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lieux-classement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lieux-classement-detail.edit', {
            parent: 'lieux-classement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/Documentation/lieux-classement/lieux-classement-dialog.html',
                    controller: 'LieuxClassementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LieuxClassement', function(LieuxClassement) {
                            return LieuxClassement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lieux-classement.new', {
            parent: 'lieux-classement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/Documentation/lieux-classement/lieux-classement-dialog.html',
                    controller: 'LieuxClassementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                lieux: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lieux-classement', null, { reload: 'lieux-classement' });
                }, function() {
                    $state.go('lieux-classement');
                });
            }]
        })
        .state('lieux-classement.edit', {
            parent: 'lieux-classement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/Documentation/lieux-classement/lieux-classement-dialog.html',
                    controller: 'LieuxClassementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LieuxClassement', function(LieuxClassement) {
                            return LieuxClassement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lieux-classement', null, { reload: 'lieux-classement' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lieux-classement.delete', {
            parent: 'lieux-classement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/Documentation/lieux-classement/lieux-classement-delete-dialog.html',
                    controller: 'LieuxClassementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LieuxClassement', function(LieuxClassement) {
                            return LieuxClassement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lieux-classement', null, { reload: 'lieux-classement' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
