'use strict';

describe('Controller Tests', function() {

    describe('Discipline Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDiscipline, MockNaturediscipline;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDiscipline = jasmine.createSpy('MockDiscipline');
            MockNaturediscipline = jasmine.createSpy('MockNaturediscipline');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Discipline': MockDiscipline,
                'Naturediscipline': MockNaturediscipline
            };
            createController = function() {
                $injector.get('$controller')("DisciplineDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'qualiMakerApp:disciplineUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
