(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('evaluation-formation', {
            parent: 'entity',
            url: '/evaluation-formation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.evaluationFormation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/GRH/evaluation-formation/evaluation-formations.html',
                    controller: 'EvaluationFormationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('evaluationFormation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('evaluation-formation-detail', {
            parent: 'evaluation-formation',
            url: '/evaluation-formation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.evaluationFormation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/GRH/evaluation-formation/evaluation-formation-detail.html',
                    controller: 'EvaluationFormationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('evaluationFormation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EvaluationFormation', function($stateParams, EvaluationFormation) {
                    return EvaluationFormation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'evaluation-formation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('evaluation-formation-detail.edit', {
            parent: 'evaluation-formation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/evaluation-formation/evaluation-formation-dialog.html',
                    controller: 'EvaluationFormationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EvaluationFormation', function(EvaluationFormation) {
                            return EvaluationFormation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('evaluation-formation.new', {
            parent: 'evaluation-formation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/evaluation-formation/evaluation-formation-dialog.html',
                    controller: 'EvaluationFormationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                matricuel: null,
                                effectue: null,
                                afroid: null,
                                achaud: null,
                                evaluerpar: null,
                                refaire: null,
                                dateprevu: null,
                                efficace: null,
                                commentaire: null,
                                methodologie: null,
                                competece: null,
                                supportcours: null,
                                animation: null,
                                lieu: null,
                                respecthoraire: null,
                                contnucours: null,
                                traveauxp: null,
                                objectif: null,
                                duree: null,
                                ambiace: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('evaluation-formation', null, { reload: 'evaluation-formation' });
                }, function() {
                    $state.go('evaluation-formation');
                });
            }]
        })
        .state('evaluation-formation.edit', {
            parent: 'evaluation-formation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/evaluation-formation/evaluation-formation-dialog.html',
                    controller: 'EvaluationFormationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EvaluationFormation', function(EvaluationFormation) {
                            return EvaluationFormation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('evaluation-formation', null, { reload: 'evaluation-formation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('evaluation-formation.delete', {
            parent: 'evaluation-formation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/evaluation-formation/evaluation-formation-delete-dialog.html',
                    controller: 'EvaluationFormationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EvaluationFormation', function(EvaluationFormation) {
                            return EvaluationFormation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('evaluation-formation', null, { reload: 'evaluation-formation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
