(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('RespondentDialogController', RespondentDialogController);

    RespondentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Respondent', 'ReportingUnitAssociation', 'Enrolment'];

    function RespondentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Respondent, ReportingUnitAssociation, Enrolment) {
        var vm = this;

        vm.respondent = entity;
        vm.clear = clear;
        vm.save = save;
        vm.reportingunitassociations = ReportingUnitAssociation.query();
        vm.enrolments = Enrolment.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.respondent.id !== null) {
                Respondent.update(vm.respondent, onSaveSuccess, onSaveError);
            } else {
                Respondent.save(vm.respondent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rrmApp:respondentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
