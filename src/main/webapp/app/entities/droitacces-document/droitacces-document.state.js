(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('droitacces-document', {
            parent: 'entity',
            url: '/droitacces-document?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.droitaccesDocument.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/droitacces-document/droitacces-documents.html',
                    controller: 'DroitaccesDocumentController',
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
                    $translatePartialLoader.addPart('droitaccesDocument');
                    $translatePartialLoader.addPart('roles');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('droitacces-document-detail', {
            parent: 'droitacces-document',
            url: '/droitacces-document/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.droitaccesDocument.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/droitacces-document/droitacces-document-detail.html',
                    controller: 'DroitaccesDocumentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('droitaccesDocument');
                    $translatePartialLoader.addPart('roles');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DroitaccesDocument', function($stateParams, DroitaccesDocument) {
                    return DroitaccesDocument.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'droitacces-document',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('droitacces-document-detail.edit', {
            parent: 'droitacces-document-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/droitacces-document/droitacces-document-dialog.html',
                    controller: 'DroitaccesDocumentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DroitaccesDocument', function(DroitaccesDocument) {
                            return DroitaccesDocument.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('droitacces-document.new', {
            parent: 'droitacces-document',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/droitacces-document/droitacces-document-dialog.html',
                    controller: 'DroitaccesDocumentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                roles: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('droitacces-document', null, { reload: 'droitacces-document' });
                }, function() {
                    $state.go('droitacces-document');
                });
            }]
        })
        .state('droitacces-document.edit', {
            parent: 'droitacces-document',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/droitacces-document/droitacces-document-dialog.html',
                    controller: 'DroitaccesDocumentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DroitaccesDocument', function(DroitaccesDocument) {
                            return DroitaccesDocument.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('droitacces-document', null, { reload: 'droitacces-document' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('droitacces-document.delete', {
            parent: 'droitacces-document',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/droitacces-document/droitacces-document-delete-dialog.html',
                    controller: 'DroitaccesDocumentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DroitaccesDocument', function(DroitaccesDocument) {
                            return DroitaccesDocument.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('droitacces-document', null, { reload: 'droitacces-document' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
