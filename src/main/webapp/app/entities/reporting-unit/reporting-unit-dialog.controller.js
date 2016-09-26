(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('ReportingUnitDialogController', ReportingUnitDialogController);

    ReportingUnitDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ReportingUnit', 'ReportingUnitAssociation'];

    function ReportingUnitDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ReportingUnit, ReportingUnitAssociation) {
        var vm = this;

        vm.reportingUnit = entity;
        vm.clear = clear;
        vm.save = save;
        vm.reportingunitassociations = ReportingUnitAssociation.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.reportingUnit.id !== null) {
                ReportingUnit.update(vm.reportingUnit, onSaveSuccess, onSaveError);
            } else {
                ReportingUnit.save(vm.reportingUnit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rrmApp:reportingUnitUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
