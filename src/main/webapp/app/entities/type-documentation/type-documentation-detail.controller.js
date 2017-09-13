(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('TypeDocumentationDetailController', TypeDocumentationDetailController);

    TypeDocumentationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TypeDocumentation', 'Employe', 'DocumenExterne'];

    function TypeDocumentationDetailController($scope, $rootScope, $stateParams, previousState, entity, TypeDocumentation, Employe, DocumenExterne) {
        var vm = this;

        vm.typeDocumentation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:typeDocumentationUpdate', function(event, result) {
            vm.typeDocumentation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
