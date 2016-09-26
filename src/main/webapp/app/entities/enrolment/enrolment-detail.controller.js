(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('EnrolmentDetailController', EnrolmentDetailController);

    EnrolmentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Enrolment', 'Respondent', 'ReportingUnitAssociation', 'Survey'];

    function EnrolmentDetailController($scope, $rootScope, $stateParams, previousState, entity, Enrolment, Respondent, ReportingUnitAssociation, Survey) {
        var vm = this;

        vm.enrolment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('rrmApp:enrolmentUpdate', function(event, result) {
            vm.enrolment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
