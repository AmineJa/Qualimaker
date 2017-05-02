'use strict';

describe('Controller Tests', function() {

    describe('Employe Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEmploye, MockCarriere, MockConge, MockSites, MockGroupe, MockServiice, MockFormation, MockFonction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEmploye = jasmine.createSpy('MockEmploye');
            MockCarriere = jasmine.createSpy('MockCarriere');
            MockConge = jasmine.createSpy('MockConge');
            MockSites = jasmine.createSpy('MockSites');
            MockGroupe = jasmine.createSpy('MockGroupe');
            MockServiice = jasmine.createSpy('MockServiice');
            MockFormation = jasmine.createSpy('MockFormation');
            MockFonction = jasmine.createSpy('MockFonction');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Employe': MockEmploye,
                'Carriere': MockCarriere,
                'Conge': MockConge,
                'Sites': MockSites,
                'Groupe': MockGroupe,
                'Serviice': MockServiice,
                'Formation': MockFormation,
                'Fonction': MockFonction
            };
            createController = function() {
                $injector.get('$controller')("EmployeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'qualiMakerApp:employeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
