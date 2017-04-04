(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('AbscenceDetailController', AbscenceDetailController);

    AbscenceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Abscence'];

    function AbscenceDetailController($scope, $rootScope, $stateParams, previousState, entity, Abscence) {
        var vm = this;

        vm.abscence = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:abscenceUpdate', function(event, result) {
            vm.abscence = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
