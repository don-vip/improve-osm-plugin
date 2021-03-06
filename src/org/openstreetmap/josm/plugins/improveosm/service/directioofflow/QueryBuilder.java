/*
 *  Copyright 2015 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.improveosm.service.directioofflow;

import org.openstreetmap.josm.plugins.improveosm.argument.OnewayFilter;
import org.openstreetmap.josm.plugins.improveosm.service.BaseQueryBuilder;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.ServiceConfig;
import com.telenav.josm.common.argument.BoundingBox;
import com.telenav.josm.common.http.HttpUtil;


/**
 * Builds DirectionOfFlowService specific HTTP queries.
 *
 * @author Beata
 * @version $Revision$
 */
final class QueryBuilder extends BaseQueryBuilder {

    String buildSearchQuery(final BoundingBox bbox, final OnewayFilter filter, final int zoom) {
        final StringBuilder query = new StringBuilder();
        appendGeneralSearchFilters(query, ServiceConfig.getDirectionOfFlowInstance().getVersion(), bbox, zoom);

        if (filter != null) {
            appendStatusFilter(query, filter.getStatus());
            if (filter.getConfidenceLevels() != null) {
                query.append(AND).append(Parameter.CONFIDENCE_LEVEL).append(EQ);
                query.append(HttpUtil.utf8Encode(filter.getConfidenceLevels()));
            }
        }
        return build(ServiceConfig.getDirectionOfFlowInstance().getServiceUrl(), Method.SEARCH, query);
    }

    String buildRetrieveCommentsQuery(final Long wayId, final Long fromNodeId, final Long toNodeId) {
        final StringBuilder query = new StringBuilder();
        appendGeneralParameters(query, ServiceConfig.getDirectionOfFlowInstance().getVersion());
        query.append(AND).append(Parameter.WAY_ID).append(EQ).append(wayId);
        query.append(AND).append(Parameter.FROM_NODE_ID).append(EQ).append(fromNodeId);
        query.append(AND).append(Parameter.TO_NODE_ID).append(EQ).append(toNodeId);
        return build(ServiceConfig.getDirectionOfFlowInstance().getServiceUrl(), Method.RETRIEVE_COMMENTS, query);
    }

    String buildCommentQuery() {
        final StringBuilder query = new StringBuilder();
        appendGeneralParameters(query, ServiceConfig.getDirectionOfFlowInstance().getVersion());
        return build(ServiceConfig.getDirectionOfFlowInstance().getServiceUrl(), Method.COMMENT, query);
    }


    private final class Parameter {

        private static final String CONFIDENCE_LEVEL = "confidenceLevel";
        private static final String WAY_ID = "wayId";
        private static final String FROM_NODE_ID = "fromNodeId";
        private static final String TO_NODE_ID = "toNodeId";

        private Parameter() {}
    }
}