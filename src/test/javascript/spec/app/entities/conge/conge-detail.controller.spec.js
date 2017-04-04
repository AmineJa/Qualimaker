'use strict';

describe('Controller Tests', function() {

    describe('Conge Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockConge, MockEmploye, MockTypeConge;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockConge = jasmine.createSpy('MockConge');
            MockEmploye = jasmine.createSpy('MockEmploye');
            MockTypeConge = jasmine.createSpy('MockTypeConge');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Conge': MockConge,
                'Employe': MockEmploye,
                'TypeConge': MockTypeConge
            };
            createController = function() {
                $injector.get('$controller')("CongeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'qualiMakerApp:congeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
