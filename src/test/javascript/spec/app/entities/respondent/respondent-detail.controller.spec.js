'use strict';

describe('Controller Tests', function() {

    describe('Respondent Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRespondent, MockReportingUnitAssociation, MockEnrolment;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRespondent = jasmine.createSpy('MockRespondent');
            MockReportingUnitAssociation = jasmine.createSpy('MockReportingUnitAssociation');
            MockEnrolment = jasmine.createSpy('MockEnrolment');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Respondent': MockRespondent,
                'ReportingUnitAssociation': MockReportingUnitAssociation,
                'Enrolment': MockEnrolment
            };
            createController = function() {
                $injector.get('$controller')("RespondentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'rrmApp:respondentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
