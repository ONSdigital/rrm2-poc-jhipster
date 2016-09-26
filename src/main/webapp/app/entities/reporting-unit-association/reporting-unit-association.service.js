(function() {
    'use strict';
    angular
        .module('rrmApp')
        .factory('ReportingUnitAssociation', ReportingUnitAssociation);

    ReportingUnitAssociation.$inject = ['$resource'];

    function ReportingUnitAssociation ($resource) {
        var resourceUrl =  'api/reporting-unit-associations/:id';

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
