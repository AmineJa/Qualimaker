(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('GroupeDetailController', GroupeDetailController);

    GroupeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Groupe'];

    function GroupeDetailController($scope, $rootScope, $stateParams, previousState, entity, Groupe) {
        var vm = this;

        vm.groupe = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:groupeUpdate', function(event, result) {
            vm.groupe = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
