(function() {
    'use strict';

    angular
        .module('msgApp')
        .controller('ClientsDeleteController',ClientsDeleteController);

    ClientsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Clients'];

    function ClientsDeleteController($uibModalInstance, entity, Clients) {
        var vm = this;

        vm.clients = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Clients.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
