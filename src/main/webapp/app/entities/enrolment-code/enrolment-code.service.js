(function() {
    'use strict';
    angular
        .module('rrmApp')
        .factory('EnrolmentCode', EnrolmentCode);

    EnrolmentCode.$inject = ['$resource'];

    function EnrolmentCode ($resource) {
        var resourceUrl =  'api/enrolment-codes/:id';

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
