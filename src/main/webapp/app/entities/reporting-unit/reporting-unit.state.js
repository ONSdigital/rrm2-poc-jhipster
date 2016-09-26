(function() {
    'use strict';

    angular
        .module('rrmApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('reporting-unit', {
            parent: 'entity',
            url: '/reporting-unit?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rrmApp.reportingUnit.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reporting-unit/reporting-units.html',
                    controller: 'ReportingUnitController',
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
                    $translatePartialLoader.addPart('reportingUnit');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('reporting-unit-detail', {
            parent: 'entity',
            url: '/reporting-unit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rrmApp.reportingUnit.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reporting-unit/reporting-unit-detail.html',
                    controller: 'ReportingUnitDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reportingUnit');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ReportingUnit', function($stateParams, ReportingUnit) {
                    return ReportingUnit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'reporting-unit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('reporting-unit-detail.edit', {
            parent: 'reporting-unit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reporting-unit/reporting-unit-dialog.html',
                    controller: 'ReportingUnitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ReportingUnit', function(ReportingUnit) {
                            return ReportingUnit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reporting-unit.new', {
            parent: 'reporting-unit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reporting-unit/reporting-unit-dialog.html',
                    controller: 'ReportingUnitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                uniqueReference: null,
                                businessName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('reporting-unit', null, { reload: 'reporting-unit' });
                }, function() {
                    $state.go('reporting-unit');
                });
            }]
        })
        .state('reporting-unit.edit', {
            parent: 'reporting-unit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reporting-unit/reporting-unit-dialog.html',
                    controller: 'ReportingUnitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ReportingUnit', function(ReportingUnit) {
                            return ReportingUnit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reporting-unit', null, { reload: 'reporting-unit' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reporting-unit.delete', {
            parent: 'reporting-unit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reporting-unit/reporting-unit-delete-dialog.html',
                    controller: 'ReportingUnitDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ReportingUnit', function(ReportingUnit) {
                            return ReportingUnit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reporting-unit', null, { reload: 'reporting-unit' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
