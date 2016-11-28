(function() {
    'use strict';

    angular
        .module('msgApp')
        .controller('Keycloak_usersDetailController', Keycloak_usersDetailController);

    Keycloak_usersDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Keycloak_users'];

    function Keycloak_usersDetailController($scope, $rootScope, $stateParams, previousState, entity, Keycloak_users) {
        var vm = this;

        vm.keycloak_users = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('msgApp:keycloak_usersUpdate', function(event, result) {
            vm.keycloak_users = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
