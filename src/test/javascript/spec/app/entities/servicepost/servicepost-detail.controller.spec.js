'use strict';

describe('Controller Tests', function() {

    describe('Servicepost Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockServicepost, MockServiice, MockPoste;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockServicepost = jasmine.createSpy('MockServicepost');
            MockServiice = jasmine.createSpy('MockServiice');
            MockPoste = jasmine.createSpy('MockPoste');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Servicepost': MockServicepost,
                'Serviice': MockServiice,
                'Poste': MockPoste
            };
            createController = function() {
                $injector.get('$controller')("ServicepostDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'qualiMakerApp:servicepostUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
