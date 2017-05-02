(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('calendrier', {
            parent: 'entity',
            url: '/calendrier?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.calendrier.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/calendrier/calendriers.html',
                    controller: 'CalendrierController',
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
                    $translatePartialLoader.addPart('calendrier');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('calendrier-detail', {
            parent: 'calendrier',
            url: '/calendrier/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.calendrier.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/calendrier/calendrier-detail.html',
                    controller: 'CalendrierDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('calendrier');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Calendrier', function($stateParams, Calendrier) {
                    return Calendrier.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'calendrier',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('calendrier-detail.edit', {
            parent: 'calendrier-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/calendrier/calendrier-dialog.html',
                    controller: 'CalendrierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Calendrier', function(Calendrier) {
                            return Calendrier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('calendrier.new', {
            parent: 'calendrier',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/calendrier/calendrier-dialog.html',
                    controller: 'CalendrierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('calendrier', null, { reload: 'calendrier' });
                }, function() {
                    $state.go('calendrier');
                });
            }]
        })
        .state('calendrier.edit', {
            parent: 'calendrier',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/calendrier/calendrier-dialog.html',
                    controller: 'CalendrierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Calendrier', function(Calendrier) {
                            return Calendrier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('calendrier', null, { reload: 'calendrier' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('calendrier.delete', {
            parent: 'calendrier',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/calendrier/calendrier-delete-dialog.html',
                    controller: 'CalendrierDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Calendrier', function(Calendrier) {
                            return Calendrier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('calendrier', null, { reload: 'calendrier' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
