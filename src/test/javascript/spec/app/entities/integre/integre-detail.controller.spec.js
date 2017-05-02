'use strict';

describe('Controller Tests', function() {

    describe('Integre Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockIntegre, MockEmploye;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockIntegre = jasmine.createSpy('MockIntegre');
            MockEmploye = jasmine.createSpy('MockEmploye');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Integre': MockIntegre,
                'Employe': MockEmploye
            };
            createController = function() {
                $injector.get('$controller')("IntegreDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'qualiMakerApp:integreUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
