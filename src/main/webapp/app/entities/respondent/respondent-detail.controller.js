(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('RespondentDetailController', RespondentDetailController);

    RespondentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Respondent', 'ReportingUnitAssociation', 'Enrolment'];

    function RespondentDetailController($scope, $rootScope, $stateParams, previousState, entity, Respondent, ReportingUnitAssociation, Enrolment) {
        var vm = this;

        vm.respondent = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('rrmApp:respondentUpdate', function(event, result) {
            vm.respondent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
