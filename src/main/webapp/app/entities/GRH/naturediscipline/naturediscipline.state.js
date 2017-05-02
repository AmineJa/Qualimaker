(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('naturediscipline', {
            parent: 'entity',
            url: '/naturediscipline?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.naturediscipline.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/GRH/naturediscipline/naturedisciplines.html',
                    controller: 'NaturedisciplineController',
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
                    $translatePartialLoader.addPart('naturediscipline');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('naturediscipline-detail', {
            parent: 'naturediscipline',
            url: '/naturediscipline/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.naturediscipline.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/GRH/naturediscipline/naturediscipline-detail.html',
                    controller: 'NaturedisciplineDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('naturediscipline');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Naturediscipline', function($stateParams, Naturediscipline) {
                    return Naturediscipline.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'naturediscipline',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('naturediscipline-detail.edit', {
            parent: 'naturediscipline-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/naturediscipline/naturediscipline-dialog.html',
                    controller: 'NaturedisciplineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Naturediscipline', function(Naturediscipline) {
                            return Naturediscipline.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('naturediscipline.new', {
            parent: 'naturediscipline',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/naturediscipline/naturediscipline-dialog.html',
                    controller: 'NaturedisciplineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nature: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('naturediscipline', null, { reload: 'naturediscipline' });
                }, function() {
                    $state.go('naturediscipline');
                });
            }]
        })
        .state('naturediscipline.edit', {
            parent: 'naturediscipline',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/naturediscipline/naturediscipline-dialog.html',
                    controller: 'NaturedisciplineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Naturediscipline', function(Naturediscipline) {
                            return Naturediscipline.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('naturediscipline', null, { reload: 'naturediscipline' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('naturediscipline.delete', {
            parent: 'naturediscipline',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/naturediscipline/naturediscipline-delete-dialog.html',
                    controller: 'NaturedisciplineDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Naturediscipline', function(Naturediscipline) {
                            return Naturediscipline.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('naturediscipline', null, { reload: 'naturediscipline' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
