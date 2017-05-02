'use strict';

describe('Controller Tests', function() {

    describe('ServicePost Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockServicePost, MockServiice, MockPoste;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockServicePost = jasmine.createSpy('MockServicePost');
            MockServiice = jasmine.createSpy('MockServiice');
            MockPoste = jasmine.createSpy('MockPoste');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ServicePost': MockServicePost,
                'Serviice': MockServiice,
                'Poste': MockPoste
            };
            createController = function() {
                $injector.get('$controller')("ServicePostDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'qualiMakerApp:servicePostUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
