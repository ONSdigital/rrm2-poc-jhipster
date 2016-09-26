'use strict';

describe('Controller Tests', function() {

    describe('ReportingUnitAssociation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockReportingUnitAssociation, MockReportingUnit, MockRespondent, MockEnrolment;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockReportingUnitAssociation = jasmine.createSpy('MockReportingUnitAssociation');
            MockReportingUnit = jasmine.createSpy('MockReportingUnit');
            MockRespondent = jasmine.createSpy('MockRespondent');
            MockEnrolment = jasmine.createSpy('MockEnrolment');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ReportingUnitAssociation': MockReportingUnitAssociation,
                'ReportingUnit': MockReportingUnit,
                'Respondent': MockRespondent,
                'Enrolment': MockEnrolment
            };
            createController = function() {
                $injector.get('$controller')("ReportingUnitAssociationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'rrmApp:reportingUnitAssociationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
