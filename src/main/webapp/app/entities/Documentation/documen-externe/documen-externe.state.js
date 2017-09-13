(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('documen-externe', {
            parent: 'entity',
            url: '/documen-externe?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.documenExterne.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/Documentation/documen-externe/documen-externes.html',
                    controller: 'DocumenExterneController',
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
                    $translatePartialLoader.addPart('documenExterne');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('documen-externe-detail', {
            parent: 'documen-externe',
            url: '/documen-externe/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.documenExterne.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/Documentation/documen-externe/documen-externe-detail.html',
                    controller: 'DocumenExterneDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('documenExterne');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DocumenExterne', function($stateParams, DocumenExterne) {
                    return DocumenExterne.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'documen-externe',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('documen-externe-detail.edit', {
            parent: 'documen-externe-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/Documentation/documen-externe/documen-externe-dialog.html',
                    controller: 'DocumenExterneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DocumenExterne', function(DocumenExterne) {
                            return DocumenExterne.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('documen-externe.new', {
            parent: 'documen-externe',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/Documentation/documen-externe/documen-externe-dialog.html',
                    controller: 'DocumenExterneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                libele: null,
                                daterevision: null,
                                indiceEvolution: null,
                                fichier: null,
                                fichierContentType: null,
                                notif: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('documen-externe', null, { reload: 'documen-externe' });
                }, function() {
                    $state.go('documen-externe');
                });
            }]
        })
        .state('documen-externe.edit', {
            parent: 'documen-externe',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/Documentation/documen-externe/documen-externe-dialog.html',
                    controller: 'DocumenExterneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DocumenExterne', function(DocumenExterne) {
                            return DocumenExterne.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('documen-externe', null, { reload: 'documen-externe' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('documen-externe.delete', {
            parent: 'documen-externe',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/Documentation/documen-externe/documen-externe-delete-dialog.html',
                    controller: 'DocumenExterneDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DocumenExterne', function(DocumenExterne) {
                            return DocumenExterne.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('documen-externe', null, { reload: 'documen-externe' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
