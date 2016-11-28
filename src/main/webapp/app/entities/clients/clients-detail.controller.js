(function() {
    'use strict';

    angular
        .module('msgApp')
        .controller('ClientsDetailController', ClientsDetailController);

    ClientsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Clients', 'KeycloakUser'];

    function ClientsDetailController($scope, $rootScope, $stateParams, previousState, entity, Clients, KeycloakUser) {
        var vm = this;

        vm.clients = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('msgApp:clientsUpdate', function(event, result) {
            vm.clients = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
