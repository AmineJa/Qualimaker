'use strict';

describe('Controller Tests', function() {

    describe('TypeDocumentation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTypeDocumentation, MockEmploye, MockDocumenExterne;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTypeDocumentation = jasmine.createSpy('MockTypeDocumentation');
            MockEmploye = jasmine.createSpy('MockEmploye');
            MockDocumenExterne = jasmine.createSpy('MockDocumenExterne');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'TypeDocumentation': MockTypeDocumentation,
                'Employe': MockEmploye,
                'DocumenExterne': MockDocumenExterne
            };
            createController = function() {
                $injector.get('$controller')("TypeDocumentationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'qualiMakerApp:typeDocumentationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
