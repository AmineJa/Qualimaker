(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('SitesDetailController', SitesDetailController);

    SitesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Sites', 'DocumentInterne'];

    function SitesDetailController($scope, $rootScope, $stateParams, previousState, entity, Sites, DocumentInterne) {
        var vm = this;

        vm.sites = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:sitesUpdate', function(event, result) {
            vm.sites = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
