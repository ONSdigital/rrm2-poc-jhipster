(function() {
    'use strict';
    angular
        .module('rrmApp')
        .factory('ReportingUnit', ReportingUnit);

    ReportingUnit.$inject = ['$resource'];

    function ReportingUnit ($resource) {
        var resourceUrl =  'api/reporting-units/:id';

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
