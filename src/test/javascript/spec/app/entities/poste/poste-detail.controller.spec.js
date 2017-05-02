'use strict';

describe('Controller Tests', function() {

    describe('Poste Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPoste, MockServiice, MockServicepost;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPoste = jasmine.createSpy('MockPoste');
            MockServiice = jasmine.createSpy('MockServiice');
            MockServicepost = jasmine.createSpy('MockServicepost');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Poste': MockPoste,
                'Serviice': MockServiice,
                'Servicepost': MockServicepost
            };
            createController = function() {
                $injector.get('$controller')("PosteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'qualiMakerApp:posteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
