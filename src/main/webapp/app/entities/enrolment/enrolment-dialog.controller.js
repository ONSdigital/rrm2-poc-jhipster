(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('EnrolmentDialogController', EnrolmentDialogController);

    EnrolmentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Enrolment', 'Respondent', 'ReportingUnitAssociation', 'Survey'];

    function EnrolmentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Enrolment, Respondent, ReportingUnitAssociation, Survey) {
        var vm = this;

        vm.enrolment = entity;
        vm.clear = clear;
        vm.save = save;
        vm.respondents = Respondent.query();
        vm.reportingunitassociations = ReportingUnitAssociation.query();
        vm.participatesins = Survey.query({filter: 'enrolment-is-null'});
        $q.all([vm.enrolment.$promise, vm.participatesins.$promise]).then(function() {
            if (!vm.enrolment.participatesIn || !vm.enrolment.participatesIn.id) {
                return $q.reject();
            }
            return Survey.get({id : vm.enrolment.participatesIn.id}).$promise;
        }).then(function(participatesIn) {
            vm.participatesins.push(participatesIn);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.enrolment.id !== null) {
                Enrolment.update(vm.enrolment, onSaveSuccess, onSaveError);
            } else {
                Enrolment.save(vm.enrolment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rrmApp:enrolmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
