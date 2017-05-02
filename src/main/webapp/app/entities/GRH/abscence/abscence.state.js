(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('abscence', {
            parent: 'entity',
            url: '/abscence?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.abscence.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/GRH/abscence/abscences.html',
                    controller: 'AbscenceController',
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
                    $translatePartialLoader.addPart('abscence');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('abscence-detail', {
            parent: 'abscence',
            url: '/abscence/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.abscence.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/GRH/abscence/abscence-detail.html',
                    controller: 'AbscenceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('abscence');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Abscence', function($stateParams, Abscence) {
                    return Abscence.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'abscence',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('abscence-detail.edit', {
            parent: 'abscence-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/abscence/abscence-dialog.html',
                    controller: 'AbscenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Abscence', function(Abscence) {
                            return Abscence.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('abscence.new', {
            parent: 'abscence',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/abscence/abscence-dialog.html',
                    controller: 'AbscenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                justifier: null,
                                dateD: null,
                                dateF: null,
                                etat: null,
                                integre: null,
                                commentaire: null,
                                oui: null,
                                non: null,
                                doc: null,
                                docContentType: null,
                                com: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('abscence', null, { reload: 'abscence' });
                }, function() {
                    $state.go('abscence');
                });
            }]
        })
        .state('abscence.edit', {
            parent: 'abscence',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/abscence/abscence-dialog.html',
                    controller: 'AbscenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Abscence', function(Abscence) {
                            return Abscence.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('abscence', null, { reload: 'abscence' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('abscence.delete', {
            parent: 'abscence',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/GRH/abscence/abscence-delete-dialog.html',
                    controller: 'AbscenceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Abscence', function(Abscence) {
                            return Abscence.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('abscence', null, { reload: 'abscence' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
