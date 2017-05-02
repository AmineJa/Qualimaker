(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('PosteDetailController', PosteDetailController);

    PosteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Poste', 'Serviice', 'Servicepost'];

    function PosteDetailController($scope, $rootScope, $stateParams, previousState, entity, Poste, Serviice, Servicepost) {
        var vm = this;

        vm.poste = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:posteUpdate', function(event, result) {
            vm.poste = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
