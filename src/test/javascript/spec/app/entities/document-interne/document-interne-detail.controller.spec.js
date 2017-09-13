'use strict';

describe('Controller Tests', function() {

    describe('DocumentInterne Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDocumentInterne, MockSites, MockProcessus, MockTypeDocumentation, MockDroitaccesDocument;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDocumentInterne = jasmine.createSpy('MockDocumentInterne');
            MockSites = jasmine.createSpy('MockSites');
            MockProcessus = jasmine.createSpy('MockProcessus');
            MockTypeDocumentation = jasmine.createSpy('MockTypeDocumentation');
            MockDroitaccesDocument = jasmine.createSpy('MockDroitaccesDocument');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DocumentInterne': MockDocumentInterne,
                'Sites': MockSites,
                'Processus': MockProcessus,
                'TypeDocumentation': MockTypeDocumentation,
                'DroitaccesDocument': MockDroitaccesDocument
            };
            createController = function() {
                $injector.get('$controller')("DocumentInterneDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'qualiMakerApp:documentInterneUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
