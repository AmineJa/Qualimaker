(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('enregistrement', {
            parent: 'entity',
            url: '/enregistrement?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.enregistrement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/Documentation/enregistrement/enregistrements.html',
                    controller: 'EnregistrementController',
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
                    $translatePartialLoader.addPart('enregistrement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('enregistrement-detail', {
            parent: 'enregistrement',
            url: '/enregistrement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.enregistrement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/Documentation/enregistrement/enregistrement-detail.html',
                    controller: 'EnregistrementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('enregistrement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Enregistrement', function($stateParams, Enregistrement) {
                    return Enregistrement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'enregistrement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('enregistrement-detail.edit', {
            parent: 'enregistrement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/Documentation/enregistrement/enregistrement-dialog.html',
                    controller: 'EnregistrementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Enregistrement', function(Enregistrement) {
                            return Enregistrement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('enregistrement.new', {
            parent: 'enregistrement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/Documentation/enregistrement/enregistrement-dialog.html',
                    controller: 'EnregistrementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libelle: null,
                                titre: null,
                                fichier: null,
                                fichierContentType: null,
                                commentaire: null,
                                motclef: null,
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('enregistrement', null, { reload: 'enregistrement' });
                }, function() {
                    $state.go('enregistrement');
                });
            }]
        })
        .state('enregistrement.edit', {
            parent: 'enregistrement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/Documentation/enregistrement/enregistrement-dialog.html',
                    controller: 'EnregistrementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Enregistrement', function(Enregistrement) {
                            return Enregistrement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('enregistrement', null, { reload: 'enregistrement' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('enregistrement.delete', {
            parent: 'enregistrement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/Documentation/enregistrement/enregistrement-delete-dialog.html',
                    controller: 'EnregistrementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Enregistrement', function(Enregistrement) {
                            return Enregistrement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('enregistrement', null, { reload: 'enregistrement' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
