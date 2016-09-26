(function() {
    'use strict';

    angular
        .module('rrmApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('collection-response', {
            parent: 'entity',
            url: '/collection-response?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rrmApp.collectionResponse.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/collection-response/collection-responses.html',
                    controller: 'CollectionResponseController',
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
                    $translatePartialLoader.addPart('collectionResponse');
                    $translatePartialLoader.addPart('responseStatusKind');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('collection-response-detail', {
            parent: 'entity',
            url: '/collection-response/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rrmApp.collectionResponse.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/collection-response/collection-response-detail.html',
                    controller: 'CollectionResponseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('collectionResponse');
                    $translatePartialLoader.addPart('responseStatusKind');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CollectionResponse', function($stateParams, CollectionResponse) {
                    return CollectionResponse.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'collection-response',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('collection-response-detail.edit', {
            parent: 'collection-response-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collection-response/collection-response-dialog.html',
                    controller: 'CollectionResponseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CollectionResponse', function(CollectionResponse) {
                            return CollectionResponse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('collection-response.new', {
            parent: 'collection-response',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collection-response/collection-response-dialog.html',
                    controller: 'CollectionResponseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('collection-response', null, { reload: 'collection-response' });
                }, function() {
                    $state.go('collection-response');
                });
            }]
        })
        .state('collection-response.edit', {
            parent: 'collection-response',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collection-response/collection-response-dialog.html',
                    controller: 'CollectionResponseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CollectionResponse', function(CollectionResponse) {
                            return CollectionResponse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('collection-response', null, { reload: 'collection-response' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('collection-response.delete', {
            parent: 'collection-response',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collection-response/collection-response-delete-dialog.html',
                    controller: 'CollectionResponseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CollectionResponse', function(CollectionResponse) {
                            return CollectionResponse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('collection-response', null, { reload: 'collection-response' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
