'use strict';

describe('Controller Tests', function() {

    describe('Remplacer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRemplacer, MockEmploye, MockProfilsfonction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRemplacer = jasmine.createSpy('MockRemplacer');
            MockEmploye = jasmine.createSpy('MockEmploye');
            MockProfilsfonction = jasmine.createSpy('MockProfilsfonction');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Remplacer': MockRemplacer,
                'Employe': MockEmploye,
                'Profilsfonction': MockProfilsfonction
            };
            createController = function() {
                $injector.get('$controller')("RemplacerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'qualiMakerApp:remplacerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
