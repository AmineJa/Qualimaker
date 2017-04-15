(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('fichierjoint', {
            parent: 'entity',
            url: '/fichierjoint',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.fichierjoint.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fichierjoint/fichierjoints.html',
                    controller: 'FichierjointController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fichierjoint');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('fichierjoint-detail', {
            parent: 'fichierjoint',
            url: '/fichierjoint/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.fichierjoint.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fichierjoint/fichierjoint-detail.html',
                    controller: 'FichierjointDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fichierjoint');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Fichierjoint', function($stateParams, Fichierjoint) {
                    return Fichierjoint.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'fichierjoint',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('fichierjoint-detail.edit', {
            parent: 'fichierjoint-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fichierjoint/fichierjoint-dialog.html',
                    controller: 'FichierjointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Fichierjoint', function(Fichierjoint) {
                            return Fichierjoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fichierjoint.new', {
            parent: 'fichierjoint',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fichierjoint/fichierjoint-dialog.html',
                    controller: 'FichierjointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                commentaire: null,
                                fichejoint: null,
                                fichejointContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('fichierjoint', null, { reload: 'fichierjoint' });
                }, function() {
                    $state.go('fichierjoint');
                });
            }]
        })
        .state('fichierjoint.edit', {
            parent: 'fichierjoint',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fichierjoint/fichierjoint-dialog.html',
                    controller: 'FichierjointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Fichierjoint', function(Fichierjoint) {
                            return Fichierjoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fichierjoint', null, { reload: 'fichierjoint' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fichierjoint.delete', {
            parent: 'fichierjoint',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fichierjoint/fichierjoint-delete-dialog.html',
                    controller: 'FichierjointDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Fichierjoint', function(Fichierjoint) {
                            return Fichierjoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fichierjoint', null, { reload: 'fichierjoint' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
