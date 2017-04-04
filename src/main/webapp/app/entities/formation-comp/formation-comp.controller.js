(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FormationCompController', FormationCompController);

    FormationCompController.$inject = ['FormationComp', 'FormationCompSearch'];

    function FormationCompController(FormationComp, FormationCompSearch) {

        var vm = this;

        vm.formationComps = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            FormationComp.query(function(result) {
                vm.formationComps = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FormationCompSearch.query({query: vm.searchQuery}, function(result) {
                vm.formationComps = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
