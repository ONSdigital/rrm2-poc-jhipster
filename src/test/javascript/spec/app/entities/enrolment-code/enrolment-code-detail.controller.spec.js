'use strict';

describe('Controller Tests', function() {

    describe('EnrolmentCode Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEnrolmentCode, MockSampleSelection;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEnrolmentCode = jasmine.createSpy('MockEnrolmentCode');
            MockSampleSelection = jasmine.createSpy('MockSampleSelection');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'EnrolmentCode': MockEnrolmentCode,
                'SampleSelection': MockSampleSelection
            };
            createController = function() {
                $injector.get('$controller')("EnrolmentCodeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'rrmApp:enrolmentCodeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
