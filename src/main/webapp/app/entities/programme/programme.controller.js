(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('ProgrammeController', ProgrammeController);

    ProgrammeController.$inject = ['Programme', 'ProgrammeSearch'];

    function ProgrammeController(Programme, ProgrammeSearch) {

        var vm = this;

        vm.programmes = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Programme.query(function(result) {
                vm.programmes = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ProgrammeSearch.query({query: vm.searchQuery}, function(result) {
                vm.programmes = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
