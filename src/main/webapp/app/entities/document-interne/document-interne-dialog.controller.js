(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DocumentInterneDialogController', DocumentInterneDialogController)
        .directive('ckeditor', Directive);

    function Directive($rootScope) {
        return {
            require: 'ngModel',
            link: function (scope, element, attr, ngModel) {
                var editorOptions;
                if (attr.ckeditor === 'minimal') {
                    // minimal editor
                    editorOptions = {
                        height: 100,
                        toolbar: [
                            {name: 'basic', items: ['Bold', 'Italic', 'Underline']},
                            {name: 'links', items: ['Link', 'Unlink']},
                            {name: 'tools', items: ['Maximize']},
                            {name: 'document', items: ['Source']},
                        ],
                        removePlugins: 'elementspath',
                        resize_enabled: false
                    };
                } else {
                    // regular editor
                    editorOptions = {
                        filebrowserImageUploadUrl: $rootScope.globals.apiUrl + '/upload',
                        removeButtons: 'About,Form,Checkbox,Radio,TextField,Textarea,Select,Button,ImageButton,HiddenField,Save,CreateDiv,Language,BidiLtr,BidiRtl,Flash,Iframe,addFile,Styles',
                        extraPlugins: 'simpleuploads,imagesfromword'
                    };
                }

                // enable ckeditor
                var ckeditor = element.ckeditor(editorOptions);

                // update ngModel on change
                ckeditor.editor.on('change', function () {
                    ngModel.$setViewValue(this.getData());
                });
            }
        };
    };


    DocumentInterneDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'DocumentInterne', 'Sites', 'Processus', 'TypeDocumentation', 'DroitaccesDocument'];

    function DocumentInterneDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, DocumentInterne, Sites, Processus, TypeDocumentation, DroitaccesDocument) {
        var vm = this;

        vm.documentInterne = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.sites = Sites.query();
        vm.processuses = Processus.query();
        vm.typedocumentations = TypeDocumentation.query();
        vm.droitaccesdocuments = DroitaccesDocument.query();
        vm.EnvoyerSupVerificateur=EnvoyerSupVerificateur;
        vm.EnvoyerSupRedacteur=EnvoyerSupRedacteur;
        vm.EnvoyerRedacteurSup=EnvoyerRedacteurSup;
        vm.modifPdf=modifPdf;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        function EnvoyerSupRedacteur() {
            if (vm.documentInterne.id !== null){
                vm.documentInterne.id +=1;
                /* vm.documentInterne.precedent =" ";
                 vm.documentInterne.suivant =" ";*/
                vm.documentInterne.version +=1;
                /* DocumentInterne.update(vm.documentInterne, onSaveSuccess, onSaveError);*/
                vm.documentInterne.etat="En cours";
                vm.documentInterne.precedent ="R";
                vm.documentInterne.suivant ="S";
                DocumentInterne.save(vm.documentInterne, onSaveSuccess, onSaveError);
            }


        }
        function EnvoyerSupVerificateur() {
            vm.documentInterne.id +=1;
            //vm.documentInterne.etat="Modifier par le rédacteur";
            vm.documentInterne.precedent ="S";
            vm.documentInterne.suivant ="A";
            DocumentInterne.save(vm.documentInterne, onSaveSuccess, onSaveError);


        }
        function EnvoyerApprobateurSup() {
            vm.documentInterne.id +=1;
            vm.documentInterne.etat="Modifier par l'approuvateur";
            vm.documentInterne.precedent ="S";
            vm.documentInterne.suivant ="S";
            DocumentInterne.save(vm.documentInterne, onSaveSuccess, onSaveError);
        }
        function Terminer() {
            DocumentInterne.update(vm.documentInterne, onSaveSuccess, onSaveError);

        }
        function EnvoyerSupApprobateur() {
            vm.documentInterne.id +=1;
            vm.documentInterne.etat="Modifier par le Verificateur";
            vm.documentInterne.precedent ="S";
            vm.documentInterne.suivant ="A";
            DocumentInterne.save(vm.documentInterne, onSaveSuccess, onSaveError);


        }
        function EnvoyerRedacteurSup() {
            vm.documentInterne.id +=1;
            vm.documentInterne.etat="Modifier par le rédacteur";
            vm.documentInterne.precedent ="S";
            vm.documentInterne.suivant ="V";
            DocumentInterne.save(vm.documentInterne, onSaveSuccess, onSaveError);

        }
     function EnvoyerSupVerificateur() {

          /*vm.documentInterne.etat="En cours";
          vm.documentInterne.id +=1;
         DocumentInterne.save(vm.documentInterne, onSaveSuccess, onSaveError);*/


}
        function save () {
            vm.isSaving = true;
            if (vm.documentInterne.id !== null) {
                DocumentInterne.update(vm.documentInterne, onSaveSuccess, onSaveError);
            } else {
                vm.documentInterne.precedent ="S";
                vm.documentInterne.suivant ="R";
                DocumentInterne.save(vm.documentInterne, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:documentInterneUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setFichier = function ($file, documentInterne) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        documentInterne.fichier = base64Data;
                        documentInterne.fichierContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        function modifPdf(src)
        {
            // Créer l'objet qui permet de manipuler un PDF
            var pdf = new ActiveXObject('AcroExch.PDDoc');
            // Variables
            var pdfJS;
            var annot;
            var props;
            var printParams;
            var page;
            var rectSize = new Array();
            // Ouverture du PDF
            pdf.Open(src);
            page = pdf.AcquirePage(0);
            page = page.GetSize()
            rectSize[0] = 25;
            rectSize[1] = page.y - 50;
            rectSize[2] = page.x - 25;
            rectSize[3] = page.y - 25;
            // Obtenir le javascript du PDF
            pdfJS = pdf.GetJSObject();
            // Ajout de l'annotation
            // L'annotation est crée, mais elle n'est pas positionnée (rect n'est pas pris en compte)
            pdfJS.addAnnot({page: 0,type: 'FreeText',rect: rectSize,author: 'SA',contents: 'Test'});
            pdfJS.addAnnot({page: 0,type: 'FreeText',rect: [25,742,1199,767],author: 'SA',contents: 'Test'});
            pdfJS.addAnnot({page: 0,type: 'FreeText',rect: new Array(25,742,1199,767), author: 'SA',contents: 'Test'});
            // Impression
            printParams = pdfJS.getPrintParams();
            printParams.interactive = -1;
            printParams.firstPage = 0;
            printParams.pageHandling = printParams.constants.handling.fit;
            //pdfJS.print(printParams);
            // Je save sur mon disque dur à place d'imprimer pour les tests
            pdf.Save(1, "C:/Tempo/test.pdf" );
            pdf.Close();
        }
    }
})();
