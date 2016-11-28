(function() {
    'use strict';

    angular
        .module('msgApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('keycloak-user', {
            parent: 'entity',
            url: '/keycloak-user',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'KeycloakUsers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/keycloak-user/keycloak-users.html',
                    controller: 'KeycloakUserController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('keycloak-user-detail', {
            parent: 'entity',
            url: '/keycloak-user/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'KeycloakUser'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/keycloak-user/keycloak-user-detail.html',
                    controller: 'KeycloakUserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'KeycloakUser', function($stateParams, KeycloakUser) {
                    return KeycloakUser.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'keycloak-user',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('keycloak-user-detail.edit', {
            parent: 'keycloak-user-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/keycloak-user/keycloak-user-dialog.html',
                    controller: 'KeycloakUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['KeycloakUser', function(KeycloakUser) {
                            return KeycloakUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('keycloak-user.new', {
            parent: 'keycloak-user',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/keycloak-user/keycloak-user-dialog.html',
                    controller: 'KeycloakUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                clientId: null,
                                username: null,
                                password: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('keycloak-user', null, { reload: 'keycloak-user' });
                }, function() {
                    $state.go('keycloak-user');
                });
            }]
        })
        .state('keycloak-user.edit', {
            parent: 'keycloak-user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/keycloak-user/keycloak-user-dialog.html',
                    controller: 'KeycloakUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['KeycloakUser', function(KeycloakUser) {
                            return KeycloakUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('keycloak-user', null, { reload: 'keycloak-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('keycloak-user.delete', {
            parent: 'keycloak-user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/keycloak-user/keycloak-user-delete-dialog.html',
                    controller: 'KeycloakUserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['KeycloakUser', function(KeycloakUser) {
                            return KeycloakUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('keycloak-user', null, { reload: 'keycloak-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
