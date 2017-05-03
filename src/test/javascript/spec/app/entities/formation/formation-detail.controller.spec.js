'use strict';

describe('Controller Tests', function() {

    describe('Formation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFormation, MockDemandeFormation, MockFormationComp, MockFormateur, MockNatureformation, MockFichierjoint, MockCritereevaluation, MockJour, MockEmploye;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFormation = jasmine.createSpy('MockFormation');
            MockDemandeFormation = jasmine.createSpy('MockDemandeFormation');
            MockFormationComp = jasmine.createSpy('MockFormationComp');
            MockFormateur = jasmine.createSpy('MockFormateur');
            MockNatureformation = jasmine.createSpy('MockNatureformation');
            MockFichierjoint = jasmine.createSpy('MockFichierjoint');
            MockCritereevaluation = jasmine.createSpy('MockCritereevaluation');
            MockJour = jasmine.createSpy('MockJour');
            MockEmploye = jasmine.createSpy('MockEmploye');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Formation': MockFormation,
                'DemandeFormation': MockDemandeFormation,
                'FormationComp': MockFormationComp,
                'Formateur': MockFormateur,
                'Natureformation': MockNatureformation,
                'Fichierjoint': MockFichierjoint,
                'Critereevaluation': MockCritereevaluation,
                'Jour': MockJour,
                'Employe': MockEmploye
            };
            createController = function() {
                $injector.get('$controller')("FormationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'qualiMakerApp:formationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
