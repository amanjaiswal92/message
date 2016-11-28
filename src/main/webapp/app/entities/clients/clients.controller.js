(function() {
    'use strict';

    angular
        .module('msgApp')
        .controller('ClientsController', ClientsController);

    ClientsController.$inject = ['$scope', '$state', 'Clients'];

    function ClientsController ($scope, $state, Clients) {
        var vm = this;
        
        vm.clients = [];

        loadAll();

        function loadAll() {
            Clients.query(function(result) {
                vm.clients = result;
            });
        }
    }
})();
