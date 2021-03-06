(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FonctionDetailController', FonctionDetailController);

    FonctionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Fonction', 'Profilsfonction'];

    function FonctionDetailController($scope, $rootScope, $stateParams, previousState, entity, Fonction, Profilsfonction) {
        var vm = this;

        vm.fonction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:fonctionUpdate', function(event, result) {
            vm.fonction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
