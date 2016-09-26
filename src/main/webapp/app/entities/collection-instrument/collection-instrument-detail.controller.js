(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('CollectionInstrumentDetailController', CollectionInstrumentDetailController);

    CollectionInstrumentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CollectionInstrument'];

    function CollectionInstrumentDetailController($scope, $rootScope, $stateParams, previousState, entity, CollectionInstrument) {
        var vm = this;

        vm.collectionInstrument = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('rrmApp:collectionInstrumentUpdate', function(event, result) {
            vm.collectionInstrument = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
