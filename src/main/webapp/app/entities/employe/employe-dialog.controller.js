(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EmployeDialogController', EmployeDialogController);

    EmployeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Employe', 'Conge', 'Sites', 'Groupe', 'Serviice', 'Formation', 'DocumenExterne', 'Remplacer', 'DroitaccesDocument', 'Carriere', 'Profilsfonction', 'Competences'];

    function EmployeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Employe, Conge, Sites, Groupe, Serviice, Formation, DocumenExterne, Remplacer, DroitaccesDocument, Carriere, Profilsfonction, Competences) {
        var vm = this;

        vm.employe = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.conges = Conge.query();
        vm.sites = Sites.query();
        vm.groupes = Groupe.query();
        vm.serviices = Serviice.query();
        vm.formations = Formation.query();
        vm.documenexternes = DocumenExterne.query();
        vm.remplacers = Remplacer.query();
        vm.droitaccesdocuments = DroitaccesDocument.query();
        vm.carrieres = Carriere.query();
        vm.profilsfonctions = Profilsfonction.query();
        vm.competences = Competences.query();

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

        vm.setSignature = function ($file, employe) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        employe.signature = base64Data;
                        employe.signatureContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
