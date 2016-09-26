(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('ReportingUnitAssociationDeleteController',ReportingUnitAssociationDeleteController);

    ReportingUnitAssociationDeleteController.$inject = ['$uibModalInstance', 'entity', 'ReportingUnitAssociation'];

    function ReportingUnitAssociationDeleteController($uibModalInstance, entity, ReportingUnitAssociation) {
        var vm = this;

        vm.reportingUnitAssociation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ReportingUnitAssociation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
