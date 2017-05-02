(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('integre', {
            parent: 'entity',
            url: '/integre',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.integre.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/GRH/integre/integres.html',
                    controller: 'IntegreController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('integre');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('integre-detail', {
            parent: 'integre',
            url: '/integre/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.integre.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/GRH/integre/integre-detail.html',
                    controller: 'IntegreDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('integre');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Integre', function($stateParams, Integre) {
                    return Integre.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'integre',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('integre-detail.edit', {
            parent: 'integre-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/integre/integre-dialog.html',
                    controller: 'IntegreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Integre', function(Integre) {
                            return Integre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('integre.new', {
            parent: 'integre',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/integre/integre-dialog.html',
                    controller: 'IntegreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateD: null,
                                dateF: null,
                                pointfort: null,
                                poitaibl: null,
                                info: null,
                                etat: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('integre', null, { reload: 'integre' });
                }, function() {
                    $state.go('integre');
                });
            }]
        })
        .state('integre.edit', {
            parent: 'integre',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/integre/integre-dialog.html',
                    controller: 'IntegreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Integre', function(Integre) {
                            return Integre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('integre', null, { reload: 'integre' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('integre.delete', {
            parent: 'integre',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/integre/integre-delete-dialog.html',
                    controller: 'IntegreDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Integre', function(Integre) {
                            return Integre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('integre', null, { reload: 'integre' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
