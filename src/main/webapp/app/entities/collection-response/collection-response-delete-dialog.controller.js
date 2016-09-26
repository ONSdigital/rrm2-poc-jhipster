(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('CollectionResponseDeleteController',CollectionResponseDeleteController);

    CollectionResponseDeleteController.$inject = ['$uibModalInstance', 'entity', 'CollectionResponse'];

    function CollectionResponseDeleteController($uibModalInstance, entity, CollectionResponse) {
        var vm = this;

        vm.collectionResponse = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CollectionResponse.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
