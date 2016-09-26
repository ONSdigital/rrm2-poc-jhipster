(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('SampleSelectionDetailController', SampleSelectionDetailController);

    SampleSelectionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SampleSelection', 'CollectionExercise', 'ReportingUnit', 'CollectionInstrument'];

    function SampleSelectionDetailController($scope, $rootScope, $stateParams, previousState, entity, SampleSelection, CollectionExercise, ReportingUnit, CollectionInstrument) {
        var vm = this;

        vm.sampleSelection = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('rrmApp:sampleSelectionUpdate', function(event, result) {
            vm.sampleSelection = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
