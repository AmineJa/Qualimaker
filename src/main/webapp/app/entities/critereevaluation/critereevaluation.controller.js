(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CritereevaluationController', CritereevaluationController);

    CritereevaluationController.$inject = ['Critereevaluation', 'CritereevaluationSearch'];

    function CritereevaluationController(Critereevaluation, CritereevaluationSearch) {

        var vm = this;

        vm.critereevaluations = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Critereevaluation.query(function(result) {
                vm.critereevaluations = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CritereevaluationSearch.query({query: vm.searchQuery}, function(result) {
                vm.critereevaluations = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
