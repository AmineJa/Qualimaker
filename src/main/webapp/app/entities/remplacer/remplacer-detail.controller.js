(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('RemplacerDetailController', RemplacerDetailController);

    RemplacerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Remplacer', 'Employe', 'Profilsfonction'];

    function RemplacerDetailController($scope, $rootScope, $stateParams, previousState, entity, Remplacer, Employe, Profilsfonction) {
        var vm = this;

        vm.remplacer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:remplacerUpdate', function(event, result) {
            vm.remplacer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
