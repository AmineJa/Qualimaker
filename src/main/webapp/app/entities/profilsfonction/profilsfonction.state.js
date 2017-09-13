(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('profilsfonction', {
            parent: 'entity',
            url: '/profilsfonction?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.profilsfonction.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/profilsfonction/profilsfonctions.html',
                    controller: 'ProfilsfonctionController',
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
                    $translatePartialLoader.addPart('profilsfonction');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('profilsfonction-detail', {
            parent: 'profilsfonction',
            url: '/profilsfonction/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.profilsfonction.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/profilsfonction/profilsfonction-detail.html',
                    controller: 'ProfilsfonctionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('profilsfonction');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Profilsfonction', function($stateParams, Profilsfonction) {
                    return Profilsfonction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'profilsfonction',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('profilsfonction-detail.edit', {
            parent: 'profilsfonction-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profilsfonction/profilsfonction-dialog.html',
                    controller: 'ProfilsfonctionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Profilsfonction', function(Profilsfonction) {
                            return Profilsfonction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('profilsfonction.new', {
            parent: 'profilsfonction',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profilsfonction/profilsfonction-dialog.html',
                    controller: 'ProfilsfonctionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                profil: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('profilsfonction', null, { reload: 'profilsfonction' });
                }, function() {
                    $state.go('profilsfonction');
                });
            }]
        })
        .state('profilsfonction.edit', {
            parent: 'profilsfonction',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profilsfonction/profilsfonction-dialog.html',
                    controller: 'ProfilsfonctionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Profilsfonction', function(Profilsfonction) {
                            return Profilsfonction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('profilsfonction', null, { reload: 'profilsfonction' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('profilsfonction.delete', {
            parent: 'profilsfonction',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profilsfonction/profilsfonction-delete-dialog.html',
                    controller: 'ProfilsfonctionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Profilsfonction', function(Profilsfonction) {
                            return Profilsfonction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('profilsfonction', null, { reload: 'profilsfonction' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
