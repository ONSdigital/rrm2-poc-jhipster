(function() {
    'use strict';

    angular
        .module('rrmApp')
        .controller('SurveyDetailController', SurveyDetailController);

    SurveyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Survey', 'CollectionExercise'];

    function SurveyDetailController($scope, $rootScope, $stateParams, previousState, entity, Survey, CollectionExercise) {
        var vm = this;

        vm.survey = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('rrmApp:surveyUpdate', function(event, result) {
            vm.survey = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
