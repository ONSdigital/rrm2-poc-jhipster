'use strict';

describe('Controller Tests', function() {

    describe('Enrolment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEnrolment, MockRespondent, MockReportingUnitAssociation, MockSurvey;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEnrolment = jasmine.createSpy('MockEnrolment');
            MockRespondent = jasmine.createSpy('MockRespondent');
            MockReportingUnitAssociation = jasmine.createSpy('MockReportingUnitAssociation');
            MockSurvey = jasmine.createSpy('MockSurvey');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Enrolment': MockEnrolment,
                'Respondent': MockRespondent,
                'ReportingUnitAssociation': MockReportingUnitAssociation,
                'Survey': MockSurvey
            };
            createController = function() {
                $injector.get('$controller')("EnrolmentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'rrmApp:enrolmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
