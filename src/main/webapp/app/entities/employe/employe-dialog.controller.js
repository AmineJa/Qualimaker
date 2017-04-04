(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EmployeDialogController', EmployeDialogController);

    EmployeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'Employe', 'Carriere', 'Conge', 'Sites', 'Groupe', 'Abscence', 'Serviice', 'Formation', 'Fonction'];

    function EmployeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, Employe, Carriere, Conge, Sites, Groupe, Abscence, Serviice, Formation, Fonction) {
        var vm = this;

        vm.employe = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.carrieres = Carriere.query({filter: 'employe-is-null'});
        $q.all([vm.employe.$promise, vm.carrieres.$promise]).then(function() {
            if (!vm.employe.carriere || !vm.employe.carriere.id) {
                return $q.reject();
            }
            return Carriere.get({id : vm.employe.carriere.id}).$promise;
        }).then(function(carriere) {
            vm.carrieres.push(carriere);
        });
        vm.conges = Conge.query();
        vm.sites = Sites.query();
        vm.groupes = Groupe.query();
        vm.abscences = Abscence.query();
        vm.serviices = Serviice.query();
        vm.formations = Formation.query();
        vm.fonctions = Fonction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.employe.id !== null) {
                Employe.update(vm.employe, onSaveSuccess, onSaveError);
            } else {
                Employe.save(vm.employe, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:employeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateN = false;
        vm.datePickerOpenStatus.delivrele = false;

        vm.setImage = function ($file, employe) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        employe.image = base64Data;
                        employe.imageContentType = $file.type;
                    });
                });
            }
        };

        vm.setCv = function ($file, employe) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        employe.cv = base64Data;
                        employe.cvContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
