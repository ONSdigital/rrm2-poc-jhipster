(function() {
    'use strict';

    angular
        .module('rrmApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sample-selection', {
            parent: 'entity',
            url: '/sample-selection?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rrmApp.sampleSelection.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sample-selection/sample-selections.html',
                    controller: 'SampleSelectionController',
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
                    $translatePartialLoader.addPart('sampleSelection');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sample-selection-detail', {
            parent: 'entity',
            url: '/sample-selection/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rrmApp.sampleSelection.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sample-selection/sample-selection-detail.html',
                    controller: 'SampleSelectionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sampleSelection');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SampleSelection', function($stateParams, SampleSelection) {
                    return SampleSelection.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sample-selection',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sample-selection-detail.edit', {
            parent: 'sample-selection-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sample-selection/sample-selection-dialog.html',
                    controller: 'SampleSelectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SampleSelection', function(SampleSelection) {
                            return SampleSelection.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sample-selection.new', {
            parent: 'sample-selection',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sample-selection/sample-selection-dialog.html',
                    controller: 'SampleSelectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sample-selection', null, { reload: 'sample-selection' });
                }, function() {
                    $state.go('sample-selection');
                });
            }]
        })
        .state('sample-selection.edit', {
            parent: 'sample-selection',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sample-selection/sample-selection-dialog.html',
                    controller: 'SampleSelectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SampleSelection', function(SampleSelection) {
                            return SampleSelection.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sample-selection', null, { reload: 'sample-selection' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sample-selection.delete', {
            parent: 'sample-selection',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sample-selection/sample-selection-delete-dialog.html',
                    controller: 'SampleSelectionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SampleSelection', function(SampleSelection) {
                            return SampleSelection.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sample-selection', null, { reload: 'sample-selection' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
