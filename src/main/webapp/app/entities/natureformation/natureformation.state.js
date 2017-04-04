(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('natureformation', {
            parent: 'entity',
            url: '/natureformation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.natureformation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/natureformation/natureformations.html',
                    controller: 'NatureformationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('natureformation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('natureformation-detail', {
            parent: 'natureformation',
            url: '/natureformation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.natureformation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/natureformation/natureformation-detail.html',
                    controller: 'NatureformationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('natureformation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Natureformation', function($stateParams, Natureformation) {
                    return Natureformation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'natureformation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('natureformation-detail.edit', {
            parent: 'natureformation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/natureformation/natureformation-dialog.html',
                    controller: 'NatureformationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Natureformation', function(Natureformation) {
                            return Natureformation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('natureformation.new', {
            parent: 'natureformation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/natureformation/natureformation-dialog.html',
                    controller: 'NatureformationDialogController',
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
                    $state.go('natureformation', null, { reload: 'natureformation' });
                }, function() {
                    $state.go('natureformation');
                });
            }]
        })
        .state('natureformation.edit', {
            parent: 'natureformation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/natureformation/natureformation-dialog.html',
                    controller: 'NatureformationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Natureformation', function(Natureformation) {
                            return Natureformation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('natureformation', null, { reload: 'natureformation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('natureformation.delete', {
            parent: 'natureformation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/natureformation/natureformation-delete-dialog.html',
                    controller: 'NatureformationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Natureformation', function(Natureformation) {
                            return Natureformation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('natureformation', null, { reload: 'natureformation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
