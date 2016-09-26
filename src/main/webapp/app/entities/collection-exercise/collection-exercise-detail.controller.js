(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('CollectionExerciseDetailController', CollectionExerciseDetailController);

    CollectionExerciseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CollectionExercise', 'Survey', 'SampleSelection'];

    function CollectionExerciseDetailController($scope, $rootScope, $stateParams, previousState, entity, CollectionExercise, Survey, SampleSelection) {
        var vm = this;

        vm.collectionExercise = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('rrmApp:collectionExerciseUpdate', function(event, result) {
            vm.collectionExercise = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
