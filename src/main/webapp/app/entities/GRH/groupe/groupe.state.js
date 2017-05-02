(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('groupe', {
            parent: 'entity',
            url: '/groupe',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.groupe.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/groupe/groupes.html',
                    controller: 'GroupeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('groupe');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('groupe-detail', {
            parent: 'groupe',
            url: '/groupe/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.groupe.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/groupe/groupe-detail.html',
                    controller: 'GroupeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('groupe');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Groupe', function($stateParams, Groupe) {
                    return Groupe.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'groupe',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('groupe-detail.edit', {
            parent: 'groupe-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/groupe/groupe-dialog.html',
                    controller: 'GroupeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Groupe', function(Groupe) {
                            return Groupe.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('groupe.new', {
            parent: 'groupe',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/groupe/groupe-dialog.html',
                    controller: 'GroupeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('groupe', null, { reload: 'groupe' });
                }, function() {
                    $state.go('groupe');
                });
            }]
        })
        .state('groupe.edit', {
            parent: 'groupe',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/groupe/groupe-dialog.html',
                    controller: 'GroupeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Groupe', function(Groupe) {
                            return Groupe.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('groupe', null, { reload: 'groupe' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('groupe.delete', {
            parent: 'groupe',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/groupe/groupe-delete-dialog.html',
                    controller: 'GroupeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Groupe', function(Groupe) {
                            return Groupe.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('groupe', null, { reload: 'groupe' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
