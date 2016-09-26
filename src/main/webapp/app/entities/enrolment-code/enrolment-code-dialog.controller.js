(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('EnrolmentCodeDialogController', EnrolmentCodeDialogController);

    EnrolmentCodeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EnrolmentCode', 'SampleSelection'];

    function EnrolmentCodeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EnrolmentCode, SampleSelection) {
        var vm = this;

        vm.enrolmentCode = entity;
        vm.clear = clear;
        vm.save = save;
        vm.sampleselections = SampleSelection.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.enrolmentCode.id !== null) {
                EnrolmentCode.update(vm.enrolmentCode, onSaveSuccess, onSaveError);
            } else {
                EnrolmentCode.save(vm.enrolmentCode, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rrmApp:enrolmentCodeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
