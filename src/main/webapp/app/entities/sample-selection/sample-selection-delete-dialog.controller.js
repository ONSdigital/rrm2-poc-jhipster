(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('SampleSelectionDeleteController',SampleSelectionDeleteController);

    SampleSelectionDeleteController.$inject = ['$uibModalInstance', 'entity', 'SampleSelection'];

    function SampleSelectionDeleteController($uibModalInstance, entity, SampleSelection) {
        var vm = this;

        vm.sampleSelection = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SampleSelection.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
