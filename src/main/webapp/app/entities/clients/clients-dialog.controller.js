(function() {
    'use strict';

    angular
        .module('msgApp')
        .controller('ClientsDialogController', ClientsDialogController);

    ClientsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Clients', 'KeycloakUser'];

    function ClientsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Clients, KeycloakUser) {
        var vm = this;

        vm.clients = entity;
        vm.clear = clear;
        vm.save = save;
        vm.keycloakusers = KeycloakUser.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.clients.id !== null) {
                Clients.update(vm.clients, onSaveSuccess, onSaveError);
            } else {
                Clients.save(vm.clients, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('msgApp:clientsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
