(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('ReportingUnitAssociationDetailController', ReportingUnitAssociationDetailController);

    ReportingUnitAssociationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ReportingUnitAssociation', 'ReportingUnit', 'Respondent', 'Enrolment'];

    function ReportingUnitAssociationDetailController($scope, $rootScope, $stateParams, previousState, entity, ReportingUnitAssociation, ReportingUnit, Respondent, Enrolment) {
        var vm = this;

        vm.reportingUnitAssociation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('rrmApp:reportingUnitAssociationUpdate', function(event, result) {
            vm.reportingUnitAssociation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
