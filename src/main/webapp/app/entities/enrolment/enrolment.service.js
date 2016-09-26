(function() {
    'use strict';
    angular
        .module('rrmApp')
        .factory('Enrolment', Enrolment);

    Enrolment.$inject = ['$resource'];

    function Enrolment ($resource) {
        var resourceUrl =  'api/enrolments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
