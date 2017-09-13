(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('document-interne', {
            parent: 'entity',
            url: '/document-interne?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.documentInterne.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/document-interne/document-internes.html',
                    controller: 'DocumentInterneController',
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
                    $translatePartialLoader.addPart('documentInterne');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('document-interne-detail', {
            parent: 'document-interne',
            url: '/document-interne/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.documentInterne.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/document-interne/document-interne-detail.html',
                    controller: 'DocumentInterneDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('documentInterne');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DocumentInterne', function($stateParams, DocumentInterne) {
                    return DocumentInterne.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'document-interne',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('document-interne-detail.edit', {
            parent: 'document-interne-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/document-interne/document-interne-dialog.html',
                    controller: 'DocumentInterneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DocumentInterne', function(DocumentInterne) {
                            return DocumentInterne.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('document-interne.new', {
            parent: 'document-interne',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/document-interne/document-interne-dialog.html',
                    controller: 'DocumentInterneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                libelle: null,
                                fichier: null,
                                fichierContentType: null,
                                motif: null,
                                message: null,
                                etat: null,
                                date: null,
                                v: null,
                                r: null,
                                a: null,
                                typeuser: null,
                                precedent: null,
                                suivant: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('document-interne', null, { reload: 'document-interne' });
                }, function() {
                    $state.go('document-interne');
                });
            }]
        })
        .state('document-interne.edit', {
            parent: 'document-interne',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/document-interne/document-interne-dialog.html',
                    controller: 'DocumentInterneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DocumentInterne', function(DocumentInterne) {
                            return DocumentInterne.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('document-interne', null, { reload: 'document-interne' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('document-interne.delete', {
            parent: 'document-interne',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/document-interne/document-interne-delete-dialog.html',
                    controller: 'DocumentInterneDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DocumentInterne', function(DocumentInterne) {
                            return DocumentInterne.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('document-interne', null, { reload: 'document-interne' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
