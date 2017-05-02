(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('formateur', {
            parent: 'entity',
            url: '/formateur',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.formateur.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/GRH/formateur/formateurs.html',
                    controller: 'FormateurController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('formateur');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('formateur-detail', {
            parent: 'formateur',
            url: '/formateur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.formateur.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/GRH/formateur/formateur-detail.html',
                    controller: 'FormateurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('formateur');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Formateur', function($stateParams, Formateur) {
                    return Formateur.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'formateur',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('formateur-detail.edit', {
            parent: 'formateur-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/formateur/formateur-dialog.html',
                    controller: 'FormateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Formateur', function(Formateur) {
                            return Formateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('formateur.new', {
            parent: 'formateur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/formateur/formateur-dialog.html',
                    controller: 'FormateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                prenom: null,
                                cv: null,
                                cvContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('formateur', null, { reload: 'formateur' });
                }, function() {
                    $state.go('formateur');
                });
            }]
        })
        .state('formateur.edit', {
            parent: 'formateur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/formateur/formateur-dialog.html',
                    controller: 'FormateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Formateur', function(Formateur) {
                            return Formateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('formateur', null, { reload: 'formateur' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('formateur.delete', {
            parent: 'formateur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/formateur/formateur-delete-dialog.html',
                    controller: 'FormateurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Formateur', function(Formateur) {
                            return Formateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('formateur', null, { reload: 'formateur' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
