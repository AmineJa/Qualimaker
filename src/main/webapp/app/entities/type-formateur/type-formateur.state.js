(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-formateur', {
            parent: 'entity',
            url: '/type-formateur',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.typeFormateur.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-formateur/type-formateurs.html',
                    controller: 'TypeFormateurController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeFormateur');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-formateur-detail', {
            parent: 'type-formateur',
            url: '/type-formateur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.typeFormateur.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-formateur/type-formateur-detail.html',
                    controller: 'TypeFormateurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeFormateur');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypeFormateur', function($stateParams, TypeFormateur) {
                    return TypeFormateur.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'type-formateur',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('type-formateur-detail.edit', {
            parent: 'type-formateur-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-formateur/type-formateur-dialog.html',
                    controller: 'TypeFormateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeFormateur', function(TypeFormateur) {
                            return TypeFormateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-formateur.new', {
            parent: 'type-formateur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-formateur/type-formateur-dialog.html',
                    controller: 'TypeFormateurDialogController',
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
                    $state.go('type-formateur', null, { reload: 'type-formateur' });
                }, function() {
                    $state.go('type-formateur');
                });
            }]
        })
        .state('type-formateur.edit', {
            parent: 'type-formateur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-formateur/type-formateur-dialog.html',
                    controller: 'TypeFormateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeFormateur', function(TypeFormateur) {
                            return TypeFormateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-formateur', null, { reload: 'type-formateur' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-formateur.delete', {
            parent: 'type-formateur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-formateur/type-formateur-delete-dialog.html',
                    controller: 'TypeFormateurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypeFormateur', function(TypeFormateur) {
                            return TypeFormateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-formateur', null, { reload: 'type-formateur' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
