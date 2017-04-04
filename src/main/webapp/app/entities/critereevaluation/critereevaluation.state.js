(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('critereevaluation', {
            parent: 'entity',
            url: '/critereevaluation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.critereevaluation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/critereevaluation/critereevaluations.html',
                    controller: 'CritereevaluationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('critereevaluation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('critereevaluation-detail', {
            parent: 'critereevaluation',
            url: '/critereevaluation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.critereevaluation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/critereevaluation/critereevaluation-detail.html',
                    controller: 'CritereevaluationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('critereevaluation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Critereevaluation', function($stateParams, Critereevaluation) {
                    return Critereevaluation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'critereevaluation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('critereevaluation-detail.edit', {
            parent: 'critereevaluation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/critereevaluation/critereevaluation-dialog.html',
                    controller: 'CritereevaluationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Critereevaluation', function(Critereevaluation) {
                            return Critereevaluation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('critereevaluation.new', {
            parent: 'critereevaluation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/critereevaluation/critereevaluation-dialog.html',
                    controller: 'CritereevaluationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                critere: null,
                                categorie: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('critereevaluation', null, { reload: 'critereevaluation' });
                }, function() {
                    $state.go('critereevaluation');
                });
            }]
        })
        .state('critereevaluation.edit', {
            parent: 'critereevaluation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/critereevaluation/critereevaluation-dialog.html',
                    controller: 'CritereevaluationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Critereevaluation', function(Critereevaluation) {
                            return Critereevaluation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('critereevaluation', null, { reload: 'critereevaluation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('critereevaluation.delete', {
            parent: 'critereevaluation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/critereevaluation/critereevaluation-delete-dialog.html',
                    controller: 'CritereevaluationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Critereevaluation', function(Critereevaluation) {
                            return Critereevaluation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('critereevaluation', null, { reload: 'critereevaluation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
