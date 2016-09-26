(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('EnrolmentCodeDetailController', EnrolmentCodeDetailController);

    EnrolmentCodeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EnrolmentCode', 'SampleSelection'];

    function EnrolmentCodeDetailController($scope, $rootScope, $stateParams, previousState, entity, EnrolmentCode, SampleSelection) {
        var vm = this;

        vm.enrolmentCode = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('rrmApp:enrolmentCodeUpdate', function(event, result) {
            vm.enrolmentCode = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
