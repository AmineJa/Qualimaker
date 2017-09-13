(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DisciplineDialogController', DisciplineDialogController);

    DisciplineDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Discipline', 'Naturediscipline'];

    function DisciplineDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Discipline, Naturediscipline) {
        var vm = this;
        vm.refresh=refresh;
        vm.discipline = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.naturedisciplines = Naturediscipline.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        function dateDiff(date1, date2){
            var diff = {}                           // Initialisation du retour
            var tmp = date2 - date1;

           /* tmp = Math.floor(tmp/1000);             // Nombre de secondes entre les 2 dates
            diff.sec = tmp % 60;                    // Extraction du nombre de secondes

            tmp = Math.floor((tmp-diff.sec)/60);    // Nombre de minutes (partie entière)
            diff.min = tmp % 60;                    // Extraction du nombre de minutes

            tmp = Math.floor((tmp-diff.min)/60);    // Nombre d'heures (entières)
            diff.hour = tmp % 24;                   // Extraction du nombre d'heures

            tmp = Math.floor((tmp-diff.hour)/24);   // Nombre de jours restants
            diff.day = tmp;*/

            return temp;
        }


        function refresh(){
           /* Naturediscipline.get().function{
                vm.discipline.naturediscipline=Naturediscipline;
            };*/
          /*  $http.get('api/naturedisciplines').success(function(data) {
                vm.discipline.naturedisciplines = data;// Update Model-- Line X
            });*/
           reload :'discipline';
        }

        vm.loadData = function() {
            var promise = Discipline.getnatu
            promise.then(function(data) {
                $scope.names = data.data;
                console.log($scope.names);
            });
        }

        function save () {
            vm.isSaving = true;
            if (vm.discipline.id !== null) {
                var diff = vm.discipline.dateF.getTime() - vm.discipline.dateD.getTime();
                var oneDay = 24* 60 * 60 * 1000;
                var diffDays = Math.abs(diff / (oneDay));
                vm.discipline.duree = diffDays;
                Discipline.update(vm.discipline, onSaveSuccess, onSaveError);
            } else {

                var diff = vm.discipline.dateF.getTime() - vm.discipline.dateD.getTime();
                var oneDay = 24* 60 * 60 * 1000;
                var diffDays = Math.abs(diff / (oneDay));
                vm.discipline.duree = diffDays;
                Discipline.save(vm.discipline, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:disciplineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateD = false;
        vm.datePickerOpenStatus.dateF = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

    }
})();
