(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-documentation', {
            parent: 'entity',
            url: '/type-documentation?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.typeDocumentation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-documentation/type-documentations.html',
                    controller: 'TypeDocumentationController',
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
                    $translatePartialLoader.addPart('typeDocumentation');
                    $translatePartialLoader.addPart('natureDoc');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-documentation-detail', {
            parent: 'type-documentation',
            url: '/type-documentation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.typeDocumentation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-documentation/type-documentation-detail.html',
                    controller: 'TypeDocumentationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeDocumentation');
                    $translatePartialLoader.addPart('natureDoc');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypeDocumentation', function($stateParams, TypeDocumentation) {
                    return TypeDocumentation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'type-documentation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('type-documentation-detail.edit', {
            parent: 'type-documentation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-documentation/type-documentation-dialog.html',
                    controller: 'TypeDocumentationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeDocumentation', function(TypeDocumentation) {
                            return TypeDocumentation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-documentation.new', {
            parent: 'type-documentation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-documentation/type-documentation-dialog.html',
                    controller: 'TypeDocumentationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                type: null,
                                pDf: null,
                                securise: null,
                                abreviation: null,
                                nature: null,
                                notif: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('type-documentation', null, { reload: 'type-documentation' });
                }, function() {
                    $state.go('type-documentation');
                });
            }]
        })
        .state('type-documentation.edit', {
            parent: 'type-documentation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-documentation/type-documentation-dialog.html',
                    controller: 'TypeDocumentationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeDocumentation', function(TypeDocumentation) {
                            return TypeDocumentation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-documentation', null, { reload: 'type-documentation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-documentation.delete', {
            parent: 'type-documentation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-documentation/type-documentation-delete-dialog.html',
                    controller: 'TypeDocumentationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypeDocumentation', function(TypeDocumentation) {
                            return TypeDocumentation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-documentation', null, { reload: 'type-documentation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
