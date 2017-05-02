'use strict';

describe('Controller Tests', function() {

    describe('Abscence Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAbscence, MockEmploye, MockNatureAbs;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAbscence = jasmine.createSpy('MockAbscence');
            MockEmploye = jasmine.createSpy('MockEmploye');
            MockNatureAbs = jasmine.createSpy('MockNatureAbs');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Abscence': MockAbscence,
                'Employe': MockEmploye,
                'NatureAbs': MockNatureAbs
            };
            createController = function() {
                $injector.get('$controller')("AbscenceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'qualiMakerApp:abscenceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
