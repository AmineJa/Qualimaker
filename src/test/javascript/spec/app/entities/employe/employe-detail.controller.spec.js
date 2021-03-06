'use strict';

describe('Controller Tests', function() {

    describe('Employe Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEmploye, MockConge, MockSites, MockGroupe, MockServiice, MockFormation, MockDocumenExterne, MockRemplacer, MockDroitaccesDocument, MockCarriere, MockProfilsfonction, MockCompetences;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEmploye = jasmine.createSpy('MockEmploye');
            MockConge = jasmine.createSpy('MockConge');
            MockSites = jasmine.createSpy('MockSites');
            MockGroupe = jasmine.createSpy('MockGroupe');
            MockServiice = jasmine.createSpy('MockServiice');
            MockFormation = jasmine.createSpy('MockFormation');
            MockDocumenExterne = jasmine.createSpy('MockDocumenExterne');
            MockRemplacer = jasmine.createSpy('MockRemplacer');
            MockDroitaccesDocument = jasmine.createSpy('MockDroitaccesDocument');
            MockCarriere = jasmine.createSpy('MockCarriere');
            MockProfilsfonction = jasmine.createSpy('MockProfilsfonction');
            MockCompetences = jasmine.createSpy('MockCompetences');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Employe': MockEmploye,
                'Conge': MockConge,
                'Sites': MockSites,
                'Groupe': MockGroupe,
                'Serviice': MockServiice,
                'Formation': MockFormation,
                'DocumenExterne': MockDocumenExterne,
                'Remplacer': MockRemplacer,
                'DroitaccesDocument': MockDroitaccesDocument,
                'Carriere': MockCarriere,
                'Profilsfonction': MockProfilsfonction,
                'Competences': MockCompetences
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
