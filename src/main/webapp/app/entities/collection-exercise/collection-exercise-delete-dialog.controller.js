(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('CollectionExerciseDeleteController',CollectionExerciseDeleteController);

    CollectionExerciseDeleteController.$inject = ['$uibModalInstance', 'entity', 'CollectionExercise'];

    function CollectionExerciseDeleteController($uibModalInstance, entity, CollectionExercise) {
        var vm = this;

        vm.collectionExercise = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CollectionExercise.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
