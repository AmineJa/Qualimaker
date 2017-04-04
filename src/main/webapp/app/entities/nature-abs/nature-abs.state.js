(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('nature-abs', {
            parent: 'entity',
            url: '/nature-abs',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.natureAbs.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/nature-abs/nature-abs.html',
                    controller: 'NatureAbsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('natureAbs');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('nature-abs-detail', {
            parent: 'nature-abs',
            url: '/nature-abs/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.natureAbs.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/nature-abs/nature-abs-detail.html',
                    controller: 'NatureAbsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('natureAbs');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'NatureAbs', function($stateParams, NatureAbs) {
                    return NatureAbs.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'nature-abs',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('nature-abs-detail.edit', {
            parent: 'nature-abs-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/nature-abs/nature-abs-dialog.html',
                    controller: 'NatureAbsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NatureAbs', function(NatureAbs) {
                            return NatureAbs.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('nature-abs.new', {
            parent: 'nature-abs',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/nature-abs/nature-abs-dialog.html',
                    controller: 'NatureAbsDialogController',
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
                    $state.go('nature-abs', null, { reload: 'nature-abs' });
                }, function() {
                    $state.go('nature-abs');
                });
            }]
        })
        .state('nature-abs.edit', {
            parent: 'nature-abs',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/nature-abs/nature-abs-dialog.html',
                    controller: 'NatureAbsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NatureAbs', function(NatureAbs) {
                            return NatureAbs.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('nature-abs', null, { reload: 'nature-abs' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('nature-abs.delete', {
            parent: 'nature-abs',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/nature-abs/nature-abs-delete-dialog.html',
                    controller: 'NatureAbsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NatureAbs', function(NatureAbs) {
                            return NatureAbs.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('nature-abs', null, { reload: 'nature-abs' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
