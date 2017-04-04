(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('TypeFormateurDetailController', TypeFormateurDetailController);

    TypeFormateurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TypeFormateur', 'Formateur'];

    function TypeFormateurDetailController($scope, $rootScope, $stateParams, previousState, entity, TypeFormateur, Formateur) {
        var vm = this;

        vm.typeFormateur = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:typeFormateurUpdate', function(event, result) {
            vm.typeFormateur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
