(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('programme', {
            parent: 'entity',
            url: '/programme',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.programme.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/programme/programmes.html',
                    controller: 'ProgrammeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('programme');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('programme-detail', {
            parent: 'programme',
            url: '/programme/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'qualiMakerApp.programme.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/programme/programme-detail.html',
                    controller: 'ProgrammeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('programme');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Programme', function($stateParams, Programme) {
                    return Programme.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'programme',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('programme-detail.edit', {
            parent: 'programme-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/programme/programme-dialog.html',
                    controller: 'ProgrammeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Programme', function(Programme) {
                            return Programme.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('programme.new', {
            parent: 'programme',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/programme/programme-dialog.html',
                    controller: 'ProgrammeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                matin: null,
                                apresmidi: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('programme', null, { reload: 'programme' });
                }, function() {
                    $state.go('programme');
                });
            }]
        })
        .state('programme.edit', {
            parent: 'programme',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/programme/programme-dialog.html',
                    controller: 'ProgrammeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Programme', function(Programme) {
                            return Programme.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('programme', null, { reload: 'programme' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('programme.delete', {
            parent: 'programme',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/programme/programme-delete-dialog.html',
                    controller: 'ProgrammeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Programme', function(Programme) {
                            return Programme.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('programme', null, { reload: 'programme' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
