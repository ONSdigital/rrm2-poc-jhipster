(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('ReportingUnitDetailController', ReportingUnitDetailController);

    ReportingUnitDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ReportingUnit', 'ReportingUnitAssociation'];

    function ReportingUnitDetailController($scope, $rootScope, $stateParams, previousState, entity, ReportingUnit, ReportingUnitAssociation) {
        var vm = this;

        vm.reportingUnit = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('rrmApp:reportingUnitUpdate', function(event, result) {
            vm.reportingUnit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
