'use strict';

describe('Controller Tests', function() {

    describe('DocumenExterne Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDocumenExterne, MockEmploye, MockOrigine, MockLieuxClassement, MockTypeDocumentation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDocumenExterne = jasmine.createSpy('MockDocumenExterne');
            MockEmploye = jasmine.createSpy('MockEmploye');
            MockOrigine = jasmine.createSpy('MockOrigine');
            MockLieuxClassement = jasmine.createSpy('MockLieuxClassement');
            MockTypeDocumentation = jasmine.createSpy('MockTypeDocumentation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DocumenExterne': MockDocumenExterne,
                'Employe': MockEmploye,
                'Origine': MockOrigine,
                'LieuxClassement': MockLieuxClassement,
                'TypeDocumentation': MockTypeDocumentation
            };
            createController = function() {
                $injector.get('$controller')("DocumenExterneDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'qualiMakerApp:documenExterneUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
