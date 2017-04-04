(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FormateurDetailController', FormateurDetailController);

    FormateurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Formateur', 'TypeFormateur'];

    function FormateurDetailController($scope, $rootScope, $stateParams, previousState, entity, Formateur, TypeFormateur) {
        var vm = this;

        vm.formateur = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:formateurUpdate', function(event, result) {
            vm.formateur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
