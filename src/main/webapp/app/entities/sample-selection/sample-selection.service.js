(function() {
    'use strict';
    angular
        .module('rrmApp')
        .factory('SampleSelection', SampleSelection);

    SampleSelection.$inject = ['$resource'];

    function SampleSelection ($resource) {
        var resourceUrl =  'api/sample-selections/:id';

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
