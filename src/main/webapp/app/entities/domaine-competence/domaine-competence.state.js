(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('domaine-competence', {
            parent: 'entity',
            url: '/domaine-competence?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.domaineCompetence.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/domaine-competence/domaine-competences.html',
                    controller: 'DomaineCompetenceController',
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
                    $translatePartialLoader.addPart('domaineCompetence');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('domaine-competence-detail', {
            parent: 'domaine-competence',
            url: '/domaine-competence/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.domaineCompetence.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/domaine-competence/domaine-competence-detail.html',
                    controller: 'DomaineCompetenceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('domaineCompetence');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DomaineCompetence', function($stateParams, DomaineCompetence) {
                    return DomaineCompetence.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'domaine-competence',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('domaine-competence-detail.edit', {
            parent: 'domaine-competence-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/domaine-competence/domaine-competence-dialog.html',
                    controller: 'DomaineCompetenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DomaineCompetence', function(DomaineCompetence) {
                            return DomaineCompetence.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('domaine-competence.new', {
            parent: 'domaine-competence',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/domaine-competence/domaine-competence-dialog.html',
                    controller: 'DomaineCompetenceDialogController',
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
                    $state.go('domaine-competence', null, { reload: 'domaine-competence' });
                }, function() {
                    $state.go('domaine-competence');
                });
            }]
        })
        .state('domaine-competence.edit', {
            parent: 'domaine-competence',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/domaine-competence/domaine-competence-dialog.html',
                    controller: 'DomaineCompetenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DomaineCompetence', function(DomaineCompetence) {
                            return DomaineCompetence.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('domaine-competence', null, { reload: 'domaine-competence' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('domaine-competence.delete', {
            parent: 'domaine-competence',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/domaine-competence/domaine-competence-delete-dialog.html',
                    controller: 'DomaineCompetenceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DomaineCompetence', function(DomaineCompetence) {
                            return DomaineCompetence.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('domaine-competence', null, { reload: 'domaine-competence' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
