(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EtatController', EtatController);

    EtatController.$inject = ['Etat', 'EtatSearch'];

    function EtatController(Etat, EtatSearch) {

        var vm = this;

        vm.etats = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Etat.query(function(result) {
                vm.etats = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EtatSearch.query({query: vm.searchQuery}, function(result) {
                vm.etats = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
