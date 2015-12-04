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
package org.openstreetmap.josm.plugins.improveosm.service.turnrestriction.entity;

import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.service.entity.CommentRequest;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public class Request extends CommentRequest {

    private final List<String> targetIds;


    public Request(final Comment comment, final List<TurnRestriction> turnRestrictions) {
        super(comment);
        this.targetIds = new ArrayList<>();
        for (final TurnRestriction turnRestriction : turnRestrictions) {
            this.targetIds.add(turnRestriction.getId());
        }
    }


    public List<String> getTargetIds() {
        return targetIds;
    }
}