(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('ProfilsfonctionDetailController', ProfilsfonctionDetailController);

    ProfilsfonctionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Profilsfonction', 'Fonction'];

    function ProfilsfonctionDetailController($scope, $rootScope, $stateParams, previousState, entity, Profilsfonction, Fonction) {
        var vm = this;

        vm.profilsfonction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:profilsfonctionUpdate', function(event, result) {
            vm.profilsfonction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
