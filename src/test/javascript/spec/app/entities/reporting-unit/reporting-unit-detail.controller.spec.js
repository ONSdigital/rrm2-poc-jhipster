'use strict';

describe('Controller Tests', function() {

    describe('ReportingUnit Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockReportingUnit, MockReportingUnitAssociation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockReportingUnit = jasmine.createSpy('MockReportingUnit');
            MockReportingUnitAssociation = jasmine.createSpy('MockReportingUnitAssociation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ReportingUnit': MockReportingUnit,
                'ReportingUnitAssociation': MockReportingUnitAssociation
            };
            createController = function() {
                $injector.get('$controller')("ReportingUnitDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'rrmApp:reportingUnitUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
