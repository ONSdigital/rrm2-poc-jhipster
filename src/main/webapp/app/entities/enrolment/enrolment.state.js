(function() {
    'use strict';

    angular
        .module('rrmApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('enrolment', {
            parent: 'entity',
            url: '/enrolment?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rrmApp.enrolment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/enrolment/enrolments.html',
                    controller: 'EnrolmentController',
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
                    $translatePartialLoader.addPart('enrolment');
                    $translatePartialLoader.addPart('enrolmentStatusKind');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('enrolment-detail', {
            parent: 'entity',
            url: '/enrolment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rrmApp.enrolment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/enrolment/enrolment-detail.html',
                    controller: 'EnrolmentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('enrolment');
                    $translatePartialLoader.addPart('enrolmentStatusKind');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Enrolment', function($stateParams, Enrolment) {
                    return Enrolment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'enrolment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('enrolment-detail.edit', {
            parent: 'enrolment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/enrolment/enrolment-dialog.html',
                    controller: 'EnrolmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Enrolment', function(Enrolment) {
                            return Enrolment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('enrolment.new', {
            parent: 'enrolment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/enrolment/enrolment-dialog.html',
                    controller: 'EnrolmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                enrolmentStatus: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('enrolment', null, { reload: 'enrolment' });
                }, function() {
                    $state.go('enrolment');
                });
            }]
        })
        .state('enrolment.edit', {
            parent: 'enrolment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/enrolment/enrolment-dialog.html',
                    controller: 'EnrolmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Enrolment', function(Enrolment) {
                            return Enrolment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('enrolment', null, { reload: 'enrolment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('enrolment.delete', {
            parent: 'enrolment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/enrolment/enrolment-delete-dialog.html',
                    controller: 'EnrolmentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Enrolment', function(Enrolment) {
                            return Enrolment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('enrolment', null, { reload: 'enrolment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
