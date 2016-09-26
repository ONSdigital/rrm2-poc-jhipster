'use strict';

describe('Controller Tests', function() {

    describe('SampleSelection Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSampleSelection, MockCollectionExercise, MockReportingUnit, MockCollectionInstrument;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSampleSelection = jasmine.createSpy('MockSampleSelection');
            MockCollectionExercise = jasmine.createSpy('MockCollectionExercise');
            MockReportingUnit = jasmine.createSpy('MockReportingUnit');
            MockCollectionInstrument = jasmine.createSpy('MockCollectionInstrument');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'SampleSelection': MockSampleSelection,
                'CollectionExercise': MockCollectionExercise,
                'ReportingUnit': MockReportingUnit,
                'CollectionInstrument': MockCollectionInstrument
            };
            createController = function() {
                $injector.get('$controller')("SampleSelectionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'rrmApp:sampleSelectionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
