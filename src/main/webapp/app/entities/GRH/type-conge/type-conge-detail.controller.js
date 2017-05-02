(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('TypeCongeDetailController', TypeCongeDetailController);

    TypeCongeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TypeConge'];

    function TypeCongeDetailController($scope, $rootScope, $stateParams, previousState, entity, TypeConge) {
        var vm = this;

        vm.typeConge = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:typeCongeUpdate', function(event, result) {
            vm.typeConge = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
