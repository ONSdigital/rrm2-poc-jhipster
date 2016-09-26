(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('ReportingUnitDeleteController',ReportingUnitDeleteController);

    ReportingUnitDeleteController.$inject = ['$uibModalInstance', 'entity', 'ReportingUnit'];

    function ReportingUnitDeleteController($uibModalInstance, entity, ReportingUnit) {
        var vm = this;

        vm.reportingUnit = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ReportingUnit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
