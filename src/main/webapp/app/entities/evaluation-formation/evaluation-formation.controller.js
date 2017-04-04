(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EvaluationFormationController', EvaluationFormationController);

    EvaluationFormationController.$inject = ['EvaluationFormation', 'EvaluationFormationSearch'];

    function EvaluationFormationController(EvaluationFormation, EvaluationFormationSearch) {

        var vm = this;

        vm.evaluationFormations = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            EvaluationFormation.query(function(result) {
                vm.evaluationFormations = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EvaluationFormationSearch.query({query: vm.searchQuery}, function(result) {
                vm.evaluationFormations = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
