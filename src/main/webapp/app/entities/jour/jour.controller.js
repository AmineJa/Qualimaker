(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('JourController', JourController);

    JourController.$inject = ['Jour', 'JourSearch'];

    function JourController(Jour, JourSearch) {

        var vm = this;

        vm.jours = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Jour.query(function(result) {
                vm.jours = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            JourSearch.query({query: vm.searchQuery}, function(result) {
                vm.jours = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
