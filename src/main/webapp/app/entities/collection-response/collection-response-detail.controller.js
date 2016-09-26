(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('CollectionResponseDetailController', CollectionResponseDetailController);

    CollectionResponseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CollectionResponse', 'SampleSelection'];

    function CollectionResponseDetailController($scope, $rootScope, $stateParams, previousState, entity, CollectionResponse, SampleSelection) {
        var vm = this;

        vm.collectionResponse = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('rrmApp:collectionResponseUpdate', function(event, result) {
            vm.collectionResponse = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
