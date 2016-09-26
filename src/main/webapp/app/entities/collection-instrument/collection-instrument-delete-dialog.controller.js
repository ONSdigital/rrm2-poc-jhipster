(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('CollectionInstrumentDeleteController',CollectionInstrumentDeleteController);

    CollectionInstrumentDeleteController.$inject = ['$uibModalInstance', 'entity', 'CollectionInstrument'];

    function CollectionInstrumentDeleteController($uibModalInstance, entity, CollectionInstrument) {
        var vm = this;

        vm.collectionInstrument = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CollectionInstrument.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
