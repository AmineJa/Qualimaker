(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('formation-comp', {
            parent: 'entity',
            url: '/formation-comp',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.formationComp.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/formation-comp/formation-comps.html',
                    controller: 'FormationCompController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('formationComp');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('formation-comp-detail', {
            parent: 'formation-comp',
            url: '/formation-comp/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.formationComp.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/formation-comp/formation-comp-detail.html',
                    controller: 'FormationCompDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('formationComp');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FormationComp', function($stateParams, FormationComp) {
                    return FormationComp.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'formation-comp',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('formation-comp-detail.edit', {
            parent: 'formation-comp-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/formation-comp/formation-comp-dialog.html',
                    controller: 'FormationCompDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FormationComp', function(FormationComp) {
                            return FormationComp.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('formation-comp.new', {
            parent: 'formation-comp',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/formation-comp/formation-comp-dialog.html',
                    controller: 'FormationCompDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                commentaire: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('formation-comp', null, { reload: 'formation-comp' });
                }, function() {
                    $state.go('formation-comp');
                });
            }]
        })
        .state('formation-comp.edit', {
            parent: 'formation-comp',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/formation-comp/formation-comp-dialog.html',
                    controller: 'FormationCompDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FormationComp', function(FormationComp) {
                            return FormationComp.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('formation-comp', null, { reload: 'formation-comp' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('formation-comp.delete', {
            parent: 'formation-comp',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/formation-comp/formation-comp-delete-dialog.html',
                    controller: 'FormationCompDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FormationComp', function(FormationComp) {
                            return FormationComp.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('formation-comp', null, { reload: 'formation-comp' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
