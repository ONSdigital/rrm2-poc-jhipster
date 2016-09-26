(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('SampleSelectionDialogController', SampleSelectionDialogController);

    SampleSelectionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SampleSelection', 'CollectionExercise', 'ReportingUnit', 'CollectionInstrument'];

    function SampleSelectionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SampleSelection, CollectionExercise, ReportingUnit, CollectionInstrument) {
        var vm = this;

        vm.sampleSelection = entity;
        vm.clear = clear;
        vm.save = save;
        vm.collectionexercises = CollectionExercise.query();
        vm.reportingunits = ReportingUnit.query();
        vm.collectioninstruments = CollectionInstrument.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sampleSelection.id !== null) {
                SampleSelection.update(vm.sampleSelection, onSaveSuccess, onSaveError);
            } else {
                SampleSelection.save(vm.sampleSelection, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rrmApp:sampleSelectionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
