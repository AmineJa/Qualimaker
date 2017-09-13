'use strict';

describe('Controller Tests', function() {

    describe('Carriere Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCarriere, MockEmploye, MockTypeContrat;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCarriere = jasmine.createSpy('MockCarriere');
            MockEmploye = jasmine.createSpy('MockEmploye');
            MockTypeContrat = jasmine.createSpy('MockTypeContrat');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Carriere': MockCarriere,
                'Employe': MockEmploye,
                'TypeContrat': MockTypeContrat
            };
            createController = function() {
                $injector.get('$controller')("CarriereDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'qualiMakerApp:carriereUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
