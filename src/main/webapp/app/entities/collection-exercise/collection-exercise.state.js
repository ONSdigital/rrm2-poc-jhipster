(function() {
    'use strict';

    angular
        .module('rrmApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('collection-exercise', {
            parent: 'entity',
            url: '/collection-exercise?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rrmApp.collectionExercise.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/collection-exercise/collection-exercises.html',
                    controller: 'CollectionExerciseController',
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
                    $translatePartialLoader.addPart('collectionExercise');
                    $translatePartialLoader.addPart('collectionExerciseStatusKind');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('collection-exercise-detail', {
            parent: 'entity',
            url: '/collection-exercise/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rrmApp.collectionExercise.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/collection-exercise/collection-exercise-detail.html',
                    controller: 'CollectionExerciseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('collectionExercise');
                    $translatePartialLoader.addPart('collectionExerciseStatusKind');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CollectionExercise', function($stateParams, CollectionExercise) {
                    return CollectionExercise.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'collection-exercise',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('collection-exercise-detail.edit', {
            parent: 'collection-exercise-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collection-exercise/collection-exercise-dialog.html',
                    controller: 'CollectionExerciseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CollectionExercise', function(CollectionExercise) {
                            return CollectionExercise.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('collection-exercise.new', {
            parent: 'collection-exercise',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collection-exercise/collection-exercise-dialog.html',
                    controller: 'CollectionExerciseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                startDate: null,
                                endDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('collection-exercise', null, { reload: 'collection-exercise' });
                }, function() {
                    $state.go('collection-exercise');
                });
            }]
        })
        .state('collection-exercise.edit', {
            parent: 'collection-exercise',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collection-exercise/collection-exercise-dialog.html',
                    controller: 'CollectionExerciseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CollectionExercise', function(CollectionExercise) {
                            return CollectionExercise.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('collection-exercise', null, { reload: 'collection-exercise' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('collection-exercise.delete', {
            parent: 'collection-exercise',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/collection-exercise/collection-exercise-delete-dialog.html',
                    controller: 'CollectionExerciseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CollectionExercise', function(CollectionExercise) {
                            return CollectionExercise.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('collection-exercise', null, { reload: 'collection-exercise' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
