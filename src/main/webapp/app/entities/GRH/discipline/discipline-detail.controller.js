(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DisciplineDetailController', DisciplineDetailController);

    DisciplineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Discipline', 'Naturediscipline'];

    function DisciplineDetailController($scope, $rootScope, $stateParams, previousState, entity, Discipline, Naturediscipline) {
        var vm = this;

        vm.discipline = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:disciplineUpdate', function(event, result) {
            vm.discipline = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
