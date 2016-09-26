(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('EnrolmentCodeDeleteController',EnrolmentCodeDeleteController);

    EnrolmentCodeDeleteController.$inject = ['$uibModalInstance', 'entity', 'EnrolmentCode'];

    function EnrolmentCodeDeleteController($uibModalInstance, entity, EnrolmentCode) {
        var vm = this;

        vm.enrolmentCode = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EnrolmentCode.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
