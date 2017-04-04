(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('demande-formation', {
            parent: 'entity',
            url: '/demande-formation?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.demandeFormation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/demande-formation/demande-formations.html',
                    controller: 'DemandeFormationController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('demandeFormation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('demande-formation-detail', {
            parent: 'demande-formation',
            url: '/demande-formation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.demandeFormation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/demande-formation/demande-formation-detail.html',
                    controller: 'DemandeFormationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('demandeFormation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DemandeFormation', function($stateParams, DemandeFormation) {
                    return DemandeFormation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'demande-formation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('demande-formation-detail.edit', {
            parent: 'demande-formation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/demande-formation/demande-formation-dialog.html',
                    controller: 'DemandeFormationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DemandeFormation', function(DemandeFormation) {
                            return DemandeFormation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('demande-formation.new', {
            parent: 'demande-formation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/demande-formation/demande-formation-dialog.html',
                    controller: 'DemandeFormationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                theme: null,
                                dateDemande: null,
                                datesouhaite: null,
                                nombresjours: null,
                                description: null,
                                justification: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('demande-formation', null, { reload: 'demande-formation' });
                }, function() {
                    $state.go('demande-formation');
                });
            }]
        })
        .state('demande-formation.edit', {
            parent: 'demande-formation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/demande-formation/demande-formation-dialog.html',
                    controller: 'DemandeFormationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DemandeFormation', function(DemandeFormation) {
                            return DemandeFormation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('demande-formation', null, { reload: 'demande-formation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('demande-formation.delete', {
            parent: 'demande-formation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/demande-formation/demande-formation-delete-dialog.html',
                    controller: 'DemandeFormationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DemandeFormation', function(DemandeFormation) {
                            return DemandeFormation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('demande-formation', null, { reload: 'demande-formation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
