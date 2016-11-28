(function() {
    'use strict';

    angular
        .module('msgApp')
        .controller('KeycloakUserDialogController', KeycloakUserDialogController);

    KeycloakUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'KeycloakUser', 'Clients'];

    function KeycloakUserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, KeycloakUser, Clients) {
        var vm = this;

        vm.keycloakUser = entity;
        vm.clear = clear;
        vm.save = save;
        vm.clients = Clients.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.keycloakUser.id !== null) {
                KeycloakUser.update(vm.keycloakUser, onSaveSuccess, onSaveError);
            } else {
                KeycloakUser.save(vm.keycloakUser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('msgApp:keycloakUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
