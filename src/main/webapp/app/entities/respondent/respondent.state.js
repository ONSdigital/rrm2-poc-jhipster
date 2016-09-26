(function() {
    'use strict';

    angular
        .module('rrmApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('respondent', {
            parent: 'entity',
            url: '/respondent?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rrmApp.respondent.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/respondent/respondents.html',
                    controller: 'RespondentController',
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
                    $translatePartialLoader.addPart('respondent');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('respondent-detail', {
            parent: 'entity',
            url: '/respondent/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'rrmApp.respondent.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/respondent/respondent-detail.html',
                    controller: 'RespondentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('respondent');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Respondent', function($stateParams, Respondent) {
                    return Respondent.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'respondent',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('respondent-detail.edit', {
            parent: 'respondent-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/respondent/respondent-dialog.html',
                    controller: 'RespondentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Respondent', function(Respondent) {
                            return Respondent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('respondent.new', {
            parent: 'respondent',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/respondent/respondent-dialog.html',
                    controller: 'RespondentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                emailAddress: null,
                                firstName: null,
                                lastName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('respondent', null, { reload: 'respondent' });
                }, function() {
                    $state.go('respondent');
                });
            }]
        })
        .state('respondent.edit', {
            parent: 'respondent',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/respondent/respondent-dialog.html',
                    controller: 'RespondentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Respondent', function(Respondent) {
                            return Respondent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('respondent', null, { reload: 'respondent' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('respondent.delete', {
            parent: 'respondent',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/respondent/respondent-delete-dialog.html',
                    controller: 'RespondentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Respondent', function(Respondent) {
                            return Respondent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('respondent', null, { reload: 'respondent' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
