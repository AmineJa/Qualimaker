(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-formation', {
            parent: 'entity',
            url: '/type-formation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.typeFormation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-formation/type-formations.html',
                    controller: 'TypeFormationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeFormation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-formation-detail', {
            parent: 'type-formation',
            url: '/type-formation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.typeFormation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-formation/type-formation-detail.html',
                    controller: 'TypeFormationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeFormation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypeFormation', function($stateParams, TypeFormation) {
                    return TypeFormation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'type-formation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('type-formation-detail.edit', {
            parent: 'type-formation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-formation/type-formation-dialog.html',
                    controller: 'TypeFormationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeFormation', function(TypeFormation) {
                            return TypeFormation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-formation.new', {
            parent: 'type-formation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-formation/type-formation-dialog.html',
                    controller: 'TypeFormationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libelle: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('type-formation', null, { reload: 'type-formation' });
                }, function() {
                    $state.go('type-formation');
                });
            }]
        })
        .state('type-formation.edit', {
            parent: 'type-formation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-formation/type-formation-dialog.html',
                    controller: 'TypeFormationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeFormation', function(TypeFormation) {
                            return TypeFormation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-formation', null, { reload: 'type-formation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-formation.delete', {
            parent: 'type-formation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-formation/type-formation-delete-dialog.html',
                    controller: 'TypeFormationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypeFormation', function(TypeFormation) {
                            return TypeFormation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-formation', null, { reload: 'type-formation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
