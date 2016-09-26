(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('CollectionResponseDialogController', CollectionResponseDialogController);

    CollectionResponseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'CollectionResponse', 'SampleSelection'];

    function CollectionResponseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, CollectionResponse, SampleSelection) {
        var vm = this;

        vm.collectionResponse = entity;
        vm.clear = clear;
        vm.save = save;
        vm.relatestos = SampleSelection.query({filter: 'collectionresponse-is-null'});
        $q.all([vm.collectionResponse.$promise, vm.relatestos.$promise]).then(function() {
            if (!vm.collectionResponse.relatesTo || !vm.collectionResponse.relatesTo.id) {
                return $q.reject();
            }
            return SampleSelection.get({id : vm.collectionResponse.relatesTo.id}).$promise;
        }).then(function(relatesTo) {
            vm.relatestos.push(relatesTo);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.collectionResponse.id !== null) {
                CollectionResponse.update(vm.collectionResponse, onSaveSuccess, onSaveError);
            } else {
                CollectionResponse.save(vm.collectionResponse, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rrmApp:collectionResponseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
