(function() {
    'use strict';

    angular
        .module('msgApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('keycloak-users', {
            parent: 'entity',
            url: '/keycloak-users',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Keycloak_users'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/keycloak-users/keycloak-users.html',
                    controller: 'Keycloak_usersController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('keycloak-users-detail', {
            parent: 'entity',
            url: '/keycloak-users/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Keycloak_users'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/keycloak-users/keycloak-users-detail.html',
                    controller: 'Keycloak_usersDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Keycloak_users', function($stateParams, Keycloak_users) {
                    return Keycloak_users.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'keycloak-users',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('keycloak-users-detail.edit', {
            parent: 'keycloak-users-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/keycloak-users/keycloak-users-dialog.html',
                    controller: 'Keycloak_usersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Keycloak_users', function(Keycloak_users) {
                            return Keycloak_users.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('keycloak-users.new', {
            parent: 'keycloak-users',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/keycloak-users/keycloak-users-dialog.html',
                    controller: 'Keycloak_usersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('keycloak-users', null, { reload: 'keycloak-users' });
                }, function() {
                    $state.go('keycloak-users');
                });
            }]
        })
        .state('keycloak-users.edit', {
            parent: 'keycloak-users',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/keycloak-users/keycloak-users-dialog.html',
                    controller: 'Keycloak_usersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Keycloak_users', function(Keycloak_users) {
                            return Keycloak_users.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('keycloak-users', null, { reload: 'keycloak-users' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('keycloak-users.delete', {
            parent: 'keycloak-users',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/keycloak-users/keycloak-users-delete-dialog.html',
                    controller: 'Keycloak_usersDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Keycloak_users', function(Keycloak_users) {
                            return Keycloak_users.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('keycloak-users', null, { reload: 'keycloak-users' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
