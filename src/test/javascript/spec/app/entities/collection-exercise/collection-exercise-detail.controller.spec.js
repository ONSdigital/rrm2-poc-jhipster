'use strict';

describe('Controller Tests', function() {

    describe('CollectionExercise Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCollectionExercise, MockSurvey, MockSampleSelection;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCollectionExercise = jasmine.createSpy('MockCollectionExercise');
            MockSurvey = jasmine.createSpy('MockSurvey');
            MockSampleSelection = jasmine.createSpy('MockSampleSelection');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'CollectionExercise': MockCollectionExercise,
                'Survey': MockSurvey,
                'SampleSelection': MockSampleSelection
            };
            createController = function() {
                $injector.get('$controller')("CollectionExerciseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'rrmApp:collectionExerciseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
