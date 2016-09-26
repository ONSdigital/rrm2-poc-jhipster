(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('CollectionExerciseDialogController', CollectionExerciseDialogController);

    CollectionExerciseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CollectionExercise', 'Survey', 'SampleSelection'];

    function CollectionExerciseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CollectionExercise, Survey, SampleSelection) {
        var vm = this;

        vm.collectionExercise = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.surveys = Survey.query();
        vm.sampleselections = SampleSelection.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.collectionExercise.id !== null) {
                CollectionExercise.update(vm.collectionExercise, onSaveSuccess, onSaveError);
            } else {
                CollectionExercise.save(vm.collectionExercise, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rrmApp:collectionExerciseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
