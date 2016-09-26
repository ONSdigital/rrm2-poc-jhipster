(function() {
    'use strict';

    angular
        .module('rrmApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('collection-instrument', {
            parent: 'entity',
            url: '/collection-instrument?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rrmApp.collectionInstrument.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/collection-instrument/collection-instruments.html',
                    controller: 'CollectionInstrumentController',
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
                    $translatePartialLoader.addPart('collectionInstrument');
                    $translatePartialLoader.addPart('collectionInstrumentKind');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('collection-instrument-detail', {
            parent: 'entity',
            url: '/collection-instrument/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rrmApp.collectionInstrument.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/collection-instrument/collection-instrument-detail.html',
                    controller: 'CollectionInstrumentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('collectionInstrument');
                    $translatePartialLoader.addPart('collectionInstrumentKind');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CollectionInstrument', function($stateParams, CollectionInstrument) {
                    return CollectionInstrument.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'collection-instrument',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('collection-instrument-detail.edit', {
            parent: 'collection-instrument-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collection-instrument/collection-instrument-dialog.html',
                    controller: 'CollectionInstrumentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CollectionInstrument', function(CollectionInstrument) {
                            return CollectionInstrument.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('collection-instrument.new', {
            parent: 'collection-instrument',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collection-instrument/collection-instrument-dialog.html',
                    controller: 'CollectionInstrumentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instrumentType: null,
                                formType: null,
                                urn: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('collection-instrument', null, { reload: 'collection-instrument' });
                }, function() {
                    $state.go('collection-instrument');
                });
            }]
        })
        .state('collection-instrument.edit', {
            parent: 'collection-instrument',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collection-instrument/collection-instrument-dialog.html',
                    controller: 'CollectionInstrumentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CollectionInstrument', function(CollectionInstrument) {
                            return CollectionInstrument.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('collection-instrument', null, { reload: 'collection-instrument' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('collection-instrument.delete', {
            parent: 'collection-instrument',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collection-instrument/collection-instrument-delete-dialog.html',
                    controller: 'CollectionInstrumentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CollectionInstrument', function(CollectionInstrument) {
                            return CollectionInstrument.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('collection-instrument', null, { reload: 'collection-instrument' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
