(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('EnrolmentDeleteController',EnrolmentDeleteController);

    EnrolmentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Enrolment'];

    function EnrolmentDeleteController($uibModalInstance, entity, Enrolment) {
        var vm = this;

        vm.enrolment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Enrolment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
