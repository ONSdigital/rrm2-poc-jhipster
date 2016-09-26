(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('ReportingUnitAssociationDialogController', ReportingUnitAssociationDialogController);

    ReportingUnitAssociationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ReportingUnitAssociation', 'ReportingUnit', 'Respondent', 'Enrolment'];

    function ReportingUnitAssociationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ReportingUnitAssociation, ReportingUnit, Respondent, Enrolment) {
        var vm = this;

        vm.reportingUnitAssociation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.reportingunits = ReportingUnit.query();
        vm.respondents = Respondent.query();
        vm.enrolments = Enrolment.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.reportingUnitAssociation.id !== null) {
                ReportingUnitAssociation.update(vm.reportingUnitAssociation, onSaveSuccess, onSaveError);
            } else {
                ReportingUnitAssociation.save(vm.reportingUnitAssociation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rrmApp:reportingUnitAssociationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
