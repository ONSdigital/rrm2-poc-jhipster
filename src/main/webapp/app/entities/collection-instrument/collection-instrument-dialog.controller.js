(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('CollectionInstrumentDialogController', CollectionInstrumentDialogController);

    CollectionInstrumentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CollectionInstrument'];

    function CollectionInstrumentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CollectionInstrument) {
        var vm = this;

        vm.collectionInstrument = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.collectionInstrument.id !== null) {
                CollectionInstrument.update(vm.collectionInstrument, onSaveSuccess, onSaveError);
            } else {
                CollectionInstrument.save(vm.collectionInstrument, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rrmApp:collectionInstrumentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
