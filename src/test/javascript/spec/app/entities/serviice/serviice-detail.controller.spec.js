'use strict';

describe('Controller Tests', function() {

    describe('Serviice Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockServiice, MockPoste, MockServicepost;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockServiice = jasmine.createSpy('MockServiice');
            MockPoste = jasmine.createSpy('MockPoste');
            MockServicepost = jasmine.createSpy('MockServicepost');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Serviice': MockServiice,
                'Poste': MockPoste,
                'Servicepost': MockServicepost
            };
            createController = function() {
                $injector.get('$controller')("ServiiceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'qualiMakerApp:serviiceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
