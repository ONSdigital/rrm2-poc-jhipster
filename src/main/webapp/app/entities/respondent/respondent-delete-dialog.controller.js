(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('RespondentDeleteController',RespondentDeleteController);

    RespondentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Respondent'];

    function RespondentDeleteController($uibModalInstance, entity, Respondent) {
        var vm = this;

        vm.respondent = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Respondent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
