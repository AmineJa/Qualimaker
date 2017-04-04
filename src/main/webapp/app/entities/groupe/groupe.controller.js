(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('GroupeController', GroupeController);

    GroupeController.$inject = ['Groupe', 'GroupeSearch'];

    function GroupeController(Groupe, GroupeSearch) {

        var vm = this;

        vm.groupes = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Groupe.query(function(result) {
                vm.groupes = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            GroupeSearch.query({query: vm.searchQuery}, function(result) {
                vm.groupes = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
