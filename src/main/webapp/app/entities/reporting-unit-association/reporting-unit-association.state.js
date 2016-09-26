(function() {
    'use strict';

    angular
        .module('rrmApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('reporting-unit-association', {
            parent: 'entity',
            url: '/reporting-unit-association?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rrmApp.reportingUnitAssociation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reporting-unit-association/reporting-unit-associations.html',
                    controller: 'ReportingUnitAssociationController',
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
                    $translatePartialLoader.addPart('reportingUnitAssociation');
                    $translatePartialLoader.addPart('associationStatusKind');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('reporting-unit-association-detail', {
            parent: 'entity',
            url: '/reporting-unit-association/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rrmApp.reportingUnitAssociation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reporting-unit-association/reporting-unit-association-detail.html',
                    controller: 'ReportingUnitAssociationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reportingUnitAssociation');
                    $translatePartialLoader.addPart('associationStatusKind');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ReportingUnitAssociation', function($stateParams, ReportingUnitAssociation) {
                    return ReportingUnitAssociation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'reporting-unit-association',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('reporting-unit-association-detail.edit', {
            parent: 'reporting-unit-association-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reporting-unit-association/reporting-unit-association-dialog.html',
                    controller: 'ReportingUnitAssociationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ReportingUnitAssociation', function(ReportingUnitAssociation) {
                            return ReportingUnitAssociation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reporting-unit-association.new', {
            parent: 'reporting-unit-association',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reporting-unit-association/reporting-unit-association-dialog.html',
                    controller: 'ReportingUnitAssociationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                associationStatus: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('reporting-unit-association', null, { reload: 'reporting-unit-association' });
                }, function() {
                    $state.go('reporting-unit-association');
                });
            }]
        })
        .state('reporting-unit-association.edit', {
            parent: 'reporting-unit-association',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reporting-unit-association/reporting-unit-association-dialog.html',
                    controller: 'ReportingUnitAssociationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ReportingUnitAssociation', function(ReportingUnitAssociation) {
                            return ReportingUnitAssociation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reporting-unit-association', null, { reload: 'reporting-unit-association' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reporting-unit-association.delete', {
            parent: 'reporting-unit-association',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reporting-unit-association/reporting-unit-association-delete-dialog.html',
                    controller: 'ReportingUnitAssociationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ReportingUnitAssociation', function(ReportingUnitAssociation) {
                            return ReportingUnitAssociation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reporting-unit-association', null, { reload: 'reporting-unit-association' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
