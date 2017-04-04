(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('TypeFormationController', TypeFormationController);

    TypeFormationController.$inject = ['TypeFormation', 'TypeFormationSearch'];

    function TypeFormationController(TypeFormation, TypeFormationSearch) {

        var vm = this;

        vm.typeFormations = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TypeFormation.query(function(result) {
                vm.typeFormations = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TypeFormationSearch.query({query: vm.searchQuery}, function(result) {
                vm.typeFormations = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
