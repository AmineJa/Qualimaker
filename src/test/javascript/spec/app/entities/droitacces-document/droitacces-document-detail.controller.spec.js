'use strict';

describe('Controller Tests', function() {

    describe('DroitaccesDocument Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDroitaccesDocument, MockEmploye, MockDocumentInterne;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDroitaccesDocument = jasmine.createSpy('MockDroitaccesDocument');
            MockEmploye = jasmine.createSpy('MockEmploye');
            MockDocumentInterne = jasmine.createSpy('MockDocumentInterne');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DroitaccesDocument': MockDroitaccesDocument,
                'Employe': MockEmploye,
                'DocumentInterne': MockDocumentInterne
            };
            createController = function() {
                $injector.get('$controller')("DroitaccesDocumentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'qualiMakerApp:droitaccesDocumentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
